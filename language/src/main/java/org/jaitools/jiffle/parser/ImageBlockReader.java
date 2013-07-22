package org.jaitools.jiffle.parser;

import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jaitools.jiffle.Jiffle;
import org.jaitools.jiffle.parser.JiffleParser.ImageVarDeclarationContext;
import org.jaitools.jiffle.parser.JiffleParser.ImagesBlockContext;

/**
 * /**
 * Collects image var declarations from the script image block (if present).
 *
 * @author michael
 */
public class ImageBlockReader extends TreeWorker {

    public final Map<String, Jiffle.ImageRole> imageVars;
    private boolean readBlock = false;

    /**
     * Creates a new reader which immediately walks the given tree.
     *
     * @param tree a parse tree
     */
    public ImageBlockReader(ParseTree tree) {
        imageVars = new HashMap<String, Jiffle.ImageRole>();
        walkTree(tree);
    }

    @Override
    public void enterImagesBlock(ImagesBlockContext ctx) {
        if (readBlock) {
            messages.error(ctx.start, "Script has more than one image block");
        }
    }

    @Override
    public void exitImagesBlock(ImagesBlockContext ctx) {
        if (!readBlock) {
            for (ImageVarDeclarationContext imgDecl : ctx.imageVarDeclaration()) {
                String imageName = imgDecl.ID().getText();

                switch (imgDecl.role().getStart().getType()) {
                    case JiffleParser.READ:
                        imageVars.put(imageName, Jiffle.ImageRole.SOURCE);
                        break;

                    case JiffleParser.WRITE:
                        imageVars.put(imageName, Jiffle.ImageRole.DEST);
                        break;

                    default:
                        Token tok = imgDecl.role().getStart();
                        messages.error(tok, "Invalid image var type ("
                                + tok.getText()
                                + "). Should be read or write.");
                }
            }
            readBlock = true;
        }
    }
}
