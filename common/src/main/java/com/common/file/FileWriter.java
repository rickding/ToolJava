package com.common.file;

import com.common.util.EmptyUtil;
import com.common.util.StrUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by user on 2017/10/1.
 */
public class FileWriter {
    private String filePath;
    private BufferedWriter writer;
    private boolean showInfo = true;

    public FileWriter(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            filePath = "fw";
        }
        this.filePath = filePath;
    }

    public FileWriter(String filePath, boolean showInfo) {
        if (StrUtil.isEmpty(filePath)) {
            filePath = "fw";
        }
        this.filePath = filePath;
        this.showInfo = showInfo;
    }

    public boolean isOpen() {
        return writer != null;
    }

    public boolean open() {
        if (isOpen()) {
            close();
        }

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
        } catch (IOException e) {
            writer = null;
            System.out.println(e.getMessage());
        }

        boolean isOpen = isOpen();
        if (showInfo) {
            System.out.printf("FileWriter open %s: %s\n", isOpen ? "successfully" : "Failed", filePath);
        }
        return isOpen;
    }

    public void close() {
        if (!isOpen()) {
            return;
        }

        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            writer = null;
        }

        if (showInfo) {
            System.out.printf("FileWriter close successfully: %s\n", filePath);
        }
    }

    public void writeLineArr(String[] strs) {
        writeLineArr(strs, false);
    }

    public void writeLineArr(String[] strs, boolean ignoreEmpty) {
        if (!isOpen()) {
            System.out.println("Please call open() firstly.");
            return;
        }

        if (!EmptyUtil.isEmpty(strs)) {
            for (String str : strs) {
                if (!ignoreEmpty || !StrUtil.isEmpty(str)) {
                    writeLine(str);
                }
            }
        }
    }

    public void writeLine(List<String> strList) {
        writeLine(strList, false);
    }

    public void writeLine(List<String> strList, boolean ignoreEmpty) {
        if (!isOpen()) {
            System.out.println("Please call open() firstly.");
            return;
        }

        if (!EmptyUtil.isEmpty(strList)) {
            for (String str : strList) {
                if (!ignoreEmpty || !StrUtil.isEmpty(str)) {
                    writeLine(str);
                }
            }
        }
    }

    public void writeLine(String str) {
        if (!isOpen()) {
            System.out.println("Please call open() firstly.");
            return;
        }

        write(str);
        try {
            writer.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void write(String str) {
        if (!isOpen()) {
            System.out.println("Please call open() firstly.");
            return;
        }

        if (StrUtil.isEmpty(str)) {
            return;
        }

        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
