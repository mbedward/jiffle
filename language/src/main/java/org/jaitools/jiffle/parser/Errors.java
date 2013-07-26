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
    
    ASSIGNMENT_TO_CONSTANT("Attempting to assign a value to a constant"),

    DUPLICATE_VAR_DECL("Duplicate variable declaration"),
    
    IMAGE_VAR_INIT_BLOCK("Image variable cannot be used in init block"),
    
    INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE(
            "Invalid assignment operator with destination image variable"),
    
    READING_FROM_DEST_IMAGE("Attempting to read from destination image"),
    
    VAR_UNDEFINED("Variable not initialized prior to use"),
    
    WRITING_TO_SOURCE_IMAGE("Attempting to write to source image");

    private final String msg;
    
    private Errors(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
    
}
