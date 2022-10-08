package org.eddie.tools.geocaching.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class LetterUtils {
	
	private LetterUtils() {
		//no instance
	}
	
//	1 Punkt: E (15), N (9), S (7), I (6), R (6), T (6), U (6), A (5), D (4)
//	2 Punkte: H (4), G (3), L (3), O (3)
//	3 Punkte: M (4), B (2), W (1), Z (1)
//	4 Punkte: C (2), F (2), K (2), P (1)
//	6 Punkte: x (1), J (1), x (1), V (1)
//	8 Punkte: x (1), X (1)
//	10 Punkte: Q (1), Y (1)
//	0 Punkte: Joker/Blanko (2)
	
	public static final Map<Character,Integer> SCRABBLE_VALUES = Utils.toMap(new HashMap<Character,Integer>(),
			'E',1,'N',1,'S',1,'I',1,'R',1,'T',1,'U',1,'A',1,'D',1,
			'H',2,'G',2,'L',2,'O',2,
			'M',3,'B',2,'W',2,'Z',2,
			'C',4,'F',4,'K',4,'P',4,
			'Ä',6,'J',6,'Ö',6,'V',6,
			'Ü',8,'X',8,'Q',10,'Y',10
);
	
	
	public static int letterCount(String text) {
		return clean(text).length();
	}
	
	public static int letterValue(char c) {
		if (c>='A' && c<='Z') {
			return c- (int)'A' +1;
		}
		if (c>='a' && c<='z') {
			return c- (int)'a' +1;
		}
		return 0;
	}
	
	public static int letterValueSum(String text) {
		return letterValueSum(text,null);
	}

	
	public static int letterValueSum(String text,Map<Character,Integer> map) {
		int sum = 0;
		for(char c : clean(text).toCharArray()) {
			if (map!=null && map.containsKey(Character.toUpperCase(c))) {
				sum+=map.get(Character.toUpperCase(c));
			}
			else {
				sum+=letterValue(c);
			}
		}
		return sum;		
	}
	
	public static int crosssum(int i) {
		int sum = 0;
		int ii = Math.abs(i);
		while (ii>0) {
			sum+=(ii%10);
			ii=ii/10;
		}
		return sum;
	}
	
	public static int absoluteCrosssum(int i) {
		int ii = Math.abs(i);
		while (ii>10) {
			ii = crosssum(ii);			
		}
		return ii;
	}
	
	public static String clean(String text) {
		if (text==null) {
			return "";
		}
		return text.replaceAll("\\s", "");
	}
	
	public static String sortLetters(String text, boolean caseSensitive) {
		List<Character> sorted = new ArrayList<Character>();
		for(char c : (caseSensitive?text:text.toUpperCase()).toCharArray()) {
			sorted.add(c);
		};
		Collections.sort(sorted);
		StringBuilder sb = new StringBuilder();
		for(Character cc : sorted) {
			sb.append(cc);
		}
		return sb.toString();				
	}

	public static String getStatistics(String text) {
		return getStatistics(text,false);
	}
	
	public static String getStatistics(String text, boolean caseSensitive) {
		Map<Character,Integer> letterCount = getStatisticsMap(text, caseSensitive);
		return statisticsMapToString(letterCount);
	}
	
	public static String statisticsMapToString(Map<Character,Integer> map) {
		List<Map.Entry<Character, Integer>> sortedResult = new ArrayList<Map.Entry<Character,Integer>>(map.entrySet());
		Collections.sort(sortedResult,new Comparator<Map.Entry<Character,Integer>>(){
			public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}});
		StringBuilder sb = new StringBuilder("[");
		boolean first = true;
		for(Map.Entry<Character, Integer> s : sortedResult) {
			if (s.getValue()==0) {
				continue;
			}
			if (!first) {
				sb.append(" ");
			}
			first = false;
			sb.append(s.getKey()+"("+s.getValue()+")");
			
		}
		return sb.append("]").toString();
	}
	
	public static Map<Character,Integer> getStatisticsMap(String text, boolean caseSensitive) {		
		Map<Character,Integer> letterCount = new HashMap<Character,Integer>();
		for(char c: (caseSensitive?text:text.toUpperCase()).toCharArray()) {
			if ((c>='A' && c<='Z') || (c>='a' && c<='z')) {
				int count = 0;
				if (!letterCount.containsKey(c)) {
					letterCount.put(c,0);
				}
				else {
					count = letterCount.get(c);
				}
				letterCount.put(c, ++count);
			}			
		}
		return letterCount;
	}
	
	

}
