package com.tool.groovy.pack;

import org.junit.Assert;
import org.junit.Test;

public class PackConfigTest {
    @Test
    public void testConfig() {
        PackConfig inst = PackConfig.getInst();
        boolean ret = inst.readFile("config.json", true);
        Assert.assertEquals(true, ret);

        Assert.assertEquals(inst.getCompat(), true);
        Assert.assertEquals(inst.getVersion(), "0.0.1");
        Assert.assertEquals(inst.getIgnoredFileNamePatternSet().size(), 2);
    }
}
