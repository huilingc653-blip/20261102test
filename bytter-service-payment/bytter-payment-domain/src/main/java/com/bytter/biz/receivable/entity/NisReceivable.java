package com.bytter.biz.receivable.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 收款通知表
 * @author wangzx
 * @date 2019-07-02 17:30:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_RECEIVABLE", schema = "")
@SuppressWarnings("serial")
public class NisReceivable extends IdEntity implements java.io.Serializable {
    /**收款款单号*/
    private java.lang.String billNo;
    /**单位*/
    private java.lang.String corpId;
    /**业务类别*/
    private java.lang.String busType;
    /**本方账号*/
    private java.lang.String bankAcc;
    /**本方账户名*/
    private java.lang.String accName;
    /**本方账户开户行*/
    private java.lang.String bankName;
    /**金额*/
    private java.math.BigDecimal amt;
    /**币别*/
    private java.lang.String curId;
    /**对方账号*/
    private java.lang.String oppAccNo;
    /**对方账户名*/
    private java.lang.String oppAccName;
    /**对方开户行行号*/
    private java.lang.String oppBankCode;
    /**对方账户开户行*/
    private java.lang.String oppAccBank;
    /**备注*/
    private java.lang.String rmk;
    /**单据来源 1-收款管理 2-流水认领 3-ERP系统*/
    private java.lang.String billFrom;
    /**状态*/
    private java.lang.Integer status;
    /**银行流水号*/
    private java.lang.String bankSerialNo;
    /**交易时间*/
    private java.util.Date transTime;
    /**附件ID*/
    private java.lang.String attachmentId;
    /**创建人登录名称*/
    private java.lang.String createBy;
    /**创建日期*/
    private java.util.Date createDate;
    /**更新人登录名称*/
    private java.lang.String updateBy;
    /**更新日期*/
    private java.util.Date updateDate;
    /**业务单号ID*/
    private String billId;
    /**系统业务类别*/
    private String sysBusType;
    /**关联单号*/
    private String busBillNo;
    /**审批时间*/
    private java.util.Date approveDate;
    @Column(name ="BUS_BILL_NO")
    public String getBusBillNo() {
        return busBillNo;
    }
    public void setBusBillNo(String busBillNo) {
        this.busBillNo = busBillNo;
    }
    @Column(name ="BILL_ID")
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    @Column(name ="SYS_BUS_TYPE")
    public String getSysBusType() {
        return sysBusType;
    }
    public void setSysBusType(String sysBusType) {
        this.sysBusType = sysBusType;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  收款单号
     */
    @Column(name ="BILL_NO",nullable=false,length=30)
    public String getBillNo() {
        return billNo;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  收款单号
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  单位
     */
    @Column(name ="CORP_ID",nullable=false,length=32)
    public java.lang.String getCorpId(){
        return this.corpId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  单位
     */
    public void setCorpId(java.lang.String corpId){
        this.corpId = corpId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务类别
     */
    @Column(name ="BUS_TYPE",nullable=true,length=32)
    public java.lang.String getBusType(){
        return this.busType;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务类别
     */
    public void setBusType(java.lang.String busType){
        this.busType = busType;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  本方账号
     */
    @Column(name ="BANK_ACC",nullable=true,length=60)
    public String getBankAcc() {
        return bankAcc;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务类别
     */
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  本方账户名
     */
    @Column(name ="ACC_NAME",nullable=true,length=200)
    public String getAccName() {
        return accName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  本方账户名
     */
    public void setAccName(String accName) {
        this.accName = accName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  本方账户开户行
     */
    @Column(name ="BANK_NAME",nullable=true,length=200)
    public String getBankName() {
        return bankName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  本方账户开户行
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  金额
     */
    @Column(name ="AMT",nullable=true,scale=2,length=15)
    public BigDecimal getAmt() {
        return amt;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@return: java.math.BigDecimal  金额
     */
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  币别
     */
    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }
    /**
     *方法: 设置java.math.String
     *@return: java.math.String  币别
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  对方账号
     */
    @Column(name ="OPP_ACC_NO",nullable=true,length=60)
    public String getOppAccNo() {
        return oppAccNo;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  对方账号
     */
    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  对方账户名
     */
    @Column(name ="OPP_ACC_NAME",nullable=true,length=200)
    public String getOppAccName() {
        return oppAccName;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  对方账户名
     */
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  对方开户行行号
     */
    @Column(name ="OPP_BANK_CODE",nullable=true,length=20)
    public String getOppBankCode() {
        return oppBankCode;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  对方开户行行号
     */
    public void setOppBankCode(String oppBankCode) {
        this.oppBankCode = oppBankCode;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  对方账户开户行
     */
    @Column(name ="OPP_ACC_BANK",nullable=true,length=200)
    public String getOppAccBank() {
        return oppAccBank;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  对方账户开户行
     */
    public void setOppAccBank(String oppAccBank) {
        this.oppAccBank = oppAccBank;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  备注
     */
    @Column(name ="RMK",nullable=true,length=200)
    public String getRmk() {
        return rmk;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  单据来源
     */
    @Column(name ="BILL_FROM",nullable=true,length=2)
    public String getBillFrom() {
        return billFrom;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  单据来源
     */
    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  状态
     */
    @Column(name ="STATUS",nullable=true,length=10)
    public Integer getStatus() {
        return status;
    }
    /**
     *方法: 设置java.lang.Integer
     *@return: java.lang.Integer  状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  银行流水号
     */
    @Column(name ="BANK_SERIAL_NO",nullable=true,length=60)
    public String getBankSerialNo() {
        return bankSerialNo;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  银行流水号
     */
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  交易时间
     */
    @Column(name ="TRANS_TIME",nullable=true)
    public Date getTransTime() {
        return transTime;
    }
    /**
     *方法: 设置java.util.Date
     *@return: java.util.Date  交易时间
     */
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  附件
     */
    @Column(name ="ATTACHMENT_ID",nullable=true,length=32)
    public String getAttachmentId() {
        return attachmentId;
    }
    /**
     *方法: 设置java.util.Date
     *@return: java.util.Date  附件ID
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  创建人登录名称
     */
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public java.lang.String getCreateBy(){
        return this.createBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(java.lang.String createBy){
        this.createBy = createBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  创建日期
     */
    @Column(name ="CREATE_DATE",nullable=true)
    public java.util.Date getCreateDate(){
        return this.createDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  创建日期
     */
    public void setCreateDate(java.util.Date createDate){
        this.createDate = createDate;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  更新人登录名称
     */
    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public java.lang.String getUpdateBy(){
        return this.updateBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(java.lang.String updateBy){
        this.updateBy = updateBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  更新日期
     */
    @Column(name ="UPDATE_DATE",nullable=true)
    public java.util.Date getUpdateDate(){
        return this.updateDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  更新日期
     */
    public void setUpdateDate(java.util.Date updateDate){
        this.updateDate = updateDate;
    }
    @Column(name ="APPROVE_DATE")
    public Date getApproveDate() {
        return approveDate;
    }
    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }
}
