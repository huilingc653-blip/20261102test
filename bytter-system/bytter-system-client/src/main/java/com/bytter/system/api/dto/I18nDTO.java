package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.Locale;
/**
 * 国际化数据传输对象
 *
 * @author zhoul
 * @since 2019-11-12
 */
public class I18nDTO implements Serializable {
    public I18nDTO() {
    }
    public I18nDTO(String keyId, String locale, String context) {
        this.keyId = keyId;
        this.locale = locale;
        this.context = context;
    }
    /**
     * 业务主键ID
     */
    private String keyId;
    /**
     * 国际化语言
     */
    private String locale;
    /**
     * 国际化内容
     */
    private String context;
    public String getKeyId() {
        return keyId;
    }
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
    public String getLocale() {
        return locale;
    }
    /**
     * 统一使用{@link Locale#SIMPLIFIED_CHINESE}系列方法，该方法默认返回"zh_CN"
     *
     * @param locale 国际化语言值
     */
    public void setLocale(Locale locale) {
        this.locale = locale.toString();
    }
    /**
     * 请参考{@link Locale}中的国际化语言
     *
     * @param locale 国际化语言值
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }
}
