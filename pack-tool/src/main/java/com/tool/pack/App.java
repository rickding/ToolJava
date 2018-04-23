package com.tool.pack;

import com.common.util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Specify the script file or folder:");

        // Check the files and folders
        Date timeStart = new Date();
        List<String> projects = new ArrayList<String>();

        // Read config
        if (PackConfig.init()) {
            // Pack files
            String[] files = (new PackHelper(PackConfig.getInst())).process();
            if (files != null && files.length > 0) {
                projects.addAll(Arrays.asList(files));
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
