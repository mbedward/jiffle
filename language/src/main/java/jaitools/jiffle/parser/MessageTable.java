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

import java.util.List;
import java.util.Map;
import jaitools.CollectionFactory;
import java.util.Collections;

/**
 * Used by Jiffle parsers to record errors and warnings.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class MessageTable {

    private Map<String, List<Message>> errors = CollectionFactory.map();

    /**
     * Adds a message.
     * 
     * @param varName the variable that the message relates to
     * @param code the message code
     */
    public void add(String varName, Message code) {
        List<Message> codes = errors.get(varName);
        if (codes == null) {
            codes = CollectionFactory.list();
            errors.put(varName, codes);
        }
        codes.add(code);
    }
    
    /**
     * Checks if this table contains any error messages.
     * @return {@code true} if errors are present, {@code false} otherwise
     */
    public boolean hasErrors() {
        for (List<Message> codes : errors.values()) {
            for (Message code : codes) {
                if (code.isError()) return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if this table contains any warning messages.
     * @return {@code true} if warnings are present, {@code false} otherwise
     */
    public boolean hasWarnings() {
        for (List<Message> codes : errors.values()) {
            for (Message code : codes) {
                if (code.isWarning()) return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets all messages. The returned {@code Map} has variable names
     * as keys and {@code Lists} of messages as values.
     * 
     * @return all messages keyed by variable name
     */
    public Map<String, List<Message>> getMessages() {
        return Collections.unmodifiableMap(errors);
    }
}
