package org.jaitools.jiffle.parser.node;

import org.jaitools.jiffle.parser.Errors;
import org.jaitools.jiffle.parser.JiffleParser;
import org.jaitools.jiffle.parser.JiffleType;

/**
 *
 * @author michael
 */
public class BinaryExpression extends Expression {
    
    public static enum Operator {

        PLUS(JiffleParser.PLUS, "%s + %s"),
        MINUS(JiffleParser.MINUS, "%s - %s"),
        TIMES(JiffleParser.TIMES, "%s * %s"),
        DIV(JiffleParser.DIV, "%s / %s"),
        MOD(JiffleParser.MOD, "%s ^ %s"),
        
        POW(JiffleParser.POW, "math.pow(%s, %s)"),
        
        ASSIGN(JiffleParser.ASSIGN, "%s = %s"),
        PLUSEQ(JiffleParser.PLUSEQ, "%s += %s"),
        MINUSEQ(JiffleParser.MINUSEQ, "%s -= %s"),
        TIMESEQ(JiffleParser.TIMESEQ, "%s *= %s"),
        DIVEQ(JiffleParser.DIVEQ, "%s /= %s"),
        
        UNKNOWN(-1, "");
        
        private final int code;
        private final String fmt;

        private Operator(int code, String fmt) {
            this.code = code;
            this.fmt = fmt;
        }
        
        public static Operator get(int code) {
            for (Operator o : Operator.values()) {
                if (code == o.code) {
                    return o;
                }
            }
            
            return UNKNOWN;
        }
        
        public String getFormat() {
            return fmt;
        }
    }
        
    
    private final Expression left;
    private final Expression right;
    private final Operator op;
    

    private static JiffleType getReturnType(Expression left, Expression right) {
        if (left.getType() == right.getType()) {
            return left.getType();
        } else {
            // types are different so result must be list
            return JiffleType.LIST;
        }
    }

    public BinaryExpression(int opCode, Expression left, Expression right) 
            throws NodeException {
        
        super(getReturnType(left, right));
        
        this.op = Operator.get(opCode);
        if (op == Operator.UNKNOWN) {
            throw new NodeException(Errors.INVALID_BINARY_EXPRESSION);
        }

        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return String.format(op.getFormat(), left, right);
    }

}
