package org.eddie.tools.geocaching.common;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LetterAnalyzerTest {
	
	
	@Test
	public void wordAnalyze() {
		analyse("aabbcc", "aa","bb","cc");
		
	}
	
	private static void analyse(String text, String ... words) {
		
		List<List<String>> res = LetterAnalyzer.findWords(text, Arrays.asList(words));
	
		System.out.println("Found "+res.size()+" results for '"+text+"' => '"+words+"': ");
		for(List<String> r : res) {
			System.out.println("  "+r);
		}
	
	}

}
