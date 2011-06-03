/* 
 *  Copyright (c) 2011, Michael Bedward. All rights reserved. 
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

import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeParser;

/**
 * A base class for Jiffle tree parsers that want to intercept
 * ANTLR error and warning messages.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public abstract class ErrorHandlingTreeParser extends TreeParser {

    /** Stores error and warning messages. */
    protected ParsingErrorReporter errorReporter;
    
    /**
     * Constructor.
     *
     * @param input AST node stream
     */
    public ErrorHandlingTreeParser(TreeNodeStream input) {
        super(input);
    }
    
    /**
     * Constructor.
     * 
     * @param input input AST node stream
     * @param state recognizer state
     */
    public ErrorHandlingTreeParser(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    /**
     * Overrides the ANTLR parser method to capture an error message that
     * would otherwise be sent to std err.
     *
     * @param msg the message
     */
    @Override
    public void emitErrorMessage(String msg) {
        if (errorReporter != null) {
            errorReporter.addError(msg);
        } else {
            super.emitErrorMessage(msg);
        }
    }

    /**
     * Gets the error reporter object.
     *
     * @return the error reporter
     */
    public ParsingErrorReporter getErrorReporter() {
        return errorReporter;
    }

    /**
     * Sets the error reporter.
     *
     * @param er the error reporter (may be {@code null} if message
     *        interception is not required).
     */
    public void setErrorReporter(ParsingErrorReporter er) {
        errorReporter = er;
    }

}
