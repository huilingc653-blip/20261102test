package com.bytter.system.vo;
/**
 * Created by zhoul on 2017/5/11.
 */
public class MultiLangVO {
    private String id;
    /**
     * 语言主键
     */
    private String langKey;
    /**
     * 内容
     */
    private String langContext;
    /**
     * 语言
     */
    private String langCode;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLangKey() {
        return langKey;
    }
    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }
    public String getLangContext() {
        return langContext;
    }
    public void setLangContext(String langContext) {
        this.langContext = langContext;
    }
    public String getLangCode() {
        return langCode;
    }
    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }
}
