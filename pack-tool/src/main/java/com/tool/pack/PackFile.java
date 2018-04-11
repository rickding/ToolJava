package com.tool.pack;

import com.common.file.FileUtil;
import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PackFile {
    private String srcFile;
    private Map<String, String> nameFileMap;
    private String srcPath;
    private String dstPath;
    private String fileExt;

    private final String localImportFlag = "import com.erp.";
    private final String specialDependedFlag = "* Note: need ";

    public PackFile(String srcFile, Map<String, String> nameFileMap, String srcPath, String dstPath, String fileExt) {
        this.srcFile = srcFile;
        this.nameFileMap = nameFileMap;
        this.srcPath = srcPath;
        this.dstPath = dstPath;
        this.fileExt = fileExt;
    }

    public boolean process() {
        if (StrUtil.isEmpty(srcFile)) {
            return false;
        }

        // Read the source file
        String[] lines = FileUtil.read(srcFile);
        if (EmptyUtil.isEmpty(lines)) {
            return false;
        }

        // Filter the imports, find the depended files. Note: only use the last file name and ignore the package path
        String[] importedFiles = filterImports(lines);

        // If found: ensure the depended files can be found
        boolean hasImportedFiles = true;
        if (!EmptyUtil.isEmpty(importedFiles)) {
            if (EmptyUtil.isEmpty(nameFileMap)) {
                hasImportedFiles = false;
            }
            for (String dependedFileName : importedFiles) {
                if (!nameFileMap.containsKey(dependedFileName.toLowerCase())) {
                    hasImportedFiles = false;
                    break;
                }
            }
        }
        if (!hasImportedFiles) {
            return false;
        }

        // Merge the depended files
        if (!EmptyUtil.isEmpty(importedFiles)) {

        }

        // Save to dst file
        String dstFile = FileUtil.getOutputFile(srcFile, srcPath, dstPath);
        return FileUtil.write(dstFile, lines);
    }

    private String appendFileExt(String fileName) {
        if (StrUtil.isEmpty(fileName)) {
            return null;
        }
        fileName = fileName.trim().toLowerCase();
        if (!StrUtil.isEmpty(fileExt) && !fileName.endsWith(fileExt)) {
            fileName = String.format("%s%s", fileName, fileExt);
        }
        return fileName;
    }

    /**
     * Filter the imports and add to the list
     * @param lines
     * @return
     */
    private String[] filterImports(String[] lines) {
        if (EmptyUtil.isEmpty(lines)) {
            return null;
        }

        Set<String> importFileNameSet = new HashSet<String>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (StrUtil.isEmpty(line)) {
                continue;
            }

            // Check the local imports
            String tmp = line.trim().toLowerCase();
            if (tmp.startsWith(localImportFlag.toLowerCase())) {
                // Comment
                lines[i] = String.format("// Pack: %s", line);

                // Get the class (file) name
                String fileName = tmp.substring(tmp.lastIndexOf(".") + 1);
                if (!StrUtil.isEmpty(fileName)) {
                    importFileNameSet.add(appendFileExt(fileName));
                }
            } else if (tmp.startsWith(specialDependedFlag.toLowerCase())) {
                // Get the file name
                String fileName = tmp.substring(tmp.indexOf(specialDependedFlag.toLowerCase()) + specialDependedFlag.length());
                if (!StrUtil.isEmpty(fileName)) {
                    importFileNameSet.add(appendFileExt(fileName));
                }
            }
        }

        // Convert to array
        if (importFileNameSet.size() <= 0) {
            return null;
        }
        String[] importFileNames = new String[importFileNameSet.size()];
        importFileNameSet.toArray(importFileNames);
        return importFileNames;
    }
}
