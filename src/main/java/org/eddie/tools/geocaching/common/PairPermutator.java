package org.eddie.tools.geocaching.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Iterateas over a collection returning all possible permutations of PAIRS of elements in that collection
 */
public class PairPermutator<T> extends AbstractSubsetIterator<T> {
	
	public static PairPermutator<Integer> createFor(int size) {
		return new PairPermutator<Integer>(AbstractSubsetIterator.createIntegerList(size));		
	}
	
	public static PairPermutator<Character> createFor(String chars) {
		return new PairPermutator<Character>(AbstractSubsetIterator.createCharList(chars));		
	}
	
	public PairPermutator(T ... elements) {
		this (Arrays.asList(elements));
	}
	
	
	public PairPermutator(Collection<T> coll) {
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
		for(long i=elements.size()/2;i>0;i--) {
			s = s.multiply( new BigDecimal(i*2-1)) ;
			//System.out.println(i+": "+s);
		}
		return s;
		
	}

	@Override
	protected List<T> calculateIterationElement(List<T> elements, BigDecimal index) {
		System.out.println("Calculate for index: "+index);
		List<T> list = new ArrayList<T>(elements);
		BigDecimal idx = index;
		for(int pos=list.size()/2;pos>1;pos--) {
			//BigDecimal bigPos = new BigDecimal(pos);
			BigDecimal[] posDivRem = idx.divideAndRemainder(new BigDecimal(pos*2-1));
			swap(list,pos*2-2,posDivRem[1].intValue());
			idx = posDivRem[0];
		}
		return list;
	}


}
