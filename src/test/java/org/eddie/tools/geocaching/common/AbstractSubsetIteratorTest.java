package org.eddie.tools.geocaching.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbstractSubsetIteratorTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractSubsetIteratorTest.class);
	
	@Test
	public void testPairPermutator() {
		PairPermutator<Character> pp = PairPermutator.createFor("abcd");
		LOG.info("PERMUTATIONS:"+pp.count());
		int cnt = 0;
		Set<Character> pairsOfA = new HashSet<Character>();
		for(List<Character> perm:pp) {
			LOG.info(""+perm);
			cnt++;
			int indexOfA = perm.indexOf('a');
			//0->1, 1->0, 2->3, 3->2
			int pairIdx = (indexOfA/2)*2;
			if (indexOfA%2==0) {
				pairIdx++;
			}
			pairsOfA.add(perm.get(pairIdx));
		}
		LOG.info("Pairs of a:"+pairsOfA);
		assertEquals(3,pp.countAsLong());
		assertEquals(3,cnt);
		//search for all pairs of "a"
		assertEquals(3,pairsOfA.size());
		assertTrue(pairsOfA.contains('b'));
		assertTrue(pairsOfA.contains('c'));
		assertTrue(pairsOfA.contains('d'));
	}
	
	
	@Test
	public void testPermutator() {
		Permutator<String> p = new Permutator<String>("A","B","C");
		int cnt = 0;
		Map<String,Integer> cntMap = new HashMap<String, Integer>();
		for(List<String> e : p) {
			LOG.info("P: "+e);
			cnt++;
			Utils.addToMap(cntMap, e.get(0), 1);
		}
		assertEquals(6,p.countAsLong());
		assertEquals(6,cnt);
		assertEquals(3,cntMap.size());
		assertEquals(2,(int)cntMap.get("A"));
		assertEquals(2,(int)cntMap.get("B"));
		assertEquals(2,(int)cntMap.get("C"));
	}
	
	@Test
	public void testSizedSubset() {
		SizedSubsetIterator<String> ss = new SizedSubsetIterator<String>(Arrays.asList(new String[]{"A","B","C"}),2);
		
		List<List<String>> all = new ArrayList<List<String>>();
		for(List<String> s : ss) {
			LOG.info("SS:"+s);
			assertEquals(2,s.size());
			all.add(s);
		}
		
		assertEquals(3,ss.countAsLong());
		assertEquals(3,all.size());
		assertContains(all,"A","B");
		assertContains(all,"A","C");
		assertContains(all,"B","C");		
	}
	
	private static <T extends Comparable<T>> void assertContains(List<List<T>> listlist, T ... elements) {
		List<T> list = Arrays.asList(elements);
		Collections.sort(list);
		boolean found = false;
		for(List<T> l : listlist) {
			//boolean c = true;
			if (l.size()!=list.size()) {
				continue;
			}
			Collections.sort(l);
			for(int i=0;i<list.size();i++) {
				if (!l.get(i).equals(list.get(i))) {
					continue;
				}
			}
			found = true;
		}
		if (!found) {
			throw new RuntimeException("Did not find '"+list+"' in '"+listlist+"'");
		}
	}


}
