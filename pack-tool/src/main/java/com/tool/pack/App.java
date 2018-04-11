package com.tool.pack;

import com.common.utils.EmptyUtil;
import com.common.utils.DateUtil;
import com.common.utils.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Specify the script file or folder:");

        Date timeStart = new Date();
        Set<String> filePaths = new HashSet<String>(){{
            add("C:\\Work\\jira\\scriptrunner-samples\\jira\\src\\main\\groovy");
        }};

        // Add the parameters
        if (!EmptyUtil.isEmpty(args)) {
            for (String arg : args) {
                if (!StrUtil.isEmpty(arg)) {
                    filePaths.add(arg);
                }
            }
        }

        // Check the files and folders
        List<String> projects = new ArrayList<String>();
        for (String filePath : filePaths) {
            String[] files = (new PackHelper()).process(filePath);
            if (files != null && files.length > 0) {
                projects.addAll(Arrays.asList(files));
            }
        }

        // Summary the information
        System.out.printf("%d folders, %d files, start: %s, end: %s\n",
                filePaths.size(),
                projects.size(),
                DateUtil.format(timeStart, "hh:mm:ss"),
                DateUtil.format(new Date(), "hh:mm:ss")
        );
        for (String project : projects) {
            System.out.println(project);
        }
    }
}
