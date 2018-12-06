package com.tool.doc.parser;

import com.common.util.EmptyUtil;
import com.common.util.StrUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.common.util.StrUtil.eraseHtmlBrace;

/**
 * Created by user on 2017/9/23.
 */
public class ParserHelper {
    /**
     * Parse the file line by line
     *
     * @param file
     * @return
     */
    public static void process(File file, DB db) {
        if (file == null || !file.canRead() || db == null) {
            return;
        }

        // http://www.cnblogs.com/lovebread/archive/2009/11/23/1609122.html
        BufferedReader reader = null;
        try {
            // table, field
            Table table = null;
            DBItem type = null;

            reader = new BufferedReader(new FileReader(file));

            // Read and parse
            String str;
            DBItem lastItem = null;
            while ((str = reader.readLine()) != null) {
                if (StrUtil.isEmpty(str)) {
                    continue;
                }

                DBItem item = parse(str.trim());
                if (item == null) {
                    continue;
                }

                if (item.isTable()) {
                    table = (Table) item;
                    System.out.println(table.toString());

                    if (!isIgnored(item, ParserConfig.TableIgnoreArr)) {
                        db.addTable(table);
                        lastItem = item;
                    }
                } else if (item.isType()) {
                    type = item;
                } else if (item.isField()) {
                    if (!isIgnored(item, ParserConfig.FieldIgnoreArr)) {
                        Field field = (Field) item;
                        if (type != null) {
                            field.setType(type.getName());
                        }

                        if (table != null) {
                            table.addField(field);
                        } else {
                            System.out.printf("Orphan field: %s, %s\n", item.toString(), file.getName());
                        }
                        lastItem = item;
                    }

                    // Ignore the previous type when field appears
                    type = null;
                } else if (item.isComment()) {
                    if (lastItem != null && (lastItem.isTable() || lastItem.isField())) {
                        // comment of table or field
                        lastItem.setComment(item.getComment());
                    } else {
                        System.out.printf("Un matched comment: %s, %s\n", item.toString(), file.getName());
                    }
                } else {
                    System.out.printf("Un-supported item: %s\n", item.toString());
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
    }

    /**
     * Get the db, table, field or comment
     *
     * @param sql
     * @return
     */
    public static DBItem parse(String sql) {
        if (StrUtil.isEmpty(sql)) {
            return null;
        }

        String sqlLowercase = sql.toLowerCase();

        // Check table, field, type and comment
        if (sqlLowercase.startsWith(ParserConfig.TableFlag)) {
            String name = sql.substring(sqlLowercase.indexOf(ParserConfig.TableFlag));
            name = parseName(name, ParserConfig.TableFlag, ParserConfig.TableSplitter, ParserConfig.TableIndex, ParserConfig.TableTrimArr);
            if (!StrUtil.isEmpty(name)) {
                return new Table(name);
            }
        }

        if (sqlLowercase.startsWith(ParserConfig.FieldFlag)) {
            String name = sql.substring(sqlLowercase.indexOf(ParserConfig.FieldFlag));
            name = parseName(name, ParserConfig.FieldFlag, ParserConfig.FieldSplitter, ParserConfig.FieldIndex, ParserConfig.FieldTrimArr);
            if (!StrUtil.isEmpty(name)) {
                return new Field(name);
            }
        }

        if (sqlLowercase.startsWith(ParserConfig.TypeFlag)) {
            String name = sql.substring(sqlLowercase.indexOf(ParserConfig.TypeFlag));
            name = parseName(name, ParserConfig.TypeFlag, ParserConfig.TypeSplitter, ParserConfig.TypeIndex, ParserConfig.TypeTrimArr);
            if (!StrUtil.isEmpty(name)) {
                return new Type(name);
            }
        }

        if (sqlLowercase.startsWith(ParserConfig.CommentFlag)) {
            // Find the comment of the table, which is at the end of the definition block.
            String name = sql.substring(sqlLowercase.indexOf(ParserConfig.CommentFlag));
            name = parseName(name, ParserConfig.CommentFlag, ParserConfig.CommentSplitter, ParserConfig.CommentIndex, ParserConfig.CommentTrimArr);
            if (!StrUtil.isEmpty(name)) {
                return new Comment(name);
            }
        }
        return null;
    }

    /**
     * Get the name
     *
     * @param str
     * @param splitter
     * @param index
     * @param trims
     * @return
     */
    public static String parseName(String str, String flag, String splitter, int index, String[] trims) {
        if (StrUtil.isEmpty(str) || StrUtil.isEmpty(splitter, false) || index < 0) {
            return str == null ? "" : str;
        }

        // Process with only lowercase
        String strLowercase = str.toLowerCase();
        int offset = 0;
        if (strLowercase.startsWith(flag)) {
            offset = flag.length();
            strLowercase = strLowercase.substring(offset);
        }

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
                name = str.substring(offset + index, offset + index + name.length());
            }
        }
        return eraseHtmlBrace(name);
    }

    /**
     * Check if it is ignored
     *
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
}
