package org.eddie.tools.geocaching.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSubsetIterator<T> implements Iterable<List<T>> {
	
	private final List<T> list;
	private BigDecimal count;
	
	protected static List<Integer> createIntegerList(int size) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<size;i++) {
			list.add(i);
		}
		return list;		
	}
	
	protected static List<Character> createCharList(String chars) {
		Character[] charsArray = new Character[chars.length()];
		for(int i=0;i<charsArray.length;i++) {
			charsArray[i] = chars.charAt(i);
		}
		return Arrays.asList(charsArray);
	}
	
	protected AbstractSubsetIterator(Collection<T> coll) {
		this.list = new ArrayList<T>(coll);
	}
	
	public BigDecimal count() {
		if (this.count==null) {
			this.count = calculateIteratorCount(this.list);
		}
		return this.count;
	}
	
	public long countAsLong() {
		return count().longValue();
	}
	
	protected abstract BigDecimal calculateIteratorCount(List<T> elements);
	
	protected abstract List<T> calculateIterationElement(List<T> elements, BigDecimal index);
	
	public Iterator<List<T>> iterator() {		
		return new SubsetIterator<T>(this);
	}
	
	private static class SubsetIterator<T> implements Iterator<List<T>> {
		
		private final AbstractSubsetIterator<T> parent;
		private BigDecimal index = new BigDecimal(-1);
		
		public SubsetIterator(AbstractSubsetIterator<T> parent) {
			this.parent = parent;			
		}

		public boolean hasNext() {
			return this.parent.count().signum()==1 && this.index.add(BigDecimal.ONE).compareTo(this.parent.count()) < 0; 
		}

		public List<T> next() {
			if (!hasNext()) {
				return null;
			}
			index = index.add(BigDecimal.ONE);
			return this.parent.calculateIterationElement(parent.list, index);			
		}
		
		public void remove() {
			throw new UnsupportedOperationException("Remove not supported");
		}
		
	}
	

}
