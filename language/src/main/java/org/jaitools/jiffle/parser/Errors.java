/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaitools.jiffle.parser;

/**
 *
 * @author michael
 */
public enum Errors {

    DUPLICATE_VAR_DECL("Duplicate variable declaration"),
    
    IMAGE_VAR_INIT_BLOCK("Image variable cannot be used in init block");

    private final String msg;
    
    private Errors(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
    
}
