package org.eddie.tools.geocaching.common.parser;

public interface IParseAction<Ttarget> {
	
	void doAction(Ttarget target, String ... parameters);

}
