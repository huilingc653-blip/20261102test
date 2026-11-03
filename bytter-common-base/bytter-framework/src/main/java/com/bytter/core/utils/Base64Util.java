package com.bytter.core.utils;

import java.nio.charset.StandardCharsets;

/**
 * Base64加密工具类
 *
 * @author zhol
 * @since 1.0
 */
public class Base64Util extends Base64 {

    public static boolean isBase64(String s) {
        try {
            byte[] decode = java.util.Base64.getDecoder().decode(s);
            return true; // 如果解码成功，那么字符串就是有效的Base64
        } catch (IllegalArgumentException e) {
            return false; // 如果解码失败，那么字符串就不是有效的Base64
        }
    }

    public static String decodeToString(String base64Encoded) {
        return new String(Base64Util.decode(base64Encoded.getBytes()), StandardCharsets.UTF_8);
    }
}
