package com.common;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {
    @Test
    public void testConfig() {
        boolean ret = Config.getInst().readFile("config.json");
        Assert.assertEquals(true, ret);

        String v = Config.getInst().readValue("version");
        Assert.assertEquals("0.0.0", v);

        String[] arr = Config.getInst().readValues("ignoredFileNamePatterns");
        Assert.assertEquals(1, arr.length);
    }
}
