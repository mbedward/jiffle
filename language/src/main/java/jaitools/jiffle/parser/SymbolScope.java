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
import java.util.Collections;
import java.util.List;

/**
 * Stores symbols in a Jiffle script at a single scope level. Used during
 * script compilation.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class SymbolScope {

    private final String name;
    private final List<Symbol> symbols;

    /**
     * Creates a new scope.
     * 
     * @param name a scope label
     */
    public SymbolScope(String name) {
        this.name = name;
        this.symbols = CollectionFactory.list();
    }

    /**
     * Gets the scope label.
     * 
     * @return scope label
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of symbols in this scope. The list
     * is returned as an unmodifiable view.
     * 
     * @return list of symbols
     */
    public List<Symbol> getSymbols() {
        return Collections.unmodifiableList(symbols);
    }
    
    /**
     * Tests if this scope is empty.
     * 
     * @return {@code true} if there are no symbols; {@code false} otherwise
     */
    public boolean isEmpty() {
        return symbols.isEmpty();
    }
    
    /**
     * Gets the number of symbols in this scope.
     * 
     * @return number of symbols
     */
    public int size() {
        return symbols.size();
    }
    
    /**
     * Adds a symbol to this scope.
     * 
     * @param symbol the symbol
     * @throws IllegalArgumentException if {@code symbol} is {@code null}
     */
    public void add(Symbol symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("symbol must not be null");
        }
        symbols.add(symbol);
    }

    /**
     * Tests if this scope contains a symbol with the given name.
     * 
     * @param name symbol name
     * @return {@code true} if a symbol with this name is found; 
     *         {@code false} otherwise
     */
    public boolean hasSymbolNamed(String name) {
        return findSymbolNamed(name) != null;
    }
    
    /**
     * Gets the symbol with the given name if one exists.
     * 
     * @param name symbol name
     * @return the symbol or {@code null} if not match was found
     */
    public Symbol findSymbolNamed(String name) {
        for (Symbol s : symbols) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}
