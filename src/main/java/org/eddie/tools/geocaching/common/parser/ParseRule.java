package org.eddie.tools.geocaching.common.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParseRule<Ttarget> {
	
	private Pattern regexPattern;
	IParseAction<Ttarget> action;
	
	public ParseRule(String regex, IParseAction<Ttarget> action) {
		this.regexPattern = Pattern.compile("^("+regex+")(.*)$");
		this.action = action;
	}
	
	public boolean matches(String source) {
		return this.regexPattern.matcher(source).matches();
	}
	
	public String apply(String source, Ttarget context) {
		Matcher m = this.regexPattern.matcher(source);
		if (!m.matches()) {
			return source;
		}
		
		int groupCnt = m.groupCount();
		String newSource = m.group(groupCnt);
		String wholePattern = m.group(1);
		String[] params = new String[groupCnt-1];
		params[0] = wholePattern;
		for(int i=2;i<groupCnt;i++) {
			params[i-1] = m.group(i);
		}
		if (action!=null) {
			action.doAction(context, params);
		}
		
		return newSource;
		
	}
	
	
	

}
