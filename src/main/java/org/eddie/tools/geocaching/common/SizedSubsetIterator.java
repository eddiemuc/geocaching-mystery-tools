package org.eddie.tools.geocaching.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Iteartes over all subsets of a given SIZE for a given collection
 */
public class SizedSubsetIterator<T> extends AbstractSubsetIterator<T> {
	
	private final int subsize;

	protected SizedSubsetIterator(Collection<T> coll, int subsize) {
		super(coll);
		this.subsize = subsize;
	}

	@Override
	protected BigDecimal calculateIteratorCount(List<T> elements) {
		BigDecimal result = BigDecimal.ONE;
		for(int i=subsize+1;i<=elements.size();i++) {
			result = result.multiply(new BigDecimal(i));
		}
		return result;
	}

	@Override
	protected List<T> calculateIterationElement(List<T> elements, BigDecimal index) {
		List<T> list = new ArrayList<T>(this.subsize);
		BigDecimal idx = index;
		for(int i=0;i<subsize;i++) {
		//for(int pos=list.size();pos>list.size()-subsize;pos--) {
			//BigDecimal bigPos = new BigDecimal(pos);
			BigDecimal[] posDivRem = idx.divideAndRemainder(new BigDecimal(elements.size()-i));
			list.add(elements.get(posDivRem[1].intValue()));
			idx = posDivRem[0];
		}
		return list;
	}

}
