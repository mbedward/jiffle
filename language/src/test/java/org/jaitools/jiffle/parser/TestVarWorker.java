package org.jaitools.jiffle.parser;

import java.io.InputStream;
import org.antlr.v4.runtime.tree.ParseTree;
import static org.hamcrest.CoreMatchers.*;
import org.jaitools.jiffle.util.Pair;
import static org.junit.Assert.*;
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
    
    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description desc) {
            System.out.println(desc.getMethodName());
        }
    };
    

    @Test
    public void scriptNodeIsAnnotatedWithGlobalScope() throws Exception {
        Pair<ParseTree, VarWorker> result = parseAndVarWorker("ValidScript.jfl");

        ParseTree tree = result.elem1();
        VarWorker worker = result.elem2();
        
        SymbolScope scope = worker.getProperties().get(tree);
        assertThat(scope, is(GlobalScope.class));
    }
    
    @Test
    public void imageVarsInGlobalScope() throws Exception {
        Pair<ParseTree, VarWorker> result = parseAndVarWorker("ValidScript.jfl");

        ParseTree tree = result.elem1();
        VarWorker worker = result.elem2();
        assertFalse(worker.messages.isError());
        
        SymbolScope scope = worker.getProperties().get(tree);
        
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
        Pair<ParseTree, VarWorker> result = parseAndVarWorker("InitBlockFooBar.jfl");

        ParseTree tree = result.elem1();
        VarWorker worker = result.elem2();
        assertFalse(worker.messages.isError());
        
        SymbolScope scope = worker.getProperties().get(tree);
        
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
        Pair<ParseTree, VarWorker> result = parseAndVarWorker(scriptFileName);
        assertTrue(hasError(result.elem2(), error));
    }

    private Pair<ParseTree, VarWorker> parseAndVarWorker(String scriptFileName) throws Exception {
        InputStream input = getClass().getResourceAsStream(scriptFileName);
        ParseTree tree = ParseHelper.parse(input);
        ImagesBlockWorker ib = new ImagesBlockWorker(tree);
        return Pair.of(tree, new VarWorker(tree, ib.imageVars));
    }

    private boolean hasError(VarWorker worker, Errors error) {
        if (!worker.messages.isError()) {
            return false;
        }
        
        boolean found = false;
        for (Message cm : worker.messages.getMessages()) {
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
