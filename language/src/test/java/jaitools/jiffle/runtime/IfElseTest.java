/*
 * Copyright 2011 Michael Bedward
 * 
 * This file is part of jai-tools.
 *
 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package jaitools.jiffle.runtime;

import org.junit.Test;

/**
 * Unit tests for if-else statements.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class IfElseTest extends StatementsTestBase {
    
    @Test
    public void ifWithExpression() throws Exception {
        System.out.println("   if statement with simple expression");
        String script = String.format(
                  "z = 0; \n"
                + "if (src > %d) z = 1; \n"
                + "dest = z;", NUM_PIXELS / 2);
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return (val > NUM_PIXELS / 2 ? 1.0 : 0.0);
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void ifWithBlock() throws Exception {
        System.out.println("   if statement with block");
        String script = String.format(
                  "z = 0; \n"
                + "if (src > %d) { \n"
                + "  z = src; \n"
                + "  z = z * z; \n"
                + "}\n"
                + "dest = z;", NUM_PIXELS / 2);
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return (val > NUM_PIXELS / 2 ? (val*val) : 0.0);
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void ifElseWithExpression() throws Exception {
        System.out.println("   if-else statement with simple expressions");
        String script = String.format(
                  "z = 0; \n"
                + "if (src < %d) \n"
                + "  z = 1; \n"
                + "else \n"
                + "  z = 2; \n"
                + "dest = z;", NUM_PIXELS / 2);

        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return (val < NUM_PIXELS / 2 ? 1.0 : 2.0);
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void ifElseWithBlocks() throws Exception {
        System.out.println("   if-else statement with blocks");
        String script = String.format(
                  "z = 0; \n"
                + "if (src < %d) { \n"
                + "  z = src; \n"
                + "  z = 2 * z; \n"
                + "} \n"
                + "else { \n"
                + "  z = src; \n"
                + "  z = 4 * z; \n"
                + "} \n"
                + "dest = z;", NUM_PIXELS / 2);

        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return (val < NUM_PIXELS / 2 ? 2*val : 4*val);
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void nestedIf() throws Exception {
        System.out.println("   nested if statement");
        String script = String.format(
                  "z = 0; \n"
                + "if (src < %d) { \n"
                + "  if (src < %d)"
                + "     z = 1; \n"
                + "  else \n"
                + "     z = 2;"
                + "}"
                + "dest = z;", NUM_PIXELS / 2, NUM_PIXELS / 4);

        Evaluator e = new Evaluator() {
            public double eval(double val) {
                return (val < NUM_PIXELS / 4 ? 1 : (val < NUM_PIXELS / 2) ? 2 : 0);
            }
        };
        
        testScript(script, e);
    }
    
    @Test
    public void ifTreatsNullAsFalse() throws Exception {
        System.out.println("   if statement treats null as false");
        String script = 
                  "val = con(src % 2, 1, null); \n"
                + "if (val) dest = 1; \n"
                + "else dest = 0;" ;
        
        Evaluator e = new Evaluator() {
            public double eval(double val) {
                if (((int)val) % 2 == 1) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        
        testScript(script, e);
    }
    
}
