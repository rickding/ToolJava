package com.common;

import com.common.utils.StrUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilTest {
    @Test
    public void testSendHttpPostJson() {
        String url = "http://localhost:1337/parse/classes/GameScore";
        Map<String, String> headers = new HashMap<String, String>() {{
            put("X-Parse-Application-Id", "myAppId");
            put("X-Parse-Master-Key", "myMasterKey");
        }};
        String param = "{\"score\":1337,\"playerName\":\"Sean from Java\",\"cheatMode\":false}";

        String ret = HttpUtil.sendHttpPostJson(url, headers, param);
        System.out.println(ret);
    }

    @Test
    public void testHttpGet() {
        String ret = HttpUtil.sendHttpGet("http://www.baidu.com", null);
        if (!StrUtil.isEmpty(ret) && ret.length() > 100) {
            ret = ret.substring(0, 100);
        }
        System.out.println(ret);
    }
}
