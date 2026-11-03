package com.bytter.biz.finacial.paramconfig.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_BUSINESS_RULE_DEFINE
 * @date 2017-05-22 19:23:13
 */
@Entity
@Table(name = "FC_BUSINESS_RULE_DEFINE", schema = "")
@SuppressWarnings("serial")
public class FcBusinessRuleDefine extends IdEntity implements java.io.Serializable {
    /**
     * 所属网点
     */
    private java.lang.String netId;
    /**
     * 业务种类编号
     */
    private java.lang.String sysRaceCode;
    /**
     * 指定科目
     */
    private java.lang.String subjCode;
    /**
     * 币别代码
     */
    private java.lang.String curCode;
    /**
     * 有效标志
     */
    private java.lang.String validSign;
    /**
     * createTime
     */
    private java.util.Date createTime;
    /**
     * createBy
     */
    private java.lang.String createBy;
    /**
     * updateTime
     */
    private java.util.Date updateTime;
    /**
     * updateBy
     */
    private java.lang.String updateBy;
    /**
     * 凭证种类
     */
    private java.lang.String voucherType;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否初始化：1-是，0-否
     */
    private String isInit;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  所属网点
     */
    @Column(name = "NET_ID")
    public java.lang.String getNetId() {
        return this.netId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  所属网点
     */
    public void setNetId(java.lang.String netId) {
        this.netId = netId;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  系统业务品种
     */
    @Column(name = "SYS_RACE_CODE")
    public java.lang.String getSysRaceCode() {
        return this.sysRaceCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  系统业务品种
     */
    public void setSysRaceCode(java.lang.String sysRaceCode) {
        this.sysRaceCode = sysRaceCode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  指定科目
     */
    @Column(name = "SUBJ_CODE")
    public java.lang.String getSubjCode() {
        return this.subjCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  指定科目
     */
    public void setSubjCode(java.lang.String subjCode) {
        this.subjCode = subjCode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  币别代码
     */
    @Column(name = "CUR_CODE")
    public java.lang.String getCurCode() {
        return this.curCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  币别代码
     */
    public void setCurCode(java.lang.String curCode) {
        this.curCode = curCode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  有效标志
     */
    @Column(name = "VALID_SIGN")
    public java.lang.String getValidSign() {
        return this.validSign;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  有效标志
     */
    public void setValidSign(java.lang.String validSign) {
        this.validSign = validSign;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  createTime
     */
    @Column(name = "CREATE_TIME")
    public java.util.Date getCreateTime() {
        return this.createTime;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  createTime
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  createBy
     */
    @Column(name = "CREATE_BY")
    public java.lang.String getCreateBy() {
        return this.createBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  createBy
     */
    public void setCreateBy(java.lang.String createBy) {
        this.createBy = createBy;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  updateTime
     */
    @Column(name = "UPDATE_TIME")
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  updateTime
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  updateBy
     */
    @Column(name = "UPDATE_BY")
    public java.lang.String getUpdateBy() {
        return this.updateBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  updateBy
     */
    public void setUpdateBy(java.lang.String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  凭证种类
     */
    @Column(name = "VOUCHER_TYPE")
    public java.lang.String getVoucherType() {
        return this.voucherType;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  凭证种类
     */
    public void setVoucherType(java.lang.String voucherType) {
        this.voucherType = voucherType;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "IS_INIT")
    public String getIsInit() {
        return isInit;
    }

    public void setIsInit(String isInit) {
        this.isInit = isInit;
    }
}
