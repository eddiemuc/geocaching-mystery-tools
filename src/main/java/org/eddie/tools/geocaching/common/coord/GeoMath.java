package org.eddie.tools.geocaching.common.coord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eddie.tools.geocaching.common.CoordMath;

public final class GeoMath {
	
	public static final GeoPoint getAverage(Collection<I3dPoint> coords) {
		
		double xSum = 0d;
		double ySum = 0d;
		GeoPoint anyCoord = null;
		for(I3dPoint c : coords) {
			xSum+=c.getX();
			ySum+=c.getY();	
			//anyCoord = c;
		}
		return GeoPoint.createFromUtm(anyCoord, xSum/coords.size(), ySum/coords.size());
	}

	public static final GeoPoint getGeodaeticAverage(Collection<GeoPoint> coords) {
		
		double latSum = 0d;
		double lonSum = 0d;
		for(GeoPoint c : coords) {
			latSum+=c.getLat();
			lonSum+=c.getLon();	
		}
		return GeoPoint.createFromDecimal(latSum/coords.size(), lonSum/coords.size());
	}

	public static final GeoPoint getUtmAverage(Collection<GeoPoint> coords) {
		
		double xSum = 0d;
		double ySum = 0d;
		GeoPoint someCoord = null;
		for(GeoPoint c : coords) {
			someCoord = c;
			xSum+=c.getUtm().getX();
			ySum+=c.getUtm().getY();	
		}
		return GeoPoint.createFromUtm(someCoord, xSum/coords.size(), ySum/coords.size());
	}
	
	public static final GeoPoint getIntersection(GeoPoint a1, GeoPoint a2, GeoPoint b1, GeoPoint b2) {
		
		double[] raw = CoordMath.intersection2d(dblArrayUtm(a1), dblArrayUtm(a2),dblArrayUtm(b1),dblArrayUtm(b2));
		return GeoPoint.createFromUtm(a1,raw[0],raw[1]);
		
	}
	
	public static final List<GeoPoint> getTwoCircleIntersection(GeoPoint m1, double r1, GeoPoint m2, double r2) {
		
		double[][]raws = CoordMath.intersectPointsTwoCircles2d(dblArrayUtm(m1), r1, dblArrayUtm(m2),r2);
		List<GeoPoint> result = new ArrayList<GeoPoint>();
		if (raws!=null) {
			for(double[] r : raws) {
				result.add(GeoPoint.createFromUtm(m1, r[0], r[1]));
			}
		}
		return result;
		
		
	}
	
	
	public static final double getDegree(GeoPoint middle, GeoPoint a, GeoPoint b) {
		
		double[] md = new double[]{middle.getLat(),middle.getLon()}; 
		double[] ad = new double[]{a.getLat(),a.getLon()}; 
		double[] bd = new double[]{b.getLat(),b.getLon()};
		return CoordMath.degree(md, ad, bd);
		
	}
	
	public static final double getDistance(GeoPoint gc, GeoPoint a) {
		return CoordMath.distance(dblArrayUtm(gc), dblArrayUtm(a));
		
	}
	
	public static final double getDegree(GeoPoint middle, GeoPoint a) {
		return CoordMath.degree2d(dblArrayUtm(middle), dblArrayUtm(a));
	}
	
	private static double[] dblArrayUtm(GeoPoint gc) {
		return new double[]{gc.getUtm().getX(),gc.getUtm().getY()};
	}

	public static GeoPoint getCircumscribedCircle(final GeoPoint a, final GeoPoint b, final GeoPoint c) {
		final double[] raw = CoordMath.circumscribedCircle(dblArrayUtm(a), dblArrayUtm(b), dblArrayUtm(c));
		return GeoPoint.createFromUtm(raw[0], raw[1], a.getElevation(), a.getUtm().getZone(), a.getUtm().isNorthernHemisphere());
	}


}
