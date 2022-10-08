package org.eddie.tools.geocaching.common.coord;

public class UtmPoint implements I3dPoint {
	
	private double utmX;
	private double utmY;
	private double utmZ;
	private int utmZone;
	private boolean utmNorthernHemisphere;
	
	UtmPoint(double lat, double lon, double elevation) {
		Object[] utmRaw = TransformUtil.geodeticToUtm(lat, lon, elevation);
		this.utmX = (Double)utmRaw[0];
		this.utmY = (Double)utmRaw[1];
		this.utmZ = (Double)utmRaw[2];
		this.utmZone = (Integer)utmRaw[3];
		this.utmNorthernHemisphere = (Boolean)utmRaw[4];
	}

	public double getX() {
		return utmX;
	}

	public double getY() {
		return utmY;
	}

	public double getZ() {
		return utmZ;
	}
	
	public int getZone() {
		return utmZone;		
	}
	
	public boolean isNorthernHemisphere() {
		return utmNorthernHemisphere;
	}
	
	public String toString() {
		return "UTM["+getZone()+" "+Math.round(getX())+" "+Math.round(getY())+"]";
	}

}
