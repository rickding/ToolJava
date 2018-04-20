package com.common.util;

import org.junit.Test;

public class JsonUtilTest {
    @Test
    public void testParseJson() {
        String[] i = {
                "{",
                "   \"fileExt\": \".groovy\",",
                "   \"ignoredFileNamePatterns\": [",
                "       \"\\\\w*[Tt][Ee][Ss][Tt].groovy\",",
                "       \"[Tt][Ee][Ss][Tt]\\\\w*.groovy\"",
                "   ]",
                "}"
        };
        String str = StrUtil.join(i, "\n");
        System.out.println(str);
        Object ret = JsonUtil.parseJson(str);
        System.out.println(ret);
    }
}
