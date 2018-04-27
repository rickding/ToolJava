package com.tool.sql.updater;

import java.util.HashMap;
import java.util.Map;

public class UpdaterConfig {
    public static String srcPath = "C:\\Work\\jira-svn\\db\\jiradb_test";
    public static String dstPath = "C:\\Work\\jira-svn\\db\\jiradb_test_updated";
    public static String fileExt = ".sql";
    public static String ignoredFileName = "_updated.sql";

    // Action configuration
    public static boolean showInfo = false;
    public static boolean replaceTailFlag = true;
    public static boolean addHeaderArrFlag = false;
    public static boolean updateSql = false;

    // Check if the tail existed.
    public static String sqlTailFlag = "-- dump completed";

    // Write the header into file
    public static long sqlUpdateHeaderLineIndex = 17;
    public static String[] sqlUpdateHeaderArr = {
            "set @companyId = 1000;",
            "-- Original 51 is replaced:",
            "-- ,51, => ,@companyId,",
            "-- ,51) => ,@companyId)",
            ""
    };

    // Replace the special strings
    public static Map<String, String> sqlReplaceMap = new HashMap<String, String>() {{
        put(",51,", ",@companyId,");
        put(",51)", ",@companyId)");
    }};

    // Some special strings are replaced wrongly, which should be restored.
    public static Map<String, String> sqlRestoreMap = new HashMap<String, String>() {{
        put(",@companyId,2,'Autographs-Original',", ",51,2,'Autographs-Original',");
    }};
}
