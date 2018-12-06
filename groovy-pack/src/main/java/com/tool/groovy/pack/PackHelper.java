package com.tool.groovy.pack;

import com.common.file.FileUtil;
import com.common.util.EmptyUtil;
import com.common.util.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PackHelper {
    private String srcPath;
    private String dstPath;
    private String version;
    private String fileExt;
    private Set<String> ignoredFileNamePatternSet;
    private Set<String> excludeFileNamePatternSet;

    public PackHelper(PackConfig config) {
        if (config == null) {
            return;
        }
        this.srcPath = config.getSrcPath();
        this.dstPath = config.getDstPath();
        this.version = config.getVersion();
        this.fileExt = config.getFileExt();

        ignoredFileNamePatternSet = new HashSet<String>();
        excludeFileNamePatternSet = new HashSet<String>();
        Set<String> tmpSet = PackConfig.getInst().getIgnoredFileNamePatternSet();
        if (tmpSet != null && tmpSet.size() > 0) {
            ignoredFileNamePatternSet.addAll(tmpSet);
        }
        tmpSet = PackConfig.getInst().getExcludeFileNamePatternSet();
        if (tmpSet != null && tmpSet.size() > 0) {
            excludeFileNamePatternSet.addAll(tmpSet);
        }
    }

    public String[] process() {
        if (StrUtil.isEmpty(srcPath)) {
            return null;
        }

        // Get the files
        List<PackFile> fileList = new ArrayList<PackFile>();
        File[] fileArr = FileUtil.findFiles(srcPath, fileExt, true);
        if (!EmptyUtil.isEmpty(fileArr)) {
            for (File file : fileArr) {
                boolean ignored = false;
                for (String pattern : ignoredFileNamePatternSet) {
                    if (StrUtil.matches(file.getName(), pattern)) {
                        ignored = true;
                        break;
                    }
                }
                if (!ignored) {
                    fileList.add(new PackFile(file.getPath()));
                }
            }
        }
        if (fileList.size() <= 0) {
            return null;
        }

        // Scan firstly
        Map<String, PackFile> fileMap = new HashMap<String, PackFile>();
        for (PackFile file : fileList) {
            // Analyse the information
            if (file.scan()) {
                // package + class name
                String[] classNameArr = file.getClassNameArr();
                if (!EmptyUtil.isEmpty(classNameArr)) {
                    for (String className : classNameArr) {
                        fileMap.put(String.format("%s.%s", file.getPackagePath(), className), file);
                    }
                }

                // file path - base src path + file name with ext
                String fileName = FileUtil.getFileName(file.getFilePath(), srcPath);
                fileMap.put(FileUtil.appendFileExt(fileName, fileExt), file);
                fileMap.put(FileUtil.removeFileExt(fileName, fileExt), file);

                // file name with ext
                fileName = file.getFileName();
                fileMap.put(FileUtil.appendFileExt(fileName, fileExt), file);
                fileMap.put(FileUtil.removeFileExt(fileName, fileExt), file);
            }
        }

        // Pack secondly
        List<String> fileNameList = new ArrayList<String>();
        for (PackFile file : fileList) {
            boolean ret = file.pack(fileMap);
            if (!ret) {
                fileNameList.add(String.format("Fail to pack: %s", file.toString()));
            }
        }

        // Write to file
        for (PackFile file : fileList) {
            boolean excluded = false;
            for (String pattern : excludeFileNamePatternSet) {
                if (StrUtil.matches(file.getFileName(), pattern)) {
                    excluded = true;
                    break;
                }
            }
            if (!excluded) {
                String dstFile = FileUtil.getOutputFile(file.getFilePath(), srcPath, dstPath);
                boolean ret = file.write(dstFile, version);
                fileNameList.add(String.format("%s to write: %s, dst: %s", ret ? "Success" : "Fail", file.toString(), dstFile));
            }
        }

        // Convert to array
        if (fileNameList.size() <= 0) {
            return null;
        }
        String[] fileNameArr = new String[fileNameList.size()];
        fileNameList.toArray(fileNameArr);
        return fileNameArr;
    }
}
