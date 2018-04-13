package com.tool.pack;

import com.common.file.FileUtil;
import com.common.file.FileWriter;
import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PackFile {
    private String fileName;
    private String filePath;

    private String[] lineArr;
    private List<String> headerList;
    private Set<PackFile> neededFileSet;

    private String packagePath;
    private String[] importedClassPathArr;
    private String[] neededFileNameArr;
    private String[] classNameArr;

    private final static String packageFlag = "package ";
    private final static String importFlag = "import ";
    private final static String localImportFlag = "import com.erp.";
    private final static String neededFileFlag = "* Note: need ";
    private final static String[] classFlagArr = {"class ", "public class "};
    private final static String[] classEndingFlagArr = {" ", "{"};

    public PackFile(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Write to file, recursive to find the needed files while remove the duplicated ones
     * @param filePath
     * @return
     */
    public boolean write(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return false;
        }

        FileUtil.mkdirs(filePath);
        FileWriter writer = new FileWriter(filePath, false);
        if (!writer.open()) {
            System.out.printf("Fail to create output file: %s\n", filePath);
            return false;
        }

        // Write
        Set<PackFile> writtenFileSet = new HashSet<PackFile>();
        write(writer, writtenFileSet);

        // Close
        writer.close();
        return true;
    }

    private boolean write(FileWriter writer, Set<PackFile> writtenFileSet) {
        if (writer == null || !writer.isOpen() || writtenFileSet == null) {
            return false;
        }

        // Write
        boolean writeItself = false;
        if (!writtenFileSet.contains(this)) {
            writtenFileSet.add(this);
            writeItself = true;
        } else {
            System.out.printf("Ignore one duplicated file: %s\n", this.toString());
        }

        // Recursive the needed ones
        boolean ret = true;
        if (!EmptyUtil.isEmpty(neededFileSet)) {
            for (PackFile file : neededFileSet) {
                if (!file.write(writer, writtenFileSet)) {
                    ret = false;
                } else {
                    writer.writeLine("");
                }
            }
        }

        if (writeItself) {
            writer.writeLine(String.format("// File: %s.%s", packagePath, fileName));
            writer.writeLine(headerList);
            writer.writeLines(lineArr);
        }
        return ret;
    }

    /**
     * Pack the depended files
     * @param fileMap
     * @return
     */
    public boolean pack(Map<String, PackFile> fileMap) {
        if (StrUtil.isEmpty(filePath)) {
            return false;
        }
        if (StrUtil.isEmpty(fileName)) {
            System.out.printf("Please call scan() firstly, %s", filePath);
            return false;
        }

        // Include the local imports and needed files
        headerList = new ArrayList<String>();
        neededFileSet = new HashSet<PackFile>();
        if (!EmptyUtil.isEmpty(fileMap)) {
            for (String[] fileNameArr : new String[][]{importedClassPathArr, neededFileNameArr}) {
                if (!EmptyUtil.isEmpty(fileNameArr)) {
                    for (String fileName : fileNameArr) {
                        boolean found = fileMap.containsKey(fileName);
                        headerList.add(String.format("* %sFound: %s", found ? "" : "Not ", fileName));
                        if (found) {
                            neededFileSet.add(fileMap.get(fileName));
                        }
                    }
                }
            }
        }
        if (headerList.size() > 0) {
            headerList.add(0, "/**");
            headerList.add("*/");
        }
        return true;
    }

    /**
     * Scan and analyse
     * @return
     */
    public boolean scan() {
        if (StrUtil.isEmpty(filePath)) {
            return false;
        }

        // Read the source file
        lineArr = FileUtil.read(filePath);
        if (EmptyUtil.isEmpty(lineArr)) {
            return false;
        }

        // Get the file name
        File file = new File(filePath);
        fileName = file.getName();

        // Analyse the file
        List<String> importedClassPathList = new ArrayList<String>();
        List<String> neededFileNameList = new ArrayList<String>();
        List<String> classNameList = new ArrayList<String>();
        for (int i = 0; i < lineArr.length; i++) {
            String line = lineArr[i];
            if (StrUtil.isEmpty(line)) {
                continue;
            }

            // Check the package
            line = line.trim();
            if (line.startsWith(packageFlag)) {
                lineArr[i] = String.format("// %s", line);
                packagePath = line.substring(packageFlag.length()).trim();
                continue;
            }

            // Check the local import
            if (line.startsWith(localImportFlag)) {
                // Comment
                lineArr[i] = String.format("// Pack: %s", line);

                // Get the class path
                String classPath = line.substring(importFlag.length()).trim();
                if (!StrUtil.isEmpty(classPath)) {
                    importedClassPathList.add(classPath);
                }
                continue;
            }

            // Check the class name
            if (line.startsWith(neededFileFlag)) {
                // Get the file name
                String fileName = line.substring(neededFileFlag.length()).trim();
                if (!StrUtil.isEmpty(fileName)) {
                    neededFileNameList.add(fileName);
                }
                continue;
            }

            // Check the class name
            for (String classFlag : classFlagArr) {
                if (line.startsWith(classFlag)) {
                    // Get the class name
                    String className = line.substring(classFlag.length()).trim();
                    if(!StrUtil.isEmpty(className)) {
                        // Remove the ending
                        for (String endingFlag : classEndingFlagArr) {
                            int endingIndex = className.indexOf(endingFlag);
                            if (endingIndex > 0) {
                                className = className.substring(0, endingIndex).trim();
                                break;
                            }
                        }
                        classNameList.add(className);
                    }
                    break;
                }
            }
        }

        // Convert to array
        if (importedClassPathList.size() <= 0) {
            importedClassPathArr = null;
        } else {
            importedClassPathArr = new String[importedClassPathList.size()];
            importedClassPathList.toArray(importedClassPathArr);
        }
        if (neededFileNameList.size() <= 0) {
            neededFileNameArr = null;
        } else {
            neededFileNameArr = new String[neededFileNameList.size()];
            neededFileNameList.toArray(neededFileNameArr);
        }
        if (classNameList.size() <= 0) {
            classNameArr = null;
        } else {
            classNameArr = new String[classNameList.size()];
            classNameList.toArray(classNameArr);
        }
        return true;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public String[] getClassNameArr() {
        return classNameArr;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PackFile)) {
            return false;
        }
        PackFile item = (PackFile) obj;
        if (StrUtil.isEmpty(filePath) && StrUtil.isEmpty(item.filePath)) {
            return true;
        }
        if (StrUtil.isEmpty(filePath) || StrUtil.isEmpty(item.filePath)) {
            return false;
        }
        return filePath.trim().equalsIgnoreCase(item.filePath.trim());
    }

    @Override
    public String toString() {
        return String.format("%s.%s", packagePath, fileName);
    }
}
