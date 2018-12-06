package com.tool.groovy.pack;

import org.junit.Assert;
import org.junit.Test;

public class PackConfigTest {
    @Test
    public void testRead() {
        boolean ret = PackConfig.getInst().read("config.json", true);
        Assert.assertEquals(true, ret);
    }
}
