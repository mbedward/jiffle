package org.jaitools.jiffle.parser;

/**
 * A message for an error, warning or general info.
 * 
 * @author michael
 */
public class Message {
    public static enum Level {
        ERROR,
        WARNING,
        INFO;
    }
    
    protected final Level level;
    protected final String msg;

    public Message(Level level, String msg) {
        this.level = level;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", level, msg);
    }
    
}
