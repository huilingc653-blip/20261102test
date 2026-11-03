package com.bytter.system.dao;
import com.bytter.core.utils.ExceptionStackUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.*;
/**
 * Created by zhoul on 2017/12/13.
 */
@Repository
public class LicenseDao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String LICENSE_CACHE_NAME = "licenseCache";
    private Map<String, Object> licenseCache = new HashMap<>();
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    public Object getLicense(String name) {
        return getLicenseCache().get(name);
    }
    public void initLicense(String licenseJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode licenseNode = objectMapper.readTree(licenseJson);
            Iterator<String> stringIterator = licenseNode.fieldNames();
            while (stringIterator.hasNext()) {
                String key = stringIterator.next();
                JsonNode jsonNodeValue = licenseNode.get(key);
                System.out.println("");
                if (jsonNodeValue.isTextual()) {
                    licenseCache.put(key, jsonNodeValue.asText(""));
                } else if (jsonNodeValue.isInt()) {
                    licenseCache.put(key, jsonNodeValue.asInt(0));
                } else if (jsonNodeValue.isLong()) {
                    licenseCache.put(key, jsonNodeValue.asLong(0));
                } else if (jsonNodeValue.isArray()) {
                    List<String> menus = new ArrayList<>();
                    for (JsonNode objNode : jsonNodeValue) {
                        menus.add(objNode.asText());
                    }
                    licenseCache.put(key, menus);
                }
            }
           redisTemplate.opsForValue().set(LICENSE_CACHE_NAME,licenseCache); 
        } catch (IOException e) {
            logger.error("license加载错误{}", licenseJson, e);
            ExceptionStackUtils.getStack(e);
        }
    }
    public void removeLicenseByName(String name) {
    }
    
    public Map<String, Object> getLicenseCache() {
    	Map<String, Object> map = (Map<String, Object>) redisTemplate.opsForValue().get(LICENSE_CACHE_NAME);
        return map;
    }
}
