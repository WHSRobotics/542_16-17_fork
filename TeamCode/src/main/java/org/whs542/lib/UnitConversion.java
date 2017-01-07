package org.whs542.lib;

/**
 * A class for converting units of distance.
 * See below for usage examples.
 * Based almost entirely on {@link java.util.concurrent.TimeUnit}
 */

public enum UnitConversion {

    IN {
        public double toIN(double a){return a;}
        public double toFT(double a){return a/12;}
        public double toYD(double a){return a/36;}
        public double toMM(double a){return a*INTOMM;}
        public double toCM(double a){return a*INTOMM/10;}
        public double toM(double a){return a*INTOMM/1000;}
        public double convert(double d, UnitConversion c){
            return c.toIN(d);
        }
    },

    FT {
        public double toIN(double a){return a*12;}
        public double toFT(double a){return a;}
        public double toYD(double a){return a/3;}
        public double toMM(double a){return a*12*INTOMM;}
        public double toCM(double a){return a*1.2*INTOMM;}
        public double toM(double a){return a*0.012*INTOMM;}
        public double convert(double d, UnitConversion c){
            return c.toFT(d);
        }
    },

    YD {
        public double toIN(double a){return a*36;}
        public double toFT(double a){return a*12;}
        public double toYD(double a){return a;}
        public double toMM(double a){return a*36*INTOMM;}
        public double toCM(double a){return a*3.6*INTOMM;}
        public double toM(double a){return a*0.036*INTOMM;}
        public double convert(double d, UnitConversion c){
            return c.toYD(d);
        }
    },

    MM {
        public double toIN(double a){return a*MMTOIN;}
        public double toFT(double a){return a*MMTOIN/12;}
        public double toYD(double a){return a*MMTOIN/36;}
        public double toMM(double a){return a;}
        public double toCM(double a){return a/10;}
        public double toM(double a){return a/1000;}
        public double convert(double d, UnitConversion c){
            return c.toMM(d);
        }
    },

    CM {
        public double toIN(double a){return a*10*MMTOIN;}
        public double toFT(double a){return a*10*MMTOIN/12;}
        public double toYD(double a){return a*10*MMTOIN/36;}
        public double toMM(double a){return a*10;}
        public double toCM(double a){return a;}
        public double toM(double a){return a/100;}
        public double convert(double d, UnitConversion c){
            return c.toCM(d);
        }
    },

    M {
        public double toIN(double a){return a*1000*MMTOIN;}
        public double toFT(double a){return a*1000*MMTOIN/12;}
        public double toYD(double a){return a*1000*MMTOIN/36;}
        public double toMM(double a){return a/1000;}
        public double toCM(double a){return a/100;}
        public double toM(double a){return a;}
        public double convert(double d, UnitConversion c){
            return c.toM(d);
        }
    };

    //Conversion constants for inches to millimeters and vice-versa
    static final double INTOMM = 25.4;
    static final double MMTOIN = 0.03937;

    /**Supermethod for the convert methods
     *
     * Example: converting 6ft to mm
     * {@code UnitConversion.MM.convert(10, UnitConversion.FT)}
     *
     * @param originalValue the value to convert
     * @param originalUnit the original unit that the value is in
     * @return the converted value
     */
    public double convert(double originalValue, UnitConversion originalUnit){
        throw new AbstractMethodError();
    }

    /**Supermethods for the toUNIT methods
     * Allows for the usage of the methods in the return statement of the
     * {@link #convert(double originalValue, UnitConversion originalUnit)} methods
     */
    public double toIN(double a){throw new AbstractMethodError();}
    public double toFT(double a){throw new AbstractMethodError();}
    public double toYD(double a){throw new AbstractMethodError();}
    public double toMM(double a){throw new AbstractMethodError();}
    public double toCM(double a){throw new AbstractMethodError();}
    public double toM(double a){throw new AbstractMethodError();}


}
