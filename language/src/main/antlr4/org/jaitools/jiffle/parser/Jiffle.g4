/*
 * Copyright (c) 2009-2013 Michael Bedward. All rights reserved.
 *
 *  This file is part of JAITools.
 *
 *  Redistribution and use in source and binary forms, with or without modification, 
 *  are permitted provided that the following conditions are met: 
 *   
 *  - Redistributions of source code must retain the above copyright notice, this  
 *    list of conditions and the following disclaimer. 
 *   
 *  - Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.   
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

grammar Jiffle;

script          : specialBlock* statement+ EOF
                ;

specialBlock    : optionsBlock
                | imagesBlock
                | initBlock
                ;


optionsBlock    : OPTIONS_BLOCK_LABEL LCURLY option* RCURLY
                ;

option          : ID ASSIGN optionValue SEMI
                ;


optionValue     : ID
                | literal
                ;



imagesBlock     : IMAGES_BLOCK_LABEL LCURLY imageVarDeclaration* RCURLY
                ;


imageVarDeclaration
                : ID ASSIGN role SEMI
                ;


role            : READ
                | WRITE
                ;


initBlock       : INIT_BLOCK_LABEL LCURLY varDeclaration* RCURLY
                ;


varDeclaration  : ID (ASSIGN expression)? SEMI
                ;


block           : LCURLY statement* RCURLY
                ;


statement       : block
                | IF parenExpression statement (ELSE statement)?
                | WHILE parenExpression statement
                | UNTIL parenExpression statement
                | FOREACH LPAR ID IN loopSet RPAR statement
                | BREAKIF LPAR expression RPAR SEMI
                | BREAK SEMI
                | expression SEMI
                | SEMI
                ;

loopSet         : listLiteral
                | range
                | ID
                ;


expressionList  : expression (COMMA expression)*
                ;


range           : expression COLON expression
                ;


/*
 * The "con" function is treated separately from general
 * functions during compilation because we want to ensure lazy evaluation
 * of the alternatives.
 */
conCall         : CON LPAR expressionList RPAR
                ;


expression      : atom
                | expression POW<assoc=right> expression
                | expression (INCR | DECR)
                | (INCR | DECR | PLUS | MINUS) expression
                | NOT expression
                | expression (TIMES | DIV | MOD) expression
                | expression (PLUS | MINUS) expression
                | expression (GT | GE | LE | LT) expression
                | expression (EQ | NE) expression
                | expression AND expression
                | expression OR expression
                | expression XOR expression
                | expression QUESTION expression COLON expression  
                | expression 
                  ( ASSIGN<assoc=right>
                  | TIMESEQ<assoc=right>
                  | DIVEQ<assoc=right>
                  | MODEQ<assoc=right>
                  | PLUSEQ<assoc=right>
                  | MINUSEQ<assoc=right>
                  ) expression
                ;

parenExpression : LPAR expression RPAR
                ;

atom            : parenExpression
                | literal
                | listLiteral
                | conCall
                | identifiedAtom
                ;


identifiedAtom  : ID argumentList       # functionCall
                | ID imagePos           # imageCall
                | ID APPEND expression  # append
                | ID                    # var
                ;


argumentList    : LPAR expressionList RPAR
                ;


imagePos        : bandSpecifier pixelSpecifier
                | pixelSpecifier
                | bandSpecifier
                ;


pixelSpecifier  : LSQUARE pixelPos COMMA pixelPos RSQUARE
                ;


bandSpecifier   : LSQUARE expression RSQUARE
                ;


pixelPos        : ABS_POS_PREFIX expression // absolute pos
                | expression                // relative pos
                ;


literal         : INT_LITERAL
                | FLOAT_LITERAL
                | TRUE
                | FALSE
                | NULL
                ;


listLiteral     : LSQUARE expressionList RSQUARE
                ;


/////////////////////////////////////////////////
// Lexer rules
/////////////////////////////////////////////////

COMMENT : LINE_COMMENT | BLOCK_COMMENT ;
    
fragment
LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' -> channel(HIDDEN) ;

fragment
BLOCK_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN) ;

/* Logical constants */
TRUE    : 'TRUE' | 'true' ;
FALSE   : 'FALSE' | 'false' ;
NULL    : 'NULL' | 'null' ;

/* Keywords */
INT_TYPE        : 'int' ;
FLOAT_TYPE      : 'float' ;
DOUBLE_TYPE     : 'double' ;
BOOLEAN_TYPE    : 'boolean' ;

OPTIONS_BLOCK_LABEL : 'options' ;
IMAGES_BLOCK_LABEL  : 'images' ;
INIT_BLOCK_LABEL    : 'init' ;

READ    : 'read' ;
WRITE   : 'write' ;

CON     : 'con' ;
IF      : 'if' ;
ELSE    : 'else' ;
WHILE   : 'while' ;
UNTIL   : 'until' ;
FOREACH : 'foreach' ;
IN      : 'in' ;
BREAKIF : 'breakif' ;
BREAK   : 'break' ;

/* Operators sorted and grouped by precedence order */

ABS_POS_PREFIX
        : '$'  ;

APPEND  : '<<' ;

INCR    : '++' ;
DECR    : '--' ;

NOT     : '' ;
POW     : '^' ;
TIMES   : '*' ;
DIV     : '/' ;
MOD     : '%' ;
PLUS    : '+' ;
MINUS   : '-' ;
GT      : '>';
GE      : '>=';
LE      : '<=';
LT      : '<';
EQ      : '==';
NE      : '=';
AND     : '&&';
OR      : '||';
XOR     : '^|';
QUESTION: '?' ;  /* ternary conditional operator ?: */
TIMESEQ : '*=' ;
DIVEQ   : '/=' ;
MODEQ   : '%=' ;
PLUSEQ  : '+=' ;
MINUSEQ : '-=' ;
ASSIGN  : '='  ;

/* General tokens */
COMMA   : ',' ;
SEMI    : ';' ;
COLON   : ':' ;
LPAR    : '(' ;
RPAR    : ')' ;
LSQUARE : '[' ;
RSQUARE : ']' ;
LCURLY  : '{' ;
RCURLY  : '}' ;

ID      : (Letter) (Letter | UNDERSCORE | Digit | Dot)*
        ;


fragment
Letter  : 'a'..'z' | 'A'..'Z'
        ;

UNDERSCORE
        : '_' ;

INT_LITERAL
        : '0' | NonZeroDigit Digit*
        ;

FLOAT_LITERAL
        : ('0' | NonZeroDigit Digit*)? Dot Digit* FloatExp?
        ;

fragment
Digit   : '0'..'9' ;

fragment
Dot     : '.' ;

fragment
NonZeroDigit
        : '1'..'9'
        ;

fragment
FloatExp
        : ('e'|'E' (PLUS|MINUS)? '0'..'9'+)
        ;

WS  :   [ \t\r\n\u000C]+ -> channel(HIDDEN)
    ;


/* 
 * The following are for future use 
 */

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
