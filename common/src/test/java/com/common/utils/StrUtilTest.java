package com.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/9/23.
 */
public class StrUtilTest {
    @Test
    public void testIsEmpty() {
        Map<String, Boolean> mapIO = new HashMap<String, Boolean>() {{
            put(null, true);
            put("", true);
            put("t", false);
        }};

        for (Map.Entry<String, Boolean> io : mapIO.entrySet()) {
            boolean ret = StrUtil.isEmpty(io.getKey());
            Assert.assertEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testSplit() {
        Map<String, String[]> mapIO = new HashMap<String, String[]>() {{
            put(null, null);
            put("", null);
            put("t", new String[] {"t"});
            put("t,a", new String[] {"t", "a"});
        }};

        for (Map.Entry<String, String[]> io : mapIO.entrySet()) {
            String[] ret = StrUtil.split(io.getKey(), ",");
            Assert.assertArrayEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testJoin() {
        Map<String, String[]> mapIO = new HashMap<String, String[]>() {{
            put(null, null);
            put("t", new String[] {"t"});
            put("t, a", new String[] {"t", "a"});
        }};

        for (Map.Entry<String, String[]> io : mapIO.entrySet()) {
            String ret = StrUtil.join(io.getValue(), ", ");
            Assert.assertEquals(io.getKey(), ret);
        }
    }
}
