package com.bytter.poi.service;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ExcelCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public List<Object> putExcelDataCache(String key, List<Object> list) {
        redisTemplate.opsForValue().set( RedisCacheKey.makeRedisKey(RedisCacheKey.EXCEL_CACHE_PRE, key), list, 5, TimeUnit.MINUTES);
        return list;
    }
    public <T> T getExcelDataCache(String key, Class<T> type) {
        Object cacheData = redisTemplate.opsForValue().get( RedisCacheKey.makeRedisKey(RedisCacheKey.EXCEL_CACHE_PRE, key));
        return BeanUtils.copyNewInstanceProperties(cacheData, type);
    }
    public void clearExcelDataCache(String key) {
        redisTemplate.delete(RedisCacheKey.makeRedisKey(RedisCacheKey.EXCEL_CACHE_PRE, key));
    }
    public List<Object> getListExcelDataCache(String key) {
        key = RedisCacheKey.makeRedisKey(RedisCacheKey.EXCEL_CACHE_PRE, key);
        List<Object> objects = (List<Object>) redisTemplate.opsForValue().get(key);
        clearExcelDataCache(key);
        return objects;
    }
    public <T> List<T> getListExcelDataCache(String key,Class type) {
        key = RedisCacheKey.makeRedisKey(RedisCacheKey.EXCEL_CACHE_PRE, key);
        List<Object> objects = (List<Object>) redisTemplate.opsForValue().get(key);
        clearExcelDataCache(key);
        return BeanUtils.copyListProperties(objects, type);
    }

    public Object putExcelObjectCache(String key, Object list) {
        redisTemplate.opsForValue().set(RedisCacheKey.makeRedisKey(RedisCacheKey.EXCEL_CACHE_PRE, key), list, 5, TimeUnit.MINUTES);
        return list;
    }

}
