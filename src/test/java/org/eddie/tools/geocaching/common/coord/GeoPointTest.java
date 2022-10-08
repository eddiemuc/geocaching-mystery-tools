package org.eddie.tools.geocaching.common.coord;

import org.eddie.tools.geocaching.common.CoordMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoPointTest {
	
	//Ein Beispiel zum Spielen
	private static final String WGS84_EXAMPLE = "N48 01.234 E011 41.543";
	private static final double LAT_EXAMPLE = 48.02056666666667;
	private static final double LON_EXAMPLE = 11.692383333333334;

	
	//Noch ein Beispiel zum Spielen
	private static final double MARIENPLATZ_LAT = 48.137393;
	private static final double MARIENPLATZ_LON = 11.575448;

	private static final double DELTA = 0.00001d;
	
	
	
	@Test
	public void wgs84Parsing()   {
		GeoPoint gc = GeoPoint.parseFromDegreeString(WGS84_EXAMPLE);
		System.out.println("GC:"+gc);
		assertEquals(LAT_EXAMPLE, gc.getLat(),0.000001d);
		assertEquals(LON_EXAMPLE, gc.getLon(),0.000001d);
		assertEquals(234,gc.getLatDegree().getMinuteDecimalThousands());
		assertEquals(543,gc.getLonDegree().getMinuteDecimalThousands());
	}
	
	@Test
	public void nontrivialParsing() {
		GeoPoint gc = GeoPoint.parseFromDegreeString("N48 05.000 E11 38.000");
		assertEquals(38,gc.getLonDegree().getMinuteWholePart());
		assertEquals(0,gc.getLonDegree().getMinuteDecimalThousands());
	}
	

	@Test
	public void degree() {
		double deg = CoordMath.degree(new double[]{2, 2}, new double[]{1,1},new double[]{-1,1});
		assertEquals(26.565051177077994, deg, DELTA);
	}

	@Test
	public void genau() {
		final int a = 0;
		final int b = 0;
		final int c = 0;

		final int north = 1000 + new java.util.Random(a).nextInt(500) - b;
		final int east = 45000 + Integer.SIZE * 10 + c;

		final String northString = north / 1000 + "." + String.format("%1$3s", "" + north % 1000).replace(' ', '0');
		final String eastString = east / 1000 + "." + String.format("%1$3s", "" + east % 1000).replace(' ', '0');

		System.out.println("N48 " + northString + " E011 " + eastString);
	}

	@Test
	public void toUtmAndBack() {
		GeoPoint MARIENPLATZ = GeoPoint.createFromDecimal(MARIENPLATZ_LAT,MARIENPLATZ_LON);
		GeoPoint utmAndBack = GeoPoint.createFromUtm(MARIENPLATZ,MARIENPLATZ.getUtm().getX(),MARIENPLATZ.getUtm().getY());
		assertEquals(MARIENPLATZ.getLat(),utmAndBack.getLat(),DELTA);
		assertEquals(MARIENPLATZ.getLon(),utmAndBack.getLon(),DELTA);
	}

}
