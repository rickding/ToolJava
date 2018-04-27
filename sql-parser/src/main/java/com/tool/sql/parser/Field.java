package com.tool.sql.parser;

public class Field extends DBItem {
    public Field(String name) {
        this.name = name;
    }

    @Override
    public boolean isField() {
        return true;
    }
}
