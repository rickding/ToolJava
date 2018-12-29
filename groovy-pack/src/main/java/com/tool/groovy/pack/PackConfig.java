package com.tool.groovy.pack;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.Config;
import com.common.file.FileUtil;
import com.common.util.EmptyUtil;
import com.common.util.StrUtil;

import java.io.File;
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
        return init(null);
    }

    public static boolean init(String filePath) {
        synchronized (PackConfig.class) {
            inst = null;
        }

        // Read from the source folder
        for (String srcPath : new String[]{filePath, ".\\"}) {
            File[] fileArr = FileUtil.findFiles(srcPath, configFileName);
            if (!EmptyUtil.isEmpty(fileArr)) {
                for (File file : fileArr) {
                    if (file.getName().trim().equalsIgnoreCase(configFileName)) {
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
        boolean ret = getInst().readFile(configFileName, true);
        System.out.printf("%s to read config from source: %s\n", ret ? "Success" : "Fail", configFileName);
        return ret;
    }

    private PackConfig() {
        ignoredFileNamePatternSet = new HashSet<String>();
        excludeFileNamePatternSet = new HashSet<String>();
    }

    /**
     * Read the config by key
     * @param key
     * @param values
     */
    private void read(String key, Set<String> values) {
        if (StrUtil.isEmpty(key) || values == null) {
            return;
        }

        JSONObject obj = lastFileObj;
        if (obj.containsKey(key)) {
            JSONArray arr = obj.getJSONArray(key);
            if (arr != null && arr.size() > 0) {
                for (int i = 0; i < arr.size(); i++) {
                    values.add(arr.get(i).toString());
                }
            }
        }
    }

    private String read(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }

        JSONObject obj = lastFileObj;
        if (obj.containsKey(key)) {
            return obj.getString(key);
        }
        return null;
    }

    @Override
    public boolean readFile(String fileName, boolean isResource) {
        boolean ret = super.readFile(fileName, isResource);
        if (!ret) {
            return ret;
        }

        // Parse the json object
        srcPath = read("srcPath");
        dstPath = read("dstPath");
        version = read("version");
        fileExt = read("fileExt");
        read("ignoredFileNamePatterns", ignoredFileNamePatternSet);
        read("excludeFileNamePatterns", excludeFileNamePatternSet);
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
