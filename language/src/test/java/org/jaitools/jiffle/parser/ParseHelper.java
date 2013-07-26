package org.jaitools.jiffle.parser;

import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author michael
 */
public class ParseHelper {
    
    public static ParseTree parse(String script) {
        return doParse(new ANTLRInputStream(script));
    }
    
    public static ParseTree parse(InputStream input) throws IOException {
        return doParse(new ANTLRInputStream(input));
    }
    
    private static ParseTree doParse(CharStream stream) {
        JiffleLexer lexer = new JiffleLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        
        JiffleParser parser = new JiffleParser(tokens);
        return parser.script();
    }
    
}
