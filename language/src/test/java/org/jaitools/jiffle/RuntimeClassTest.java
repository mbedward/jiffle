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

package org.jaitools.jiffle;

import java.util.Map;

import org.jaitools.CollectionFactory;
import org.jaitools.jiffle.runtime.JiffleDirectRuntime;
import org.jaitools.jiffle.runtime.JiffleIndirectRuntime;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Unit tests for basic Jiffle object creation, setting attributes and compiling.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class RuntimeClassTest {
    
    private Jiffle jiffle;
    private Map<String, Jiffle.ImageRole> imageParams;
    
    @Before
    public void setup() {
        jiffle = new Jiffle();
        imageParams = CollectionFactory.map();
    }
    
    @Test
    public void getDirectRuntime() throws Exception {
        setupSingleDestScript();
        Object runtime = jiffle.getRuntimeInstance(Jiffle.RuntimeModel.DIRECT);
        assertTrue(runtime instanceof JiffleDirectRuntime);
    }

    @Test
    public void getIndirectRuntime() throws Exception {
        setupSingleDestScript();
        Object runtime = jiffle.getRuntimeInstance(Jiffle.RuntimeModel.INDIRECT);
        assertTrue(runtime instanceof JiffleIndirectRuntime);
    }

    @Test
    public void customDirectBaseClass() throws Exception {
        setupSingleDestScript();
        Object runtime = jiffle.getRuntimeInstance(MockDirectBaseClass.class);
        assertTrue(runtime instanceof MockDirectBaseClass);
    }    

    @Test
    public void customIndirectBaseClass() throws Exception {
        setupSingleDestScript();
        Object runtime = jiffle.getRuntimeInstance(MockIndirectBaseClass.class);
        assertTrue(runtime instanceof MockIndirectBaseClass);
    }
    
    @Test(expected=JiffleException.class)
    public void invalidBaseClass() throws Exception {
        class Foo extends NullRuntime { }
        
        setupSingleDestScript();
        Object runtime = jiffle.getRuntimeInstance(Foo.class);
    }

    private void setupSingleDestScript() throws JiffleException {
        String script = "dest = 42;";
        jiffle.setScript(script);
        
        imageParams.put("dest", Jiffle.ImageRole.DEST);
        jiffle.setImageParams(imageParams);
        jiffle.compile();
    }
    
}
