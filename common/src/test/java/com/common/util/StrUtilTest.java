package com.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/9/23.
 */
public class StrUtilTest {
    @Test
    public void testMatches() {
        Map<String[], Boolean> mapIO = new HashMap<String[], Boolean>() {{
            put(new String[]{null, null}, false);
            put(new String[]{"AbcTestC.groovy", "((\\w*[Tt][Ee][Ss][Tt])|([Tt][Ee][Ss][Tt]\\w*)).groovy"}, false);
            put(new String[]{"AbcTest.groovy", "((\\w*[Tt][Ee][Ss][Tt])|([Tt][Ee][Ss][Tt]\\w*)).groovy"}, true);
            put(new String[]{"TestC.groovy", "((\\w*[Tt][Ee][Ss][Tt])|([Tt][Ee][Ss][Tt]\\w*)).groovy"}, true);
            put(new String[]{"Utils.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, true);
            put(new String[]{"StrUtil.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, true);
            put(new String[]{"utils.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, true);
            put(new String[]{"util.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, true);
            put(new String[]{"uti.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, false);
            put(new String[]{"Helper.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, true);
            put(new String[]{"IssueHelper.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, true);
            put(new String[]{"HelperVer.groovy", "\\w*(([Uu]tils*)|([Hh]elper)).groovy"}, false);
        }};
        for (Map.Entry<String[], Boolean> io : mapIO.entrySet()) {
            String[] params = io.getKey();
            boolean ret = StrUtil.matches(params[0], params[1]);
            Assert.assertEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testIsEmpty() {
        Map<String, Boolean> mapIO = new HashMap<String, Boolean>() {{
            put(null, true);
            put("", true);
            put("t", false);
        }};

        for (Map.Entry<String, Boolean> io : mapIO.entrySet()) {
            boolean ret = StrUtil.isEmpty(io.getKey());
            Assert.assertEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testSplit() {
        Map<String, String[]> mapIO = new HashMap<String, String[]>() {{
            put(null, null);
            put("", null);
            put("t", new String[] {"t"});
            put("t,a", new String[] {"t", "a"});
        }};

        for (Map.Entry<String, String[]> io : mapIO.entrySet()) {
            String[] ret = StrUtil.split(io.getKey(), ",");
            Assert.assertArrayEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testJoin() {
        Map<String, String[]> mapIO = new HashMap<String, String[]>() {{
            put(null, null);
            put("t", new String[] {"t"});
            put("t, a", new String[] {"t", "a"});
        }};

        for (Map.Entry<String, String[]> io : mapIO.entrySet()) {
            String ret = StrUtil.join(io.getValue(), ", ");
            Assert.assertEquals(io.getKey(), ret);
        }
    }

    @Test
    public void testReplace() {
        Map<String, String> mapIO = new HashMap<String, String>(){{
            put(null, null);
            put("", "");
            put("a", "a");
            put("List&lt;EquityShare&gt;", "List<EquityShare>");
        }};

        Map<String, String> replaceMap = new HashMap<String, String>() {{
            put("&lt;", "<");
            put("&gt;", ">");
        }};

        for (Map.Entry<String, String> io : mapIO.entrySet()) {
            String ret = StrUtil.replace(io.getKey(), replaceMap);
            Assert.assertEquals(io.getValue(), ret);
        }
    }
}
