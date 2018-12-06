package com.tool.doc.parser;

import com.common.file.FileUtil;
import com.common.file.FileWriter;
import com.common.util.StrUtil;

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
     *
     * @param table
     */
    public void addTable(Table table) {
        if (table == null) {
            return;
        }

        if (tableList == null) {
            synchronized (DB.class) {
                if (tableList == null) {
                    tableList = new ArrayList<Table>();
                }
            }
        }
        tableList.add(table);
    }

    /**
     * Save to csv file
     *
     * @param filePath
     */
    public boolean saveToFile(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return false;
        }

        filePath = FileUtil.appendFileExt(filePath, ".csv");
        FileUtil.mkdirs(filePath);

        FileWriter writer = new FileWriter(filePath);
        if (!writer.open()) {
            System.out.printf("Fail to create output file: %s\n", filePath);
            return false;
        }

        // Table, field, type, comment
        if (tableList != null && tableList.size() > 0) {
            // Write header
            writer.writeLine("table,field,type,comment");

            for (Table table : tableList) {
                List<Field> fieldList = table.getFieldList();
                if (fieldList != null && fieldList.size() > 0) {
                    for (Field field : fieldList) {
                        writer.writeLine(String.format("%s,%s,%s,\"%s\"",
                                table.getName(), field.getName(), field.getType(),
                                StrUtil.isEmpty(field.getComment()) ? "" : field.getComment()
                        ));
                    }
                }
            }
        } else {
            writer.writeLine("Has no tables.");
        }

        // Close
        writer.close();
        return true;
    }
}
