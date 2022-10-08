package org.eddie.tools.geocaching.common;

public class CoordMath {
	

	public static double distance(double[] c1, double[] c2) {
		return distance(c1, c2,c1.length);
	}

	
	public static double distance(double[] c1, double[] c2, int dim) {
		
		double quadSum = 0d;
		for(int i=0;i<dim;i++) {
			quadSum+=(c1[i]-c2[i])* (c1[i]-c2[i]);
		}
		return Math.sqrt(quadSum);		
	}
	
	public static double[] incircleCenter(double[] a, double[] b, double[] c) {
		//See https://de.wikipedia.org/wiki/Inkreis
		double da = distance(b,c);
		double db = distance(a,c);
		double dc = distance(a,b);
		double p = da+db+dc;
		
		double[] result = new double[2];
		result[0] = (da*a[0]+db*b[0]+dc*c[0])/p;
		result[1] = (da*a[1]+db*b[1]+dc*c[1])/p;
		return result;
	}

	public static double[] circumscribedCircle(double[] a, double[] b, double[] c) {
		//See https://de.wikipedia.org/wiki/Umkreis;
		double x1 = a[0];
		double y1 = a[1];
		double x2 = b[0];
		double y2 = b[1];
		double x3 = c[0];
		double y3 = c[1];

		double d = 2* (x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2));
		double xu = ((x1*x1+y1*y1)*(y2-y3)+(x2*x2+y2*y2)*(y3-y1)+(x3*x3+y3*y3)*(y1-y2)) / d;
		double yu = ((x1*x1+y1*y1)*(x3-x2)+(x2*x2+y2*y2)*(x1-x3)+(x3*x3+y3*y3)*(x2-x1)) / d;
		return new double[]{xu, yu};
	}
	
	public static double average(double ...numbers) {
		
		double avg = 0d;
		for(int i=0;i<numbers.length;i++) {
			avg+=numbers[i];
		}
		return avg/numbers.length;
		
	}
	
	public static double[] add(double[] u, double[] v) {
		double[] result = new double[u.length];
		for(int i=0;i<u.length;i++) {
			result[i] = u[i]+v[i];
		}
		return result;
	}
	
	public static double[] substract(double[] u, double[] v) {
		double[] result = new double[u.length];
		for(int i=0;i<u.length;i++) {
			result[i] = u[i]-v[i];
		}
		return result;
	}
	
	public static double degree2d(double[] middle, double[] b) {
		
		double angle = Math.toDegrees(Math.atan2(b[0]-middle[0], b[1]-middle[1]));
		angle = angle + Math.ceil( -angle / 360 ) * 360;
		return angle;
		
		//double[] a = new double[]{middle[0],middle[1]+100};
		//double f = degree(middle,a,b);
		
		//return (middle[0]>b[0])?360d-f:f; 
	}

	
	public static double degree(double[] middle, double[] a, double[] b) {
		double[] u = substract(a,middle);
		double[] v = substract(b,middle);
		
		double tmp = scalarProduct(u,v)/(distance(a,middle)*distance(b,middle));
		double d = Math.acos(tmp);
		return 360/(2*Math.PI)*d;		
	}
	
	public static double scalarProduct(double[] u, double[] v) {
		double result = 0;
		for(int i=0;i<u.length;i++) {
			result+=u[i]*v[i];
		}
		return result;
	}
	
	public static double[] intersection2d(double[] a1, double[] a2, double[] b1, double[] b2) {
		
		double x1 = a1[0];
		double y1 = a1[1];
		double x2 = a2[0];
		double y2 = a2[1];
		double x3 = b1[0];
		double y3 = b1[1];
		double x4 = b2[0];
		double y4 = b2[1];
		
		double xs=((x4-x3)*(x2*y1-x1*y2)-(x2-x1)*(x4*y3-x3*y4))/((y4-y3)*(x2-x1)-(y2-y1)*(x4-x3));
		double ys=((y1-y2)*(x4*y3-x3*y4)-(y3-y4)*(x2*y1-x1*y2))/((y4-y3)*(x2-x1)-(y2-y1)*(x4-x3));
		
		return new double[]{x2,ys};
	}
	
	public static double square(double d) {
		return d*d;
	}
	
	/**
	 * Given two circles A and B with middle points aMiddle/bMIddle and radius aRadius/bRadius, calculates intersection points of those circles
	 * If method returns null then there is NO intersection OR circles A and B are identical
	 * If method returns array of size 1 then there is only ONE intersection point
	 * If method returns array of size 2 ten there are two intersection points
	 * Formula is from here: http://walter.bislins.ch/blog/index.asp?page=Schnittpunkte+zweier+Kreise+berechnen+(JavaScript)
	 * 
	 * @param aMiddle
	 * @param aRadius
	 * @param bMiddle
	 * @param bRadius
	 * @return
	 */
	//
	public static double[][] intersectPointsTwoCircles2d(double[] aMiddle, double a, double[] bMiddle, double b) {
		double c = distance(aMiddle,bMiddle);
		System.out.println("C="+c);
		if (Math.abs(c)<0.000001d) {
			//middle point of two circles is identical, either no intersection or identical circles
			return null;
		}
		double a_square = square(a);
		double x = (a_square+square(c)-square(b)) / (2*c);
		//System.out.println("x="+x);
		double x_square = square(x);
		if (x_square>a_square) {
			//two circles with no intersection
			return null;
		}
		double y = Math.sqrt(a_square-x_square);
		//System.out.println("y="+y);
		
		double bax_c = (bMiddle[0]-aMiddle[0])/c;
		double bay_c = (bMiddle[1]-aMiddle[1])/c;
		
		double q1x = aMiddle[0] + x*bax_c - y*bay_c;
		double q1y = aMiddle[1] + x*bay_c + y*bax_c;
		if (y<0.000001d) {
			//only one intersection point
			//System.out.println("RESULT: "+q1x+"/"+q1y);
			return new double[][]{{q1x,q1y}};
		}
		double q2x = aMiddle[0] + x*bax_c + y*bay_c;
		double q2y = aMiddle[1] + x*bay_c - y*bax_c;
		return new double[][]{{q1x,q1y},{q2x,q2y}};
		
		
	}
	

}
