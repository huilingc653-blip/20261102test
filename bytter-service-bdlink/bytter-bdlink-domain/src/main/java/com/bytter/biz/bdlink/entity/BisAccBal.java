package com.bytter.biz.bdlink.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity 银行账号余额表
 * @Description: BIS_ACC_BAL
 * @date 2017-06-23 15:02:30
 */
@Entity
@Table(name = "BIS_ACC_BAL")
public class BisAccBal extends IdEntity implements java.io.Serializable {
    /**
     * 业务单号
     */
    private java.lang.String accNo;
    /**
     * 银行账号
     */
    private java.lang.String bankAcc;
    /**
     * 单位ID
     */
    private java.lang.String corpId;
    /**
     * 币别ID
     */
    private java.lang.String curId;
    /**
     * 接口代码
     */
    private java.lang.String bifCode;
    /**
     * 获取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date getDate;
    /**
     * 余额日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date balDate;
    /**
     * 余额
     */
    private java.math.BigDecimal bal;
    /**
     * 可用余额
     */
    private java.math.BigDecimal availBal;
    /**
     * 冻结金额
     */
    private java.math.BigDecimal frzBal;
    /**
     * 系统冻结金额
     */
    private java.math.BigDecimal sysFrzBal;
    /**
     * 返回码
     */
    private java.lang.String returnCode;
    /**
     * 返回描述信息
     */
    private java.lang.String returnMsg;
    /**
     * 状态
     */
    private java.lang.Integer status;
    /**
     * 创建人登录名称
     */
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 初始化余额标记（针对离线账号）
     */
    private String initSign;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  业务单号
     */
    @Column(name = "ACC_NO", nullable = true, length = 40)
    public java.lang.String getAccNo() {
        return this.accNo;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  业务单号
     */
    public void setAccNo(java.lang.String accNo) {
        this.accNo = accNo;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  银行账号
     */
    @Column(name = "BANK_ACC", nullable = true, length = 30)
    public java.lang.String getBankAcc() {
        return this.bankAcc;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  银行账号
     */
    public void setBankAcc(java.lang.String bankAcc) {
        this.bankAcc = bankAcc;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单位ID
     */
    @Column(name = "CORP_ID", nullable = true, length = 32)
    public java.lang.String getCorpId() {
        return this.corpId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  单位ID
     */
    public void setCorpId(java.lang.String corpId) {
        this.corpId = corpId;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  币别ID
     */
    @Column(name = "CUR_ID", nullable = true, length = 32)
    public java.lang.String getCurId() {
        return this.curId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  币别ID
     */
    public void setCurId(java.lang.String curId) {
        this.curId = curId;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  接口代码
     */
    @Column(name = "BIF_CODE", nullable = true, length = 32)
    public java.lang.String getBifCode() {
        return this.bifCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  接口代码
     */
    public void setBifCode(java.lang.String bifCode) {
        this.bifCode = bifCode;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  获取时间
     */
    @Column(name = "GET_DATE", nullable = true)
    public java.util.Date getGetDate() {
        return this.getDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  获取时间
     */
    public void setGetDate(java.util.Date getDate) {
        this.getDate = getDate;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  余额日期
     */
    @Column(name = "BAL_DATE", nullable = true)
    public java.util.Date getBalDate() {
        return this.balDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  余额日期
     */
    public void setBalDate(java.util.Date balDate) {
        this.balDate = balDate;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  余额
     */
    @Column(name = "BAL", nullable = true, scale = 2, length = 19)
    public java.math.BigDecimal getBal() {
        return this.bal;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  余额
     */
    public void setBal(java.math.BigDecimal bal) {
        this.bal = bal;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  可用余额
     */
    @Column(name = "AVAIL_BAL", nullable = true, scale = 2, length = 19)
    public java.math.BigDecimal getAvailBal() {
        return this.availBal;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  可用余额
     */
    public void setAvailBal(java.math.BigDecimal availBal) {
        this.availBal = availBal;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  冻结金额
     */
    @Column(name = "FRZ_BAL", nullable = true, scale = 2, length = 19)
    public java.math.BigDecimal getFrzBal() {
        return this.frzBal;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  冻结金额
     */
    public void setFrzBal(java.math.BigDecimal frzBal) {
        this.frzBal = frzBal;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  系统冻结金额
     */
    @Column(name = "SYS_FRZ_BAL", nullable = true, scale = 2, length = 19)
    public BigDecimal getSysFrzBal() {
        return sysFrzBal;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  系统冻结金额
     */
    public void setSysFrzBal(BigDecimal sysFrzBal) {
        this.sysFrzBal = sysFrzBal;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  返回码
     */
    @Column(name = "RETURN_CODE", nullable = true)
    public java.lang.String getReturnCode() {
        return this.returnCode;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  返回码
     */
    public void setReturnCode(java.lang.String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  返回描述信息
     */
    @Column(name = "RETURN_MSG", nullable = true, length = 500)
    public java.lang.String getReturnMsg() {
        return this.returnMsg;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  返回描述信息
     */
    public void setReturnMsg(java.lang.String returnMsg) {
        this.returnMsg = returnMsg;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  状态
     */
    @Column(name = "STATUS", nullable = true, length = 10)
    public java.lang.Integer getStatus() {
        return this.status;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  状态
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人登录名称
     */
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public java.lang.String getCreateBy() {
        return this.createBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(java.lang.String createBy) {
        this.createBy = createBy;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
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
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public java.lang.String getUpdateBy() {
        return this.updateBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(java.lang.String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
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

    @Column(name = "INIT_SIGN")
    public String getInitSign() {
        return initSign;
    }

    public void setInitSign(String initSign) {
        this.initSign = initSign;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
