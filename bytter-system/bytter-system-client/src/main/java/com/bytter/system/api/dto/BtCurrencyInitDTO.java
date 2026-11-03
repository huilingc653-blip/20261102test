package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * @author Jw
 * @date 2023-5-9
 */
public class BtCurrencyInitDTO implements Serializable {
    private String id;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 英文简码
     */
    private String englishCode;
    /**
     * 方法: 取得String
     *
     * @return: String  币别名称
     */
    public String getCurName() {
        return this.curName;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  币别名称
     */
    public void setCurName(String curName) {
        this.curName = curName;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  英文简码
     */
    public String getEnglishCode() {
        return this.englishCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  英文简码
     */
    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
