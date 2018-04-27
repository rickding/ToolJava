package com.tool.sql.parser;

import com.common.file.FileUtil;
import com.common.file.FileWriter;

import java.util.ArrayList;
import java.util.List;

public class DB extends DBItem {
    private List<Table> tableList;

    public DB(String name) {
        this.name = name;
    }

    @Override
    public boolean isDB() {
        return true;
    }

    /**
     * Add table into list
     * @param table
     */
    public void addTable(Table table) {
        if (table == null) {
            return;
        }

        synchronized ("addTable") {
            if (tableList == null) {
                tableList = new ArrayList<Table>();
            }
        }
        tableList.add(table);
    }

    /**
     * Save to file
     * @param filePath
     */
    public boolean saveToFile(String filePath) {
        FileUtil.mkdirs(filePath);
        FileWriter writer = new FileWriter(filePath);
        if (!writer.open()) {
            System.out.printf("Fail to create output file: %s\n", filePath);
            return false;
        }

        // Write
        writer.writeLine(String.format("database: %s\n", name));

        // Tables
        if (tableList != null && tableList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Table table : tableList) {
                sb.append(", ");
                sb.append(table.getName());
            }
            writer.writeLine(String.format("tables: %s", sb.substring(1)));
        } else {
            writer.writeLine("Has no tables.");
        }

        // TODO: fields

        // Close
        writer.close();
        return true;
    }
}
