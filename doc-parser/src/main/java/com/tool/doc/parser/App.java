package com.tool.doc.parser;

import com.common.Config;
import com.common.file.FileUtil;
import com.common.util.DateUtil;
import com.common.util.EmptyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Start");

        // Check the files and folders
        Date timeStart = new Date();
        List<String> projects = new ArrayList<String>();

        // Parse files
        String srcPath = Config.getInst().readFile() ? Config.getInst().readValue("srcPath") : ParserConfig.srcPath;
        File[] files = FileUtil.findFiles(srcPath, ParserConfig.fileExt, true);
        if (!EmptyUtil.isEmpty(files)) {
            DB db = new DB("ams");
            for (File f : files) {
                // Filter db files: entity or po
                String filePath = f.getPath().toLowerCase();
                if (!filePath.contains("entity") && !filePath.endsWith(String.format("po%s", ParserConfig.fileExt))) {
                    continue;
                }

                if (filePath.endsWith("package-summary.html")) {
                    continue;
                }

                // Parse
                projects.add(filePath);
                ParserHelper.process(f, db);
            }

            // Save csv file
            db.saveToFile(ParserConfig.dstPath);
        }

        // Summary the information
        for (String project : projects) {
            System.out.println(project);
        }
        System.out.printf("%d files, start: %s, end: %s\n",
                projects.size(),
                DateUtil.format(timeStart, "hh:mm:ss"),
                DateUtil.format(new Date(), "hh:mm:ss")
        );
    }
}
