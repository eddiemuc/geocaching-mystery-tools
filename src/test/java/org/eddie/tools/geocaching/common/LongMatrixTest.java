package org.eddie.tools.geocaching.common;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LongMatrixTest {
    
    @Test
    public void traverse() {
        LongMatrix m = new LongMatrix(2,3,1l,2l,3l,4l,5l,6l);
        LongMatrix t = m.clone();
        t.traverse();
        System.out.println(m);
        System.out.println(t);
        t.traverse();
        
        assertEquals(m,t);
    }
    
    @Test
    public void multiply() {
        //example from wikipedia
        LongMatrix m = new LongMatrix(3,2,3l,2l,1l,1l,0l,2l);
        LongMatrix t = new LongMatrix(2,3,1l,2l,0l,1l,4l,0l);
        LongMatrix exp = new LongMatrix(2,2,7l,8l,9l,2l);
        LongMatrix r = m.multiply(t);
        System.out.println(m);
        System.out.println(t);
        System.out.println(r);
        
        assertEquals(exp,r);
    
    }
    
}
