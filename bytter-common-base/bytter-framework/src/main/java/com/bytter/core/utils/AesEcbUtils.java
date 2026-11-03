package com.bytter.core.utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
/**
 * Java标准库提供的对称加密接口非常简单，使用时按以下步骤编写代码：
 * <p>
 * 根据算法名称/工作模式/填充模式获取Cipher实例； 根据算法名称初始化一个SecretKey实例，密钥必须是指定长度； 使用SerectKey初始化Cipher实例，并设置加密或解密模式； 传入明文或密文，获得密文或明文。
 *
 * @author chenlw
 * @date 2020/03/13
 */
public class AesEcbUtils {
    /**
     * 密钥长度为16，即128位
     */
    // private static final String KEY = "1234567890abcdef";
    private static final String KEY = "1234567890abcdef1234567890abcdef";
    public static void main(String[] args) throws Exception {
        // 原文:
        String message = "Hello, world!";
        System.out.println("Message: " + message);
        String charsetName = StandardCharsets.UTF_8.name();
        // 128位密钥 = 16 bytes Key:
        byte[] key = KEY.getBytes(charsetName);
        // 加密:
        byte[] data = message.getBytes(charsetName);
        byte[] encrypted = encrypt(key, data);
        System.out.println("Encrypted: " + Base64.byteArrayToBase64(encrypted));
        // 解密:
        byte[] decrypted = decrypt(key, encrypted);
        System.out.println("Decrypted: " + new String(decrypted, charsetName));
    }
    /**
     * 加密
     *
     * @param key
     * @param input
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] encrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }
    /**
     * 解密:
     *
     * @param key
     * @param input
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] decrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }
}
