package org.eddie.tools.geocaching.common.encoder;

import java.util.HashMap;
import java.util.Map;

public class ReplacementEncoder {
	
	private final boolean doLowerUpper;
	//private String code = null;
	private final Map<Character, Character> map;
	
	public ReplacementEncoder(String code, boolean doLowerUpper) {
		this.doLowerUpper = doLowerUpper;
		this.map = mapFromString(code,doLowerUpper);		
	}
	
	private static Map<Character, Character> mapFromString(String code, boolean doLowerUpper) {
		Map<Character, Character> map = new HashMap<Character, Character>();
		for(int i=0;i<code.length()/2;i++) {
			map = addToMap(map,code.charAt(i*2),code.charAt(i*2+1),doLowerUpper);
		}
		return map;
	}
	
	private static Map<Character, Character> addToMap(Map<Character, Character> mmap, char cFrom, char cTo, boolean doLowerToUpper) {
		Map<Character, Character> map = mmap!=null?mmap:new HashMap<Character, Character>();
		if (doLowerToUpper) {
			map.put(Character.toLowerCase(cFrom), Character.toUpperCase(cTo));
		}
		else {
			map.put(cFrom, cTo);
		}
		return map;
	}
	
	public String encode(String text) {
		return encode(this.doLowerUpper?text.toLowerCase():text,this.map);
	}
	
	private static String encode(String text, Map<Character,Character> codeMap) {
		StringBuilder result = new StringBuilder();
		for(char ce : text.toCharArray()) {
			if (codeMap.containsKey(ce)) {
				result.append(codeMap.get(ce));
			}
			else {
				result.append(ce);
			}
						
		}
		return result.toString();
	}
	
}
