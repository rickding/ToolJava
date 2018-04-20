package com.tool.pack;

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
    private String fileExt;
    private Set<String> ignoredFileNamePatternSet;
    private Set<String> excludeFileNamePatternSet;
    private static final String configFileName = "config.json";

    public static PackConfig getInst() {
        synchronized ("PackConfig.getInst()") {
            if (inst == null) {
                inst = new PackConfig();
            }
        }
        return (PackConfig) inst;
    }

    public static void init(String filePath) {
        synchronized ("PackConfig.getInst()") {
            inst = null;
        }

        // Read from the source folder
        for (String srcPath : new String[]{filePath, ".\\"}) {
            File[] fileArr = FileUtil.findFiles(srcPath, configFileName);
            if (!EmptyUtil.isEmpty(fileArr)) {
                for (File file : fileArr) {
                    if (file.getName().trim().equalsIgnoreCase(configFileName)) {
                        getInst().read(file.getPath(), false);
                        System.out.printf("Read the config file: %s\n", file.getPath());
                        return;
                    }
                }
            }
        }

        // Read the packed source
        getInst().read(configFileName, true);
        System.out.printf("Read the source file: %s\n", configFileName);
    }

    private PackConfig() {
        ignoredFileNamePatternSet = new HashSet<String>();
        excludeFileNamePatternSet = new HashSet<String>();
    }

    private void update(String key, Set<String> values) {
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

    @Override
    public boolean read(String fileName, boolean isResource) {
        boolean ret = super.read(fileName, isResource);
        if (!ret) {
            return ret;
        }

        // Parse the json object
        JSONObject obj = lastFileObj;
        String key = "fileExt";
        if (obj.containsKey(key)) {
            fileExt = obj.getString(key);
        }

        update("ignoredFileNamePatterns", ignoredFileNamePatternSet);
        update("excludeFileNamePatterns", excludeFileNamePatternSet);
        return ret;
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
