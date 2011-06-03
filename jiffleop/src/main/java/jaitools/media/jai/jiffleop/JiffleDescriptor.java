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

package jaitools.media.jai.jiffleop;

import java.awt.Rectangle;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * Jiffle operation.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class JiffleDescriptor extends OperationDescriptorImpl {
    
    static final int SCRIPT_ARG = 0;
    static final int DEST_NAME_ARG = 1;
    static final int DEST_BOUNDS_ARG = 2;

    private static final String[] paramNames = {
        "script",
        "destName",
        "destBounds"
    };

    private static final Class[] paramClasses = {
         String.class,
         String.class,
         Rectangle.class
    };

    private static final Object[] paramDefaults = {
         NO_PARAMETER_DEFAULT,
         "dest",
         (Rectangle)null
    };

    public JiffleDescriptor() {
        super(new String[][]{
                    {"GlobalName", "Jiffle"},
                    {"LocalName", "Jiffle"},
                    {"Vendor", "jaitools.media.jai"},
                    {"Description", "Execute a Jiffle script"},
                    {"DocURL", "http://code.google.com/p/jai-tools/"},
                    {"Version", "1.2.0"},
                    {"arg0Desc", paramNames[0] + " (String):" +
                             "the Jiffle script"},
                    {"arg1Desc", paramNames[1] + " (String, default \"dest\"):" +
                             "the destination variable name"}

                },
                new String[]{RenderedRegistryMode.MODE_NAME},   // supported modes
                
                1,                                              // number of sources
                
                paramNames,
                paramClasses,
                paramDefaults,
                    
                null                                            // valid values (none defined)
                );
    }

    @Override
    public int getNumSources() {
        return 0;
    }

    
}
