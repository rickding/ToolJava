package com.tool.doc.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParserHelperTest {

    @Test
    public void testParseName() {
        Map<Object[], String> mapIO = new HashMap<Object[], String>() {{
            put(new Object[]{null, null, null, 0, null}, "");
            put(new Object[]{"name", null, null, 0, null}, "name");

            put(new Object[]{
                    "public class <span class=\"typeNameLabel\">BatchSuspendPo</span>",
                    ParserConfig.TableFlag, ParserConfig.TableSplitter, ParserConfig.TableIndex, ParserConfig.TableTrimArr
            }, "BatchSuspendPo");

            put(new Object[]{
                    "<td class=\"colFirst\"><code>private <a href=\"../../../../../com/ideatech/ams/account/enums/AcctBigType.html\" title=\"com.ideatech.ams.account.enums中的枚举\">AcctBigType</a></code></td>",
                    ParserConfig.TypeFlag, ParserConfig.TypeSplitter, ParserConfig.TypeIndex, ParserConfig.TypeTrimArr
            }, "AcctBigType");
            put(new Object[]{
                    "<td class=\"colFirst\"><code>private java.lang.String</code></td>",
                    ParserConfig.TypeFlag, ParserConfig.TypeSplitter, ParserConfig.TypeIndex, ParserConfig.TypeTrimArr
            }, "String");

            put(new Object[]{
                    "<td class=\"colLast\"><code><span class=\"memberNameLink\"><a href=\"../../../../../com/ideatech/ams/account/entity/BatchSuspendPo.html#acctBigType\">acctBigType</a></span></code>",
                    ParserConfig.FieldFlag, ParserConfig.FieldSplitter, ParserConfig.FieldIndex, ParserConfig.FieldTrimArr
            }, "acctBigType");

            put(new Object[]{
                    "<div class=\"block\">批量久悬任务表</div>",
                    ParserConfig.CommentFlag, ParserConfig.CommentSplitter, ParserConfig.CommentIndex, ParserConfig.CommentTrimArr
            }, "批量久悬任务表");
            put(new Object[]{
                    "<div class=\"block\">账户性质大类</div>",
                    ParserConfig.CommentFlag, ParserConfig.CommentSplitter, ParserConfig.CommentIndex, ParserConfig.CommentTrimArr
            }, "账户性质大类");
        }};

        for (Map.Entry<Object[], String> io : mapIO.entrySet()) {
            Object[] params = io.getKey();
            String ret = ParserHelper.parseName((String) params[0], (String) params[1], (String) params[2], (Integer) params[3], (String[]) params[4]);
            Assert.assertEquals(io.getValue(), ret);
        }
    }
}
