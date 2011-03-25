/*
 * Copyright 2009-2011 Michael Bedward
 * 
 * This file is part of jai-tools.
 *
 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package jaitools.jiffle.runtime;

import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;
import java.awt.image.WritableRenderedImage;
import org.junit.Test;

/**
 *
 * @author michael
 */
public class OptionsImageVarsTest extends StatementsTestBase {
    
    @Test
    public void foo() throws Exception {
        String script = 
                  "images { foo = write; }  foo = 42;";
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return 42;
            }
        };
        
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        
        runtimeInstance = (JiffleDirectRuntime) jiffle.getRuntimeInstance();
        WritableRenderedImage img = ImageUtils.createConstantImage(WIDTH, WIDTH, 0d);
        runtimeInstance.setDestinationImage("foo", img);
        runtimeInstance.evaluateAll(null);
        
        assertImage(null, img, e);
    }

}
