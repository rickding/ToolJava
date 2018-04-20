package com.tool.pack;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.Config;
import com.common.util.StrUtil;

import java.util.HashSet;
import java.util.Set;

public class PackConfig extends Config {
    private String fileExt;
    private Set<String> ignoredFileNamePatternSet;
    private Set<String> excludeFileNamePatternSet;

    public PackConfig() {
        ignoredFileNamePatternSet = new HashSet<String>();
        excludeFileNamePatternSet = new HashSet<String>();
    }

    private void update(String key, Set<String> values) {
        if (StrUtil.isEmpty(key) || values == null) {
            return;
        }

        JSONObject obj = lastFileObj;
        if (obj.containsKey(key)) {
            JSONArray arr = obj.getJSONArray(key);
            if (arr != null && arr.size() > 0) {
                for (int i = 0; i < arr.size(); i++) {
                    values.add(arr.get(i).toString());
                }
            }
        }
    }

    @Override
    public boolean read(String fileName, boolean isResource) {
        boolean ret = super.read(fileName, isResource);
        if (!ret) {
            return ret;
        }

        // Parse the json object
        JSONObject obj = lastFileObj;
        String key = "fileExt";
        if (obj.containsKey(key)) {
            fileExt = obj.getString(key);
        }

        update("ignoredFileNamePatterns", ignoredFileNamePatternSet);
        update("excludeFileNamePatterns", excludeFileNamePatternSet);
        return ret;
    }
}
