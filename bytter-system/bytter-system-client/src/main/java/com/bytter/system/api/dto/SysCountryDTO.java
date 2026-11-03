package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * @author zuoy
 * @since 2020-01-16
 */
public class SysCountryDTO implements Serializable {
    /**
     * code
     */
    private String id;
    /**
     * code
     */
    private String code;
    /**
     * 中文名称
     */
    private String nameZh;
    /**
     * 英文名称
     */
    private String nameEn;
    /**
     * 系统初始化标志
     */
    private String isSystem;
    /**
     * 国家所属大洲
     */
    private String continent;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getNameZh() {
        return nameZh;
    }
    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public String getIsSystem() {
        return isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
}
