package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author zhengg
 * @version V1.0
 * @Title: Entity
 * @Description: 组织架构表
 * @date 2020-05-13 17:18:23
 */
@Entity
@Table(name = "sys_organtype", schema = "")
@SuppressWarnings("serial")
public class Organtype extends IdEntity implements java.io.Serializable {
    /**
     * 组织架构名称
     */
    private String orgtName;
    /**
     * 是否已初始化:0否 1是
     */
    private Integer isInit;
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
     * 状态，0禁用，1启用, 2删除
     */
    private String validSign;
    /**
     * 是否改变单位 0否 1是
     */
    private String isChangCorp;
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
     * 更新时间
     */
    private java.util.Date updateTime;
    /**
     * 更新人ID
     */
    private String updateBy;
    @Column(name = "orgt_Name", nullable = true, length = 100)
    public String getOrgtName() {
        return orgtName;
    }
    public void setOrgtName(String orgtName) {
        this.orgtName = orgtName;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  是否已初始化:0否 1是
     */
    @Column(name = "IS_INIT", nullable = true, precision = 1, scale = 0)
    public Integer getIsInit() {
        return this.isInit;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  是否已初始化:0否 1是
     */
    public void setIsInit(Integer isInit) {
        this.isInit = isInit;
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
     * @return: java.lang.String  状态，0禁用，1启用, 2删除
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 2)
    public String getValidSign() {
        return this.validSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  状态，0禁用，1启用, 2删除
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "IS_CHANG_CORP", nullable = true, length = 2)
    public String getIsChangCorp() {
        return isChangCorp;
    }
    public void setIsChangCorp(String isChangCorp) {
        this.isChangCorp = isChangCorp;
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
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新时间
     */
    @Column(name = "UPDATE_TIME", nullable = true, scale = 6)
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
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人ID
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
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
    @Override
    public String toString() {
        return "Organtype{" +
                "orgtName='" + orgtName + '\'' +
                ", isInit=" + isInit +
                ", orgtVersion='" + orgtVersion + '\'' +
                ", releaseDate=" + releaseDate +
                ", effectiveDate=" + effectiveDate +
                ", validSign='" + validSign + '\'' +
                ", isChangCorp='" + isChangCorp + '\'' +
                ", rmk='" + rmk + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}
