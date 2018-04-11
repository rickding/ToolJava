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
            int index = 0;
            for (File file : ret) {
                System.out.println(file.getPath());
                if (index++ > 1) {
                    break;
                }
            }
        }
        Assert.assertTrue(ret != null && ret.length > 0);
    }

    @Test
    public void testFindFiles() {
        File[] ret = FileUtil.findFiles(".", ".java", true);
        System.out.printf("Files: %d\n", ret == null ? 0 : ret.length);
        if (ret != null) {
            int count = 0;
            for (File file : ret) {
                System.out.println(file.getPath());
                if (count++ > 2) {
                    break;
                }
            }
        }
        Assert.assertTrue(ret != null && ret.length > 0);
    }
}
