package org.eddie.tools.geocaching.common;

import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextStatisticsTest {

    @Test
    public void testReplaceChars() {
        testReplaceChars("ABC","ABC");
        testReplaceChars("BBxxCC","AABBCC",'A','B');
        testReplaceChars("BBCCxx","AABBCC",'A','B','B','C');
    }

    @Test
    public void testEqualsPattern() {
        assertTrue(TextStatistics.equalsPattern("ABC","ABC"));
        assertFalse(TextStatistics.equalsPattern("aBC","BBC"));

    }

    public void testReplaceChars(String expected, String original, Character ... chars) {
        String replaced = TextStatistics.replaceChars(original, Utils.toMap(new HashMap<>(), (Object[]) chars));
        String msg = "Expected:"+expected+" ["+original+"]->("+Utils.toMap(new HashMap<>(), chars)+")->["+replaced+"]";
        System.out.println(msg);
        if (!TextStatistics.equalsPattern(expected,replaced)) {
            throw new RuntimeException("Result not as expected:"+msg);
        }
    }

}
