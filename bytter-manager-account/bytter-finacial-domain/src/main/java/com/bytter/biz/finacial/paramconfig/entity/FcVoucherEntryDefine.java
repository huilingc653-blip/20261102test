package com.bytter.biz.finacial.paramconfig.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_VOUCHER_ENTRY_DEFINE
 * @date 2017-05-22 16:44:06
 */
@Entity
@Table(name = "FC_VOUCHER_ENTRY_DEFINE", schema = "")
@SuppressWarnings("serial")
public class FcVoucherEntryDefine extends IdEntity implements java.io.Serializable {
    /**
     * 业务规则ID
     */
    private java.lang.String businessRuleDefineId;
    /**
     * 所属类型
     */
    private java.lang.Integer belongType;
    /**
     * 借贷标识
     */
    private java.lang.Integer balanceWay;
    /**
     * 科目
     */
    private java.lang.String subjectCode;
    /**
     * 凭证种类
     */
    private java.lang.String voucherType;
    /**
     * 账号
     */
    private java.lang.String account;
    /**
     * 币别代码
     */
    private java.lang.String curCode;
    /**
     * 摘要
     */
    private java.lang.String summary;
    /**
     * 分录类型
     */
    private java.lang.Integer entryType;
    /**
     * 组号
     */
    private java.lang.Integer groupNum;
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
     * 内部账号
     */
    private java.lang.String blnctrAcc;
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
     * @return: java.lang.String  业务规则ID
     */
    @Column(name = "BUSINESS_RULE_DEFINE_ID")
    public java.lang.String getBusinessRuleDefineId() {
        return this.businessRuleDefineId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  业务规则ID
     */
    public void setBusinessRuleDefineId(java.lang.String businessRuleDefineId) {
        this.businessRuleDefineId = businessRuleDefineId;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  所属类型
     */
    @Column(name = "BELONG_TYPE")
    public java.lang.Integer getBelongType() {
        return this.belongType;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  所属类型
     */
    public void setBelongType(java.lang.Integer belongType) {
        this.belongType = belongType;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  借贷标识
     */
    @Column(name = "BALANCE_WAY")
    public java.lang.Integer getBalanceWay() {
        return this.balanceWay;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  借贷标识
     */
    public void setBalanceWay(java.lang.Integer balanceWay) {
        this.balanceWay = balanceWay;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  科目
     */
    @Column(name = "SUBJECT_CODE")
    public java.lang.String getSubjectCode() {
        return this.subjectCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  科目
     */
    public void setSubjectCode(java.lang.String subjectCode) {
        this.subjectCode = subjectCode;
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

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账号
     */
    @Column(name = "ACCOUNT")
    public java.lang.String getAccount() {
        return this.account;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账号
     */
    public void setAccount(java.lang.String account) {
        this.account = account;
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
     * @return: java.lang.String  摘要
     */
    @Column(name = "SUMMARY")
    public java.lang.String getSummary() {
        return this.summary;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  摘要
     */
    public void setSummary(java.lang.String summary) {
        this.summary = summary;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  分录类型
     */
    @Column(name = "ENTRY_TYPE")
    public java.lang.Integer getEntryType() {
        return this.entryType;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  分录类型
     */
    public void setEntryType(java.lang.Integer entryType) {
        this.entryType = entryType;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  组号
     */
    @Column(name = "GROUP_NUM")
    public java.lang.Integer getGroupNum() {
        return this.groupNum;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  组号
     */
    public void setGroupNum(java.lang.Integer groupNum) {
        this.groupNum = groupNum;
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
     * 内部账号
     *
     * @return
     */
    @Column(name = "BLNCTR_ACC")
    public String getBlnctrAcc() {
        return blnctrAcc;
    }

    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
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
