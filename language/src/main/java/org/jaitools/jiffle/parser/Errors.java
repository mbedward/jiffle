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
    
    ASSIGNMENT_LIST_TO_SCALAR("Attempting to assign a list to a scalar variable"),
    
    ASSIGNMENT_SCALAR_TO_LIST("Attempting to assign a scalar to a list variable"),
    
    ASSIGNMENT_TO_CONSTANT("Attempting to assign a value to constant"),
    
    ASSIGNMENT_TO_LOOP_VAR("Cannot assign a new value to loop variable"),
    
    CON_CONDITION_MUST_BE_SCALAR("The first (condition) arg in a con expression must be a scalar variable"),
    
    CON_RESULTS_MUST_BE_SAME_TYPE("Alternative return values in a con expression must have same type"),
    
    DUPLICATE_VAR_DECL("Duplicate variable declaration"),
    
    IMAGE_VAR_INIT_BLOCK("Image variable cannot be used in init block"),
    
    INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE(
            "Invalid assignment operator with destination image variable"),
    
    INVALID_BINARY_EXPRESSION("Invalid binary expression"),
    
    LIST_AS_TERNARY_CONDITION("A list variable cannot be used as a condition in a ternary expression"),
    
    LIST_IN_RANGE("A range specifier must have scalar end-points, not list"),
    
    NOT_OP_IS_INVALID_FOR_LIST("Logical negation is not valid with a list variable"),
    
    POW_EXPR_WITH_LIST_EXPONENT("A list variable cannot be used as the exponent in a power expression"),
    
    READING_FROM_DEST_IMAGE("Attempting to read from destination image"),
    
    UNKNOWN_FUNCTION("Uknown function"),
    
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
