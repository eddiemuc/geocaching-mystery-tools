package org.eddie.tools.geocaching.common;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoordMathTest {
	
	@Test
	public void testCircleIntersection() {
		double[][] result = CoordMath.intersectPointsTwoCircles2d(new double[]{0, 1}, 1d, new double[]{2,1},1d);
		assertEquals(1,result.length);
		assertEquals(1d,result[0][0],0.00001d);
		assertEquals(1d,result[0][1],0.00001d);
	}
	
}
