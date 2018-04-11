package com.tool.pack;

import com.common.file.FileUtil;
import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.util.Map;
import java.util.Set;

public class PackFile {
    private String srcFile;
    private Map<String, String> nameFileMap;
    private String srcPath;
    private String dstPath;

    public PackFile(String srcFile, Map<String, String> nameFileMap, String srcPath, String dstPath) {
        this.srcFile = srcFile;
        this.nameFileMap = nameFileMap;
        this.srcPath = srcPath;
        this.dstPath = dstPath;
    }

    public boolean process() {
        if (StrUtil.isEmpty(srcFile)) {
            return false;
        }

        // Analyse the source, find the depended files
        Set<String> dependedFileNameSet = null;

        // If found: ensure the depended files can be found
        if (!EmptyUtil.isEmpty(dependedFileNameSet)) {
            if (EmptyUtil.isEmpty(nameFileMap)) {
                return false;
            }

            boolean depended = true;
            for (String dependedFileName : dependedFileNameSet) {
                if (!nameFileMap.containsKey(dependedFileName.toLowerCase())) {
                    depended = false;
                    break;
                }
            }
            if (!depended) {
                return false;
            }
        }

        // Read the source file

        // Merge the depended files
        if (!EmptyUtil.isEmpty(dependedFileNameSet)) {

        }

        // Save to dst file
        String dstFile = FileUtil.getOutputFile(srcFile, srcPath, dstPath);
        return true;
    }
}
