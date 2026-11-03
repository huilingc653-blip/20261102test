package com.bytter.system.service.impl;

import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.entity.MultiLang;
import com.bytter.system.service.MultiLangService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@DubboService(group = "multiLangServiceI", version = "1.0.0")
public class MultiLangServiceImpl implements IMultiLangService {
    @Autowired
    private MultiLangService multiLangService;

    public String getLang(String langKey) {
        return multiLangService.getLang(langKey);
    }

    public String getLang(String langKey, String... langArg) {
        return multiLangService.getLang(langKey, langArg);
    }

    @Override
    public String getLang(Locale locale, String langKey, String... args) {
        return multiLangService.getLang(locale, langKey, args);
    }

    @Override
    public String getText(String langKey, Locale locale) {
        return multiLangService.getText(langKey, locale);
    }

    @Override
    public Map<String, String> getLangList(Locale locale, List<String> langKeyList) {
        if (StringUtils.isBlank(locale.toString()) || EmptyUtils.isEmptyList(langKeyList)) {
            return new HashMap<>();
        }
        List<MultiLang> langList = multiLangService.getLangList(locale, langKeyList);
        if (EmptyUtils.isEmptyList(langList)) {
            return new HashMap<>();
        }
        return langList.stream().collect(Collectors.toMap(MultiLang::getLangKey, MultiLang::getLangContext));
    }
}
