package com.bytter.biz.creditRating.util;

import java.util.regex.Pattern;

/**
 * 邮箱地址正则表达式校验工具类
 */

public class EmailUtils {
    // 定义邮箱地址的正则表达式
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // 编译正则表达式
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    public static boolean isEmail(String email) {
        return pattern.matcher(email).matches();
    }
}
