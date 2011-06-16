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

package org.jaitools.jiffle.runtime;

import java.util.ArrayList;
import java.util.List;

import org.jaitools.jiffle.JiffleException;


/**
 * The default abstract base class for runtime classes that implement
 * indirect evaluation.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public abstract class AbstractIndirectRuntime extends AbstractJiffleRuntime implements JiffleIndirectRuntime {
    
    /* 
     * Note: not using generics here because they are not
     * supported by the Janino compiler.
     */
    List sourceImageNames = new ArrayList();
    String destImageName;

    /**
     * Creates a new instance and initializes script-option variables.
     */
    public AbstractIndirectRuntime() {
        initOptionVars();
    }

    public void setDestinationImage(String varName) {
        try {
            doSetDestinationImage(varName, null);
        } catch (WorldNotSetException ex) {
            // Passing a null transform does not cause an Exception
        }
    }

    public void setDestinationImage(String varName, CoordinateTransform tr) 
            throws JiffleException {
        try {
            doSetDestinationImage(varName, tr);
        } catch (WorldNotSetException ex) {
            throw new JiffleException(String.format(
                    "Setting a coordinate tranform for a source (%s) without"
                    + "having first set the world bounds and resolution", varName));
        }
    }
    
    private void doSetDestinationImage(String varName, CoordinateTransform tr)
            throws WorldNotSetException {
        
        destImageName = varName;
        setTransform(varName, tr);
    }

    public void setSourceImage(String varName) {
        try {
            doSetSourceImage(varName, null);
        } catch (WorldNotSetException ex) {
            // Passing a null transform does not cause an Exception
        }
    }

    public void setSourceImage(String varName, CoordinateTransform tr) 
            throws JiffleException {
        
        try {
            doSetSourceImage(varName, tr);
        } catch (WorldNotSetException ex) {
            throw new JiffleException(String.format(
                    "Setting a coordinate tranform for a source (%s) without"
                    + "having first set the world bounds and resolution", varName));
        }
    }

    private void doSetSourceImage(String varName, CoordinateTransform tr)
            throws WorldNotSetException {
        
        sourceImageNames.add(varName);
        setTransform(varName, tr);
    }

}
