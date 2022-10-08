package org.eddie.tools.geocaching.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a multi-dimensional Cube 
 */
public class MultiCube<T> {
	
	private final int[] dimSizes;
	private int totalElementCount;
	
	private List<T> content;
	private Map<T,MultiCubePos>  contentToPos;
	
	public MultiCube(String content, Function<String,T> parser, int ... dimSizes) {
		this(null,dimSizes);
		parseString(content,parser);		
	}
	
	public MultiCube(T initValue, int ... dimSizes) {
		this(null,initValue,dimSizes);
	}
	
	public <TT> MultiCube(MultiCube<TT> source, Function<TT,T> cloner) {
		this.dimSizes = source.dimSizes;
		this.totalElementCount = source.totalElementCount;
		this.content = new ArrayList<T>(source.content.size());
		this.contentToPos = new HashMap<>();
		for(TT e : source.content) {
			T eClone = cloner.apply(e);
			this.content.add(eClone);
			this.contentToPos.put(eClone,source.contentToPos.get(e));
		}
	}
	
	public MultiCube(List<T> content, T initValue, int ... dimSizes) {
		this.dimSizes = dimSizes;
		if (this.dimSizes.length==0) {
			throw new IllegalArgumentException("At least one dim must be given");
		}
		for(int i=0;i<this.dimSizes.length;i++) {
			if (this.dimSizes[i]<=0) {
				throw new IllegalArgumentException("DimSizes must be 1 or bigger each");
			}
		}
		this.totalElementCount = 1;
		for(int i=0;i<this.dimSizes.length;i++) {
			this.totalElementCount*=(this.dimSizes[i]);
		}
		this.content = (content==null?new ArrayList<T>(this.totalElementCount):content);
		
		while (this.content.size()<this.totalElementCount) {
			this.content.add(initValue);
		}
		while (this.content.size()>this.totalElementCount) {
			this.content.remove(this.content.size()-1);
		}
		this.contentToPos = new HashMap<>();
		for(int i=0;i<totalElementCount;i++) {
			this.contentToPos.put(this.content.get(i),new MultiCubePos(indexToPos(i)));
		}
	}
	
	public int posToIndex(int[] pos) {
		int result = 0;
		int multi = 1;
		for(int i=0;i<dimSizes.length;i++) {
			result += multi*pos[i];
			multi *= dimSizes[i];
		}
		return result;
		
	}

	public int[] indexToPos(int idx) {
		int[] result = new int[dimSizes.length];
		int curr = idx;
		for(int i=0;i<dimSizes.length;i++) {
			result[i] = curr%dimSizes[i];
			curr /= dimSizes[i];
		}
		return result;		
	}
	
	public int getDimCount() {
		return dimSizes.length;
	}
	
	public int[] getDims() {
		return dimSizes;
	}
	
	public int getTotalElementCount() {
		return this.totalElementCount;
	}
	
	public boolean isInside(int ... pos) {
		if (pos.length!=this.dimSizes.length) {
			return false;
		}
		for(int i=0;i<pos.length;i++) {
			if (pos[i]<0 || pos[i]>= dimSizes[i]) {
				return false;
			}
		}
		return true;		
	}
	
	public T get(int ... pos) {
		if (!isInside(pos)) {
			return null;
		}
		int listPos = posToIndex(pos);
		
		return this.content.get(listPos);
	}
	
	public MultiCubePos getPosFor(T value) {
		return this.contentToPos.get(value);
	}
	
	public void set(T value, int ... pos) {
		if (!isInside(pos)) {
			return;
		}
		int listPos = posToIndex(pos);
		this.content.set(listPos, value);
		this.contentToPos.put(value,new MultiCubePos(pos));
	}
	
	public boolean isInside(MultiCubePos p) {
		return isInside(p.getX(),p.getY(),p.getZ());
	}
	
	public T get(MultiCubePos p) {
		return get(p.getPos());
	}
	
	public void set(MultiCubePos p, T value) {
		set(value,p.getPos());
	}
	
	public Stream<Pair<MultiCubePos,T>> stream() {
		return IntStream.range(0, this.content.size()).mapToObj(idx -> {
			T value = this.content.get(idx);
			return new Pair<>(new MultiCubePos(indexToPos(idx)),value);
		});
		
	}
	
	public static class PersistentPosPredicate<T> implements Predicate<Pair<MultiCubePos,T>> {
		
		private Predicate<Pair<MultiCubePos,T>> filter = x->true;
		private final MultiCube<T> cube;
		private final PersistentPosPredicate<T> previous;
		private Set<Integer> listPos = null;
		
		public PersistentPosPredicate(MultiCube<T> cube) {
			this.cube = cube;
			this.previous = null;
		}
		
		public void setFilter(Predicate<Pair<MultiCubePos,T>> filter) {
			this.filter = filter;
		}

		public PersistentPosPredicate(PersistentPosPredicate<T> previous) {
			this.previous = previous;
			this.cube = this.previous.getCube();
		}
		
