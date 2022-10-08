package org.eddie.tools.geocaching.common;

public class LongMatrix extends Matrix<Long> {
    
    public LongMatrix(int width, int height, Long ... values) {
        super(width,height,values);
    }
    
    public LongMatrix(LongMatrix other) {
        super(other);
    }
    
    public LongMatrix clone() {
        return new LongMatrix(this);
    }
    
    public LongMatrix multiply(LongMatrix other) {
        if (this.getWidth()!=other.getHeight()) {
            throw new RuntimeException("Can't multiply matrixes, "+this.getWidth()+"!="+this.getHeight());
        }
        
        LongMatrix result = new LongMatrix(other.getWidth(),this.getHeight());
        for(int x=0;x<result.getWidth();x++) {
            for(int y=0;y<result.getHeight();y++) {
                long d = 0;
                for(int i=0;i<this.getWidth();i++) {
                    d+=this.get(i,y)*other.get(x,i);
                }
                result.set(x,y,d);
            }
        }
        return result;
    }
}
