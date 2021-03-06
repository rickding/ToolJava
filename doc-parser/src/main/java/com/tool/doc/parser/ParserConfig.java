package com.tool.doc.parser;

import java.util.HashMap;
import java.util.Map;

public class ParserConfig {
//    public static String srcPath = "D:\\work\\ams\\docker\\JavaDoc";
//    public static String dstPath = "D:\\work\\ams\\docker\\doc\\db.csv";
    public static String srcPath = "../JavaDoc";
    public static String dstPath = "./db.csv";
    public static String fileExt = ".html";

    public static String SectionStart = "@Entity";
    public static String SectionEnd = "<!-- ======== CONSTRUCTOR SUMMARY ======== -->";

    // Table, Type, Field, Comment
    public static String TableFlag = "public class <span class=\"typeNameLabel\">".toLowerCase();
    public static String TableSplitter = "</span>";
    public static int TableIndex = 0;
    public static String[] TableTrimArr = {" ", "Po"};
    public static String[] TableIgnoreArr = {"_copy"};

    public static String TypeFlag = "<td class=\"colFirst\"><code>".toLowerCase();
    public static String TypeSplitter = "</code></td>";
    public static int TypeIndex = 0;
    public static String[] TypeTrimArr = {"private ", "protected ", "static ", " ", "java.lang.", "java.util.", "java.math.", "java.sql."};
    public static Map<String, String> TypeReplaceMap = new HashMap<String, String>() {{
        put("&lt;", "<");
        put("&gt;", ">");
    }};

    public static String FieldFlag = "<td class=\"colLast\"><code><span class=\"memberNameLink\">".toLowerCase();
    public static String FieldSplitter = "</span></code>";
    public static int FieldIndex = 0;
    public static String[] FieldTrimArr = {"<code>", "</code>"};
    public static String[] FieldIgnoreArr = {};

    public static String CommentFlag = "<div class=\"block\">".toLowerCase();
    public static String CommentSplitter = "</div>";
    public static int CommentIndex = 0;
    public static String[] CommentTrimArr = {",", ";", "'", "\"", "()"};
}
