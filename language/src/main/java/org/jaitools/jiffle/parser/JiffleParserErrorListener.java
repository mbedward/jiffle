package org.jaitools.jiffle.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 *
 * @author michael
 */
public class JiffleParserErrorListener extends BaseErrorListener {
    public CompilerMessages messages = new CompilerMessages();
    
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        messages.error(line, charPositionInLine + 1, msg);
    }
    
}
