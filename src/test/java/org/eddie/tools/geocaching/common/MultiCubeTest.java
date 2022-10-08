package org.eddie.tools.geocaching.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertEquals;

public class MultiCubeTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(MultiCubeTest.class);
	
	@Test
	public void index() {
		MultiCube<Integer> m = new MultiCube<>(-1,3,4,5);
		assertEquals(3,m.getDimCount());
		assertEquals(3*5*4,m.getTotalElementCount());
		
		for(int i=0;i<3*4*5;i++) {
			int[] pos = m.indexToPos(i);
			int idx = m.posToIndex(pos);
			LOG.info("idx:"+i+" -> POS:"+new MultiCubePos(pos)+" -> "+idx);
			assertEquals(i,idx);
		}		
	}
	
	@Test
	public void streaming() {
		MultiCube<String> m = new MultiCube<String>("", 3,5,4);
		LOG.info("pti:"+m.posToIndex(new int[] {3,5,4}));
		LOG.info("pti:"+m.posToIndex(new int[] {2,4,3}));
		assertEquals(3*5*4,m.getTotalElementCount());
		long cnt = m.stream().filter(m.slice(1,2,3).slice(2, 0,1)).map(p -> { LOG.info("FILTERED: ->"+p);return p; } ).count();
		assertEquals(12,cnt); //=3*2*2
	}
	
	@Test
	public void toStringTest() {
		MultiCube<Integer> m = new MultiCube<>(-1,3,4,5);
		m.set(5, 1,2,3);
		m.set(6, 2,2,3);
		LOG.info("CUbe:"+m);
	}
	
	@Test
	public void parseStringTest() {
		MultiCube<Integer> m = new MultiCube<>(-5,2,7,2);
		m.set(2, 1,2,1);
		String mStr = m.toString();
		LOG.info("STRING: "+mStr);
		MultiCube<Integer> mClone = new MultiCube<Integer>(mStr,s -> Integer.parseInt(s), 2,7,2);
		String mCloneStr = mClone.toString();
		
		assertEquals(mStr,mCloneStr);
		
		
	}

}
