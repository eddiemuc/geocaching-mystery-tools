package org.eddie.tools.geocaching.common.encoder;

public class CaesarEncoder {
	
	public static String encode(String text, int key) {
		
		StringBuilder sb = new StringBuilder();
		for(char c : text.toUpperCase().toCharArray()) {
			char nc = c;
			if (c>='A' && c<='Z') {
				nc = (char)(c+key);
				if (nc>'Z') {
					nc = (char)(nc-26);
				}				
			}
			sb.append(nc);
		}
		
		return sb.toString();		
	}

}
