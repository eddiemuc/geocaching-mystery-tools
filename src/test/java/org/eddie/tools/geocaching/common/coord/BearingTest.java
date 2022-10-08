package org.eddie.tools.geocaching.common.coord;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BearingTest {

    private static final GeoPoint CENTER = GeoPoint.createFromDecimal(49,11);

    @Test
    public void simpleBearingTest() {
        fundamentalTests(Bearing.SIMPLE);
        shortDistanceTests(Bearing.SIMPLE);
    }

    @Test
    public void complexBearingTest() {
        fundamentalTests(Bearing.COMPLEX);
        shortDistanceTests(Bearing.COMPLEX);

        //long distance
        assertBearing("N48 59.995 E011 08.225",CENTER,90,10000,Bearing.COMPLEX);
    }

    @Test
    public void utmBearingTest() {
        fundamentalTests(Bearing.UTM);
    }

    @Test
    public void fundamentalBearingTests() {
        for(Bearing b : Bearing.values()) {
            fundamentalTests(b);
        }
    }


    //close bearings which should be identical for different bearing types
    private void fundamentalTests(Bearing b) {
        //example from https://www.cachewiki.de/wiki/Wegpunktprojektion
        assertBearing("N48 10.694 E9 57.392", "N48 10.568 E9 57.314", 21, 250, b);

        //zero distance
        for (int i = 0; i < 8; i++) {
            assertBearing(CENTER, CENTER, i * 45, 0, b);
        }

        //identical for 360+ degrees
        assertBearing(b.calculateDeg(CENTER, 21, 250), CENTER, 360 + 21, 250, b);

        //identical for negative degrees
        assertBearing(b.calculateDeg(CENTER, 21, 250), CENTER, 21 - 360, 250, b);

        //VERY close bearing into 8 degree directions
        assertBearing(GeoPoint.createFromDecimal(49, 11.0013692598445), CENTER, 90, 100, b);
    }
    private void shortDistanceTests(Bearing b) {
        //
        assertBearing("N49 00.162 E011 00.000",CENTER,0,300,b);
        assertBearing("N49 00.114 E011 00.174",CENTER,45,300,b);
        assertBearing("N49 00.000 E011 00.246",CENTER,90,300,b);
        assertBearing("N48 59.886 E011 00.174",CENTER,135,300,b);
        assertBearing("N48 59.838 E011 00.000",CENTER,180,300,b);
        assertBearing("N48 59.886 E010 59.826",CENTER,225,300,b);
        assertBearing("N49 00.000 E010 59.754",CENTER,270,300,b);
        assertBearing("N49 00.114 E010 59.826",CENTER,315,300,b);
    }

    private void assertBearing(String expected, String start, double angleInDegree, double distanceInMeters, Bearing b) {
        assertBearing(GeoPoint.parseFromDegreeString(expected), GeoPoint.parseFromDegreeString(start),angleInDegree,distanceInMeters,b);
    }

    private void assertBearing(String expected, GeoPoint start, double angleInDegree, double distanceInMeters, Bearing b) {
        assertBearing(GeoPoint.parseFromDegreeString(expected),start,angleInDegree,distanceInMeters,b);
    }

    private void assertBearing(GeoPoint expected, GeoPoint start, double angleInDegree, double distanceInMeters, Bearing b) {
        System.out.println("Bearing '"+b+"' from ["+start+"], bear "+distanceInMeters+"m in "+angleInDegree+"° => expect: ["+expected+"]");
        assertEquals(expected,b.calculateDeg(start,angleInDegree,distanceInMeters));
    }

}
