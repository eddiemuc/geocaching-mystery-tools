package org.eddie.tools.geocaching.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Primes {
	
	private static final Primes instance = new Primes();
	
	public static Primes get() {
		return instance;
	}
	
	private final Map<Long,Integer> primeMap = new HashMap<>();
	private final List<Long> primeList = new ArrayList<>();
	
	private Primes() {
		//prefill with first prime -> never empty :-)
		primeMap.put(2l,0);
		primeList.add(2l);
		primeMap.put(3l,1);
		primeList.add(3l);
		
	}
	
	private long getHighestKnownPrime() {
		return this.primeList.get(this.primeList.size()-1);
	}
	
	private long calculateNextPrime(long currentHighestKnownPrime) {
		long candidate = currentHighestKnownPrime+2;
		for(;;) {
			boolean isPrime = true;
			for(long p : this.primeList) {
				if (candidate%p==0) {
					isPrime = false;
					break;
				}
				if (p*p>candidate) {
					break;
				}
			}
			if (isPrime) {
				primeMap.put(candidate,primeList.size());
				primeList.add(candidate);
//				if (primeList.size()%1000==0) {
//					System.err.println("Calculated prime no. "+primeList.size()+": "+candidate);
//				}
				return candidate;
			}
			candidate+=2;
		}
	}
	
	private void fillUntilNumber(long number) {
		long currPrime = getHighestKnownPrime();
		while (currPrime < number) {
			currPrime = calculateNextPrime(currPrime);
		}
	}
	
	private void fillUntilIndex(int idx) {
		long currPrime = getHighestKnownPrime();
		while (this.primeList.size()<=idx) {
			currPrime = calculateNextPrime(currPrime);
		}
	}
	
	public boolean isPrime(long number) {
		fillUntilNumber(number);
		return primeMap.containsKey(number);
	}
	
	public long getPrime(int idxx) {
		int idx = idxx-1;
		if (idx<0) {
			return 0;
		}
		fillUntilIndex(idx);
		return primeList.get(idx);
	}
	
	public int getIndexForPrime(long prime) {
		if (!isPrime(prime)) {
			return -1;
		}
		return primeMap.get(prime)+1;
	}
	
	public String toString() {
		return "PRIMES["+primeList.size()+" primes, highest prime is "+getHighestKnownPrime()+"]";
	}

}
