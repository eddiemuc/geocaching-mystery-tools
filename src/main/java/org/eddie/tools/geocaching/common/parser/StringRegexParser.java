package org.eddie.tools.geocaching.common.parser;

import java.util.ArrayList;
import java.util.List;

public class StringRegexParser<Ttarget> {
	
	private List<ParseRule<Ttarget>> parseRules = new ArrayList<ParseRule<Ttarget>>();
	
	
	public StringRegexParser<Ttarget> addRule(String regex, IParseAction<Ttarget> action) {
		parseRules.add(new ParseRule<Ttarget>(regex,action));
		return this;		
	}
	
	public void parse(String source, Ttarget target) {
		
		String mSource = source;
		
		while (mSource!=null && !mSource.isEmpty()) {
			
			boolean matchFound = false;
			for(ParseRule<Ttarget> rule : this.parseRules) {
				if (rule.matches(mSource)) {
					matchFound = true;
					String prevSource = mSource;
					mSource = rule.apply(mSource, target);
					if (prevSource.equals(mSource)) {
						throw new RuntimeException("Rule did not modify string [rule="+rule+", string="+prevSource+"]");
					}
					break;
				}
			}
			if (!matchFound) {
				throw new RuntimeException("No matching Rule found for remaining source [source="+mSource+"]");
			}
		}
		
	}
	
	

}
