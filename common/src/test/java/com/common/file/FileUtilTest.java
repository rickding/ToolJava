package com.common.file;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileUtilTest {
    @Test
    public void testGetOutputFile() {
        Map<String[], String> mapIO = new HashMap<String[], String>() {{
            put(new String[]{null, null, null}, null);
            put(new String[]{"a", null, null}, "a_pack");
            put(new String[]{"a", ".\\", null}, "a_pack");
            put(new String[]{"a", ".\\", ".\\dst"}, ".\\dst\\a");
            put(new String[]{"a", ".\\src", ".\\dst"}, ".\\dst\\a");
            put(new String[]{"src\\b\\a", ".\\src", ".\\dst"}, ".\\dst\\a");
            put(new String[]{".\\src\\b\\a", ".\\src", ".\\dst"}, ".\\dst\\b\\a");
        }};
        for (Map.Entry<String[], String> io : mapIO.entrySet()) {
            String[] i = io.getKey();
            String ret = FileUtil.getOutputFile(i[0], i[1], i[2]);
            Assert.assertEquals(io.getValue(), ret);
        }
    }

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
