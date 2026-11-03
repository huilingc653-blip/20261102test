package com.bytter.system.vo;
import java.io.Serializable;
/**
 * @author zhengg
 * @version V1.0
 * @Title: VO
 * @Description: 组织架构发版表VO
 * @date 2020-05-14 16:01:42
 */
@SuppressWarnings("serial")
public class OrgantypeReleaseVO implements Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 组织架构id
     */
    private String orgtId;
    /**
     * 组织架构名称
     */
    private String orgtName;
    /**
     * 版本号
     */
    private String orgtVersion;
    /**
     * 发布日期
     */
    private java.util.Date releaseDate;
    /**
     * 生效日期
     */
    private java.util.Date effectiveDate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建时间
     */
    private java.util.Date createTime;
    /**
     * 创建人ID
     */
    private String createBy;
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
     * @return: java.lang.String  组织架构id
     */
    public String getOrgtId() {
        return this.orgtId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  组织架构id
     */
    public void setOrgtId(String orgtId) {
        this.orgtId = orgtId;
    }
    public String getOrgtName() {
        return orgtName;
    }
    public void setOrgtName(String orgtName) {
        this.orgtName = orgtName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  版本号
     */
    public String getOrgtVersion() {
        return this.orgtVersion;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  版本号
     */
    public void setOrgtVersion(String orgtVersion) {
        this.orgtVersion = orgtVersion;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  发布日期
     */
    public java.util.Date getReleaseDate() {
        return this.releaseDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  发布日期
     */
    public void setReleaseDate(java.util.Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  生效日期
     */
    public java.util.Date getEffectiveDate() {
        return this.effectiveDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  生效日期
     */
    public void setEffectiveDate(java.util.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  备注
     */
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建时间
     */
    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人ID
     */
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人ID
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Override
    public String toString() {
        return "OrgantypeReleaseVO{" +
                "id='" + id + '\'' +
                ", orgtId='" + orgtId + '\'' +
                ", orgtName='" + orgtName + '\'' +
                ", orgtVersion='" + orgtVersion + '\'' +
                ", releaseDate=" + releaseDate +
                ", effectiveDate=" + effectiveDate +
                ", rmk='" + rmk + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                '}';
    }
}
