package com.bytter.biz.foreign.utils;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.json.JSONUtil;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class SmUtils {

    /**
     * 私钥
     */
    public static final String PRIVATE_KEY = "00a903f84f8c48f1eb7110852ca2527d47b4e930a9db74b5162fb8415ed7ffb366";


    /**
     * 公钥
     */
    public static final String PUBLIC_KEY = "048459d70b5ede121e2effc2327885c9de43f2c2297ea26b1ad9fc76062d415cac60fcc134e8123b27e5a29680080072ba56bd8ae420e2cc604524cb21d4abaaa2";


    /**
     * 私钥签名
     * @param params
     * @return
     */
    public static String signByPriKey(Map<String, String> params) {
        TreeMap<String, String> sort = MapUtil.sort(params);
        String jsonStr = JSONUtil.toJsonStr(sort);
        ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(PRIVATE_KEY);
        //创建sm2 对象
        SM2 sm2 = new SM2(privateKeyParameters, null);
        //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C2C3);
        byte[] sign = sm2.sign(jsonStr.getBytes(StandardCharsets.UTF_8));
        return HexUtil.encodeHexStr(sign);

    }

    /**
     * 私钥签名
     * @param content
     * @return
     */
    public static String signByPriKey(String content) {
        ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(PRIVATE_KEY);
        //创建sm2 对象
        SM2 sm2 = new SM2(privateKeyParameters, null);
        //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C2C3);
        byte[] sign = sm2.sign(content.getBytes(StandardCharsets.UTF_8));
        return HexUtil.encodeHexStr(sign);
    }

    /**
     * 公钥验签
     * @param params
     * @Param signStr
     * @return
     */
    public static boolean checkSignByPubKey(Map<String, String> params, String signStr) {
        TreeMap<String, String> sort = MapUtil.sort(params);
        String jsonStr = JSONUtil.toJsonStr(sort);

        String publicKey = PUBLIC_KEY;
        //这里需要根据公钥的长度进行加工
        if (publicKey.length() == 130) {
            //这里需要去掉开始第一个字节 第一个字节表示标记
            publicKey = publicKey.substring(2);
        }
        String xhex = publicKey.substring(0, 64);
        String yhex = publicKey.substring(64, 128);
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        //创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C2C3);
        return sm2.verify(jsonStr.getBytes(StandardCharsets.UTF_8), HexUtil.decodeHex(signStr));
    }

    /**
     * 公钥验签
     * @param content
     * @param signStr
     * @return
     */
    public static boolean checkSignByPubKey(String content, String signStr) {
        String publicKey = PUBLIC_KEY;
        //这里需要根据公钥的长度进行加工
        if (publicKey.length() == 130) {
            //这里需要去掉开始第一个字节 第一个字节表示标记
            publicKey = publicKey.substring(2);
        }
        String xhex = publicKey.substring(0, 64);
        String yhex = publicKey.substring(64, 128);
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        //创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C2C3);
        return sm2.verify(content.getBytes(StandardCharsets.UTF_8), HexUtil.decodeHex(signStr));
    }

}
