package com.bytter.system.vo;
import java.io.Serializable;
/**
 * @author zhengg
 * @version V1.0
 * @Title: VO
 * @Description: 组织架构对应关系维护表VO
 * @date 2020-05-14 16:01:41
 */
@SuppressWarnings("serial")
public class OrgantypeModuleVO implements Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 模块代码
     */
    private String sysCode;
    /**
     * 模块代码名称
     */
    private String sysCodeName;
    /**
     * 模块多语言代码
     */
    private String sysLangCode;
    /**
     * 组织机构ID
     */
    private String orgtId;
    /**
     * 组织机构名称
     */
    private String orgtName;
    /**
     * 更新人ID
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private java.util.Date updateTime;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  主键ID
     */
    public String getId() {
        return this.id;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  主键ID
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  模块代码
     */
    public String getSysCode() {
        return this.sysCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  模块代码
     */
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  模块多语言代码
     */
    public String getSysLangCode() {
        return this.sysLangCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  模块多语言代码
     */
    public void setSysLangCode(String sysLangCode) {
        this.sysLangCode = sysLangCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  组织机构ID
     */
    public String getOrgtId() {
        return this.orgtId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  组织机构ID
     */
    public void setOrgtId(String orgtId) {
        this.orgtId = orgtId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人ID
     */
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人ID
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新时间
     */
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getOrgtName() {
        return orgtName;
    }
    public void setOrgtName(String orgtName) {
        this.orgtName = orgtName;
    }
    public String getSysCodeName() {
        return sysCodeName;
    }
    public void setSysCodeName(String sysCodeName) {
        this.sysCodeName = sysCodeName;
    }
    @Override
    public String toString() {
        return "OrgantypeModuleVO{" +
                "id='" + id + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", sysCodeName='" + sysCodeName + '\'' +
                ", sysLangCode='" + sysLangCode + '\'' +
                ", orgtId='" + orgtId + '\'' +
                ", orgtName='" + orgtName + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
