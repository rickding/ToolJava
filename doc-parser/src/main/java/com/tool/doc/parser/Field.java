package com.tool.doc.parser;

public class Field extends DBItem {
    private String type;

    public Field(String name) {
        this.name = name;
    }

    @Override
    public boolean isField() {
        return true;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
