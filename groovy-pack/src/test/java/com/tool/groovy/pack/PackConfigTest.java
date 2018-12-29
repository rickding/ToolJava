package com.tool.groovy.pack;

import org.junit.Assert;
import org.junit.Test;

public class PackConfigTest {
    @Test
    public void testReadFile() {
        boolean ret = PackConfig.getInst().readFile("config.json", true);
        Assert.assertEquals(true, ret);
    }
}
