package com.common;

import com.alibaba.fastjson.JSONObject;
import com.common.file.FileUtil;
import com.common.file.ResourceUtil;
import com.common.util.JsonUtil;
import com.common.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class Config {
    protected static Config inst = null;

    public static Config getInst() {
        if (inst == null) {
            synchronized (Config.class) {
                if (inst == null) {
                    inst = new Config();
                }
            }
        }
        return inst;
    }

    // Save the file name and content map
    private Map<String, Object> fileObjMap;
    protected JSONObject lastFileObj;

    protected Config() {
        fileObjMap = new HashMap<String, Object>();
    }

    /**
     * Read config file
     * @param fileName
     * @param isResource
     * @return
     */
    public boolean read(String fileName, boolean isResource) {
        if (StrUtil.isEmpty(fileName)) {
            return false;
        }
        if (fileObjMap.containsKey(fileName.trim().toLowerCase())) {
            return true;
        }

        // Read the file
        String str = isResource ? ResourceUtil.readAsStr(fileName) : FileUtil.readAsStr(fileName);
        lastFileObj = (JSONObject)JsonUtil.parseJson(str);
        fileObjMap.put(fileName.trim().toLowerCase(), lastFileObj);
        return true;
    }
}
