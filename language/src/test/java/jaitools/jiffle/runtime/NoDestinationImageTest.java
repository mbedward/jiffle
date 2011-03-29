/*
 * Copyright 2011 Michael Bedward
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

import java.awt.image.RenderedImage;
import java.util.Random;

import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;
import jaitools.jiffle.JiffleException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for scripts with no destination image.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class NoDestinationImageTest {
    
    private static final int WIDTH = 10;

    private class TestData {
        RenderedImage image;
        int expectedCount;
    }
    
    @Test
    public void countValues() throws Exception {
        System.out.println("   counting pixels that meet a condition");
        
        final int testVal = 10;
        
        String script = String.format(
                  "images { src=read; } \n"
                + "init { count = 0; } \n"
                + "count += src > %d;",
                testVal);
        
        TestData testData = createTestData(testVal);
        
        Jiffle jiffle = getCompiledJiffle(script);
        JiffleDirectRuntime runtime = jiffle.getRuntimeInstance();
        runtime.setSourceImage("src", testData.image);
        runtime.evaluateAll(null);
        
        Double count = runtime.getVar("count");
        assertNotNull(count);
        assertEquals(testData.expectedCount, count.intValue());
    }
    
    @Test(expected=JiffleException.class)
    public void noImagesAtAll() throws Exception {
        System.out.println("   no source or destination images causes exception");
        
        String script = "answer = 42;" ;
        getCompiledJiffle(script);
    }
    
    private Jiffle getCompiledJiffle(String script) throws JiffleException {
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        
        return jiffle;
    }
    
    private TestData createTestData(int midPoint) {
        Integer[] data = new Integer[WIDTH * WIDTH];
        Random rr = new Random();
        int n = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] = (int) (2 * midPoint * rr.nextDouble());
            if (data[i] > midPoint) {
                n++;
            }
        }
        
        TestData testData = new TestData();
        testData.image = ImageUtils.createImageFromArray(data, WIDTH, WIDTH);
        testData.expectedCount = n;
        return testData;
    }
}
