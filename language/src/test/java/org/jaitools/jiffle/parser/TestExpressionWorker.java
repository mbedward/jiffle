package org.jaitools.jiffle.parser;

import java.io.InputStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

/**
 *
 * @author michael
 */
public class TestExpressionWorker {

    @Test
    public void foo() throws Exception {
        doParseAndWork("ValidScript.jfl");
    }
    
    private void doParseAndWork(String scriptFileName) throws Exception {
        InputStream input = getClass().getResourceAsStream(scriptFileName);
        ParseTree tree = ParseHelper.parse(input);
        
        ImagesBlockWorker ibw = new ImagesBlockWorker(tree);
        VarWorker vw = new VarWorker(tree, ibw.imageVars);
        
        ExpressionWorker ew = new ExpressionWorker(tree, vw);
    }
    
}
