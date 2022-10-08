package org.eddie.tools.geocaching.common;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrimesTest {
	
	@Test
	public void simplePrimes() {
		assertTrue(Primes.get().isPrime(7));
		assertFalse(Primes.get().isPrime(6));
		assertTrue(Primes.get().isPrime(13));
		assertFalse(Primes.get().isPrime(15));
	}
	
	@Test
	public void highPrimes() {
		assertTrue(Primes.get().isPrime(10079));
	}
	
	@Test
	public void index() {
		assertEquals(1,Primes.get().getIndexForPrime(2));
		assertEquals(2,Primes.get().getIndexForPrime(3));
		assertEquals(4,Primes.get().getIndexForPrime(7));
		assertEquals(-1,Primes.get().getIndexForPrime(4));
	}

	@Test
	public void primeForIndex() {
		assertEquals(611953,Primes.get().getPrime(50000));
		System.out.println(Primes.get());
	}
	
}
