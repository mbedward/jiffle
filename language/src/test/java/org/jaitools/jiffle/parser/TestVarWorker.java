package org.jaitools.jiffle.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jaitools.jiffle.Jiffle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 *
 * @author michael
 */
public class TestVarWorker {
    
    private static final boolean PRINT_WORKER_MESSAGES = true;
    
    private Map<String, Jiffle.ImageRole> imageParams;
    
    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description desc) {
            System.out.println(desc.getMethodName());
        }
    };
    
    @Before
    public void setup() {
        imageParams = new HashMap<String, Jiffle.ImageRole>();
    }
    

    @Test
    public void imageVarsInGlobalScope() throws Exception {
        VarWorker worker = parseAndWork("ValidScript.jfl");
        
        SymbolScope scope = worker.globalScope;
        String[] names = { "src", "dest" };
        Symbol.Type[] types = { Symbol.Type.SOURCE_IMAGE, Symbol.Type.DEST_IMAGE };
        
        int i = 0;
        for (String name : names) {
            assertTrue( scope.has(name) );
            assertTrue( scope.get(name).getType() == types[i++] );
        }
    }
    
    @Test
    public void initBlockVarsInGlobalScope() throws Exception {
        VarWorker worker = parseAndWork("InitBlockFooBar.jfl");
        assertFalse(worker.messages.isError());
        
        SymbolScope scope = worker.globalScope;
        assertTrue( scope.has("foo") );
        assertTrue( scope.has("bar") );
    }
    
    @Test
    public void initBlockWithDuplicateVar() throws Exception {
        assertScriptHasError(
                "InitBlockDuplicateVar.jfl", 
                Errors.DUPLICATE_VAR_DECL);
    }
    
    @Test
    public void initBlockWithImageVarOnLHS() throws Exception {
        assertScriptHasError(
                "InitBlockImageVarLHS.jfl", 
                Errors.IMAGE_VAR_INIT_BLOCK);
    }
    
    @Test
    public void readingFromDestinationImage() throws Exception {
        assertScriptHasError(
                "ReadingFromDestImage.jfl",
                Errors.READING_FROM_DEST_IMAGE);
    }
    
    @Test
    public void writingToSourceImage() throws Exception {
        assertScriptHasError(
                "WritingToSourceImage.jfl", 
                Errors.WRITING_TO_SOURCE_IMAGE);
    }
    
    @Test
    public void assignmentToConstant() throws Exception {
        assertScriptHasError(
                "AssignmentToConstant.jfl",
                Errors.ASSIGNMENT_TO_CONSTANT);
    }
    
    @Test
    public void assignmentToLoopVar() throws Exception {
        assertScriptHasError(
                "AssignmentToLoopVar.jfl",
                Errors.ASSIGNMENT_TO_LOOP_VAR);
    }
    
    @Test
    public void invalidAssignmentOpForDestinationImage() throws Exception {
        assertScriptHasError(
                "InvalidAssignmentOpForDestinationImage.jfl", 
                Errors.INVALID_ASSIGNMENT_OP_WITH_DEST_IMAGE);
    }
    
    private void assertScriptHasError(String scriptFileName, Errors error) throws Exception {
        VarWorker worker = parseAndWork(scriptFileName);
        assertTrue(hasError(worker, error));
    }

    private VarWorker parseAndWork(String scriptFileName) throws Exception {
        InputStream input = getClass().getResourceAsStream(scriptFileName);
        ParseTree tree = ParseHelper.parse(input);
        loadImageParams(tree);
        return new VarWorker(tree, imageParams);
    }

    private void loadImageParams(ParseTree tree) {
        ImagesBlockWorker ib = new ImagesBlockWorker(tree);
        imageParams.putAll(ib.imageVars);
    }

    private boolean hasError(VarWorker worker, Errors error) {
        if (!worker.messages.isError()) {
            return false;
        }
        
        boolean found = false;
        for (CompilerMessage cm : worker.messages.getMessages()) {
            if (PRINT_WORKER_MESSAGES) {
                System.out.println(cm);
            }
            
            if (cm.toString().contains(error.toString())) {
                found = true;
                if (!PRINT_WORKER_MESSAGES) {
                    break;
                }
            }
        }
        
        if (PRINT_WORKER_MESSAGES) {
            System.out.println();
        }
        
        return found;
    }

}
