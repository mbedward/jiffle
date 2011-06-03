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

import jaitools.CollectionFactory;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the options block parser.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class OptionsBlockReaderTest {
    private String script;
    private MessageTable msgTable;
    private Map<String, String> options;
    private Map<String, String> expectedOptions;
    
    @Before
    public void setup() {
        expectedOptions = CollectionFactory.map();
    }
    

    @Test
    public void simpleBlock() throws Exception {
        System.out.println("   simple block");
        script = "options { outside = 0; } dest = 42;" ;

        parseOptions(script);
        assertMessages();
        expectedOptions.put("outside", "0");
        assertOptions();
    }
    
    @Test
    public void blockWithNewLines() throws Exception {
        System.out.println("   block with newlines");
        script =
                  "options { \n"
                + "  outside = 0; \n"
                + "} \n"
                + "dest = 42;" ;

        parseOptions(script);
        assertMessages();
        expectedOptions.put("outside", "0");
        assertOptions();
    }
    
    @Test
    public void emptyBlock() throws Exception {
        System.out.println("   empty block");
        script = "options { } dest = 42;" ;

        parseOptions(script);
        assertMessages();
        assertOptions();
    }
    
    @Test
    public void outsideNull() throws Exception {
        System.out.println("   outside option with null");
        script = "options { outside = null; } dest = 42;" ;
        
        parseOptions(script);
        assertMessages();
        expectedOptions.put("outside", "null");
        assertOptions();
    }

    @Test
    public void invalidOptionName() throws Exception {
        System.out.println("   invalid option");
        script = "options { foo = 0; } dest = 42;" ;
        
        parseOptions(script);
        assertMessages(Message.INVALID_OPTION);
    }
    
    @Test
    public void invalidOutsideOptionValue() throws Exception {
        System.out.println("   invalid outside option value");
        script = "options { outside = foo; } dest = 42;" ;
        
        parseOptions(script);
        assertMessages(Message.INVALID_OPTION_VALUE);
    }
    
    
    private void assertOptions() {
        assertEquals(expectedOptions.size(), options.size());
        for (String key : expectedOptions.keySet()) {
            assertTrue(expectedOptions.get(key).equals(options.get(key)));
        }
    }
    
    private void assertMessages(Message ...expectedMessages) {
        Map<String, List<Message>> messages = msgTable.getMessages();
        assertEquals(expectedMessages.length, messages.size());
    }
    
    private void parseOptions(String script) throws Exception {
        ANTLRStringStream stream = new ANTLRStringStream(script);
        JiffleLexer lexer = new JiffleLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        JiffleParser parser = new JiffleParser(tokens);
        CommonTree tree = (CommonTree) parser.prog().getTree();
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);

        msgTable = new MessageTable();
        OptionsBlockReader reader = new OptionsBlockReader(nodes, msgTable);
        reader.downup(tree);

        options = reader.getOptions();
    }

}
