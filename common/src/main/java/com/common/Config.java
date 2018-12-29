package com.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.file.FileUtil;
import com.common.file.ResourceUtil;
import com.common.util.JsonUtil;
import com.common.util.StrUtil;

import java.util.*;

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
    public boolean readFile(String fileName, boolean isResource) {
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

    /**
     * Read the config value
     * @param key
     */
    public String readValue(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }

        JSONObject obj = lastFileObj;
        if (obj.containsKey(key)) {
            return obj.getString(key);
        }
        return null;
    }

    public String[] readValues(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }

        JSONObject obj = lastFileObj;
        if (obj.containsKey(key)) {
            List<String> values = new ArrayList<String>();
            JSONArray arr = obj.getJSONArray(key);
            if (arr != null && arr.size() > 0) {
                for (int i = 0; i < arr.size(); i++) {
                    values.add(arr.get(i).toString());
                }
            }

            if (values.size() > 0) {
                String[] valueArr = new String[values.size()];
                values.toArray(valueArr);
                return valueArr;
            }
        }
        return null;
    }
}
