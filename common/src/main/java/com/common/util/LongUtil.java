package com.common.util;

public class LongUtil {
    public static long valueOf(String v) {
        return valueOf(v, false);
    }

    public static long valueOf(String v, boolean showError) {
        if (StrUtil.isEmpty(v)) {
            return 0L;
        }

        try {
            return Long.valueOf(v);
        } catch (Exception e) {
            if (showError) {
                e.printStackTrace();
            }
        }

        return 0L;
    }
}
