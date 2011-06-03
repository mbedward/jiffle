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

import jaitools.jiffle.Jiffle;
import jaitools.jiffle.JiffleException;

/**
 * Defines methods for classes that generate runtime Java source from
 * compiled Jiffle scripts.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public interface SourceGenerator {
    
    /**
     * Sets the runtime model.
     * 
     * @param model runtime model
     */
    void setRuntimeModel(Jiffle.RuntimeModel model);
    
    /**
     * Sets the runtime base class name.
     * 
     * @param baseClassName base class name
     */
    void setBaseClassName(String baseClassName);

    /**
     * Returns the source for the runtime class. The runtime model and base class
     * name must be set before calling this method. If the script is passed to 
     * this method it will be included in the class javadocs.
     * 
     * @param script the Jiffle script which is being compiled; may be {@code null}
     *        or empty
     * 
     * @return source of the runtime class as a single String.
     * 
     * @throws JiffleException on errors creating source
     * @throws RuntimeException if the runtime model or base class name are not set
     */
    String getSource(String script) throws JiffleException;

}
