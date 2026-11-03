package com.bytter.biz.erp.service.impl;

import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;

import javax.jws.WebService;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lifh
 * @description
 * @createDate 2019/8/6
 */
@WebService(serviceName = "WsTokenCacheService")
public class WsTokenCacheServiceImpl implements com.bytter.erp.service.WsTokenCacheService {
    private static final String TOKEN_CACHE_NAME = "wsTokenCache";

    @Autowired(required=false)
    private CacheManager cacheManager;

    /**
     * key为token，value为存入token时的时间戳
     */
    private static ConcurrentHashMap<String, Long> tokenCache = new ConcurrentHashMap<>(100);


    @CachePut(value = TOKEN_CACHE_NAME, key = "#token", condition = "#token != null")
    @Override
    public String writeToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        return token;
    }

    @Override
    public String readToken(String token) {
        //这里会刷新缓存的最后一次访问时间
        return cacheManager.getCache(TOKEN_CACHE_NAME).get(token, String.class);
    }

    /**
     * 清除过期的缓存
     */
//    private static void clearValidToken() {
//        long currentTime = System.currentTimeMillis();
//        Iterator<Map.Entry<String, Long>> it = tokenCache.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Long> entry = it.next();
//            if (entry.getValue() - currentTime > TOKEN_DEAD_TIME) {
//                it.remove();//使用迭代器的remove()方法删除元素
//            }
//        }
//    }

}
