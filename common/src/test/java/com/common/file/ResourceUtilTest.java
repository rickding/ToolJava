package com.common.file;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ResourceUtilTest {
    @Test
    public void testRead() {
        Map<String, Boolean> mapIO = new HashMap<String, Boolean> () {{
            put("config.json", true);
            put("test.txt", true);
            put("abc.json", false);
            put("", false);
            put(null, false);
        }};
        for (Map.Entry<String, Boolean> io : mapIO.entrySet()) {
            String[] ret = ResourceUtil.read(io.getKey());
            Assert.assertEquals(io.getValue(), ret != null);

            String ret2 = ResourceUtil.readAsStr(io.getKey());
            Assert.assertEquals(io.getValue(), ret2 != null);
        }
    }
}
