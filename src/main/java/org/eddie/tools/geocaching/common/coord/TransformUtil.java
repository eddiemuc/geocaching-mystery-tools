package org.eddie.tools.geocaching.common.coord;

/**
 * Internal helper util class for Geopoint.
 */
final class TransformUtil {

	private static final double WGS_84_RADIUS = 6378137.0d;
	private static final double WGS_84_ECC_SQUARED = 0.00669438d;
	private static final double ECC_PRIME_SQUARED = WGS_84_ECC_SQUARED / (1 - WGS_84_ECC_SQUARED);
	private static final double ECC_SQUARED_2 = WGS_84_ECC_SQUARED * WGS_84_ECC_SQUARED;
	private static final double ECC_SQUARED_3 = ECC_SQUARED_2 * WGS_84_ECC_SQUARED;
	private static final double E_1 = (1 - Math.sqrt(1 - WGS_84_ECC_SQUARED)) / (1 + Math.sqrt(1 - WGS_84_ECC_SQUARED));
	private static final double K_0 = 0.9996;
	private static final double FALSE_EASTING = 500000.0d;
	private static final double FALSE_NORTHING = 10000000.0d;


	private TransformUtil() {
		//no instance
	}
	
	/**
	 * Converts a DECIMAL GeoPoint to UTM representataion
	 * Parameters are DECIMAL representation of geodetic coordinates
	 * returns an array with following content:
	 * [0]: x-value of UTM coordinate (double)
	 * [1]: y-value of UTM coordinate (double)
	 * [2]: z-value of UTM coordinate (double)
	 * [3]: UTM-zone (byte)
	 * [4]: whether coordinate is in northern hemisphere (boolean)
	 *
	 * TODO: handles only northern hemispehre as of now!
	 */
	public static Object[] geodeticToUtm(double lat, double lon, double elev) {

		final int zoneNumber = getZoneNumber(lat, lon);
		final double latRad = Math.toRadians(lat);
		final double longRad = Math.toRadians(lon);

		// in middle of zone
		final double longOrigin = (zoneNumber - 1) * 6 - 180 + 3; // +3 puts origin
		final double longOriginRad = Math.toRadians(longOrigin);

		final double tanLatRad = Math.tan(latRad);
		final double sinLatRad = Math.sin(latRad);
		final double cosLatRad = Math.cos(latRad);

		final double n = WGS_84_RADIUS / Math.sqrt(1 - WGS_84_ECC_SQUARED * sinLatRad * sinLatRad);
		final double t = tanLatRad * tanLatRad;
		final double c = ECC_PRIME_SQUARED * cosLatRad * cosLatRad;
		final double a = cosLatRad * (longRad - longOriginRad);

		final double m =
				WGS_84_RADIUS
						* ((1 - WGS_84_ECC_SQUARED / 4 - 3 * ECC_SQUARED_2 / 64 - 5 * ECC_SQUARED_3 / 256) * latRad
						- (3 * WGS_84_ECC_SQUARED / 8 + 3 * ECC_SQUARED_2 / 32 + 45 * ECC_SQUARED_3 / 1024) * Math.sin(2 * latRad)
						+ (15 * ECC_SQUARED_2 / 256 + 45 * ECC_SQUARED_3 / 1024) * Math.sin(4 * latRad) - (35 * ECC_SQUARED_3 / 3072)
						* Math.sin(6 * latRad));

		final double utmEasting =
				K_0
						* n
						* (a + (1 - t + c) * a * a * a / 6.0d + (5 - 18 * t + t * t + 72 * c - 58 * ECC_PRIME_SQUARED) * a * a * a
						* a * a / 120.0d) + FALSE_EASTING;

		final double utmNorthing =
				K_0 * (m + n
						* Math.tan(latRad)
						* (a * a / 2 + (5 - t + 9 * c + 4 * c * c) * a * a * a * a / 24.0d + (61 - 58 * t + t * t + 600 * c - 330 * ECC_PRIME_SQUARED)
						* a * a * a * a * a * a / 720.0d));

		//final char zoneLetter = getLetterDesignator(lat);

		//TODO: handles only northern hemisphere correctly as of now
		return new Object[]{utmEasting,utmNorthing,elev,zoneNumber,true};

	}

