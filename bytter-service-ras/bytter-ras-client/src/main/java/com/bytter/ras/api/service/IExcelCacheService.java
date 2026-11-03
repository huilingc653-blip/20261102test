package com.bytter.ras.api.service;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 */
public interface IExcelCacheService {//TODO 实现，看业务情况实现，是从缓存里拿数据
	
    List<Object> putExcelDataCache(String key, List<Object> list);

    <T> T getExcelDataCache(String key, Class<T> type);

    void clearExcelDataCache(String key);

    <T> List<T> getListExcelDataCache(String key, Class<T> type);

    Object putExcelObjectCache(String key, Object list);
    
    
    /**
    package com.bytter.biz.excel.service.impl;

    import com.bytter.core.utils.BeanUtils;
    import com.bytter.core.utils.EmptyUtils;
    import com.bytter.facade.excel.service.IExcelCacheService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.cache.CacheManager;
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.cache.annotation.CachePut;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;

    @Service("excelCacheServiceImpl")
    public class ExcelCacheServiceImpl implements IExcelCacheService {
        public static final String OTHER_CACHE_NAME = "oneDay";
        @Autowired
        CacheManager cacheManager;

        @Override
        @CachePut(value = OTHER_CACHE_NAME, key = "#key")
        public List<Object> putExcelDataCache(String key, List<Object> list) {
            if (EmptyUtils.isEmpty(list)) {
                return new ArrayList<Object>();
            }
            return list;
        }

        @Override
        public <T> T getExcelDataCache(String key, Class<T> type) {
            return cacheManager.getCache(OTHER_CACHE_NAME).get(key, type);
        }

        @Override
        @CacheEvict(value = OTHER_CACHE_NAME, key = "#key")
        public void clearExcelDataCache(String key) {

        }

        @Override
        public <T> List<T> getListExcelDataCache(String key, Class<T> type) {
            List<Object> cacheList = (List<Object>) getExcelDataCache(key, List.class);
            // 只获取一次，清除缓存
            clearExcelDataCache(key);
            return BeanUtils.copyListProperties(cacheList, type);
        }

        @Override
        @CachePut(value = OTHER_CACHE_NAME, key = "#key")
        public Object putExcelObjectCache(String key, Object obj) {
            return obj;
        }
    }

    */
}
