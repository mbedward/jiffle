package org.jaitools.jiffle.parser;

import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jaitools.jiffle.parser.JiffleParser.OptionContext;
import org.jaitools.jiffle.parser.JiffleParser.OptionsBlockContext;

/**
 *
 * @author michael
 */
public class OptionsBlockReader extends TreeWorker {

    public final Map<String, String> options;
    private boolean readBlock = false;

    public OptionsBlockReader(ParseTree tree) {
        options = new HashMap<String, String>();
        walkTree(tree);
    }

    @Override
    public void enterOptionsBlock(OptionsBlockContext ctx) {
        if (readBlock) {
            messages.error(ctx.start, "Script has more than one options block");
        }
    }

    @Override
    public void exitOptionsBlock(OptionsBlockContext ctx) {
        if (!readBlock) {
            for (OptionContext oc : ctx.option()) {
                addOption(oc);
            }
            readBlock = true;
        }
    }

    private void addOption(OptionContext oc) {
        String name = oc.ID().getText();
        String value = oc.optionValue().getText();
        try {
            
            if (OptionLookup.isValidValue(name, value)) {
                options.put(name, value);
            } else {
                messages.error(oc.getStart(),
                        String.format("Invalid value (%s) for option %s",
                        value, name));
            }
        } catch (UndefinedOptionException ex) {
            messages.error(oc.getStart(), "Unknown option " + name);
        }
    }
}
