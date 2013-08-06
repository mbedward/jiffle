package org.jaitools.jiffle.parser;

import java.util.Arrays;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.parser.node.ImagePos;

/**
 *
 * @author michael
 */
public class DirectSources {

    public static String setDestValue(
            Jiffle.RuntimeModel runtimeModel, String destVar, String expr) {
        
        switch (runtimeModel) {
            case DIRECT:
                return String.format("writeToImage(%s, %s, %s)",
                        destVar, ImagePos.DEFAULT, expr);
                
            case INDIRECT:
                return "return " + expr;
                
            default:
                throw new IllegalArgumentException("Invalid runtime model: " + runtimeModel);
        }
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
