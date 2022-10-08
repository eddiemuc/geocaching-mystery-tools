package org.eddie.tools.geocaching.common.coord;

import java.util.function.BiFunction;

public enum Bearing {

    /**
     * Calcilate bearing as described here: https://de.wikipedia.org/wiki/Wegpunkt-Projektion (simplified variant)
     */
    SIMPLE( (s,d) -> {
        GeoPoint start = s;
        double angleInRadial = d[0];
        double distanceInMeters = d[1];

        //double distanceNorth = Math.cos(angleInRadial) * distanceInMeters;
        //double distanceEast = Math.sin(angleInRadial) * distanceInMeters;

        double diffLatInRad = Math.cos(angleInRadial) * distanceInMeters / 1853d;
        double diffLonInRad = diffLatInRad * Math.tan(angleInRadial) / Math.cos(start.getLat() * Math.PI / 180d + diffLatInRad/60d);

        return GeoPoint.createFromDecimal(start.getLat()+diffLatInRad /60d,start.getLon()+diffLonInRad /60d);
    }),
    /**
     * Calculate bearing as described here: https://de.wikipedia.org/wiki/Wegpunkt-Projektion (complex variant)
     */
    COMPLEX( (s, d) -> {
        GeoPoint start = s;
        double angleInRad = d[0];
        double distanceInMeters = d[1];

        double distanceInRad = distanceInMeters / 6371000d;
        double startLatInRad = s.getLat() * Math.PI / 180d;
        double startLonInRad = s.getLon() * Math.PI / 180d;

        double targetLatInRad = Math.asin(Math.sin(startLatInRad)*Math.cos(distanceInRad) + Math.cos(startLatInRad)*Math.sin(distanceInRad)*Math.cos(angleInRad));
        double targetLonInRad = startLonInRad + Math.asin(Math.sin(distanceInRad) / Math.cos(targetLatInRad) * Math.sin(angleInRad));

        return GeoPoint.createFromDecimal(targetLatInRad * 180d / Math.PI, targetLonInRad * 180d / Math.PI);
    }),
    /**
     * Calculate bearing using UTM representation of coordinates. Zone is not changed
     */
    UTM( (s,d) -> {
        GeoPoint start = s;
        double angleInRadial = d[0];
        double distanceInMeters = d[1];

        UtmPoint utm = start.getUtm();
        double newX = utm.getX()+Math.sin(angleInRadial)*distanceInMeters;
        double newY = utm.getY()+Math.cos(angleInRadial)*distanceInMeters;
        return GeoPoint.createFromUtm(start,newX,newY);
    });

    private final BiFunction<GeoPoint, Double[], GeoPoint> bearingCalc;

    private Bearing(BiFunction<GeoPoint, Double[], GeoPoint> bearingCalc) {
        this.bearingCalc = bearingCalc;
    }

    public GeoPoint calculateDeg(GeoPoint start, double angleInDegree, double distanceInMeters) {
        return calculate(start,angleInDegree*Math.PI/180d,distanceInMeters);
    }

    public GeoPoint calculate(GeoPoint start, double angleInRadial, double distanceInMeters) {
        return this.bearingCalc.apply(start, new Double[]{angleInRadial, distanceInMeters});
    }

}
