package com.tool.groovy.pack;

import com.common.Config;
import org.junit.Assert;
import org.junit.Test;

public class PackConfigTest {
    @Test
    public void testPackConfig() {
        boolean ret = PackConfig.getInst().readFile("config.json");
        Assert.assertEquals(true, ret);

        String v = Config.getInst().readValue("version");
        Assert.assertEquals("0.0.1", v);

        String[] arr = Config.getInst().readValues("ignoredFileNamePatterns");
        Assert.assertEquals(2, arr.length);
    }
}
