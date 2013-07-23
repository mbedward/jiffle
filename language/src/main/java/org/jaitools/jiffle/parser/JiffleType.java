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
    SCALAR_DOUBLE("D"),
    LIST("LIST");
    
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
