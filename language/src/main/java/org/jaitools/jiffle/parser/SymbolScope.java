/* 
 *  Copyright (c) 2011-2013, Michael Bedward. All rights reserved. 
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for symbol scope levels. 
 * <p>
 * Adapted from "Language Implementation Patterns" by Terence Parr,
 * published by The Pragmatic Bookshelf, 2010.
 * 
 * @author michael
 */
/**
 * A symbol scope level. 
 * <p>
 * Adapted from "Language Implementation Patterns" by Terence Parr,
 * published by The Pragmatic Bookshelf, 2010.
 * 
 * @author michael
 */
public abstract class SymbolScope {
    
    protected final String name;
    
    /** Parent scope or {@code null} if top level. */
    protected final SymbolScope enclosingScope;
    
    /** Symbols defined within this scope, keyed by name. */
    protected final Map<String, Symbol> symbols;

    /**
     * Creates a new instance.
     * 
     * @param name label for this scope
     * @param parent scope or {@code null} if top level
     */
    public SymbolScope(String name, SymbolScope enclosingScope) {
        this.name = name;
        this.enclosingScope = enclosingScope;
        symbols = new LinkedHashMap<String, Symbol>();
    }
    
    /**
     * Gets the name of this scope instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the parent scope or {@code null} if this is the
     * top scope.
     */
    public SymbolScope getEnclosingScope() {
        return enclosingScope;
    }

    /**
     * Adds a symbol to this scope.
     */
    public void add(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }
    
    /**
     * Tests if a symbol is defined in this scope or any
     * enclosing scope.
     */
    public boolean has(String name) {
        if (symbols.containsKey(name)) {
            return true;
        } else if (enclosingScope != null) {
            return enclosingScope.has(name);
        }
        return false;
    }

    /**
     * Searches for a symbol in this scope and, if not found,
     * any enclosing scopes.
     * 
     * @return the symbol
     * @throws IllegalArgumentException if the symbol is not found
     */
    public Symbol get(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        } else if (enclosingScope != null) {
            return enclosingScope.get(name);
        } else {
            throw new IllegalArgumentException(
                    "Missing symbol " + name + " in scope " + getName());
        }
    }
}
