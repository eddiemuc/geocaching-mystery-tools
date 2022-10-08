package org.eddie.tools.geocaching.common;

public final class NumericSystemUtils {

    private NumericSystemUtils() {
        //no instance
    }

    /**
     * Converts a number in one numeric system into another numeric system.
     * after 0-9, letters a-z are used for higher-level numbers. Thus this method can convert
     * up to systems with 36 digits for now.
     * @param inputNumber input
     * @param inputBase base for input
     * @param outputBase base for output
     * @return putput
     */
    public static String convert(String inputNumber, double inputBase, double outputBase) {
        //convert input to 10-based digit
        double tenbased = 0;
        double currBase = 1;
        for(int i=inputNumber.length()-1;i>=0;i--) {
            tenbased+=currBase*digitValue(inputNumber.charAt(i));
            currBase*=inputBase;
        }
        //convert 10-based digit to output;
        currBase = 1;
        int digitCount = 0;
        while (currBase<tenbased) {
            currBase*=outputBase;
            digitCount++;
        }
        currBase/=outputBase;
        StringBuilder output = new StringBuilder();
        for(int i=0;i<digitCount;i++) {
            int digit = (int)(tenbased/currBase);
            output.append(charValue(digit));
            tenbased-=digit*currBase;
            currBase/=outputBase;
        }
        return output.toString();

    }

    private static int digitValue(char c) {
        if (c >= '0' && c <= '9') {
            return (int)(c)-48;
        }
        if (c >='a' && c <= 'z') {
            return (int)(c) - 97+10;
        }
        if (c >='A' && c <= 'Z') {
            return (int)(c) - 65+10;
        }
        return 0;
    }

    private static char charValue(int i) {
        if (i>=0 && i<=9) {
            return (char)(i+48);
        }
        if (i>=10 && i<=35) {
            return (char)(i+65-10);
        }
        return 0;
    }

}
