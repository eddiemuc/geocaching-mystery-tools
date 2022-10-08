package org.eddie.tools.geocaching.common.encoder;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BitEncoderTest {
	
	@Test
	public void simpleTest() {
		List<Integer> result = BitEncoder.toIntArray("11111111");
		assertEquals(1,result.size());
		assertEquals((Integer)255,(Integer)result.get(0));
	}
	
	@Test
	public void morecomplexTest() {
		List<Integer> result = BitEncoder.toIntArray("111111110000000011111111");
		assertEquals(3,result.size());
		assertEquals((Integer)255,(Integer)result.get(0));
		assertEquals((Integer)0,(Integer)result.get(1));
		assertEquals((Integer)255,(Integer)result.get(2));
	}
	
	@Test
	public void simpleRotates() {
		assertEquals("efabcd",BitEncoder.rotate("abcdef",2));
		assertEquals("cdefab",BitEncoder.rotate("abcdef",-2));
		assertEquals("abcdef",BitEncoder.rotate("abcdef",0));
	}


}
