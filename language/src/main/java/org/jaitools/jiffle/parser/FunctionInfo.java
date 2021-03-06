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

package org.jaitools.jiffle.parser;

import java.util.Arrays;
import java.util.List;

import org.jaitools.CollectionFactory;


/**
 * Used by the {@link FunctionLookup} class when servicing lookup requests
 * from the Jiffle compiler.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class FunctionInfo {
    
    /** Constants to indicate the runtime provider of a function */
    public enum Provider {
        /** Indicates a function provided by JiffleFunctions class */
        JIFFLE("jiffle"),
        /** Indicates a function provided by java.lang.Math */
        MATH("math"),
        /** Indicates a function that is a proxy for a runtime class variable */
        PROXY("proxy");

        private String name;
        private Provider(String name) {
            this.name = name;
        }

        /**
         * Gets the {@code Provider} for a given provider name.
         *
         * @param name the provider name to look up
         *
         * @return the {@code Provider} or null if the name was not found
         */
        public static Provider get(String name) {
            String s = name.toLowerCase().trim();
            for (Provider p : Provider.values()) {
                if (p.name.equals(s)) {
                    return p;
                }
            }
            return null;
        }
    }
    
    private final String jiffleName;
    private final String runtimeName;
    private final Provider provider;
    private final boolean isVolatile;
    private final String returnType;
    private final List<String> argTypes;

    /**
     * Creates a function info object.
     *
     * @param jiffleName name of the function used in Jiffle scripts
     * 
     * @param runtimeName Java name used in runtime class source
     * 
     * @param provider the provider: one of {@link Provider#JIFFLE}, 
     *        {@link Provider#MATH} or {@link Provider#PROXY}
     * 
     * @param isVolatile {@code true} if the function returns a new value on each
     *        invocation regardless of pixel position (e.g. rand()); {@code false}
     *        otherwise
     * 
     * @param returnType function return type ("D", "List")
     * 
     * @param argTypes array of Strings specifying argument types; 
     *        null or empty for no-arg functions
     */
    public FunctionInfo(String jiffleName, String runtimeName, Provider provider, 
            boolean isVolatile, String returnType, String ...argTypes) {
        
        this.jiffleName = jiffleName;
        this.runtimeName = runtimeName;
        this.provider = provider;
        this.isVolatile = isVolatile;
        this.returnType = returnType;
        
        this.argTypes = CollectionFactory.list();
        if (argTypes != null && argTypes.length > 0) {
            this.argTypes.addAll(Arrays.asList(argTypes));
        }
    }

    /**
     * Gets the name of the function used in Jiffle scripts.
     *
     * @return Jiffle function name
     */
    public String getJiffleName() {
        return jiffleName;
    }

    /**
     * Gets the Java source for the function provider and name used
     * in the runtime class.
     *
     * @return runtime class source for the function
     */
    public String getRuntimeExpr() {
        switch (provider) {
            case MATH:
                return "Math." + runtimeName;
            case JIFFLE:
                // _FN is the instance of JiffleFunctions in AbstractJiffleRuntime
                return "_FN." + runtimeName;
            case PROXY:
                return runtimeName;
            default:
                throw new IllegalStateException("Internal compiler error: getRuntimeExpr");
        }
    }

    /**
     * Tests if this function is volatile, ie. returns a different value
     * on each invocation regardless of image position.
     *
     * @return {@code true} if volatile, {@code false} otherwise
     */
    public boolean isVolatile() {
        return isVolatile;
    }

    /**
     * Gets the number of arguments used by the function.
     *
     * @return number of arguments
     */
    public int getNumArgs() {
        return argTypes.size();
    }

    /**
     * Tests if this is a proxy function, ie. one that is translated to a
     * runtime class field defined by Jiffle. Examples are {@code x()} and
     * {@code width()}.
     *
     * @return {@code true} is a proxy function; {@code false} otherwise
     */
    public boolean isProxy() {
        return provider == Provider.PROXY;
    }
    
    /**
     * Gets the function return type.
     * 
     * @return return type: "D" or "List"
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * Tests if this object matches the given name and argument types.
     * 
     * @param name function name used in scripts
     * @param argTypes argument type names; null or empty for no-arg functions
     * 
     * @return {@code true} if this object matches; {@code false} otherwise
     */
    public boolean matches(String name, List<String> argTypes) {
        if (!this.jiffleName.equals(name)) {
            return false;
        }
        if ((argTypes == null || argTypes.isEmpty()) && !this.argTypes.isEmpty()) {
            return false;
        }
        if (argTypes != null && (argTypes.size() != this.argTypes.size())) {
            return false;
        }
        
        int k = 0;
        for (String argType : this.argTypes) {
            if (!argType.equals(argTypes.get(k++))) {
                return false;
            }
        }
        
        return true;
    }
}
