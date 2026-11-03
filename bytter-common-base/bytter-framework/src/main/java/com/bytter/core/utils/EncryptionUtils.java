package com.bytter.core.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Base64;
/**
 * @author luxy
 * @Title: EncryptionUtils
 * @ProjectName T3.4
 * @Description: 加密解密工具类
 * @date 2020/8/28 17:35
 */
public class EncryptionUtils {
    public static String encodeUrl(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
        } catch (Exception e) {
        }
        return value;
    }
    public static String getCommonNoPermissionPageUrl(String msg) {
        StringBuilder url = new StringBuilder();
        url.append("http://192.168.1.7:8080/t2/appH5/#/pages/commonPages/commonNoPermissionPage");
        url.append("?msg=").append(encodeUrl(msg));
        return url.toString();
    }
    /**
     * 生成随机数
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    /**
     * AES-CBC模式加密数据
     *
     * @param data
     *            要加密的数据
     * @param key
     *            密钥，长度为16
     * @param ivString
     *            加密用的IV干扰串，长度为16
     * @param charsetName
     *            编码格式
     * @return 加密后的字符串（base64编码）
     * @throws Exception
     *             异常
     */
    public static String aesCbcEncrypt(String data, String key, String ivString, String charsetName) throws Exception {
        byte[] raw = key.getBytes(charsetName);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivString.getBytes(charsetName));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes(charsetName));
        // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new String(Base64.getEncoder().encode(encrypted), charsetName);
    }
    /**
     * AES-CBC解密数据
     *
     * @param data
     *            要解密的数据
     * @param key
     *            密钥，长度为16
     * @param ivString
     *            加密用的IV干扰串，长度为16
     * @param charsetName
     *            编码格式
     * @return 解密后的数据，若解密失败则返回null
     */
    public static String aesCbcDecrypt(String data, String key, String ivString, String charsetName) {
        try {
            byte[] raw = key.getBytes(charsetName);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivString.getBytes(charsetName));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(data);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, charsetName);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
