/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaitools.jiffle.parser;

/**
 * Constants for function and expression types.
 * Each is associated with a label used in the function properties file
 * (META-INF/org/jaitools/jiffle/FunctionLookup.properties).
 * 
 * @author michael
 */
public enum JiffleType {
    /** Scalar double */
    D("D"),
    
    /** List */
    LIST("LIST"),
    
    /** Not known (a placeholder type for the compiler). */
    UNKNOWN("Unknown");
    
    private final String label;
    
    private JiffleType(String label) {
        this.label = label;
    }
    
    /**
     * Gets the type with the given label (case-insensitive).
     * 
     * @param label type label
     * @return the matching type
     * @throws JiffleTypeException if no match exists
     */
    public static JiffleType get(String label) throws JiffleTypeException {
        String s = label.trim().toUpperCase();
        for (JiffleType rt : JiffleType.values()) {
            if (rt.label.equals(s)) {
                return rt;
            }
        }
        
        throw new JiffleTypeException(label);
    }

}
