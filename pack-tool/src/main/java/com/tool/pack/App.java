package com.tool.pack;

import com.common.util.DateUtil;
import com.common.util.EmptyUtil;
import com.common.util.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Specify the script file or folder:");

        Date timeStart = new Date();
        Map<String, String> filePaths = new HashMap<String, String>() {{
//            put("C:\\Work\\jira\\scriptrunner-samples\\jira\\src\\main\\groovy", "C:\\Work\\jira\\deploy\\jira");
            put("C:\\Work\\jira\\scriptrunner-samples\\jira\\src\\main\\groovy\\com\\erp\\version", "C:\\Work\\jira\\deploy\\jira\\com\\erp\\version");
        }};

        // Add the parameters
        if (!EmptyUtil.isEmpty(args)) {
            for (String arg : args) {
                if (!StrUtil.isEmpty(arg)) {
                    filePaths.put(arg, null);
                }
            }
        }

        // Check the files and folders
        List<String> projects = new ArrayList<String>();
        for (String filePath : filePaths.keySet()) {
            // Read configs
            PackConfig.init(filePath);

            // Pack files
            String[] files = (new PackHelper(filePath, filePaths.get(filePath))).process();
            if (files != null && files.length > 0) {
                projects.addAll(Arrays.asList(files));
            }
        }

        // Summary the information
        for (String project : projects) {
            System.out.println(project);
        }
        System.out.printf("%d folders, %d files, start: %s, end: %s\n",
                filePaths.size(),
                projects.size(),
                DateUtil.format(timeStart, "hh:mm:ss"),
                DateUtil.format(new Date(), "hh:mm:ss")
        );
    }
}
