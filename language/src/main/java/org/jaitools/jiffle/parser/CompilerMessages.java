package org.jaitools.jiffle.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.Token;

/**
 * A holder for compiler messages.
 *
 * @author michael
 */
public class CompilerMessages {
    private List<CompilerMessage> messages = new ArrayList<CompilerMessage>();
    private boolean error;
    private boolean warning;
    
    public void error(Token tok, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.ERROR, tok, msg));
        error = true;
    }
    
    public void error(int line, int charPos, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.ERROR, line, charPos, msg));
        error = true;
    }

    public void warning(Token tok, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.WARNING, tok, msg));
        warning = true;
    }
    
    public void warning(int line, int charPos, String msg) {
        messages.add(new CompilerMessage(CompilerMessage.Level.WARNING, line, charPos, msg));
        warning = true;
    }

    public List<CompilerMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public boolean isError() {
        return error;
    }

    public boolean isWarning() {
        return warning;
    }
    
}
