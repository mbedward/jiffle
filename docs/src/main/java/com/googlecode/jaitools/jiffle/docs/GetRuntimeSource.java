/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.jaitools.jiffle.docs;

import jaitools.jiffle.Jiffle;
import jaitools.jiffle.JiffleBuilder;
import jaitools.jiffle.JiffleException;

/**
 *
 * @author michael
 */
public class GetRuntimeSource {
    
    // docs start getSourceFromJiffleBuilder
    public void getSourceFromJiffleBuilder(String script) throws JiffleException {
        JiffleBuilder builder = new JiffleBuilder();
        builder.script(script);
        
        // Set source and destination parameters, then...
        
        String runtimeSource = builder.getRuntimeSource();
    }
    // docs end getSourceFromJiffleBuilder
    
    
    // docs start getSourceFromJiffleObject
    public void getSourceFromJiffleObject(String script) throws JiffleException {
        Jiffle jiffle = new Jiffle();
        jiffle.setScript(script);
        
        // You have to compile the script before getting the runtime
        // source otherwise an Exception will be thrown
        jiffle.compile();
        
        // Get the Java source. The boolean argument specifies that we
        // want the input script copied into the class javadocs
        String runtimeSource = jiffle.getRuntimeSource(true);
    }
    // docs end getSourceFromJiffleObject
    
}
