package com.tool.doc.parser;

public class Type extends DBItem {
    public Type(String name) {
        this.name = name;
    }

    @Override
    public boolean isType() {
        return true;
    }
}
