package com.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2017/9/23.
 */
public class StrUtil {
    /**
     * String is null or empty
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return isEmpty(str, true);
    }

    public static boolean isEmpty(String str, boolean trim) {
        return str == null || str.length() <= 0 || (trim && str.trim().length() <= 0);
    }

    /**
     * Match the part of version to the corresponding pattern
     * @param str
     * @param pattern
     * @return
     */
    public static boolean matches(String str, String pattern) {
        if (isEmpty(str) || isEmpty(pattern)) {
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean contains(String str, String subStr) {
        return contains(str, subStr, ",");
    }

    public static boolean contains(String str, String subStr, String separator) {
        if (subStr == null || subStr.length() <= 0) {
            return false;
        }

        if (subStr.equalsIgnoreCase(str)) {
            return true;
        }

        String[] strArray = split(str, separator);
        if (EmptyUtil.isEmpty(strArray)) {
            return false;
        }

        for (String tmpStr : strArray) {
            if (tmpStr.trim().length() == 0 && subStr.trim().length() == 0) {
                return true;
            }

            if (tmpStr.trim().equalsIgnoreCase(subStr.trim())) {
                return true;
            }
        }
        return false;
    }

    public static String[] split(String str, String separator) {
        if (StrUtil.isEmpty(str) || separator == null || separator.length() <= 0) {
            return null;
        }
        return str.split(separator);
    }

    public static String join(String[] strArray, String separator) {
        if (EmptyUtil.isEmpty(strArray) || separator == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(separator);
            sb.append(str);
        }
        return sb.substring(separator.length());
    }

    public static String eraseHtmlBrace(String str) {
        if (StrUtil.isEmpty(str)) {
            return "";
        }

        String b1 = "<", b2 = ">";
        while (str.indexOf(b1) >= 0 && str.indexOf(b2) > str.indexOf(b1)) {
            if (str.indexOf(b1) == 0) {
                str = str.substring(str.indexOf(b2) + b2.length());
            } else if (str.indexOf(b2) == str.length() - b2.length()) {
                str = str.substring(0, str.indexOf(b1));
            } else {
                str = String.format("%s%s",
                        str.substring(0, str.indexOf(b1)), str.substring(str.indexOf(b2) + b2.length())
                );
            }
        }
        return str;
    }

    public static String replace(String str, Map<String, String> replaceMap) {
        if (StrUtil.isEmpty(str) || replaceMap == null || replaceMap.size() <= 0) {
            return str;
        }

        // Replace
        for (Map.Entry<String, String> replace : replaceMap.entrySet()) {
            str = str.replace(replace.getKey(), replace.getValue());
        }
        return str;
    }

    public static boolean isInList(String str, String[] strArr) {
        if (str == null || strArr == null || strArr.length <= 0) {
            return false;
        }

        return isInList(str, Arrays.asList(strArr));
    }

    public static boolean isInList(String str, List<String> strList) {
        if (str == null || strList == null || strList.size() <= 0) {
            return false;
        }

        return strList.contains(str);
    }
}
