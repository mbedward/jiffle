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

package jaitools.jiffle;

import java.awt.geom.Rectangle2D;

import jaitools.jiffle.Jiffle.ImageRole;
import jaitools.jiffle.runtime.CoordinateTransform;
import jaitools.jiffle.runtime.JiffleRuntime;
import jaitools.jiffle.runtime.JiffleRuntimeException;
import java.util.Map;

/**
 * A stub class used in unit tests.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class NullRuntime implements JiffleRuntime {

    public Double getVar(String varName) {
        return null;
    }

    public void setVar(String varName, Double value) throws JiffleRuntimeException {}

    public void setWorldByResolution(Rectangle2D bounds, double xres, double yres) {}

    public void setWorldByNumPixels(Rectangle2D bounds, int nx, int ny) {}

    public boolean isWorldSet() {
        return true;
    }

    public double getMinX() {
        return 0;
    }

    public double getMaxX() {
        return 0;
    }

    public double getMinY() {
        return 0;
    }

    public double getMaxY() {
        return 0;
    }

    public double getWidth() {
        return 0;
    }

    public double getHeight() {
        return 0;
    }

    public double getXRes() {
        return 0;
    }

    public double getYRes() {
        return 0;
    }

    public long getNumPixels() {
        return 0;
    }

    public void setDefaultTransform(CoordinateTransform tr) {}

    public void setImageParams(Map<String, ImageRole> imageParams) {}

    public String[] getSourceVarNames() {
        return new String[0];
    }

    public String[] getDestinationVarNames() {
        return new String[0];
    }

    public String[] getVarNames() {
        return new String[0];
    }

}
