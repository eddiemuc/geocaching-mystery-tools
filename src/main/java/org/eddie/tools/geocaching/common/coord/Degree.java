package org.eddie.tools.geocaching.common.coord;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Degree {
	
	private static final double DELTA = 0.00000001d;
	
	//toString-ing
	private static final DecimalFormat TWO_DIGITS = new DecimalFormat("00");
	private static final DecimalFormat THREE_DIGITS = new DecimalFormat("000");
	private static final DecimalFormat DEGREE_FORMAT = new DecimalFormat("0.000000",new DecimalFormatSymbols(Locale.US));
	
	
	//parsing
	private static final String SEP_SINGLE = "[\\s:-;/\\\"',.]";
	private static final String SEP_OPT = SEP_SINGLE + "*";
	private static final String SEP = SEP_SINGLE+"+";
	private final static String P_DEGREE_BASE = "([0-9]+)"+SEP+"([0-9]+(?:[,.][0-9]+)?)";
	private final static String P_DEGREE_SINGLE = "^"+SEP_OPT+"([NSnsEWew])"+SEP_OPT+P_DEGREE_BASE + SEP_OPT+"$"; 
	private final static String P_DEGREE_PAIR = "^"+SEP_OPT+"([NSns])"+SEP_OPT+P_DEGREE_BASE + SEP + "([EWew])"+SEP_OPT+P_DEGREE_BASE + SEP_OPT+"$"; 
	
	private final static Pattern PATTERN_DEGREE_SINGLE = Pattern.compile(P_DEGREE_SINGLE);
	private final static Pattern PATTERN_DEGREE_PAIR = Pattern.compile(P_DEGREE_PAIR);

	
	private final double deg;
	private final boolean northSouth;
	private final boolean isPositive;
	private final double minutes;
	
	public Degree(double deg, boolean northSouth) {
		this.isPositive = (deg>0d);
		this.deg = Math.abs(deg);
		this.northSouth = northSouth;
		this.minutes = (deg-Math.floor(deg+DELTA))*60;
	}
	
	public double getDecimal() {
		return isPositive?deg:-deg;
	}
	
	public int getDegreeWholePart() {
		return (int)Math.floor(deg+DELTA);
	}
	
	public int getMinuteWholePart() {
		return (int)Math.floor(minutes+DELTA);
	}
	
	public int getMinuteDecimalThousands() {		
		return (int)Math.round((minutes-Math.floor(minutes+DELTA))*1000);
	}
	
	public double getMinutes() {
		return minutes;
	}
	
	public String toString() {
		return toWgs84String();
	}
	
	public String toDegreeString() {
		return DEGREE_FORMAT.format(getDecimal());
	}
	
	public String toWgs84String() {
		StringBuilder sb = new StringBuilder();
		if (this.northSouth) {
			sb.append(this.isPositive?"N":"S");
		}
		else {
			sb.append(this.isPositive?"E":"W");
		}
		sb.append(TWO_DIGITS.format(getDegreeWholePart())).append(" ")
		.append(TWO_DIGITS.format(getMinuteWholePart())).append(".").append(THREE_DIGITS.format(getMinuteDecimalThousands()));
		
		return sb.toString();
		
	}
	
	/**
	 * expected String format is like "N48 01.017" OR "E011 41.817"
	 * creates a single degree instance
	 * @throws ParseException 
	 */
	public static Degree parse(String degreeString) throws ParseException {
		Matcher m = PATTERN_DEGREE_SINGLE.matcher(degreeString);
		if (!m.find()) {
			throw new ParseException("String could not be parsed as degree coordinate: '"+degreeString+"'", 0);
		}
		
		String nsew = m.group(1).toLowerCase();
		
		double lat = dbl(m.group(2))+dbl(m.group(3))/60.0;
		if ("s".equals(nsew) || "w".equals(nsew)) {
			lat = -lat;
		}
		return new Degree(lat,("s".equals(nsew) || "n".equals(nsew)));
		
	}
	
	/**
	 * expected String format is like "N48 01.017 E011 41.817"
	 * creates a lat/lon pair inside a double[2];
	 * @throws ParseException 
	 */
	public static Degree[] parsePair(String degreeString) {
		
		Matcher m = PATTERN_DEGREE_PAIR.matcher(degreeString);
		if (!m.find()) {
			throw new RuntimeException("ParsingException: String could not be parsed as degree coordinate: '"+degreeString+"'");
		}
		
		double lat = dbl(m.group(2))+dbl(m.group(3))/60.0;
		if ("s".equals(m.group(1).toLowerCase())) {
			lat = -lat;
		}
		double lon = dbl(m.group(5))+dbl(m.group(6))/60.0;
		if ("w".equals(m.group(4).toLowerCase())) {
			lon = -lon;
		}
		
		return new Degree[]{new Degree(lat,true),new Degree(lon,false)};
	}
	
	private static double dbl(String dbl) {
		return Double.parseDouble(dbl);
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Degree)) {
			return false;
		}
		Degree o = (Degree)other;
		return Math.abs(o.getDecimal()-this.getDecimal()) < 0.0001d;
		//return o.toWgs84String().equals(this.toWgs84String());		
	}



	

}
