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

/**
 * Represents a symbol in a Jiffle script. Used by {@link SymbolScopeStack}
 * during script compilation.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class Symbol {
    private final String name;
    private final SymbolType type;
    private final ScopeType scopeType;

    /**
     * Creates a new symbol.
     * 
     * @param name name as used in the Jiffle script
     * @param type type of symbol
     * @param scopeType scope of symbol 
     */
    public Symbol(String name, SymbolType type, ScopeType scopeType) {
        this.name = name;
        this.type = type;
        this.scopeType = scopeType;
    }

    /**
     * Gets this symbol's name.
     * 
     * @return symbol name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets this symbol's type.
     * 
     * @return symbol type
     */
    public SymbolType getType() {
        return type;
    }
    
    /**
     * Gets this symbol's scope type.
     * 
     * @return  symbol scope type
     */
    public ScopeType getScopeType() {
        return scopeType;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Symbol other = (Symbol) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.scopeType != other.scopeType) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 59 * hash + (this.scopeType != null ? this.scopeType.hashCode() : 0);
        return hash;
    }

}
