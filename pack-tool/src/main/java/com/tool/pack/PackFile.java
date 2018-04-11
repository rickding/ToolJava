package com.tool.pack;

import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.util.Map;
import java.util.Set;

public class PackFile {
    private String srcFile;
    private String dstFile;
    private Map<String, String> nameFileMap;

    public PackFile(String srcFile, String dstFile, Map<String, String> nameFileMap) {
        this.srcFile = srcFile;
        this.dstFile = dstFile;
        this.nameFileMap = nameFileMap;
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
        if (StrUtil.isEmpty(dstFile)) {
            dstFile = String.format("%s_pack", srcFile);
        }
        return true;
    }
}
