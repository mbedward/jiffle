package org.jaitools.jiffle.parser.node;

import static org.hamcrest.CoreMatchers.*;
import org.jaitools.jiffle.parser.DirectSources;
import org.jaitools.jiffle.parser.FunctionInfo;
import org.jaitools.jiffle.parser.FunctionLookup;
import org.jaitools.jiffle.parser.JiffleType;
import org.jaitools.jiffle.parser.UndefinedFunctionException;
import org.jaitools.jiffle.util.Strings;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author michael
 */
public class TestNodes {
    
    @Test
    public void intLiteral() throws Exception {
        Node node = new IntLiteralNode("42");
        assertThat( node.toString(), is("42") );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void intLiteralRejectsFloatArg() throws Exception {
        Node node = new IntLiteralNode("1.2");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void intLiteralRejectsNonNumericArg() throws Exception {
        Node node = new IntLiteralNode("foo");
    }
    
    @Test
    public void bandDefault() throws Exception {
        assertThat( Band.DEFAULT.toString(), is("0") );
    }
    
    @Test
    public void bandWithIntLiteral() throws Exception {
        Band b = new Band( new IntLiteralNode("1") );
        assertThat( b.toString(), is("1") );
    }
    
    @Test
    public void pixelDefault() throws Exception {
        assertThat( Pixel.DEFAULT.toString(), is("_x,_y") );
    }
    
    @Test
    public void pixel() throws Exception {
        Expression x = new IntLiteralNode("42");
        Expression y = new Function("y");
        Pixel p = new Pixel(x, y);
        
        String expected = Strings.commas(x, y);
        assertThat( p.toString(), is(expected) );
    }
    
    @Test
    public void proxyFunction() throws Exception {
        String name = "xres";
        FunctionInfo info = getFnInfo(name);
        
        Function fn = new Function(name);
        assertThat(fn.toString(), is(info.getRuntimeName()));
    }

    @Test
    public void mathFunction() throws Exception {
        String name = "min";
        
        Expression[] args = { mockDExpr("a"), mockDExpr("b") };
        JiffleType[] argTypes = { JiffleType.D, JiffleType.D };
        
        Function fn = new Function(name, args);
        FunctionInfo info = getFnInfo(name, argTypes);
        
        String expected = info.getRuntimeName() + 
                String.format("(%s)", Strings.commas((Object[])args));
        
        assertThat(fn.toString(), is(expected));
    }
    
    @Test
    public void conFunction() throws Exception {
        Expression[] args = { mockDExpr("a"), mockDExpr("b"), mockDExpr("c") };
        String[] argStrs = {"a", "b", "c"};
        
        Node node = new ConFunction(args);
        String expected = DirectSources.conCall(argStrs);
        assertThat(node.toString(), is(expected));
    }
    
    @Test
    public void imageRead() throws Exception {
        Expression e = new ImageRead("src", ImagePos.DEFAULT);
        assertThat(e.toString(), is("readFromImage(src,_x,_y,0)"));
    }

    
    private FunctionInfo getFnInfo(String name, JiffleType ...argTypes) {
        try {
            return FunctionLookup.getInfo(name, argTypes);
        } catch (UndefinedFunctionException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private Expression mockDExpr(final String name) {
        return new Expression() {
            @Override
            public JiffleType getType() {
                return JiffleType.D;
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }
}
