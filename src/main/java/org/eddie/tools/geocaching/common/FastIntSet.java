package org.eddie.tools.geocaching.common;

import java.util.AbstractSet;
import java.util.Iterator;

public class FastIntSet extends AbstractSet<Integer> implements Cloneable {
	
	private static final  byte [] BITMASK = {1,2,4,8,16,32,64,-128};
	private static final  byte [] BITMASK_INVERSE = {(byte) 0xFE,(byte) 0xFD,(byte) 0xFB,(byte) 0xF7,(byte) 0xEF,(byte) 0xDF,(byte) 0xBF,(byte) 0x7F};
	//1110, 1101, 1011, 0111 -> 14, 13, 11, 7 -> E, D, B, 7
    
    //LOOKUP table for bit count for each possible byte value
    private static final byte[] BITCOUNTS = new byte[256];
    
    static {
        BITCOUNTS[0] = 0;
        for (int i = 0; i < 256; i++)
        {
            BITCOUNTS[i] = (byte)((i & 1) + BITCOUNTS[i / 2]);
        }
    }
	
	private int maxInt;
	private byte[] rawData;
	private int size;
	private int min = -1;
	private int max = -1;
	
	public FastIntSet(int maxInt, int ... values) {
		this(maxInt);
		for(int v : values) {
			add(v);
		}
	}
	
	public FastIntSet(int maxInt, String setString) {
		this(maxInt);
		for(String e : setString.split("[,;:]")) {
			String iStr = e.replaceAll("[^0-9]", "").trim();
			if (!iStr.isEmpty()) {
				this.add(Integer.valueOf(iStr));
			}
		}
	}
	
	public FastIntSet(int maxInt) {
		prepareFor(maxInt);
		this.size = 0;
	}
	
	//for cloning
	private FastIntSet(FastIntSet source) {
		this.maxInt = source.maxInt;
		this.size= source.size;
		this.min = source.min;
		this.max = source.max;
		this.rawData = new byte[source.rawData.length];
		System.arraycopy(source.rawData, 0, this.rawData, 0, source.rawData.length);		
	}
	
	public int getMin() {
		if (min>=0 || size==0) {
			return min;
		}
		
		min = iterator().next();
		return min;
	}
	
	public int getMax() {
		if (max>=0 || size==0) {
			return max;
		}
		
		Iterator<Integer> it = iterator();
		while (it.hasNext()) {
			max = it.next();
		}
		return max;
	}
	
	public void addAll(int minInt, int maxIntExcl) {
		prepareFor(maxIntExcl);
		for(int i=minInt;i<maxIntExcl;i++) {
			add(i);
		}
	}
	
	public void retainAll(int ... values) {
	    retainAll(new FastIntSet(this.maxInt,values));
    }
	
	public void retainAll(FastIntSet set) {
	    int minRawLength = Math.min(this.rawData.length,set.rawData.length);
	    int newSize = 0;
	    for(int i=0;i<minRawLength;i++) {
	        rawData[i] = (byte)(rawData[i] & set.rawData[i]);
	        newSize+=bitCount(rawData[i]);
        }
        for(int i=minRawLength;i<rawData.length;i++) {
            rawData[i] = 0;
        }
        size = newSize;
	    min = -1;
	    max = -1;
	}
	
	public boolean containsAny(FastIntSet set) {
        int minRawLength = Math.min(this.rawData.length,set.rawData.length);
        for(int i=0;i<minRawLength;i++) {
            if ((rawData[i] & set.rawData[i]) != 0) {
                return true;
            }
        }
        return false;
    }
	
	public static byte bitCount(byte b) {
	    return (BITCOUNTS[b<0?b+256:b]);
    }
	
	private void prepareFor(int newInt) {
		if (rawData!=null && newInt<this.maxInt) {
			return;
		}
		this.maxInt = ((newInt-1)/8+1)*8;
		byte[] oldRawData = this.rawData;
		this.rawData = new byte[this.maxInt/8];
		if (oldRawData!=null) {
			System.arraycopy(oldRawData, 0, this.rawData, 0, oldRawData.length);
		}
	}
	
	
	@Override
	public boolean contains(Object obj) {
		if (!(obj instanceof Integer)) {
			return false;
		}
		int value = ((Integer)obj).intValue();
		
		return value < this.maxInt && value >=0 && (this.rawData[value/8] & BITMASK[value%8])!=0;
	}
	
	@Override
	public boolean add(Integer value) {
		if (value==null) {
			return false;
		}
		if (value<0) {
	        throw new UnsupportedOperationException("Only positive integers supported by this set");
		}
		if (contains(value)) {
			return false;
		}
		prepareFor(value+1);
		this.rawData[value/8] |= BITMASK[value%8];
		this.size++;
		min = (min>=value?value:-1);
		max = (max<=value?value:-1);
		return true;
	}
	
	@Override
	public boolean remove(Object obj) {
		if (!contains(obj)) {
			return false;
		}
		Integer value = (Integer)obj;
		this.rawData[value/8] &= BITMASK_INVERSE[value%8];
		this.size--;
		min = (min>=value?-1:min);
		max = (max<=value?-1:max);
		return true;
	}
	
	public void remove(int ... ints) {
		for(Object o : ints) {
			remove(o);
		}
	}
	
	@Override
	public void clear() {
		for(int i=0;i<rawData.length;i++) {
			rawData[i] = 0;
		}
		size = 0;
		min = -1;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new SetIterator();
	}
	
	private class SetIterator implements Iterator<Integer> {
		
		private int next;
		private int current;
		
		public SetIterator() {
			current = -1;
			next = nextEntry(current);
		}
		
		private int nextEntry(int current) {
			int next = current+1;
			while (next < maxInt && !contains(next)) {
				next++;
			}
			return (next>=maxInt?-1:next);
		}

		@Override
		public boolean hasNext() {
			return next>=0;
		}

		@Override
		public Integer next() {
			current = next;
			next = nextEntry(next);
			return current;
		}
		
		@Override
		public void remove() {
			if (current>=0) {
				FastIntSet.this.remove(current);
			}
		}
		
	}

	@Override
	public int size() {
		return this.size;
	}
	
	@Override
	public FastIntSet clone() {
		return new FastIntSet(this);
		
	}

}
