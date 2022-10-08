package org.eddie.tools.geocaching.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterAnalyzer {
	
	private final String original;
	private String current = null;
	private final Map<Character,Integer> statMap;
	private final Map<String,Integer> foundWords = new HashMap<String, Integer>();
	
	public LetterAnalyzer(String text) {
		this.original = text;
		this.statMap = LetterUtils.getStatisticsMap(text,true);
	}
	
	public String getOriginal() {
		return this.original;
	}
	
	public List<String> getFoundWordList() {
		List<String> result = new ArrayList<String>();
		for(Map.Entry<String,Integer> word : foundWords.entrySet()) {
			for(int i=0;i<word.getValue();i++) {
				result.add(word.getKey());
			}
		}
		return result;
	}
	
	public String getCurrent() {
		if (current == null) {
			current = LetterUtils.statisticsMapToString(this.statMap);
		}
		return this.current;
	}
	
	public void unfind(String word) {
		Utils.addToMap(this.foundWords,word,-1);
		for(char c : word.toCharArray()) {
			Utils.addToMap(this.statMap, c, 1);
		}
		this.current = null;
	}
	
	public int findableCount(String word) {
		int count = Integer.MAX_VALUE;
		Map<Character,Integer> wordMap = LetterUtils.getStatisticsMap(word, true);
		for(Map.Entry<Character,Integer> wordEntry : wordMap.entrySet()) {
			int findable = Utils.safeGetMap(this.statMap, wordEntry.getKey()) / wordEntry.getValue();
			if (findable==0) {
				return 0;
			}
			if (findable<count) {
				count = findable;
			}
			
		}
		return count;
	}
	
	public void find(String word) {
		if (this.findableCount(word) == 0) {
			throw new RuntimeException("WOrd '"+word+"' cannot be substracted from '"+this.getCurrent()+"'");
		}
		Utils.addToMap(this.foundWords, word, 1);
		for(char c : word.toCharArray()) {
			Utils.addToMap(this.statMap, c,-1);
		}
		this.current = null;
	}
	
	
	
	public static List<List<String>> findWords(String text, List<String> words) {
		LetterAnalyzer la = new LetterAnalyzer(text);
		return la.findRemainingWords(words);
	}
	
	public List<List<String>> findRemainingWords(List<String> words) {
		
		List<List<String>> result = new ArrayList<List<String>>();
		findWordsInternal(this, words, 0, result);
		return result;
	}
	
	private static void findWordsInternal(LetterAnalyzer text, List<String> words, int idx, List<List<String>> results) {
		String word = words.get(idx);
		int wCount = text.findableCount(word);
		for(int i=0;i<wCount;i++) {
			text.find(word);
		}
		for(int i=wCount;i>=0;i--) {
			if (idx+1>=words.size()) {
				List<String> wl = text.getFoundWordList();
				wl.add(text.getCurrent());
				results.add(wl);
			}
			else {
				findWordsInternal(text,words,idx+1,results);
			}
			if (i>0) {
				text.unfind(word);
			}
		}
		
	}
	
	
	
	

}
