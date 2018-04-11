package com.common.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/9/23.
 */
public class EmptyUtilTest {
    @Test
    public void testIsEmpty() {
        Map<String[], Boolean> mapIO = new HashMap<String[], Boolean>() {{
            put(null, true);
            put(new String[]{}, true);
            put(new String[]{"test"}, false);
        }};

        for (Map.Entry<String[], Boolean> io : mapIO.entrySet()) {
            boolean ret = EmptyUtil.isEmpty(io.getKey());
            Assert.assertEquals(io.getValue(), ret);
        }
    }
}
