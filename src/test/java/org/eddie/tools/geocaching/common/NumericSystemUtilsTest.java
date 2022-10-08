package org.eddie.tools.geocaching.common;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericSystemUtilsTest {

    @Test
    public void simple() {
        simpleTest("11111111","fF",16,2);
        simpleTest("102","33",8,5);
    }

    private void simpleTest(String exp, String input, double  inputBase, double outputBase) {
        System.out.println("Try to convert '"+input+"'["+inputBase+"] to ["+outputBase+"], expect '"+exp+"'");
        assertEquals(exp,NumericSystemUtils.convert(input,inputBase,outputBase));
    }
}
