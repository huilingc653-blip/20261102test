package com.bytter.system.api;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface IMultiLangService {
    String getLang(String langKey);
    String getLang(String langKey, String... args);
    String getLang(Locale locale, String langKey, String... args);
    /**
     * vm国际化需要暴露次接口
     *
     * @param langKey
     * @param locale
     * @return
     */
    String getText(String langKey, Locale locale);

    Map<String, String> getLangList(Locale locale, List<String> langKeyList);
}
