package com.bytter.core.utils;
import java.nio.charset.Charset;
/**
 * 加密相关工具类直接使用Spring util封装，减少jar依赖
 *
 * @author L.cm
 * @since 1.0
 */
public class DigestUtils extends org.springframework.util.DigestUtils {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String USER_TARGET = "bytter2017";
    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(final String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(UTF_8));
    }
    /**
     * Return a hexadecimal string representation of the MD5 digest of the given bytes.
     *
     * @param bytes
     *            the bytes to calculate the digest over
     * @return a hexadecimal digest string
     */
    public static String md5Hex(final byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }
    /**
     * 使用shiro的hash方式 todo shiro依赖准备移除 使用shiro算法加密方式移除
     * 
     * @param algorithmName
     *            算法
     * @param source
     *            源对象
     * @param salt
     *            加密盐
     * @param hashIterations
     *            hash次数
     * @return 加密后的字符
     */
    public static String hashByShiro(String algorithmName, Object source, Object salt, int hashIterations) {
        return null;
    }
    /**
     * 将用户id加上标签进行加密
     *
     * @param userId
     *            用户ID
     * @return 加密后字符串
     */
    public static String md5UserId(String userId) {
        String md5 = md5Hex(USER_TARGET);
        return md5Hex(userId + md5);
    }
}
