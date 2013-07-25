package org.jaitools.jiffle.parser;

import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.Jiffle.ImageRole;
import org.jaitools.jiffle.parser.JiffleParser.BandSpecifierContext;
import org.jaitools.jiffle.parser.JiffleParser.ImageCallContext;
import org.jaitools.jiffle.parser.JiffleParser.ImagePosContext;
import org.jaitools.jiffle.parser.JiffleParser.PixelPosContext;
import org.jaitools.jiffle.parser.JiffleParser.PixelSpecifierContext;
import org.jaitools.jiffle.parser.JiffleParser.VarIDContext;
import org.jaitools.jiffle.parser.node.*;

/**
 * Tags occurrences of image variables with read or write nodes.
 * 
 * @author michael
 */
public class ImageVarWorker extends NodeWorker {
    private final Map<String, ImageRole> imageParams;
    
    private boolean image(String name) { 
        return imageParams.containsKey(name); 
    }
    
    private boolean srcImage(String name) { 
        return image(name) && imageParams.get(name) == Jiffle.ImageRole.SOURCE; 
    }
    
    private boolean destImage(String name) { 
        return image(name) && imageParams.get(name) == Jiffle.ImageRole.DEST; 
    }
    
    private Function mkfn(String name) {
        try {
            return new Function(name);
        } catch(UndefinedFunctionException ex) {
            // we should never be here
            throw new IllegalArgumentException(ex);
        }
    }
    

    public ImageVarWorker(
            ParseTree tree, 
            ParseTreeProperty<Node> nodes, 
            Map<String, Jiffle.ImageRole> imageParams) {
        
        super(nodes);
        this.imageParams = imageParams;
        walkTree(tree);
    }

    @Override
    public void exitImageCall(ImageCallContext ctx) {
        String name = ctx.ID().getText();
        if (srcImage(name)) {
            ImagePos pos = (ImagePos) get(ctx.imagePos());
            set(ctx, new ImageRead(name, pos));
        }
    }

    @Override
    public void exitImagePos(ImagePosContext ctx) {
        Band band = (Band) getOrElse(ctx.bandSpecifier(), Band.DEFAULT);
        Pixel pixel = (Pixel) getOrElse(ctx.pixelSpecifier(), Pixel.DEFAULT);
        set(ctx, new ImagePos(band, pixel));
    }

    @Override
    public void exitBandSpecifier(BandSpecifierContext ctx) {
        Expression e = (Expression) get(ctx.expression());
        set(ctx, new Band(e));
    }

    @Override
    public void exitPixelSpecifier(PixelSpecifierContext ctx) {
        PixelPosContext xctx = ctx.pixelPos(0);
        PixelPosContext yctx = ctx.pixelPos(1);
        
        Expression expr = (Expression) get(xctx.expression());
        Expression x = xctx.ABS_POS_PREFIX() == null ?
                new BinaryExpression("+", mkfn("x"), expr) : expr ;
        
        expr = (Expression) get(yctx.expression());
        Expression y = yctx.ABS_POS_PREFIX() == null ?
                new BinaryExpression("+", mkfn("y"), expr) : expr ;
        
        set(ctx, new Pixel(x, y));
    }

    @Override
    public void exitVarID(VarIDContext ctx) {
        String name = ctx.ID().getText();
        if (srcImage(name)) {
            set(ctx, new ImageRead(name, ImagePos.DEFAULT));
        }
    }
    
    
    
    
}
