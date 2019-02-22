package com.tool.groovy.pack;

import com.common.Config;
import com.common.file.FileUtil;
import com.common.util.EmptyUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PackConfig extends Config {
    private String srcPath;
    private String dstPath;
    private String version;
    private boolean compat;
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

    @Override
    public boolean readFile(String fileName, boolean isResource) {
        boolean ret = super.readFile(fileName, isResource);
        if (!ret) {
            return false;
        }

        // Parse the json object
        srcPath = getStr("srcPath");
        dstPath = getStr("dstPath");
        version = getStr("version");
        compat = getBoolean("compat");
        fileExt = getStr("fileExt");

        String[] arr = getStrArr("ignoredFileNamePatterns");
        if (arr != null && arr.length > 0) {
            ignoredFileNamePatternSet.addAll(Arrays.asList(arr));
        }

        arr = getStrArr("excludeFileNamePatterns");
        if (arr != null && arr.length > 0) {
            excludeFileNamePatternSet.addAll(Arrays.asList(arr));
        }
        return true;
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

    public boolean getCompat() {
        return compat;
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
