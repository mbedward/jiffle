lexer grammar CommentFinder;

options {
    filter = true;
}

@header {
package jaitools.jiffle.parser;
}

@members {
private boolean lineComment = false;

// start : end pairs
private List<Integer> commentIndices = new ArrayList<Integer>();

public List<Integer> getStartEndIndices() { return commentIndices; }

}

COMMENT
@after {
    String s = getText();
    int endIndex = getCharIndex();
    int startIndex = endIndex - s.length();
    if (lineComment) endIndex -= 1;
    commentIndices.add(startIndex);
    commentIndices.add(endIndex);
}
    :   '//' ~('\n'|'\r')* '\r'? '\n' { lineComment = true; }
    |   '/*' ( options {greedy=false;} : . )* '*/' { lineComment = false; }
    ;

