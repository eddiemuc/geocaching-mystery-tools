package org.eddie.tools.geocaching.common.encoder;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class KamasutraTest {

	
	@Test
	public void simpleTest() {
		KamasutraEncoder k = new KamasutraEncoder("ngoR",true);
		assertEquals("sRGGtaNmRONeG!",k.encode("SOnntagMorgen!"));
		
	}
}
