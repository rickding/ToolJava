package com.tool.doc.parser;

public class ParserConfig {
    public static String srcPath = "D:\\work\\ams\\docker\\JavaDoc";
    public static String dstPath = "D:\\work\\ams\\docker\\doc\\db.csv";
    public static String fileExt = "po.html";

    // Table, Field, Comment
    public static String TableFlag = "public class <span class=\"typeNameLabel\">".toLowerCase();
    public static String TableSplitter = "</span>";
    public static int TableIndex = 0;
    public static String[] TableTrimArr = {" ", "Po"};
    public static String[] TableIgnoreArr = {"_copy"};

//    public static String FieldFlag = "<td class=\"colFirst\">".toLowerCase();
//    public static String FieldSplitter = "</td>";

    public static String FieldFlag = "<td class=\"colLast\"><code><span class=\"memberNameLink\">".toLowerCase();
    public static String FieldSplitter = "</span></code>";
    public static int FieldIndex = 0;
    public static String[] FieldTrimArr = {"<code>", "</code>"};
    public static String[] FieldIgnoreArr = {"createdBy", "createdDate"};

    public static String CommentFlag = "<div class=\"block\">".toLowerCase();
    public static String CommentSplitter = "</div>";
    public static int CommentIndex = 0;
    public static String[] CommentTrimArr = {",", ";", "'", "\"", "()"};
}
