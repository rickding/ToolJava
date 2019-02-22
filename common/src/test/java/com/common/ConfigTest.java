package com.common;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {
    @Test
    public void testConfig() {
        Config inst = Config.getInst();
        boolean ret = inst.readFile("config.json", true);
        Assert.assertEquals(true, ret);

        Assert.assertEquals(inst.getBoolean("compat"), true);
        Assert.assertEquals(inst.getStr("version"), "0.0.0");
        Assert.assertEquals(inst.getStrArr("ignoredFileNamePatterns").length, 2);
    }
}