		private MultiCube<T> getCube() {
			return cube;
		}
		
		@Override
		public boolean test(Pair<MultiCubePos, T> t) {
			ensureCacheFilled();
			return listPos.contains(cube.posToIndex(t.getFirst().getPos()));
		}
		
		private void ensureCacheFilled() {
			if (listPos!=null) {
				return;
			}
			listPos = streamThrough()
					.map(p -> cube.posToIndex(p.getFirst().getPos()))
					.collect(Collectors.toSet());
			//System.out.println("Persisted "+listPos.size()+" elements");
		}
		
		private Stream<Pair<MultiCubePos,T>> streamThrough() {
			return (this.previous==null?cube.stream():previous.streamThrough()).filter(this.filter);
		}		
	}
	
	public static class Slice<T> extends PersistentPosPredicate<T> {
		private int dim;
		private Set<Integer> pos;
		
		Slice(MultiCube<T> m, int dim, int ... p) {
			super(m);
			init(dim,p);
		}
		
		Slice(Slice<T> previous, int dim, int ... p) {
			super(previous);
			init(dim,p);
		}
		
		private void init(int dim, int ... pos) {
			this.dim = dim;
			this.pos = new HashSet<>();
			for(int i : pos) {
				this.pos.add(i);
			}
			setFilter( pp ->  {
				if (this.dim<0 || this.dim > pp.getFirst().getDimensionSize()) {
					return false;
				}
				int c = pp.getFirst().getPos()[this.dim];
				return (this.pos.contains(c));
				
			});
		}
		
		public Slice<T> slice(int dim, int ...p) {
			return new Slice<T>(this,dim,p);
		}
		
		
	}
	
	public Slice<T> slice(int dim, int ... pos) {
		return new Slice<T>(this,dim,pos);
	}
	
	private void addToString(int depth, int[] pos, StringBuilder sb, Map<MultiCubePos,String[]> marker) {
		if (depth==this.getDimCount()) {
            String[] markers = marker.get(new MultiCubePos(pos));
		    if (markers!=null) {
		        sb.append(markers[0]);
            }
			//string values may NOT contain [ or ]!
			sb.append(String.valueOf(this.get(pos)).replace('[','<').replace(']','>'));
            if (markers!=null) {
                sb.append(markers[1]);
            }
		}
		else {
			for(int i=0;i<this.getDims()[depth];i++) {
				pos[depth] = i;
				sb.append("[");
				addToString(depth+1,pos,sb,marker);
				sb.append("]");
			}
		}
	}
	
	public String toString() {
		return toString(Collections.emptyMap());
	}
    
    public String toString(Map<MultiCubePos,String[]> marker) {
		StringBuilder sb = new StringBuilder();
		int[] pos = new int[this.getDimCount()];
		addToString(0,pos,sb,marker);
		return sb.toString();
		
	}
	
	public void parseString(String cubeString,Function<String,T> parser) {
		int[] pos = new int[this.getDimCount()];
		addToCube(0,pos,cubeString,0,parser);
	}
	
	private static final String SINGLE_PATTERN = "\\[(.*)\\]";
	
	private int addToCube(int depth, int[] pos, String cubeLine, int cubeLinePos, Function<String,T> parser) {
		
		
		if (depth==this.getDimCount()) {
			int endIdx = cubeLine.indexOf(']', cubeLinePos);
			if (endIdx<cubeLinePos) {
				throw new IllegalArgumentException("Could not parse value from '"+cubeLine+"' (pos "+cubeLinePos+")");
			}
			
			this.set(parser.apply(cubeLine.substring(cubeLinePos,endIdx)),pos);
			return endIdx;
		}
		else {
			int clp = cubeLinePos;			
			int size =this.getDims()[depth];
			for(int i=0;i<size;i++) {
				assureChar(cubeLine,clp,'[');
				clp++;			
				pos[depth] = i;
				clp = addToCube(depth+1,pos,cubeLine,clp,parser);
				assureChar(cubeLine,clp,']');
				clp++;				
			}
			return clp;
		}
	}
	
	private void assureChar(String str, int pos, char c) {
		if (str==null || pos>=str.length() || str.charAt(pos)!=c) {
			throw new IllegalArgumentException("char at pos "+pos+" must be '"+c+"' but is not: "+str);
		}
	}
	
	private List<String> parseStringLine(String line, int count) {
		StringBuilder sb = new StringBuilder("^");
		for(int i=0;i<count;i++) {
			sb.append(SINGLE_PATTERN);
		}
		sb.append("$");
		
		Matcher m = Pattern.compile(sb.toString()).matcher(line);
		if (!m.matches() || m.groupCount()!=count) {
			throw new IllegalArgumentException("Could not parse '"+line+"' ("+count+") as part of a MultiCube-String");
		}
		List<String> result = new ArrayList<>();
		for(int i=1;i<=count;i++) {
			result.add(m.group(i));
		}
		System.out.println("FOUND: "+result);
		return result;
	}
	

}
