package org.eddie.tools.geocaching.common;

public class DoubleMatrix extends Matrix<Double> {
    
    public DoubleMatrix(int width, int height, Double ... values) {
        super(width,height,values);
    }
    
    public DoubleMatrix(DoubleMatrix other) {
        super(other);
    }
    
    public DoubleMatrix clone() {
        return new DoubleMatrix(this);
    }
    
    public DoubleMatrix multiply(DoubleMatrix other) {
        if (this.getWidth()!=other.getHeight()) {
            throw new RuntimeException("Can't multiply matrixes, "+this.getWidth()+"!="+this.getHeight());
        }
        
        DoubleMatrix result = new DoubleMatrix(other.getWidth(),this.getHeight());
        for(int x=0;x<result.getWidth();x++) {
            for(int y=0;y<result.getHeight();y++) {
                double d = 0;
                for(int i=0;i<this.getWidth();i++) {
                    d+=this.get(i,y)*other.get(x,i);
                }
                result.set(x,y,d);
            }
        }
        return result;
    }
}
