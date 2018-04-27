package com.tool.sql.parser;

import com.common.util.EmptyUtil;
import com.common.util.StrUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2017/9/23.
 */
public class SqlParser {
    /**
     * Parse the file line by line
     * @param file
     * @return
     */
    public static List<DB> process(File file) {
        if (file == null || !file.canRead()) {
            return null;
        }

        List<DB> dbList = new ArrayList<DB>();

        // http://www.cnblogs.com/lovebread/archive/2009/11/23/1609122.html
        BufferedReader reader = null;
        try {
            // DB, table, field are parent-child relationships.
            DB db = null;
            Table table = null;

            reader = new BufferedReader(new FileReader(file));

            // Read and parse
            String str;
            while ((str = reader.readLine()) != null) {
                if (StrUtil.isEmpty(str)) {
                    continue;
                }

                DBItem item = parse(str.trim());
                if (item == null) {
                    continue;
                }

                if (item.isDB()) {
                    db = (DB)item;
                    dbList.add(db);
                    System.out.println(db.toString());
                } else if (item.isTable()) {
                    table = (Table)item;
                    System.out.println(table.toString());

                    if (!isIgnored(item, SqlConfig.TableIgnoreArr)) {
                        db.addTable(table);
                    }
                } else if (item.isField()) {
                    if(!isIgnored(item, SqlConfig.FieldIgnoreArr)) {
                        table.addField((Field) item);
                    }
                } else {
                    if (table != null && item.isComment()) {
                        // Table's comment
                        table.setComment(item.getComment());
                    } else {
                        System.out.printf("Un-supported item: %s\n", item.toString());
                    }
                }
            }

            reader.close();
            reader = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
        return dbList;
    }

    /**
     * Get the db, table, field or comment
     * @param sql
     * @return
     */
    public static DBItem parse(String sql) {
        if (StrUtil.isEmpty(sql)) {
            return null;
        }

        String sqlLowercase = sql.toLowerCase();

        // Check DB, table, field and comment. Note: DB and comment are not at the beginning, while table and field are the first ones.
        if (sqlLowercase.indexOf(SqlConfig.DBFlag) >= 0) {
            // Remove the unused beginning.
            String name = sql.substring(sqlLowercase.indexOf(SqlConfig.DBFlag));
            name = parseName(name, SqlConfig.DBSplitter, SqlConfig.DBIndex, SqlConfig.DBTrimArr);
            if (!StrUtil.isEmpty(name)) {
                return new DB(name);
            }
        }

        if (sqlLowercase.startsWith(SqlConfig.TableFlag)) {
            String name = sql.substring(sqlLowercase.indexOf(SqlConfig.TableFlag));
            name = parseName(name, SqlConfig.TableSplitter, SqlConfig.TableIndex, SqlConfig.TableTrimArr);
            if (!StrUtil.isEmpty(name)) {
                return new Table(name);
            }
        }

        if (sqlLowercase.startsWith(SqlConfig.FieldFlag)) {
            String name = sql.substring(sqlLowercase.indexOf(SqlConfig.FieldFlag));
            name = parseName(name,SqlConfig.FieldSplitter, SqlConfig.FieldIndex, SqlConfig.FieldTrimArr);
            if (!StrUtil.isEmpty(name)) {
                Field item = new Field(name);

                // Find the comment of the field
                sql = sql.substring(sqlLowercase.indexOf(SqlConfig.FieldFlag));
                sqlLowercase = sql.toLowerCase();
                if (sqlLowercase.indexOf(SqlConfig.CommentFlag) >= 0) {
                    name = sql.substring(sqlLowercase.indexOf(SqlConfig.CommentFlag));
                    name = parseName(name, SqlConfig.FieldCommentSplitter, SqlConfig.CommentIndex, SqlConfig.CommentTrimArr);
                    if (!StrUtil.isEmpty(name)) {
                        item.setComment(name);
                    }
                }
                return item;
            }
        }

        if (sqlLowercase.indexOf(SqlConfig.CommentFlag) >= 0) {
            // Find the comment of the table, which is at the end of the definition block.
            String name = sql.substring(sqlLowercase.indexOf(SqlConfig.CommentFlag));
            name = parseName(name, SqlConfig.TableCommentSplitter, SqlConfig.CommentIndex, SqlConfig.CommentTrimArr);
            if (!StrUtil.isEmpty(name)) {
                return new Comment(name);
            }
        }
        return null;
    }

    /**
     * Check if it is ignored
     * @param item
     * @param ignoreNameArr
     * @return
     */
    public static boolean isIgnored(DBItem item, String[] ignoreNameArr) {
        if (item == null || StrUtil.isEmpty(item.getName()) || EmptyUtil.isEmpty(ignoreNameArr)) {
            return false;
        }

        // Check if it should be ignored
        String name = item.getName();
        List<String> list = Arrays.asList(ignoreNameArr);
        return list.contains(name);
    }

    /**
     * Get the name
     * @param str
     * @param splitter
     * @param index
     * @param trims
     * @return
     */
    public static String parseName(String str, String splitter, int index, String[] trims) {
        if (StrUtil.isEmpty(str) || StrUtil.isEmpty(splitter, false) || index < 0) {
            return str == null ? "" : str;
        }

        // Process with only lowercase
        String strLowercase = str.toLowerCase();

        // Split
        String[] values = strLowercase.split(splitter);
        if (index >= values.length || StrUtil.isEmpty(values[index])) {
            return "";
        }

        // Get the value
        String name = values[index];
        name = name.trim();

        // Trim
        if (!StrUtil.isEmpty(name) && !EmptyUtil.isEmpty(trims)) {
            for (String trim : trims) {
                // Trim the beginning
                if (name.startsWith(trim)) {
                    name = name.substring(trim.length());
                }

                // Trim the ending
                if (name.endsWith(trim)) {
                    name = name.substring(0, name.length() - trim.length());
                }
            }
        }

        // Get the original string
        if (!StrUtil.isEmpty(name)) {
            index = strLowercase.indexOf(name);
            if (index >= 0) {
                name = str.substring(index, index + name.length());
            }
        }
        return name;
    }
}
