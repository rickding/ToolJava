package com.common.utils;

import com.common.file.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FileUtilTest {
    @Test
    public void testFindSubFolders() {
        File[] ret = FileUtil.findSubFolders(".", true);
        System.out.printf("Folders: %d\n", ret == null ? 0 : ret.length);
        if (ret != null) {
            for (File file : ret) {
                System.out.println(file.getPath());
            }
        }
        Assert.assertTrue(ret != null && ret.length > 0);
    }

    @Test
    public void testFindFiles() {
        File[] ret = FileUtil.findFiles(".", ".java", true);
        System.out.printf("Files: %d\n", ret == null ? 0 : ret.length);
        if (ret != null) {
            for (File file : ret) {
                System.out.println(file.getPath());
            }
        }
        Assert.assertTrue(ret != null && ret.length > 0);
    }
}
