package com.tool.doc.parser;

public abstract class DBItem {
    protected String name;
    protected String comment;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public boolean isDB() {
        return false;
    }

    public boolean isTable() {
        return false;
    }

    public boolean isField() {
        return false;
    }

    public boolean isType() {
        return false;
    }

    public boolean isComment() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
