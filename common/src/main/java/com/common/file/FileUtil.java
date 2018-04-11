package com.common.file;

import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    /**
     * Write file
     * @param filePath
     * @param lines
     * @return
     */
    public static boolean write(String filePath, String[] lines) {
        if (StrUtil.isEmpty(filePath)) {
            return false;
        }

        FileWriter writer = new FileWriter(filePath, false);
        if (!writer.open()) {
            System.out.printf("Fail to create output file: %s\n", filePath);
            return false;
        }

        // Write
        if (!EmptyUtil.isEmpty(lines)) {
            for (String line : lines) {
                writer.writeLine(line);
            }
        }

        // Close
        writer.close();
        return true;
    }

    /**
     * Read file
     * @param filePath
     * @return
     */
    public static String[] read(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return null;
        }

        FileReader reader = new FileReader(filePath, false);
        if (!reader.open()) {
            System.out.printf("Fail to open file: %s\n", filePath);
            return null;
        }

        // read
        List<String> lines = new ArrayList<String>();
        String str;
        while ((str = reader.readLine()) != null) {
            lines.add(str);
        }

        // close
        reader.close();

        String[] strs = new String[lines.size()];
        lines.toArray(strs);
        return strs;
    }

    /**
     * Format the output file: src file name, src path, dst path
     * @return
     */
    public static String getOutputFile(String srcFile, String srcPath, String dstPath) {
        return getOutputFile(srcFile, srcPath, dstPath, null);
    }

    public static String getOutputFile(String srcFile, String srcPath, String dstPath, String postfix) {
        if (StrUtil.isEmpty(srcFile)) {
            return null;
        }

        // Check the postfix
        if (StrUtil.isEmpty(postfix)) {
            postfix = "";
        } else if (!postfix.startsWith("_") && !postfix.startsWith("-")) {
            postfix = String.format("_%s", postfix);
        }

        // Check the dst
        if (StrUtil.isEmpty(dstPath)) {
            return String.format("%s%s", srcFile, StrUtil.isEmpty(postfix) ? "_pack" : postfix);
        }

        // Append the file name directly
        File src = new File(srcFile);
        String srcFileName = src.getName();
        if (!StrUtil.isEmpty(srcPath)) {
            // Remove the src path
            int index = srcFile.toLowerCase().indexOf(srcPath.toLowerCase());
            if (index >= 0) {
                srcFileName = srcFile.substring(index + srcPath.length());
            }
        }
        String dstFile = String.format("%s%s%s%s", dstPath, dstPath.endsWith("\\") || srcFileName.startsWith("\\") ? "" : "\\", srcFileName, postfix);
        if (srcFile.trim().equalsIgnoreCase(dstFile.trim())) {
            dstFile = String.format("%s_pack", dstFile);
        }
        return dstFile;
    }

    /**
     * List the files
     *
     * @param filePath
     * @return
     */
    public static File[] findFiles(String filePath) {
        return findFiles(filePath, null, null, null, false);
    }

    public static File[] findFiles(String filePath, boolean recursive) {
        return findFiles(filePath, null, null, null, recursive);
    }

    public static File[] findFiles(String filePath, String fileExt) {
        return findFiles(filePath, null, fileExt, null, false);
    }

    public static File[] findFiles(String filePath, String fileExt, boolean recursive) {
        return findFiles(filePath, null, fileExt, null, recursive);
    }

    public static File[] findFiles(String filePath, String fileExt, String ignoreFileNamePostfix, boolean recursive) {
        return findFiles(filePath, null, fileExt, ignoreFileNamePostfix, recursive);
    }

    public static File[] findFiles(String filePath, final String fileNamePrefix, final String fileExt, final String ignoreFileNamePostfix, final boolean recursive) {
        if (StrUtil.isEmpty(filePath)) {
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        // File or directory, while not iterate sub folders
        File[] files = null;
        if (file.isFile() && filePath.toLowerCase().endsWith(fileExt.toLowerCase())) {
            files = new File[]{file};
        } else if (file.isDirectory()) {
            files = file.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return recursive;
                    } else if (pathname.isFile()) {
                        String name = pathname.getName();
                        if (StrUtil.isEmpty(name)) {
                            return false;
                        }

                        String str = name.toLowerCase();
                        if (!StrUtil.isEmpty(fileNamePrefix) && !str.startsWith(fileNamePrefix.toLowerCase())
                                || !StrUtil.isEmpty(fileExt) && !str.endsWith(fileExt.toLowerCase())
                                ) {
                            return false;
                        }
                        return StrUtil.isEmpty(ignoreFileNamePostfix) || !str.endsWith(ignoreFileNamePostfix.toLowerCase());
                    }
                    return false;
                }
            });

            // Filter sub folders
            if (recursive && !EmptyUtil.isEmpty(files)) {
                List<File> fileList = new ArrayList<File>();
                for (File tmp : files) {
                    if (tmp.isFile()) {
                        fileList.add(tmp);
                    } else if (recursive) {
                        File[] filesInSubFolder = findFiles(tmp.getPath(), fileNamePrefix, fileExt, ignoreFileNamePostfix, true);
                        if (!EmptyUtil.isEmpty(filesInSubFolder)) {
                            fileList.addAll(Arrays.asList(filesInSubFolder));
                        }
                    }
                }

                // Convert to array
                if (fileList.size() > 0) {
                    files = new File[fileList.size()];
                    fileList.toArray(files);
                } else {
                    files = null;
                }
            }
        }
        return files;
    }

    /**
     * List the sub folders
     *
     * @param filePath
     * @return
     */
    public static File[] findSubFolders(String filePath) {
        return findSubFolders(filePath, false);
    }

    public static File[] findSubFolders(String filePath, boolean recursive) {
        if (StrUtil.isEmpty(filePath)) {
            return null;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }

        // List
        File[] folders = file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.exists() && pathname.isDirectory();
            }
        });

        // Loop the sub folders
        if (recursive && !EmptyUtil.isEmpty(folders)) {
            List<File> folderList = new ArrayList<File>();
            folderList.addAll(Arrays.asList(folders));

            for (File folder : folders) {
                File[] subFolders = findSubFolders(folder.getPath(), true);
                if (!EmptyUtil.isEmpty(subFolders)) {
                    folderList.addAll(Arrays.asList(subFolders));
                }
            }

            // Convert to array
            if (folderList.size() > 0) {
                folders = new File[folderList.size()];
                folderList.toArray(folders);
            } else {
                folders = null;
            }
        }
        return folders;
    }
}
