package com.bytter.system.service.impl;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.bytter.system.dao.I18nDao.BASE_DATA;
import static com.bytter.system.dao.I18nDao.BUSINESS_DATA;

@DubboService
@Slf4j
public class I18nServiceImpl implements Ii18nService {
	@Autowired
    private I18nService i18nService;
    
    private String getLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            // log.warn("can not found current request,return default locale [zh_CN]");
            return Locale.SIMPLIFIED_CHINESE.toString();
        }
        Locale locale = LocaleContextHolder.getLocale();
        if (!StringUtils.hasText(locale.getCountry()) && Locale.SIMPLIFIED_CHINESE.getLanguage().equals(locale.getLanguage())) {
            return Locale.SIMPLIFIED_CHINESE.toString();
        }
        return locale.toString();
    }
    
    @Override
    public String getBaseContext(String keyId) {
        return i18nService.getBaseContext(getLocale(), keyId);
    }
    
    @Override
    public String getBaseContext(String locale, String keyId) {
        return i18nService.getBaseContext(locale, keyId);
    }
    
    @Override
    public String getBusinessBaseContext(String keyId) {
        return i18nService.getBusinessBaseContext(getLocale(), keyId);
    }
    
    @Override
    public String getBusinessBaseContext(String locale, String keyId) {
        return i18nService.getBusinessBaseContext(locale, keyId);
    }
    
    @Override
    public List<I18nDTO> listBaseByKeyId(String keyId) {
        return i18nService.listI18nByKeyId(BASE_DATA, keyId);
    }
    
    @Override
    public List<I18nDTO> listBusinessBaseByKeyId(String keyId) {
        return i18nService.listI18nByKeyId(BUSINESS_DATA, keyId);
    }
    
    @Override
    public void saveOrUpdateBusinessBaseContext(I18nDTO i18n) {
        if (i18n != null) {
            List<I18nDTO> i18nList = new ArrayList<>();
            i18nList.add(i18n);
            saveOrUpdateBusinessBaseContext(i18nList);
        }
    }
    
    @Override
    public void saveOrUpdateBusinessBaseContext(List<I18nDTO> i18nList) {
        i18nService.saveOrUpdate(BUSINESS_DATA, i18nList);
    }
    
    @Override
    public void deleteBusinessBaseContext(String locale, String keyId) {
        i18nService.delete(BUSINESS_DATA, locale, keyId);
    }
    
    @Override
    public void deleteBusinessBaseContext(String keyId) {
        i18nService.delete(BUSINESS_DATA, null, keyId);
    }
    
    @Override
    public Map<String, String> getBaseContextBatch(List<String> keyIds) {
        return i18nService.getBaseContextBatch(keyIds);
    }
    
    @Override
    public Map<String, String> getBaseContextBatch(String locale, List<String> keyIds) {
        return i18nService.getBaseContextBatch(locale, keyIds);
    }
}