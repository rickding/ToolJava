package com.common.file;

import com.common.util.StrUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceUtil {
    /**
     * Read content from resource file
     * http://www.mkyong.com/java/java-read-a-file-from-resources-folder/
     * @param fileName
     * @return
     */
    public static String readAsStr(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            return null;
        }

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream stream = classLoader.getResourceAsStream(fileName);
        if (stream == null) {
            return null;
        }

        String ret = null;
        try {
            ret = IOUtils.toString(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String[] read(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            return null;
        }

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL fileUrl = classLoader.getResource(fileName);
        if (fileUrl == null) {
            return null;
        }
        File file = new File(fileUrl.getFile());
        if (file == null || !file.exists()) {
            return null;
        }

        // Read
        List<String> lines = new ArrayList<String>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        String[] strs = new String[lines.size()];
        lines.toArray(strs);
        return strs;
    }
}
