/*
 * Copyright 2009 Michael Bedward
 * 
 * This file is part of jai-tools.

 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.

 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
 
 /** 
  *
  * @author Michael Bedward
  */

tree grammar TreeRebuilder;

options {
    tokenVocab = Jiffle;
    ASTLabelType = CommonTree;
    output = AST;
}

tokens {
    POS_VAR;
    IMAGE_VAR;
    SIMPLE_VAR;

    IMAGE_WRITE;
}

@header {
package jaitools.jiffle.parser;

import jaitools.jiffle.interpreter.FunctionTable;
import jaitools.jiffle.interpreter.JiffleRunner;
import jaitools.jiffle.interpreter.Metadata;
import jaitools.jiffle.interpreter.VarTable;
}

@members {
private boolean printDebug = false;
public void setPrint(boolean b) { printDebug = b; }

private Metadata metadata = null;

public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
}

private boolean isInfoFunc(String funcName) {
    return JiffleRunner.isInfoFunction(funcName);
}

private boolean isPosFunc(String funcName) {
    return JiffleRunner.isPositionalFunction(funcName);
}

private boolean isPosVar(String varName) {
    return metadata.getPositionalVars().contains(varName);
}

private boolean isImageVar(String varName) {
    return metadata.getImageVars().contains(varName);
}

private String getProxyVar(String funcName) {
    return JiffleRunner.getImageFunctionProxyVar(funcName);
}

}

start
@init{
    if (metadata == null) {
        throw new RuntimeException("failed to set metadata for TreeRebuilder");
    }
}
                : statement+ 
                ;

statement       : assignment
                | expr
                ;

expr_list       : ^(EXPR_LIST expr*)
                ;

assignment      : ^(ASSIGN assign_op var expr)
                  -> {isImageVar($var.text)}? ^(IMAGE_WRITE var expr)
                  -> ^(ASSIGN assign_op var expr)
                ;

expr            : ^(FUNC_CALL id=ID expr_list)
                  -> {isPosFunc($id.text)}? POS_VAR[getProxyVar($id.text)]
                  -> {isInfoFunc($id.text)}? SIMPLE_VAR[getProxyVar($id.text)]
                  -> ^(FUNC_CALL ID expr_list)
                  
                | ^(QUESTION expr expr expr)
                | ^(expr_op expr expr)
                | var
                | INT_LITERAL 
                | FLOAT_LITERAL 
                ;
                
var             :ID
                  -> {isPosVar($ID.text)}? POS_VAR[$ID.text]
                  -> {isImageVar($ID.text)}? IMAGE_VAR[$ID.text]
                  -> SIMPLE_VAR[$ID.text]
                ;
                
expr_op         : POW
                | TIMES 
                | DIV 
                | MOD
                | PLUS  
                | MINUS
                | OR 
                | AND 
                | XOR 
                | GT 
                | GE 
                | LE 
                | LT 
                | LOGICALEQ 
                | NE 
                ;

assign_op	: EQ
		| TIMESEQ
		| DIVEQ
		| MODEQ
		| PLUSEQ
		| MINUSEQ
		;
		
incdec_op       : INCR
                | DECR
                ;

unary_op	: PLUS
		| MINUS
		| NOT
		;
		
type_name	: 'int'
		| 'float'
		| 'double'
		| 'boolean'
		;

