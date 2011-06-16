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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for ConstantLookup.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class ConstantLookupTest {
    
    private final double TOL = 1.0e-8;
    
    public void getPI() {
        assertEquals(Math.PI, ConstantLookup.getValue("M_PI"), TOL);
    }
    
    public void getPIOn2() {
        assertEquals(Math.PI / 2.0, ConstantLookup.getValue("M_PI_2"), TOL);
    }

    public void getPIOn4() {
        assertEquals(Math.PI / 4.0, ConstantLookup.getValue("M_PI_4"), TOL);
    }
    
    public void getSqrt2() {
        assertEquals(Math.sqrt(2.0), ConstantLookup.getValue("M_SQRT2"), TOL);
    }
    
    public void getE() {
        assertEquals(Math.E, ConstantLookup.getValue("M_E"), TOL);
    }
    
    public void getNanPrefix() {
        assertTrue(Double.isNaN( ConstantLookup.getValue("M_NaN")));
        assertTrue(Double.isNaN( ConstantLookup.getValue("M_NAN")));
    }
    
    public void getNanNoPrefix() {
        assertTrue(Double.isNaN( ConstantLookup.getValue("NaN")));
        assertTrue(Double.isNaN( ConstantLookup.getValue("NAN")));
    }

    @Test(expected=IllegalArgumentException.class)
    public void unknownConstant() {
        ConstantLookup.getValue("NotAConstant");
    }
    
    
}
