package com.common;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {
    @Test
    public void testRead() {
        boolean ret = Config.getInst().read("config.json", true);
        Assert.assertEquals(true, ret);
    }
}
