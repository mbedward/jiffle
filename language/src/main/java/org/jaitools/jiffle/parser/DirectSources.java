package org.jaitools.jiffle.parser;

import java.util.Arrays;

/**
 *
 * @author michael
 */
public class DirectSources {

    public static String falseValue() { return "0.0"; }
    
    public static String trueValue() { return "1.0"; }
    
    public static String nanValue() { 
        return String.valueOf(ConstantLookup.getValue("NAN")); 
    }
    
    public static String conCall(String ...args) {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("No args provided");
        }
        if (args.length > 4) {
            throw new IllegalArgumentException("Too many args: " + Arrays.toString(args));
        }
        switch (args.length) {
            case 1: return con1(args[0]);
            case 2: return con2(args[0], args[1]);
            case 3: return con3(args[0], args[1], args[2]);
            default: return con4(args[0], args[1], args[2], args[3]);
        }
    }

    private static String con1(String x) {
        return String.format(
                "(_stk.push(_FN.sign(%s)) == null ? "
                + "Double.NaN : (_stk.peek() != 0 ? 1.0 : 0.0))",
                x);
    }

    private static String con2(String x, String a) {
        return String.format(
                "(_stk.push(_FN.sign(%s)) == null ? "
                + "Double.NaN : (_stk.peek() != 0 ? (%s) : 0.0))",
                x, a);
    }
    
    private static String con3(String x, String a, String b) {
        return String.format(
                "(_stk.push(_FN.sign(%s)) == null ? "
                + "Double.NaN : (_stk.peek() != 0 ? (%s) : (%s)))",
                x, a, b);
    }

    private static String con4(String x, String a, String b, String c) {
        return String.format(
                "(_stk.push(_FN.sign(%s)) == null ? "
                + "Double.NaN : (_stk.peek() == 1 ? (%s) : " 
                + "(_stk.peek() == 0 ? (%s) : (%s))))",
                x, a, b, c);
    }
}
