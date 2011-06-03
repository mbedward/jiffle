/* 
 *  Copyright (c) 2009-2011, Michael Bedward. All rights reserved. 
 *   
 *  Redistribution and use in source and binary forms, with or without modification, 
 *  are permitted provided that the following conditions are met: 
 *   
 *  - Redistributions of source code must retain the above copyright notice, this  
 *    list of conditions and the following disclaimer. 
 *   
 *  - Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.   
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */   

package jaitools.jiffle.parser;

/**
 * Constants used by the Jiffle script and tree parsers to report errors 
 * and warnings.
 * 
 * @see Level
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public enum Message {
    
    /**
     * Error: Invalid use of an image variable for both input and output.
     */
    ASSIGNMENT_TO_SRC_IMAGE(Level.ERROR, 
            "Cannot assign a value to a non-destination image"),
    
    /**
     * Error: attempting to assign a value to a loop variable.
     */
    ASSIGNMENT_TO_LOOP_VAR(Level.ERROR,
            "Cannot assign a new value to a loop variable"),
    
    /**
     * Error: attempting to assign a list to a scalar variable.
     */
    ASSIGNMENT_LIST_TO_SCALAR(Level.ERROR,
            "Cannot assign a list to a scalar variable"),
    
    /**
     * Error: attempting to assign a scalar to a list variable.
     */
    ASSIGNMENT_SCALAR_TO_LIST(Level.ERROR,
            "Cannot assign a scalar to a list variable"),
    
    /**
     * Error: constant on the left hand side of an assignment.
     */
    CONSTANT_LHS(Level.ERROR,
            "Constant on the left hand side of an expression"),
    
    /**
     * Warning: an image variable parameter was passed to Jiffle but not 
     * used in the script.
     */
    IMAGE_NOT_USED(Level.WARNING,
            "Image variable is defined but not used"),
    
    /**
     * Error: trying to assign a value to an image variable in the init block.
     */
    IMAGE_VAR_INIT_LHS(Level.ERROR,
            "A value cannot be assigned to an image var in the init block"),
    
    /**
     * Error: using an assignment operator other than '=' with a 
     * destination image variable.
     */
    INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE(Level.ERROR,
            "Invalid assignment op with destination image"),
    
    /**
     * Error: invalid operation for a list variable.
     */
    INVALID_OPERATION_FOR_LIST(Level.ERROR,
            "Invalid operation for list variable"),
    
    /**
     * Warning: script option not recognized.
     */
    INVALID_OPTION(Level.WARNING,
            "Unrecognized option"),
    
    /**
     * Warning: invalid script option value is ignored.
     */
    INVALID_OPTION_VALUE(Level.WARNING,
            "Invalid option value ignored"),
    
    /**
     * Error: Image position syntax cannot be used with a destination image
     * variable.
     */
    IMAGE_POS_ON_DEST(Level.ERROR,
            "Image position cannot be specified for a destination image"),
    
    /**
     * Error: trying to use image position syntax with a non-image variable.
     */
    IMAGE_POS_ON_NON_IMAGE(Level.ERROR,
            "Image position specifier(s) used with a non-image variable"),
    
    /**
     * Error: list arg invalid for this function
     */
    NON_LIST_FUNCTION(Level.ERROR,
            "List argument cannot be used with this function"),
    
    /**
     * Error: trying to read from a destination image.
     */
    READING_FROM_DEST_IMAGE(Level.ERROR, 
            "Cannot read a value from a destination image"),
    
    /**
     * Error: source image variable cannot appear in the init block.
     */
    SRC_IMAGE_IN_INIT_BLOCK(Level.ERROR,
            "Source images cannot be referenced in an init block"),
    
    /**
     * Error: list operation with an undeclared list variable.
     */
    UNDECLARED_LIST_VAR(Level.ERROR,
            "List variable has not been declared"),
    
    /**
     * Error: call to an undefined function.
     */
    UNDEFINED_FUNCTION(Level.ERROR,
            "Call to undefined function"),
    
    /**
     * Error: a non-image variable used before being assigned a value.
     */
    UNINIT_VAR(Level.ERROR, 
            "Variable used before being assigned a value");
    
    private Level level;
    private String desc;

    private Message(Level level, String desc) {
        this.level = level;
        this.desc = desc;
    }
    
    /**
     * Tests if this is an error
     * @return {@code true} if an error, {@code false} otherwise
     */
    public boolean isError() {
        return level == Level.ERROR;
    }
    
    /**
     * Tests if this is a warning.
     * @return {@code true} if a warning, {@code false} otherwise
     */
    public boolean isWarning() {
        return level == Level.WARNING;
    }

    /**
     * Returns a formatted string for the error or warning.
     * @return a string
     */
    @Override
    public String toString() {
        if (isError()) {
            return "Error: " + desc;
        } else {
            return "Warning: " + desc;
        }
    }
}


