package com.common.file;

import com.common.utils.EmptyUtil;
import com.common.utils.StrUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    public static String[] read(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return null;
        }

        FileReader reader = new FileReader(filePath);
        if (!reader.open()) {
            System.out.printf("Fail to open file: %s\n", filePath);
            return null;
        }

        // read and update, then write
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

    public static String getOutputFileName(File parent, File file, String fileExt, String newFileName, String newFolderName) {
        if (file == null || parent == null) {
            return null;
        }
        return getOutputFileName(parent, file.getName(), fileExt, newFileName, newFolderName);
    }

    public static String getOutputFileName(File parent, String fileName, String fileExt, String newFileName, String newFolderName) {
        if (parent == null) {
            return null;
        }

        String outputFileName = null;
        if (parent.isDirectory()) {
            // Prepare the folder firstly
            String outputFolderName = String.format("%s%s", parent.getPath(), newFolderName);
            File folder = new File(outputFolderName);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // Output file name
            outputFileName = String.format("%s\\%s", outputFolderName, fileName);
        } else {
            outputFileName = parent.getPath();
        }

        // Format the file name
        if (!StrUtil.isEmpty(outputFileName)) {
            if (!StrUtil.isEmpty(fileExt) && outputFileName.toLowerCase().endsWith(fileExt)) {
                outputFileName = outputFileName.substring(0, outputFileName.length() - fileExt.length());
            }
            outputFileName = String.format("%s%s", outputFileName, newFileName);
        }

        return outputFileName;
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
