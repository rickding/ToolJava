package com.tool.sql.updater;

import com.common.file.FileReader;
import com.common.file.FileUtil;
import com.common.file.FileWriter;
import com.common.util.StrUtil;

import java.io.File;
import java.util.Map;

public class UpdaterHelper {
    /**
     * Update file and save
     * @param inputFile
     * @param outputFileName
     */
    public static void process(File inputFile, String outputFileName) {
        if (inputFile == null || !inputFile.exists() || !inputFile.canRead() || StrUtil.isEmpty(outputFileName)) {
            return;
        }

        // Input
        String filePath = inputFile.getPath();
        FileReader reader = new FileReader(filePath, UpdaterConfig.showInfo);
        if (!reader.open()) {
            System.out.printf("Fail to open file: %s\n", filePath);
            return;
        }

        // Output
        FileUtil.mkdirs(outputFileName);
        FileWriter writer = new FileWriter(outputFileName, UpdaterConfig.showInfo);
        if (!writer.open()) {
            System.out.printf("Fail to create output file: %s\n", outputFileName);

            // Close
            reader.close();
            return;
        }

        // Mark the header written or not
        long index = 0;
        boolean headerWritten = !UpdaterConfig.addHeaderArrFlag;
        boolean tailFound = false;

        // read and update, then write
        String str;
        while ((str = reader.readLine()) != null) {
            if (!StrUtil.isEmpty(str)) {
                // Check if it's the tail
                if (!tailFound && isTail(str)) {
                    tailFound = true;
                    if (UpdaterConfig.showInfo) {
                        System.out.printf("Tail is found: %s\n", outputFileName);
                    }

                    if (UpdaterConfig.replaceTailFlag) {
                        str = UpdaterConfig.sqlTailFlag;
                    }
                }

                // Update the str;
                if (UpdaterConfig.updateSql) {
                    str = updateSql(str);
                }
            }

            // Write header
            if (!headerWritten && UpdaterConfig.sqlUpdateHeaderLineIndex <= index++) {
                headerWritten = true;
                index += writeHeaders(writer);
                System.out.printf("Header is written: %d, %s\n", index, outputFileName);
            }

            // Write line
            writer.writeLine(str);
        }

        // close
        writer.close();
        reader.close();
    }

    private static boolean isTail(String str) {
        if (StrUtil.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().startsWith(UpdaterConfig.sqlTailFlag);
    }

    private static String updateSql(String sql) {
        if (StrUtil.isEmpty(sql)) {
            return sql;
        }

        // Replace
        for (Map.Entry<String, String> replace : UpdaterConfig.sqlReplaceMap.entrySet()) {
            String str = replace.getKey();
            if (StrUtil.isEmpty(str)) {
                continue;
            }
            sql = sql.replace(str, replace.getValue());
        }

        // Restore after replace
        for (Map.Entry<String, String> replace : UpdaterConfig.sqlRestoreMap.entrySet()) {
            String str = replace.getKey();
            if (StrUtil.isEmpty(str)) {
                continue;
            }
            sql = sql.replace(str, replace.getValue());
        }
        return sql;
    }

    private static long writeHeaders(FileWriter writer) {
        if (writer == null || !writer.isOpen()) {
            return 0;
        }

        for (String str : UpdaterConfig.sqlUpdateHeaderArr) {
            writer.writeLine(str);
        }
        return UpdaterConfig.sqlUpdateHeaderArr.length;
    }
}
