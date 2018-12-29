package com.tool.groovy.pack;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.Config;
import com.common.file.FileUtil;
import com.common.util.EmptyUtil;
import com.common.util.StrUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PackConfig extends Config {
    private String srcPath;
    private String dstPath;
    private String version;
    private String fileExt;
    private Set<String> ignoredFileNamePatternSet;
    private Set<String> excludeFileNamePatternSet;

    private static final String configFileName = "config.json";

    public static PackConfig getInst() {
        if (inst == null) {
            synchronized (PackConfig.class) {
                if (inst == null) {
                    inst = new PackConfig();
                }
            }
        }
        return (PackConfig) inst;
    }

    /**
     * Read the config file
     * @return
     */
    public static boolean init() {
        return init(null, configFileName);
    }

    public static boolean init(String filePath, String fileName) {
        synchronized (PackConfig.class) {
            inst = null;
        }

        // Read from the source folder
        for (String srcPath : new String[]{filePath, ".\\"}) {
            File[] fileArr = FileUtil.findFiles(srcPath, fileName);
            if (!EmptyUtil.isEmpty(fileArr)) {
                for (File file : fileArr) {
                    if (file.getName().trim().equalsIgnoreCase(fileName)) {
                        boolean ret = getInst().readFile(file.getPath(), false);
                        System.out.printf("%s to read config from file: %s\n", ret ? "Success" : "Fail", file.getPath());
                        if (ret) {
                            return true;
                        }
                    }
                }
            }
        }

        // Read the packed source
        boolean ret = getInst().readFile(fileName, true);
        System.out.printf("%s to read config from source: %s\n", ret ? "Success" : "Fail", fileName);
        return ret;
    }

    private PackConfig() {
        ignoredFileNamePatternSet = new HashSet<String>();
        excludeFileNamePatternSet = new HashSet<String>();
    }

    @Override
    public boolean readFile(String fileName, boolean isResource) {
        boolean ret = super.readFile(fileName, isResource);
        if (!ret) {
            return ret;
        }

        // Parse the json object
        srcPath = readValue("srcPath");
        dstPath = readValue("dstPath");
        version = readValue("version");
        fileExt = readValue("fileExt");

        String[] arr = readValues("ignoredFileNamePatterns");
        if (arr != null) {
            ignoredFileNamePatternSet.addAll(Arrays.asList(arr));
        }

        arr = readValues("excludeFileNamePatterns");
        if (arr != null) {
            excludeFileNamePatternSet.addAll(Arrays.asList(arr));
        }
        return ret;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public String getDstPath() {
        return dstPath;
    }

    public String getVersion() {
        return version;
    }

    public String getFileExt() {
        return fileExt;
    }

    public Set<String> getIgnoredFileNamePatternSet() {
        return ignoredFileNamePatternSet;
    }

    public Set<String> getExcludeFileNamePatternSet() {
        return excludeFileNamePatternSet;
    }
}
