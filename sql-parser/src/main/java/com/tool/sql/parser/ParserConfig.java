package com.tool.sql.parser;

public class ParserConfig {
    public static String srcPath = "C:\\Work\\jira\\db\\jiradb.sql";
    public static String dstPath = "C:\\Work\\jira\\db";
    public static String fileExt = ".sql";
    public static String newFileExt = ".txt";

    // DB, Table, Field, Comment (of table and field).
    public static String DBFlag = "database";
    public static String DBSplitter = ":";
    public static int DBIndex = 1;
    public static String[] DBTrimArr = {" "};

    public static String TableFlag = "create table";
    public static String TableSplitter = " ";
    public static int TableIndex = 2;
    public static String[] TableTrimArr = {"`"};
    public static String[] TableIgnoreArr = {"_copy"};

    public static String FieldFlag = "`";
    public static String FieldSplitter = " ";
    public static int FieldIndex = 0;
    public static String[] FieldTrimArr = {"`"};
    public static String[] FieldIgnoreArr = {
            "create_time", "create_by", "update_time", "update_by", "server_ip",
            "is_available", "is_disable",
            "version", "version_no", "client_versionno",
            "create_userid", "create_username", "create_userip", "create_usermac", "create_time_db",
            "update_userid", "update_username", "update_userip", "update_usermac", "update_time_db",
            "del_flg", "crt_id", "crt_time", "upd_id", "upd_time"
    };

    public static String CommentFlag = "comment";
    public static String FieldCommentSplitter = "'";
    public static String TableCommentSplitter = "'";
    public static int CommentIndex = 1;
    public static String[] CommentTrimArr = {",", ";", "'", "\"", "()"};
}
