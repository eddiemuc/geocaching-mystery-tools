package org.eddie.tools.geocaching.common.encoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.eddie.tools.geocaching.common.Utils;

public class BitEncoder {
	
	private static final Set<Character> ONE_DEFAULT_SET = Utils.toSet(Utils.toCharArray("1"));
	private static final Set<Character> ZERO_DEFAULT_SET = Utils.toSet(Utils.toCharArray("0"));
	
	public static List<Integer> toIntArray(String bitString) {
		return toIntArray(bitString,ONE_DEFAULT_SET,ZERO_DEFAULT_SET,8);
		
	}
	
	public static List<Integer> toIntArray(String bitString, Set<Character> oneSet, Set<Character> zeroSet, int length) {
		List<Integer> result = new ArrayList<Integer>();
		int cnt = 0;
		int current = -1;
		for(char c : bitString.toCharArray()) {
			boolean isOne = oneSet.contains(c);
			boolean isZero = zeroSet.contains(c);
			
			if (isOne || isZero) {
				if (current<0) {
					current = 0;
				}
				current = current*2 + (isOne?1:0);
				cnt++;
			}
			if ((!isOne && !isZero) || (cnt>=length)) {
				result.add(current);
				current = -1;		
				cnt=0;
			}
		}
		if (current>=0) {
			result.add(current);
		}
		return result;
		
	}
	
	public static String rotate(String bitString, int rot) {
		int realRot = (rot % bitString.length());
		int bl = bitString.length();
		if (realRot>0) {
			return bitString.substring(bl-realRot,bl)+bitString.substring(0,bl-realRot);
		}
		else {
			realRot = Math.abs(realRot);
			return bitString.substring(realRot,bl)+bitString.substring(0,realRot);
		}
	}

	public static String padTo(String bitString, int wantedLength) {
		if (bitString.length()>=wantedLength) {
			return bitString;
		}
		return String.format("%1$" + wantedLength + "s", bitString).replace(' ', '0');
	}

	public static String bitwiseOperation(String bit1, String bit2, BiFunction<Boolean,Boolean,Boolean> fct) {
		int max = Math.max(bit1.length(),bit2.length());
		String b1 = padTo(bit1,max);
		String b2 = padTo(bit2,max);
		StringBuilder result = new StringBuilder();
		for(int i=0;i<b1.length();i++) {
			result.append(fct.apply(b1.charAt(i)=='1',b2.charAt(i)=='1')?'1':'0');
		}
		return result.toString();
	}

	public static String or(String bit1,String bit2) {
		return bitwiseOperation(bit1,bit2, (b1,b2) -> b1 | b2);
	}

	public static String and(String bit1,String bit2) {
		return bitwiseOperation(bit1,bit2, (b1,b2) -> b1 & b2);
	}

	public static String xor(String bit1,String bit2) {
		return bitwiseOperation(bit1,bit2, (b1,b2) -> !(b1 & b2) & (b1 | b2));
	}

	public static <T extends Number> String toAscii(Collection<T> coll) {
		StringBuilder sb = new StringBuilder();
		for(Number n : coll) {
			int v = n.intValue();
			if (v<0 || v>255) {
				//LOG.warn("Non-ASCII char!");
				sb.append("?");
			}
			else {
				sb.append((char)v);
			}
		}
		return sb.toString();
	}

	public static String toBitString(Number n, int length) {
		Long l = Math.abs(n.longValue());
		Long b = 1l;
		char[] result = new char[length];
		int cnt = length-1;
		while (cnt>=0) {
			if ((l & b) >0) {
				result[cnt] = '1';
			}
			else {
				result[cnt] = '0';
			}
			b*=2;
			cnt--;
		}
		return new String(result);
	}

	public static String asciiToBitString(Character ascii) {
		return (toBitString((int)ascii.charValue(),8));
	}

}