	private static int getZoneNumber(final double lat, final double lon) {
		// Make sure the longitude 180.00 is in Zone 60
		if (lon == 180) {
			return 60;
		}

		// Special zone for Norway
		if (lat >= 56.0f && lat < 64.0f && lon >= 3.0f && lon < 12.0f) {
			return 32;
		}

		// Special zones for Svalbard
		if (lat >= 72.0f && lat < 84.0f) {
			if (lon >= 0.0f && lon < 9.0f) {
				return 31;
			} else if (lon >= 9.0f && lon < 21.0f) {
				return 33;
			} else if (lon >= 21.0f && lon < 33.0f) {
				return 35;
			} else if (lon >= 33.0f && lon < 42.0f) {
				return 37;
			}
		}

		return (int) ((lon + 180) / 6) + 1;
	}


	/**
	 * returns an array with following content:
	 * [0]: latitude of geodetic coordinate (double)
	 * [1]: longitute of geodetic coordinate (double)
	 * [2]: elevation of geodetic coordinate (double)
	 */ 
	public static double[] utmToGeodetic(double easting, double northing, double z, int zoneNumber, boolean northernHemisphere) {
		// check the ZoneNumber is valid
		if (zoneNumber < 0 || zoneNumber > 60) {
			throw new IllegalArgumentException("ZoneNumber out of range [0-60]: " + zoneNumber);
		}

		// remove 500,000 meter offset for longitude
		final double x = easting - FALSE_EASTING;
		final double y = northing; //zoneLetter < 'N' ? northing - FALSE_NORTHING : northing;

		// There are 60 zones with zone 1 being at West -180 to -174
		final double longOrigin = (zoneNumber - 1) * 6 - 180 + 3; // +3 puts origin in middle of zone

		final double m = y / K_0;
		final double mu = m / (WGS_84_RADIUS * (1 - WGS_84_ECC_SQUARED / 4 - 3 * ECC_SQUARED_2 / 64 - 5 * ECC_SQUARED_3 / 256));

		final double phi1Rad =
				mu + (3 * E_1 / 2 - 27 * E_1 * E_1 * E_1 / 32) * Math.sin(2 * mu) + (21 * E_1 * E_1 / 16 - 55 * E_1 * E_1 * E_1 * E_1 / 32)
						* Math.sin(4 * mu) + (151 * E_1 * E_1 * E_1 / 96) * Math.sin(6 * mu);

		final double n1 = WGS_84_RADIUS / Math.sqrt(1 - WGS_84_ECC_SQUARED * Math.sin(phi1Rad) * Math.sin(phi1Rad));
		final double t1 = Math.tan(phi1Rad) * Math.tan(phi1Rad);
		final double c1 = ECC_PRIME_SQUARED * Math.cos(phi1Rad) * Math.cos(phi1Rad);
		final double r1 = WGS_84_RADIUS * (1 - WGS_84_ECC_SQUARED) / Math.pow(1 - WGS_84_ECC_SQUARED * Math.sin(phi1Rad) * Math.sin(phi1Rad), 1.5);
		final double d = x / (n1 * K_0);

		final double latRad =
				phi1Rad
						- (n1 * Math.tan(phi1Rad) / r1)
						* (d * d / 2 - (5 + 3 * t1 + 10 * c1 - 4 * c1 * c1 - 9 * ECC_PRIME_SQUARED) * d * d * d * d / 24 + (61 + 90
						* t1 + 298 * c1 + 45 * t1 * t1 - 252 * ECC_PRIME_SQUARED - 3 * c1 * c1)
						* d * d * d * d * d * d / 720);
		final double lonRad =
				(d - (1 + 2 * t1 + c1) * d * d * d / 6 + (5 - 2 * c1 + 28 * t1 - 3 * c1 * c1 + 8 * ECC_PRIME_SQUARED + 24 * t1 * t1)
						* d * d * d * d * d / 120)
						/ Math.cos(phi1Rad);
		return new double[]{Math.toDegrees(latRad), longOrigin + Math.toDegrees(lonRad), 0};
	}

}
