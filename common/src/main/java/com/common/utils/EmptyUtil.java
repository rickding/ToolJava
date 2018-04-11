package com.common.utils;

import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2017/9/23.
 */
public class EmptyUtil {
    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length <= 0;
    }

    public static <T> boolean isEmpty(Set<T> set) {
        return set == null || set.size() <= 0;
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.size() <= 0;
    }
}
