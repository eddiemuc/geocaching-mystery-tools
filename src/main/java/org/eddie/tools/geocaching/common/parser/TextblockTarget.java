package org.eddie.tools.geocaching.common.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextblockTarget {
	
	private static Logger LOG = LoggerFactory.getLogger(TextblockTarget.class);

	
	private List<String> lines = new ArrayList<String>();
	private String currentLine = "";
	private boolean forward = true;
	
	public TextblockTarget() {
	}
	
	public void reset() {
		lines.clear();
		currentLine = "";
		forward = true;
	}
	
	void newLine(boolean switchOrder) {
		if (switchOrder) {
			forward = !forward;			
		}
		lines.add(currentLine);
		currentLine = "";
	}
	
	public IParseAction<TextblockTarget> newLineAction(final boolean switchOrder) {
		return new IParseAction<TextblockTarget>() {
			
			public void doAction(TextblockTarget target, String... parameters) {
				target.newLine(switchOrder);
			}
		};
	
	}
	
	public IParseAction<TextblockTarget> add(final String text, final String reverseText) {
		return new IParseAction<TextblockTarget>() {
			
			public void doAction(TextblockTarget target, String... parameters) {
				int count = 1;
				if (parameters.length>1) {
					try {
						count = Integer.parseInt(parameters[1]);
					}
					catch(NumberFormatException nfe) {
						LOG.error("Trying to parse '"+parameters[1]+"' as number");
					}
				}
				for(int i=0;i<count;i++) {
					if (forward) {
						currentLine += text;
					}
					else {
						currentLine = reverseText + currentLine;
					}					
				}
			}
		};
	} 
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String l : lines) {
			sb.append(l).append("\n");
		}
		sb.append(currentLine);
		return sb.toString();
	}

}
