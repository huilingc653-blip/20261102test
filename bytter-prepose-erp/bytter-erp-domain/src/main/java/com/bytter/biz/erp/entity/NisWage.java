package com.bytter.biz.erp.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 代发工资总表
 *
 * @author fangcf
 * @date 2026-05-13
 */
@Entity
@Table(name = "NIS_WAGE", schema = "")
public class NisWage extends IdEntity implements java.io.Serializable {
    /**
     * 单据号码
     */
    private String wageNo;
    /**
     * 工资册标题
     */
    private String wageTitle;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 付款账号
     */
    private String payerAcc;
    /**
     * 付款账号名称
     */
    private String payerAccName;
    /**
     * 币别
     */
    private String curId;
    /**
     * 接口代码
     */
    private String bifCode;
    /**
     * 金额
     */
    private java.math.BigDecimal wages;
    /**
     * 工资发放日期
     */
    private java.util.Date wageDay;
    /**
     * 期望支付日期
     */
    private java.util.Date wishPayday;
    /**
     * 附言
     */
    private String addWord;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 交易状态
     */
    private String voucherStat;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 业务种类
     */
    private String busType;
    /**
     * erp中间表ID
     */
    private String erpId;
    /**
     * 勾兑标志
     */
    private String blendSign;
    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单据号码
     */
    @Column(name = "WAGE_NO", nullable = false, length = 12)
    public String getWageNo() {
        return this.wageNo;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  单据号码
     */
    public void setWageNo(String wageNo) {
        this.wageNo = wageNo;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  工资册标题
     */
    @Column(name = "WAGE_TITLE", nullable = false, length = 100)
    public String getWageTitle() {
        return this.wageTitle;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  工资册标题
     */
    public void setWageTitle(String wageTitle) {
        this.wageTitle = wageTitle;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单位ID
     */
    @Column(name = "CORP_ID", nullable = false, length = 32)
    public String getCorpId() {
        return this.corpId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  单位ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  付款账号
     */
    @Column(name = "PAYER_ACC", nullable = true, length = 30)
    public String getPayerAcc() {
        return this.payerAcc;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  付款账号
     */
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  付款账号名称
     */
    @Column(name = "PAYER_ACC_NAME", nullable = true, length = 150)
    public String getPayerAccName() {
        return this.payerAccName;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  付款账号名称
     */
    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  币别
     */
    @Column(name = "CUR_ID", nullable = true, length = 32)
    public String getCurId() {
        return this.curId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  币别
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  接口代码
     */
    @Column(name = "BIF_CODE", nullable = true, length = 32)
    public String getBifCode() {
        return this.bifCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  接口代码
     */
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  金额
     */
    @Column(name = "WAGES", nullable = false, scale = 2, length = 15)
    public java.math.BigDecimal getWages() {
        return this.wages;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  金额
     */
    public void setWages(java.math.BigDecimal wages) {
        this.wages = wages;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  工资发放日期
     */
    @Column(name = "WAGE_DAY", nullable = true)
    public java.util.Date getWageDay() {
        return this.wageDay;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  工资发放日期
     */
    public void setWageDay(java.util.Date wageDay) {
        this.wageDay = wageDay;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  期望支付日期
     */
    @Column(name = "WISH_PAYDAY", nullable = true)
    public java.util.Date getWishPayday() {
        return this.wishPayday;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  期望支付日期
     */
    public void setWishPayday(java.util.Date wishPayday) {
        this.wishPayday = wishPayday;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附言
     */
    @Column(name = "ADD_WORD", nullable = true, length = 100)
    public String getAddWord() {
        return this.addWord;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附言
     */
    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  状态
     */
    @Column(name = "STATUS", nullable = true, length = 10)
    public Integer getStatus() {
        return this.status;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  交易状态
     */
    @Column(name = "VOUCHER_STAT", nullable = true, length = 1)
    public String getVoucherStat() {
        return this.voucherStat;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  交易状态
     */
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  附件ID
     */
    @Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
    public String getAttachmentId() {
        return attachmentId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  附件ID
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人登录名称
     */
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return this.createBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true, scale = 6)
    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人登录名称
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return this.updateBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true, scale = 6)
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  业务种类
     */
    @Column(name = "BUS_TYPE", nullable = true, length = 32)
    public String getBusType() {
        return busType;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  业务种类
     */
    public void setBusType(String busType) {
        this.busType = busType;
    }

    @Column(name = "ERP_ID", nullable = true, length = 32)
    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    @Column(name = "BLEND_SIGN", nullable = true, length = 1)
    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }

    @Column(name = "TENANT_ID", nullable = true, length = 32)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
