package org.eddie.tools.geocaching.common.coord;

import java.text.ParseException;

import org.eddie.tools.geocaching.common.Utils;

/**
 * GeoPoint represents a Geo-Point/GPS-Coordinate and provides methods for converting and/or displaying it.
 *
 * GeoPoint instances are immutable
 */
public class GeoPoint {
	
	
	
	//internal master representation is ALWAYS (!) in decimal lat/lon + elevation
	private final Degree lat;
	private final Degree lon;
	private final double elevation;
	private String name;
	
	//UTM representation is cached
	private UtmPoint utmCoord;
	
	
	private GeoPoint(double lat, double lon, double elevation) {
		this.lat = new Degree(lat,true);
		this.lon = new Degree(lon,false);
		this.elevation = elevation;
	}
	
	public double getLat() {
		return lat.getDecimal();		
	}
	
	public double getLon() {
		return lon.getDecimal();		
	}
	
	public Degree getLatDegree() {
		return lat;
	}

	public Degree getLonDegree() {
		return lon;
	}
	
	public double getElevation() {
		return elevation;		
	}
	
	public String getName() {
		return name;
	}
	
	public GeoPoint setName(String name) {
		this.name = name;
		return this;
	}

	public static GeoPoint createFromDecimal(double lat, double lon, double elevation) {
		return new GeoPoint(lat, lon, elevation);
	}
	
	public static GeoPoint createFromDecimal(double lat, double lon) {
		return new GeoPoint(lat, lon, 0);
	}
	
	public static GeoPoint createFromUtm(double x, double y, double z, int zone, boolean northernHemisphere) {
		double[] geodetic = TransformUtil.utmToGeodetic(x, y, z, zone, northernHemisphere);
		return new GeoPoint(geodetic[0],geodetic[1],geodetic[2]);
	}
	
	public static GeoPoint createFromUtm(GeoPoint base, double x, double y) {
		return createFromUtm(x,y,base.getUtm().getZ(),base.getUtm().getZone(),base.getUtm().isNorthernHemisphere());
	}


	/**
	 * expected String format is like "N48 01.123 E011 41.321"
	 * @throws ParseException 
	 */
	public static GeoPoint parseFromDegreeString(String whs84String) {
		Degree[] degs = Degree.parsePair(whs84String);
		return createFromDecimal(degs[0].getDecimal(), degs[1].getDecimal());			
	}

	/**
	 * expected format is like "11.321 48.234" (first lon then lat)
	 * @return
	 */
	public static GeoPoint parseFromWKT(String wktString) {
		String[] tokens = wktString.split(" ");
		return GeoPoint.createFromDecimal(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[0]));
	}
	
	public String toWgs84String() {		
		return getLatDegree().toString()+" "+getLonDegree().toString();
	}

	public String toWKTString() {
		return getLon()+" "+getLat();
	}
	
	public UtmPoint getUtm() {
		if (this.utmCoord==null) {
			this.utmCoord = new UtmPoint(this.getLat(),this.getLon(),this.elevation);
		}
		return this.utmCoord;		
	}

	public GeoPoint bearingDeg(double angleInDegree, double distanceInMeter) {
		return bearingDeg(angleInDegree,distanceInMeter,Bearing.COMPLEX);
	}

	public GeoPoint bearing(double angleInRadial, double distanceInMeter) {
		return bearing(angleInRadial,distanceInMeter,Bearing.COMPLEX);
	}

	public GeoPoint bearingDeg(double angleInDegree, double distanceInMeter, Bearing b) {
		return b.calculateDeg(this,angleInDegree,distanceInMeter);
	}

	public GeoPoint bearing(double angleInRadial, double distanceInMeter, Bearing b) {
		return b.calculate(this,angleInRadial,distanceInMeter);
	}
	
	public String toString() {
		return toWgs84String()+" / "+getLatDegree().toDegreeString()+" "+getLonDegree().toDegreeString()+" / "+getUtm().toString();
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof GeoPoint)) {
			return false;
		}
		GeoPoint o = (GeoPoint)other;
		return o.getLatDegree().equals(this.getLatDegree()) && o.getLonDegree().equals(this.getLonDegree()) &&
				Math.abs(o.getElevation()-this.getElevation()) < 0.0001d &&
				Utils.safeEquals(o.getName(),this.getName());
	}

	// see https://arthur-e.github.io/Wicket/sandbox-gmaps3.html
	// see https://en.wikipedia.org/wiki/Well-known_text_representation_of_geometry
	public static String toWKTLine(Iterable<GeoPoint> it) {
		return "LINESTRING"+toWKTLineIntern(it);
	}

	// see https://arthur-e.github.io/Wicket/sandbox-gmaps3.html
	// see https://en.wikipedia.org/wiki/Well-known_text_representation_of_geometry
	public static String toWKTMultiLine(Iterable<? extends Iterable<GeoPoint>> its) {
		StringBuilder sb = new StringBuilder("MULTILINESTRING(");
		boolean first = true;
		for(Iterable<GeoPoint> it : its) {
			if (!first) {
				sb.append(",");
			}
			first = false;
			sb.append(toWKTLineIntern(it));
		}
		sb.append(")");
		return sb.toString();
	}

	private static String toWKTLineIntern(Iterable<GeoPoint> it) {
		StringBuilder sb = new StringBuilder("(");
		boolean first = true;
		for(GeoPoint gc : it) {
			if (!first) {
				sb.append(",");
			}
			sb.append(gc.toWKTString());
			first = false;
		}
		sb.append(")");
		return sb.toString();

	}
	
	
	
	

}
