package com.bytter.system.api.config;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.utils.RedisUtils;
import com.bytter.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * SM2加解密相关服务提供
 */
@Configuration
@Slf4j
public class SM2ManagerService {


    @Autowired
    private RedisUtils redisUtils;

    /**
     * 生成SM2密钥
     * @return 公钥
     */
    public String genSM2PukKey() {
        SM2 sm2 = new SM2();
        String publicKey = sm2.getPublicKeyBase64();//公钥
        String privateKey = sm2.getPrivateKeyBase64();//私钥
        //将公钥作为KEY，私钥作为value 5分钟过期
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.SM2_KEY, publicKey);
        redisUtils.put(key, privateKey, 300);
        return publicKey;
    }

    /**
     * 根据公钥解密密码
     * @return 原始密码
     *
     */
    public String decodeSm2ByKey(String enPwd,String pubKey) {
        if (StringUtils.isBlank(pubKey)) {
            log.warn("公钥为空，请检查参数");
            return "";
        }
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.SM2_KEY, pubKey);
        String privateKey = (String) redisUtils.get(key);
        if (StringUtils.isBlank(privateKey)) {
            log.warn("私钥获取失败，公钥可能已经过期");
            return "";
        }
        SM2 sm2 = new SM2(privateKey, null);
        return sm2.decryptStr(enPwd, KeyType.PrivateKey);
    }
}
