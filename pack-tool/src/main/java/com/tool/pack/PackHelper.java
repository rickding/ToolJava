package com.tool.pack;

import com.common.file.FileUtil;
import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackHelper {
    private String fileExt = ".groovy";

    public String[] process(String filePath, String dstPath) {
        if (StrUtil.isEmpty(filePath)) {
            return null;
        }
        return process(filePath, null, filePath, dstPath);
    }

    private String[] process(String filePath, Map<String, String> parentNameFileMap, String srcPath, String dstPath) {
        if (StrUtil.isEmpty(filePath)) {
            return null;
        }

        // Map the file name. The later one from sub folder will override the previous one.
        // Note the same level of sub folders will not override each other.
        Map<String, String> nameFileMap = new HashMap<String, String>();
        if (parentNameFileMap != null && parentNameFileMap.size() > 0) {
            nameFileMap.putAll(parentNameFileMap);
        }

        List<String> fileList = new ArrayList<String>();
        File[] files = FileUtil.findFiles(filePath, fileExt);
        if (!EmptyUtil.isEmpty(files)) {
            // Map firstly
            for (File file : files) {
                nameFileMap.put(file.getName().toLowerCase(), file.getPath());
            }

            // Process files
            for (File file : files) {
                if ((new PackFile(file.getPath(), nameFileMap, srcPath, dstPath)).process()) {
                    fileList.add(file.getPath());
                }
            }
        }

        // subFolders
        File[] folders = FileUtil.findSubFolders(filePath);
        if (!EmptyUtil.isEmpty(folders)) {
            for (File folder : folders) {
                String[] fileArr = process(folder.getPath(), nameFileMap, srcPath, dstPath);
                if (fileArr != null && fileArr.length > 0) {
                    fileList.addAll(Arrays.asList(fileArr));
                }
            }
        }

        // Convert to array
        if (fileList.size() <= 0) {
            return null;
        }
        String[] fileArr = new String[fileList.size()];
        fileList.toArray(fileArr);
        return fileArr;
    }
}
