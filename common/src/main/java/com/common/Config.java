package com.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.file.FileUtil;
import com.common.file.ResourceUtil;
import com.common.util.EmptyUtil;
import com.common.util.JsonUtil;
import com.common.util.StrUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    protected JSONObject fileObj;

    protected Config() {
        fileObjMap = new HashMap<String, Object>();
    }

    /**
     * Read file: search file and resource
     */
    public boolean readFile() {
        return readFile("config.json");
    }

    public boolean readFile(String fileName) {
        return readFile(null, fileName);
    }

    public boolean readFile(String filePath, String fileName) {
        // Read from the source folders
        for (String srcPath : new String[]{filePath, "./"}) {
            File[] fileArr = FileUtil.findFiles(srcPath, fileName);
            if (!EmptyUtil.isEmpty(fileArr)) {
                for (File file : fileArr) {
                    if (file.getName().trim().equalsIgnoreCase(fileName)) {
                        boolean ret = readFile(file.getPath(), false);
                        System.out.printf("%s to read config from file: %s\n", ret ? "Success" : "Fail", file.getPath());
                        if (ret) {
                            return true;
                        }
                    }
                }
            }
        }

        // Read the packed source
        boolean ret = readFile(fileName, true);
        System.out.printf("%s to read config from source: %s\n", ret ? "Success" : "Fail", fileName);
        return ret;
    }

    /**
     * Read config, resource or not
     *
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
        fileObj = (JSONObject)JsonUtil.parseJson(str);
        fileObjMap.put(fileName.trim().toLowerCase(), fileObj);
        return true;
    }

    /**
     * Read the config by key
     * @param key
     */
    public String[] getStrArr(String key) {
        if (StrUtil.isEmpty(key) || fileObj == null) {
            return null;
        }

        List<String> strList = new ArrayList<String>();
        if (fileObj.containsKey(key)) {
            JSONArray arr = fileObj.getJSONArray(key);
            if (arr != null && arr.size() > 0) {
                for (int i = 0; i < arr.size(); i++) {
                    strList.add(arr.get(i).toString());
                }
            }
        }

        String[] strArr = new String[strList.size()];
        strList.toArray(strArr);
        return strArr;
    }

    public String getStr(String key) {
        if (StrUtil.isEmpty(key) || fileObj == null) {
            return null;
        }
        return fileObj.containsKey(key) ? fileObj.getString(key) : null;
    }

    public boolean getBoolean(String key) {
        if (StrUtil.isEmpty(key) || fileObj == null) {
            return false;
        }
        return fileObj.containsKey(key) ? fileObj.getBoolean(key) : false;
    }
}
