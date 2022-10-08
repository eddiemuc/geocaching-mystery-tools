package org.eddie.tools.geocaching.common;

public class Point {
	
	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;				
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Point add(int addX, int addY) {
		return new Point(this.x+addX, this.y+addY);
	}
	
	public Point north() {
		return new Point(this.x,this.y-1);
	}
	
	public Point south() {
		return new Point(this.x,this.y+1);
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Point)) {
			return false;
		}
		Point other = (Point)o;
		return x==other.x && y==other.y;
	}


}
