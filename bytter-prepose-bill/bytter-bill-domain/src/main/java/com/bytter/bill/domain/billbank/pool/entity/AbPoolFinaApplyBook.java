package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @Title:  出票融资申请登记薄表entity
 * @author: zhenghj
 * @date:   2022-3-9
 * @version V1.0
 */
@Entity
@Table(name = "AB_POOL_FINAAPPLY_BOOK", schema = "")
public class AbPoolFinaApplyBook extends IdEntity implements java.io.Serializable{
    /**
     * 申请代码
     */
    private String applyCode;
    /**
     * 客户标识号
     */
    private String custId;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 票据池id
     */
    private String bpId;
    /**
     *融资金额
     */
    private BigDecimal financeAmt;
    /**票据类型：10 银票、11 商票*/
    private Integer billType;
    /**
     *申请日期
     */
    private Date applyDate;
    /**
     *起始日期
     */
    private Date stDate;
    /**
     *到期日期
     */
    private Date endDate;
    /**
     *申请状态 YT:已提交至银行,TH:银行退回,SL:银行已受理,CZ:已出账,JQ:已结清，YF：已重新提交
     */
    private String applyStatus;
    /**
     *申请状态名称
     */
    private String applyStatusName;
    /**
     *业务品种代码
     */
    private String bizCode;
    /**
     *业务品种名称
     */
    private String bizName;
    /**
     *备注
     */
    private String remark;
    /**
     *货币种类 01：人民币
     */
    private String currency;
    /**
     *货币种类名称
     */
    private String currname;
    /**
     *业务类型 CT:传统业务；MJ:贸金业务
     */
    private String businessType;
    /**
     *业务类型名称
     */
    private String businessName;
    /**
     *商票交易类型 03:(出票、承兑） 05:(出票、承兑、提示收票)
     */
    private String type;
    /**
     *商票网银跟踪号
     */
    private String srvrId;
    /**
     *是否上传影像 01:是；02：否
     */
    private String upimage;
    /**
     *是否上传影像名称
     */
    private String imageName;
    /**
     *是否上传成功 CG：成功；SB：失败
     */
    private String upsuccess;
    /**
     *是否上传成功名称
     */
    private String successname;
    /**
     *协议(合同)代码
     */
    private String contractCode;
    /**
     *协议(合同)编号
     */
    private String contractId;
    /**
     *协议(合同)名称
     */
    private String contractName;
    /**
     *初始保证金账号
     */
    private String firmarginAcctid;
    /**
     *初始保证金总额
     */
    private BigDecimal firmarginAmt;
    /**
     *初始保证金比例
     */
    private BigDecimal firmarginPerctge;
    /**
     *回款保证金账号
     */
    private String bacmarginAcctid;
    /**
     *付款账号
     */
    private String acctId;
    /**
     *融资用途
     */
    private String finPurpose;
    /**
     *申请说明
     */
    private String applyDesc;
    /**
     *退回原因
     */
    private String returnReason;
    /**
     *创建日期
     */
    private Date createTime;
    /**
     *更新日期
     */
    private Date updateTime;
    @Column(name = "APPLY_CODE", nullable = true)
    public String getApplyCode() {
        return applyCode;
    }
    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }
    @Column(name = "CUST_ID", nullable = true)
    public String getCustId() {
        return custId;
    }
    public void setCustId(String custId) {
        this.custId = custId;
    }
    @Column(name = "CORP_ID", nullable = true)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CORP_NAME", nullable = true)
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "BP_ID", nullable = true)
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    @Column(name = "FINANCE_AMT", nullable = true)
    public BigDecimal getFinanceAmt() {
        return financeAmt;
    }
    public void setFinanceAmt(BigDecimal financeAmt) {
        this.financeAmt = financeAmt;
    }
    @Column(name ="BILL_TYPE",nullable=true,length=10)
    public Integer getBillType(){
        return this.billType;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  票据类型：10 银票、11 商票
     */
    public void setBillType(Integer billType){
        this.billType = billType;
    }
    @Column(name = "APPLY_DATE", nullable = true)
    public Date getApplyDate() {
        return applyDate;
    }
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
    @Column(name = "ST_DATE", nullable = true)
    public Date getStDate() {
        return stDate;
    }
    public void setStDate(Date stDate) {
        this.stDate = stDate;
    }
    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "APPLY_STATUS", nullable = true)
    public String getApplyStatus() {
        return applyStatus;
    }
    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }
    @Column(name = "APPLY_STATUS_NAME", nullable = true)
    public String getApplyStatusName() {
        return applyStatusName;
    }
    public void setApplyStatusName(String applyStatusName) {
        this.applyStatusName = applyStatusName;
    }
    @Column(name = "BIZ_CODE", nullable = true)
    public String getBizCode() {
        return bizCode;
    }
    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }
    @Column(name = "BIZ_NAME", nullable = true)
    public String getBizName() {
        return bizName;
    }
    public void setBizName(String bizName) {
        this.bizName = bizName;
    }
    @Column(name = "REMARK", nullable = true)
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name = "CURRENCY", nullable = true)
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    @Column(name = "CURRNAME", nullable = true)
    public String getCurrname() {
        return currname;
    }
    public void setCurrname(String currname) {
        this.currname = currname;
    }
    @Column(name = "BUSINESS_TYPE", nullable = true)
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    @Column(name = "BUSINESS_NAME", nullable = true)
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    @Column(name = "TYPE", nullable = true)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "SRVR_ID", nullable = true)
    public String getSrvrId() {
        return srvrId;
    }
    public void setSrvrId(String srvrId) {
        this.srvrId = srvrId;
    }
    @Column(name = "UPIMAGE", nullable = true)
    public String getUpimage() {
        return upimage;
    }
    public void setUpimage(String upimage) {
        this.upimage = upimage;
    }
    @Column(name = "IMAGE_NAME", nullable = true)
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    @Column(name = "UPSUCCESS", nullable = true)
    public String getUpsuccess() {
        return upsuccess;
    }
    public void setUpsuccess(String upsuccess) {
        this.upsuccess = upsuccess;
    }
    @Column(name = "SUCCESSNAME", nullable = true)
    public String getSuccessname() {
        return successname;
    }
    public void setSuccessname(String successname) {
        this.successname = successname;
    }
    @Column(name = "CONTRACT_CODE", nullable = true)
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Column(name = "CONTRACT_ID", nullable = true)
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
    @Column(name = "CONTRACT_NAME", nullable = true)
    public String getContractName() {
        return contractName;
    }
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    @Column(name = "FIRMARGIN_ACCTID", nullable = true)
    public String getFirmarginAcctid() {
        return firmarginAcctid;
    }
    public void setFirmarginAcctid(String firmarginAcctid) {
        this.firmarginAcctid = firmarginAcctid;
    }
    @Column(name = "FIRMARGIN_AMT", nullable = true)
    public BigDecimal getFirmarginAmt() {
        return firmarginAmt;
    }
    public void setFirmarginAmt(BigDecimal firmarginAmt) {
        this.firmarginAmt = firmarginAmt;
    }
    @Column(name = "FIRMARGIN_PERCTGE", nullable = true)
    public BigDecimal getFirmarginPerctge() {
        return firmarginPerctge;
    }
    public void setFirmarginPerctge(BigDecimal firmarginPerctge) {
        this.firmarginPerctge = firmarginPerctge;
    }
    @Column(name = "BACMARGIN_ACCTID", nullable = true)
    public String getBacmarginAcctid() {
        return bacmarginAcctid;
    }
    public void setBacmarginAcctid(String bacmarginAcctid) {
        this.bacmarginAcctid = bacmarginAcctid;
    }
    @Column(name = "ACCTID", nullable = true)
    public String getAcctId() {
        return acctId;
    }
    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }
    @Column(name = "FINPURPOSE", nullable = true)
    public String getFinPurpose() {
        return finPurpose;
    }
    public void setFinPurpose(String finPurpose) {
        this.finPurpose = finPurpose;
    }
    @Column(name = "APPLYDESC", nullable = true)
    public String getApplyDesc() {
        return applyDesc;
    }
    public void setApplyDesc(String applyDesc) {
        this.applyDesc = applyDesc;
    }
    @Column(name = "RETURNREASON", nullable = true)
    public String getReturnReason() {
        return returnReason;
    }
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
