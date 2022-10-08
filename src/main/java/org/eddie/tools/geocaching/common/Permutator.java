package org.eddie.tools.geocaching.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Permutator<T> extends AbstractSubsetIterator<T> {
	
	public static Permutator<Integer> createFor(int size) {
		return new Permutator<Integer>(AbstractSubsetIterator.createIntegerList(size));		
	}
	
	public static <T> Permutator<T> createFor(int size, Function<Integer,T> fct) {
		return new Permutator<T>(AbstractSubsetIterator.createIntegerList(size).stream().map(fct).collect(Collectors.toList()));		
	}
	
	public static Permutator<Character> createFor(String chars) {
		return new Permutator<Character>(AbstractSubsetIterator.createCharList(chars));		
	}
	
	public Permutator(T ... elements) {
		this (Arrays.asList(elements));
	}
	
	
	public Permutator(Collection<T> coll) {
		super(coll);
	}
	
	private static <T> void swap(List<T> list, int pos1, int pos2) {
		if (pos1==pos2) {
			return;
		}
		T swap = list.get(pos1);
		list.set(pos1, list.get(pos2));
		list.set(pos2, swap);
	}

	@Override
	protected BigDecimal calculateIteratorCount(List<T> elements) {
		BigDecimal s = new BigDecimal(1);
		for(long i=elements.size();i>0;i--) {
			s = s.multiply( new BigDecimal(i)) ;
			//System.out.println(i+": "+s);
		}
		return s;		
	}
	

	@Override
	protected List<T> calculateIterationElement(List<T> elements, BigDecimal index) {
		List<T> list = new ArrayList<T>(elements);
		BigDecimal idx = index;
		for(int pos=list.size();pos>1;pos--) {
			//BigDecimal bigPos = new BigDecimal(pos);
			BigDecimal[] posDivRem = idx.divideAndRemainder(new BigDecimal(pos));
			swap(list,pos-1,posDivRem[1].intValue());
			idx = posDivRem[0];
		}
		return list;
	}

}
