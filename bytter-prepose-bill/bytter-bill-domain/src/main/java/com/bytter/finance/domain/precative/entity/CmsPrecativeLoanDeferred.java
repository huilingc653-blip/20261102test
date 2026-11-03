package com.bytter.finance.domain.precative.entity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 委托贷款展期申请表
 * @author onlineGenerator
 * @date 2017-08-03 10:48:22
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PRECATIVE_LOAN_DEFERRED", schema = "")
@SuppressWarnings("serial")
public class CmsPrecativeLoanDeferred extends IdEntity implements java.io.Serializable {
    /** 单位代码 */
    private String corpId;
    /** 贷款发放单号 */
    private String liCode;
    /** 展期合同号 */
    private String contractCode;
    /** 贷款到期日 */
    private java.util.Date matureDate;
    /** 展期期限 */
    private Integer deferredLimt;
    /** 期限单位 */
    private String ternUnit;
    /** 展期到期日 */
    private java.util.Date deferredRepayDate;
    /** 展期开始日期 **/
    private java.util.Date deferredStartDate;
    /** 展期利率 **/
    private BigDecimal deferredRate;
    /** 贷款账号 */
    private String loanAcc;
    /** 备注 */
    private String rmk;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 展期单号 */
    private String billCode;
    /** 到期处理类型 **/
    private String expiryHandleType;
    /** 打回原因 **/
    private String refuseWhy;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款发放单号
     */
    @Column(name = "LI_CODE")
    public String getLiCode() {
        return this.liCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款发放单号
     */
    public void setLiCode(String liCode) {
        this.liCode = liCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 展期合同号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             展期合同号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 贷款到期日
     */
    @Column(name = "MATURE_DATE")
    public java.util.Date getMatureDate() {
        return this.matureDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             贷款到期日
     */
    public void setMatureDate(java.util.Date matureDate) {
        this.matureDate = matureDate;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 展期期限
     */
    @Column(name = "DEFERRED_LIMT")
    public Integer getDeferredLimt() {
        return this.deferredLimt;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             展期期限
     */
    public void setDeferredLimt(Integer deferredLimt) {
        this.deferredLimt = deferredLimt;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 期限单位
     */
    @Column(name = "TERN_UNIT")
    public String getTernUnit() {
        return this.ternUnit;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             期限单位
     */
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 展期到期日
     */
    @Column(name = "DEFERRED_REPAY_DATE")
    public java.util.Date getDeferredRepayDate() {
        return this.deferredRepayDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             展期到期日
     */
    public void setDeferredRepayDate(java.util.Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款账号
     */
    @Column(name = "LOAN_ACC")
    public String getLoanAcc() {
        return this.loanAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款账号
     */
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "DEFERRED_START_DATE")
    public java.util.Date getDeferredStartDate() {
        return deferredStartDate;
    }
    public void setDeferredStartDate(java.util.Date deferredStartDate) {
        this.deferredStartDate = deferredStartDate;
    }
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    @Column(name = "EXPIRY_HANDLE_TYPE")
    public String getExpiryHandleType() {
        return expiryHandleType;
    }
    public void setExpiryHandleType(String expiryHandleType) {
        this.expiryHandleType = expiryHandleType;
    }
    @Column(name = "REFUSE_WHY")
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
}
