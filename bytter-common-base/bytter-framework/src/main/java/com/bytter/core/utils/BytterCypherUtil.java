package com.bytter.core.utils;
import java.security.MessageDigest;
/**
 * 拜特加解密工具类
 *
 * @author zuoy
 * @since 1.0
 */
public class BytterCypherUtil {
    /**
     * 拜特加密方式 根据加密次数进行循环加密，奇数循环次数为sha1，偶数循环次数为md5 加密次数：取得加密字符串的hash值的前两位，再根据hash前两位除2取绝对值 + 1
     *
     * @param encodeToString
     *            加密字符串
     * @return 加密后的结果
     */
    public static String encodeToString(String encodeToString) {
        // 获取加密次数
        int count = getEncodeCount(encodeToString);
        for (int i = 0; i <= count; i++) {
            if ((i & 1) == 1) {
                // 循环次数奇数 sha1加密
                encodeToString = sha1EncodeToString(encodeToString);
            } else {
                // 循环次数偶数 md5加密
                encodeToString = md5EncodeToString(encodeToString);
            }
        }
        return encodeToString;
    }
    /**
     * md5加密
     *
     * @param encodeToString
     *            需加密字符串
     * @return 加密后字符串
     */
    private static String md5EncodeToString(String encodeToString) {
        try {
            return md5Encode(encodeToString);
        } catch (Exception e) {
            return encodeToString;
        }
    }
    /**
     * sha1加密
     *
     * @param encodeToString
     *            需加密字符串
     * @return 加密后字符串
     */
    private static String sha1EncodeToString(String encodeToString) {
        try {
            return sha1Encode(encodeToString);
        } catch (Exception e) {
            return encodeToString;
        }
    }
    /**
     * 获取加密次数 取得加密字符串的hash值的前两位 再根据hash前两位除2取绝对值+1，
     *
     * @param encodeToString
     *            需加密字符串
     * @return 加密次数
     */
    private static Integer getEncodeCount(String encodeToString) {
        // 获取hash前两位
        String hash = String.valueOf(encodeToString.hashCode());
        String tempHash = hash.length() == 1 ? hash.substring(1) : hash.substring(0, 2);
        // 返回加密次数
        return Math.abs(Integer.parseInt(tempHash) / 2) + 1;
    }
    /***
     * MD5加密 生成32位md5码
     * 
     * @param inStr
     *            待加密字符串
     * @return 返回32位md5码
     */
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    /***
     * SHA加密 生成40位SHA码
     * 
     * @param inStr
     *            待加密字符串
     * @return 返回40位SHA码
     */
    public static String sha1Encode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    public static void main(String[] args) {
        String str = "哈哈";
        System.out.println("结果：" + encodeToString(str));
    }
}
