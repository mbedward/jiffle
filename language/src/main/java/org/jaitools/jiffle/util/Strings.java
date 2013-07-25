package org.jaitools.jiffle.util;

/**
 * A few assorted string operations.
 *
 * @author michael
 */
public class Strings {
    
    /**
     * Calls toString on each object of args and concatenates the
     * results with space delimiters.
     */
    public static String spaces(Object ...args) {
        return concat(' ', args);
    }
    
    public static String commas(Object ...args) {
        return concat(',', args);
    }

    /**
     * Calls toString on each object of args and concatenates the
     * results delimited by sep.
     */
    public static String concat(char sep, Object[] args) {
        int n = args.length;
        if (n == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Object o : args) {
            sb.append(o);
            if (n-- > 1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }
    
    /**
     * Replacement for String.split that doesn't return
     * empty tokens.
     */
    public static String[] split(String s, String regex) {
        return s.replaceFirst("^" + regex, "").split(regex);
    }
    
}
