package com.bytter.poi.excel.entity.params;
/**
 * 导入校验规则
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelVerifyEntity {
    /**
     *
     */
    private boolean interHandler;
    /**
     * 不允许为空
     */
    private boolean notNull;
    /**
     * 不是手机号
     */
    private boolean isMobile;
    /**
     * 不是电话号码
     */
    private boolean isTel;
    /**
     * 不是邮箱地址
     */
    private boolean isEmail;
    /**
     * 小于规定长度
     */
    private int minLength;
    /**
     * 超过规定长度
     */
    private int maxLength;
    /**
     * 数据规范
     */
    private String regex;
    /**
     * 数据不符合规范描述
     */
    private String regexTip;
    public ExcelVerifyEntity() {
    }
    public int getMaxLength() {
        return this.maxLength;
    }
    public int getMinLength() {
        return this.minLength;
    }
    public String getRegex() {
        return this.regex;
    }
    public String getRegexTip() {
        return this.regexTip;
    }
    public boolean isEmail() {
        return this.isEmail;
    }
    public boolean isInterHandler() {
        return this.interHandler;
    }
    public boolean isMobile() {
        return this.isMobile;
    }
    public boolean isNotNull() {
        return this.notNull;
    }
    public boolean isTel() {
        return this.isTel;
    }
    public void setEmail(boolean isEmail) {
        this.isEmail = isEmail;
    }
    public void setInterHandler(boolean interHandler) {
        this.interHandler = interHandler;
    }
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
    public void setMobile(boolean isMobile) {
        this.isMobile = isMobile;
    }
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }
    public void setRegexTip(String regexTip) {
        this.regexTip = regexTip;
    }
    public void setTel(boolean isTel) {
        this.isTel = isTel;
    }
}
