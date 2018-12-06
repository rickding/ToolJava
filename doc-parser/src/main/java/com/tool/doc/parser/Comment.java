package com.tool.doc.parser;

public class Comment extends DBItem {
    public Comment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean isComment() {
        return true;
    }
}
