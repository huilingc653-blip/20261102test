package com.bytter.system.service;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.dao.I18nDao;
import com.bytter.system.entity.DictGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

/**
 * 基础数据国际化、业务基础数据国际化
 *
 * @author zhoul
 * @since 2019-11-11
 */
@Service("i18nService")
public class I18nService {
    @Autowired
    private I18nDao i18nDao;



    public String getBaseContext(String locale, String keyId) {
        return i18nDao.getContext(I18nDao.BASE_DATA, locale, keyId);
    }

    public String getBusinessBaseContext(String locale, String keyId) {
        if (StringUtils.isEmpty(keyId)) {
            return "";
        }
        return i18nDao.getContext(I18nDao.BUSINESS_DATA, locale, keyId);
    }

    public List<I18nDTO> listI18nBase(String locale) {
        return i18nDao.listI18n(I18nDao.BASE_DATA, locale);
    }

    public List<I18nDTO> listI18n(String locale) {
        return i18nDao.listI18n(I18nDao.BUSINESS_DATA, locale);
    }

    public List<DictGroup> getALLBusinessBaseContext(String locale) {
        return i18nDao.getAllContext(I18nDao.BUSINESS_DATA, locale);
    }

    public List<I18nDTO> listI18nByKeyId(String type, String keyId) {
        return i18nDao.listI18nByKeyId(type, keyId);
    }

    @Transactional
    public void saveOrUpdate(String type, List<I18nDTO> i18nList) {
        Map<String, Integer> cache = new HashMap<>();
        Set<I18nDTO> i18nSet = new HashSet<>(i18nList);
        for (I18nDTO i18nDTO : i18nSet) {
            String keyId = i18nDTO.getKeyId();
            String locale = i18nDTO.getLocale();
            String context = i18nDTO.getContext();
            if (StringUtils.isNotBlank(locale)) {
                String cacheKey = locale + "-" + keyId;
                Integer integer = cache.get(cacheKey);
                if (integer == null) {
                    integer = 0;
                }
                if (integer > 1) {
                    continue;
                }
                integer++;
                cache.put(cacheKey, integer);
                i18nDao.saveOrUpdate(type, locale, keyId, context);
            }
        }
    }

    @Transactional
    public void delete(String type, String locale, String keyId) {
        if (StringUtils.isBlank(locale)) {
            String[] arr = {"zh_CN", "en_US"};
            for (String lang : arr) {
                i18nDao.delete(type, lang, keyId);
            }
        } else {
            i18nDao.delete(type, locale, keyId);
        }
    }

    /**
     * 批量获取基础数据国际化
     *
     * @param locale 语言
     * @param keyIds 基础数据ID列表
     * @return keyId到国际化内容的映射
     */
    public Map<String, String> getBaseContextBatch(String locale, List<String> keyIds) {
        if (keyIds == null || keyIds.isEmpty()) {
            return new HashMap<>();
        }
        return i18nDao.getContextBatch(I18nDao.BASE_DATA, locale, keyIds);
    }

    /**
     * 批量获取基础数据国际化
     *
     * @param keyIds 基础数据ID列表
     * @return keyId到国际化内容的映射
     */
    public Map<String, String> getBaseContextBatch(List<String> keyIds) {
        if (keyIds == null || keyIds.isEmpty()) {
            return new HashMap<>();
        }
        String locale = getLocale();
        return getBaseContextBatch(locale, keyIds);
    }

    public String getLocale() {
        return org.springframework.context.i18n.LocaleContextHolder.getLocale().toString();
    }
}
