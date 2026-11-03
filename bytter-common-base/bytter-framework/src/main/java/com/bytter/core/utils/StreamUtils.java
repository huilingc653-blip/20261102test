package com.bytter.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public final class StreamUtils {

    public static <T> Predicate<T> distinctKey(Function<? super T, Object> keyMapper) {
        Map<Object, Boolean> distMap = new HashMap<>();
        return key -> distMap.putIfAbsent(keyMapper.apply(key), Boolean.TRUE) == null;
    }

}
