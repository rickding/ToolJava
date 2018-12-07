package com.tool.doc.parser;

import com.common.file.CsvUtil;
import com.common.file.ExcelUtil;
import com.common.file.FileUtil;
import com.common.util.StrUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

    private List<String[]> toRecordList() {
        List<String[]> recordList = new ArrayList<String[]>();

        // Table, field, type, comment
        if (tableList != null && tableList.size() > 0) {
            // Write header
            recordList.add(new String[]{"table", "field", "type", "comment"});

            for (Table table : tableList) {
                List<Field> fieldList = table.getFieldList();
                if (fieldList != null && fieldList.size() > 0) {
                    for (Field field : fieldList) {
                        recordList.add(new String[]{
                                table.getName(), field.getName(), field.getType(),
                                StrUtil.isEmpty(field.getComment()) ? "" : field.getComment()
                        });
                    }
                }
            }
        } else {
            recordList.add(new String[]{"Has no tables."});
        }
        return recordList;
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

        List<String[]> recordList = toRecordList();
        if (recordList == null || recordList.isEmpty()) {
            return false;
        }

        FileUtil.mkdirs(filePath);

        // Csv
        filePath = FileUtil.appendFileExt(filePath, ".csv");
        CsvUtil.saveToFile(recordList, filePath);

        // Excel
        filePath = FileUtil.replaceFileExt(filePath, ".csv", ".xls");
        return ExcelUtil.saveToFile(recordList, filePath);
    }
}
