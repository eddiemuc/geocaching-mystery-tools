package org.eddie.tools.geocaching.common;

import java.util.function.Predicate;

public class IntMatrix extends Matrix<Integer> {

	public IntMatrix(int width, int height, int defaultValue) {
		super(width, height, defaultValue);
	}
	
	public int colSum(int col) {
		return colSum(col,y -> true);
	}

	
	public int colSum(int col,Predicate<Point> choose) {
		int sum = 0;
		for(int y=0;y<getHeight();y++) {
			if (choose.test(new Point(col,y))) {
				sum+=get(col,y);
			}
		}
		return sum;
	}
	
	public int rowSum(int row) {
		return rowSum(row,y->true);
	}

	public int rowSum(int row,Predicate<Point> choose) {
		int sum = 0;
		for(int x=0;x<getWidth();x++) {
			if (choose.test(new Point(x,row))) {
				sum+=get(x,row);
			}
		}
		return sum;
	}
	
}
