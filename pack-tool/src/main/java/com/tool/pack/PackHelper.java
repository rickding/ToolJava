package com.tool.pack;

import com.common.file.FileUtil;
import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackHelper {
    private static final String fileExt = ".groovy";
    private static final String ignoredFileNamePattern = "((\\w*[Tt][Ee][Ss][Tt])|([Tt][Ee][Ss][Tt]\\w*)).groovy";
    private static final String ignoredPackFileNamePattern = "\\w*(([Uu]tils*)|([Hh]elper)).groovy";

    private String srcPath;
    private String dstPath;

    public PackHelper(String srcPath, String dstPath) {
        this.srcPath = srcPath;
        this.dstPath = dstPath;
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
                if (!StrUtil.matches(file.getName(), ignoredFileNamePattern)) {
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
            if (!StrUtil.matches(file.getFileName(), ignoredPackFileNamePattern)) {
                String dstFile = FileUtil.getOutputFile(file.getFilePath(), srcPath, dstPath);
                boolean ret = file.pack(fileMap, dstFile);
                fileNameList.add(String.format("%s: Src: %s, dst: %s", ret ? "Success" : "Fail", file.toString(), dstFile));
            }
        }

        // Write to file


        // Convert to array
        if (fileNameList.size() <= 0) {
            return null;
        }
        String[] fileNameArr = new String[fileNameList.size()];
        fileNameList.toArray(fileNameArr);
        return fileNameArr;
    }
}
