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

import java.util.List;
import java.util.Map;

import jaitools.CollectionFactory;
import jaitools.jiffle.Jiffle;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for runtime object meta-functions.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class RuntimeMetafunctionsTest {

    @Test
    public void getSourceVarNames_ImageParamsMap() throws Exception {
        System.out.println("   setup with map and get source var names");
        
        JiffleDirectRuntime runtime = getRuntimeWithImageParams();
        String[] names = runtime.getSourceVarNames();
        assertEquals(3, names.length);
        
        List<String> expected = CollectionFactory.list();
        expected.add("src1");
        expected.add("src2");
        expected.add("src3");
        for (int i = 0; i < names.length; i++) {
            assertTrue(expected.contains(names[i]));
            expected.remove(names[i]);
        }
    }

    @Test
    public void getDestinationVarNames_ImageParamsMap() throws Exception {
        System.out.println("   setup with map and get dest var names");
        
        JiffleDirectRuntime runtime = getRuntimeWithImageParams();
        String[] names = runtime.getDestinationVarNames();
        assertEquals(3, names.length);
        
        List<String> expected = CollectionFactory.list();
        expected.add("dest1");
        expected.add("dest2");
        expected.add("dest3");
        for (int i = 0; i < names.length; i++) {
            assertTrue(expected.contains(names[i]));
            expected.remove(names[i]);
        }
    }
    
    @Test
    public void getSourceVarNames_ImagesBlock() throws Exception {
        System.out.println("   use images block and get source var names");
        
        JiffleDirectRuntime runtime = getRuntimeWithImagesBlock();
        String[] names = runtime.getSourceVarNames();
        assertEquals(3, names.length);
        
        List<String> expected = CollectionFactory.list();
        expected.add("src1");
        expected.add("src2");
        expected.add("src3");
        for (int i = 0; i < names.length; i++) {
            assertTrue(expected.contains(names[i]));
            expected.remove(names[i]);
        }
    }

    @Test
    public void getDestinationVarNames_ImagesBlock() throws Exception {
        System.out.println("   use images block and get dest var names");
        
        JiffleDirectRuntime runtime = getRuntimeWithImagesBlock();
        String[] names = runtime.getDestinationVarNames();
        assertEquals(3, names.length);
        
        List<String> expected = CollectionFactory.list();
        expected.add("dest1");
        expected.add("dest2");
        expected.add("dest3");
        for (int i = 0; i < names.length; i++) {
            assertTrue(expected.contains(names[i]));
            expected.remove(names[i]);
        }
    }
    
    @Test
    public void getImageScopeVarNames() throws Exception {
        String script = 
                  "images { dest=write; } "
                + "init { foo = 1; bar = 2; foz = 3; baz = 4; } "
                + "dest = 42;" ;

        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        JiffleDirectRuntime runtime = jiffle.getRuntimeInstance();
        
        String[] names = runtime.getVarNames();
        
        List<String> expected = CollectionFactory.list();
        expected.add("foo");
        expected.add("bar");
        expected.add("foz");
        expected.add("baz");
        
        assertEquals(expected.size(), names.length);
        for (int i = 0; i < names.length; i++) {
            assertTrue(expected.contains(names[i]));
            expected.remove(names[i]);
        }
    }
    

    private JiffleDirectRuntime getRuntimeWithImageParams() throws Exception {
        String script = "dest1 = src1; dest2 = src2; dest3 = src3;" ;

        Map<String, Jiffle.ImageRole> imageParams = CollectionFactory.map();
        imageParams.put("dest1", Jiffle.ImageRole.DEST);
        imageParams.put("dest2", Jiffle.ImageRole.DEST);
        imageParams.put("dest3", Jiffle.ImageRole.DEST);
        imageParams.put("src1", Jiffle.ImageRole.SOURCE);
        imageParams.put("src2", Jiffle.ImageRole.SOURCE);
        imageParams.put("src3", Jiffle.ImageRole.SOURCE);

        Jiffle jiffle = new Jiffle(script, imageParams);
        return jiffle.getRuntimeInstance();
    }
    
    private JiffleDirectRuntime getRuntimeWithImagesBlock() throws Exception {
        String script = 
                  "images {"
                + "  src1=read; src2=read; src3=read;"
                + "  dest1=write; dest2=write; dest3=write;"
                + "}"
                + "dest1 = src1; dest2 = src2; dest3 = src3;" ;

        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        jiffle.compile();
        return jiffle.getRuntimeInstance();
    }
    
}
