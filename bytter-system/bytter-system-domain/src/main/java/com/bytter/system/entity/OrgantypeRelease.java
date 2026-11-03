package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author zhengg
 * @version V1.0
 * @Title: Entity
 * @Description: 组织架构发版表
 * @date 2020-05-13 17:18:26
 */
@Entity
@Table(name = "sys_organtype_release", schema = "")
@SuppressWarnings("serial")
public class OrgantypeRelease extends IdEntity implements java.io.Serializable {
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
     * @return: java.lang.String  组织架构id
     */
    @Column(name = "ORGT_ID", nullable = true, length = 32)
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
    @Column(name = "orgt_Name", nullable = true, length = 100)
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
    @Column(name = "ORGT_VERSION", nullable = true, length = 10)
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
    @Column(name = "RELEASE_DATE", nullable = true)
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
    @Column(name = "EFFECTIVE_DATE", nullable = true)
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
    @Column(name = "RMK", nullable = true, length = 200)
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
    @Column(name = "CREATE_TIME", nullable = true, scale = 6)
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
    @Column(name = "CREATE_BY", nullable = true, length = 32)
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
        return "OrgantypeRelease [" +
                "id=" +
                getId() + ", " +
                "orgtId=" +
                orgtId + ", " +
                "orgtName=" +
                orgtName + ", " +
                "orgtVersion=" +
                orgtVersion + ", " +
                "releaseDate=" +
                releaseDate + ", " +
                "effectiveDate=" +
                effectiveDate + ", " +
                "rmk=" +
                rmk + ", " +
                "createTime=" +
                createTime + ", " +
                "createBy=" +
                createBy + ", " +
                "super.toString()=" + super.toString() + "]";
    }
}
