package com.tool.pack;

import org.junit.Assert;
import org.junit.Test;

public class PackConfigTest {
    @Test
    public void testRead() {
        PackConfig.setInst(new PackConfig());
        boolean ret = PackConfig.getInst().read("config.json", true);
        Assert.assertEquals(true, ret);
    }
}
