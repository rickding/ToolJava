package com.tool.doc.parser;

import java.util.ArrayList;
import java.util.List;

public class Table extends DBItem {
    private List<Field> fieldList;

    public Table(String name) {
        this.name = name;
    }

    @Override
    public boolean isTable() {
        return true;
    }

    /**
     * Add field into list
     * @param field
     */
    public void addField(Field field) {
        if (field == null) {
            return;
        }

        synchronized ("addField") {
            if (fieldList == null) {
                fieldList = new ArrayList<Field>();
            }
        }
        fieldList.add(field);
    }
}
