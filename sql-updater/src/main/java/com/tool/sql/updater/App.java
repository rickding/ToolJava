package com.tool.sql.updater;

import com.common.file.FileUtil;
import com.common.util.DateUtil;
import com.common.util.EmptyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Sql updater
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Start");

        // Check the files and folders
        Date timeStart = new Date();
        List<String> projects = new ArrayList<String>();

        // Parse files: File or directory, while not iterate sub folders
        File[] files = FileUtil.findFiles(UpdaterConfig.srcPath, UpdaterConfig.fileExt, UpdaterConfig.ignoredFileName);
        if (!EmptyUtil.isEmpty(files)) {
            for (File f : files) {
                projects.add(f.getPath());

                // Update and save
                String dstFile = FileUtil.getOutputFile(f.getPath(), UpdaterConfig.srcPath, UpdaterConfig.dstPath);
                UpdaterHelper.process(f, dstFile);
            }
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
