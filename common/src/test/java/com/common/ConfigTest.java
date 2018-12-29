package com.common;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {
    @Test
    public void testReadFile() {
        boolean ret = Config.getInst().readFile("config.json", true);
        Assert.assertEquals(true, ret);
    }
}
