package org.eddie.tools.geocaching.common;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FastIntSetTest {
	
	@Test
	public void simpleSetUsages() {
		Set<Integer> set = new FastIntSet(10);
		
		assertEquals(0,set.size());
		assertTrue(set.isEmpty());
		assertFalse(set.contains(1));
		
		set.add(1);
		set.add(2);
		set.add(2);
		assertEquals(2,set.size());
		assertFalse(set.isEmpty());
		
		assertTrue(set.contains(1));
		assertFalse(set.contains(3));
		assertEquals("[1, 2]",set.toString());
		System.out.println("Set:"+set);
		
		set.remove(1);
		assertTrue(set.contains(2));
		assertFalse(set.contains(1));
		
		set.clear();
		assertFalse(set.contains(2));
		assertFalse(set.iterator().hasNext());
		
		System.out.println("Set:"+set);
	}
	
	@Test
	public void addManyNumbers() {
		FastIntSet set = new FastIntSet(2);
		set.addAll(1,28);
		assertEquals(27,set.size());
		assertEquals(27,countItElements(set));
		
		set.remove(7);
		set.remove(12);
		assertEquals(25,set.size());
		assertEquals(25,countItElements(set));
		assertFalse(set.contains(7));
		assertFalse(set.contains(12));
		
	}
	
	private int countItElements(Set<Integer> set) {
		int c = 0;
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			it.next();
			c++;
		}
		return c;
	}
	
	@Test
	public void expand() {
		Set<Integer> set = new FastIntSet(2);
		set.add(4);
		set.add(400);
		
		assertEquals(2,set.size());
		assertEquals("[4, 400]",set.toString());		
	}
	
	@Test
	public void cloneTest() {
		FastIntSet set = new FastIntSet(5);
		set.add(5);
		set.add(80);
		Set<Integer> setClone = set.clone();
		
		setClone.add(3);
		
		assertEquals(2,set.size());
		assertEquals(3,setClone.size());		
	}
	
	@Test
	public void firstElement() {
		FastIntSet set = new FastIntSet(5);
		set.add(5);
		set.add(80);
		
		assertEquals(5,set.getMin());
		
		set.add(3);
		assertEquals(3,set.getMin());
		
		set.remove(3);
		assertEquals(5,set.getMin());
		
		set.remove(80);
		assertEquals(5,set.getMin());
		
	}

}
