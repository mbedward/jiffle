package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.Token;

/**
 *
 * @author michael
 */
public class CompilerMessage {

    public static enum Level {
        ERROR,
        WARNING,
        INFO;
    }
    
    final Level level;
    final int line;
    final int pos;
    final String msg;

    public CompilerMessage(Level level, Token tok, String msg) {
        this(level, tok.getLine(), tok.getCharPositionInLine() + 1, msg);
    }
    
    public CompilerMessage(Level level, int line, int charPos, String msg) {
        this.level = level;
        this.line = line;
        this.pos = charPos;
        this.msg = msg;
    }
    
    @Override
    public String toString() {
        return String.format("%d:%d %s : %s", line, pos, level, msg);
    }
}
