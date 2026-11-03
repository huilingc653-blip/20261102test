package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: 出票融资申请登记薄VO
 * @Description: 出票融资申请登记薄VO
 * @author: zhenghj
 * @date:   2022-3-9
 * @version V1.0
 *
 */
public class AbPoolFinaApplyBookVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**流水Id*/
	@BtCompositeSearch(fieldName="dtl.BOOK_ID")
    private String id;
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
	@BtCompositeSearch(fieldName="pc.corp_id")
	private String corpId;
	/**单位代码*/
	private String corpCode;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 票据池id
	 */
	private String bpId;
	/**票据池编号*/
	@BtCompositeSearch(fieldName="p.id")
	private String poolNo;
	/**
	 * 票据池名称
	 */
	private String poolName;
	/**
	 *融资金额
	 */
	private BigDecimal financeAmt;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="fb.bill_type")
	@Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private String billType;
	@BtCompositeSearch(fieldName="fb.bill_type")
	private String  billTypeName;
	/**
	 *申请日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date applyDate;
	/**
	 *起始日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date stDate;
	/**
	 *到期日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="fb.end_Date")
	private Date endDate;
	/**
	 *申请状态 YT:已提交至银行,TH:银行退回,SL:银行已受理,CZ:已出账,JQ:已结清，YF：已重新提交
	 */
	@Dict(dictGroup="AB_POOL_APPLY_STATUS",inject="applyStatusName")
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
	@Dict(dictGroup="AB_POOL_BUSINESSTYPE",inject="businessTypeName")
	private String businessType;
	private String businessTypeName;
	/**
	 *业务类型名称
	 */
	private String businessName;
	/**
	 *商票交易类型 03:(出票、承兑) 05:(出票、承兑、提示收票)
	 */
	@Dict(dictGroup="AB_POOL_TYPE",inject="typeName")
	private String type;
	private String typeName;
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
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 *更新日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**
	 * 登记薄ID
	 */
	private String bookId;
	/**
	 *票据号码
	 */
	private String origNo;
	/**
	 *票据金额
	 */
	private BigDecimal billMoney;
	/**
	 *业务种类 03:(出票、承兑）05:(出票、承兑、提示收票) 09：贴现
	 */
	@Dict(dictGroup="AB_POOL_TYPE",inject="busTypeName")
	private String busType;
	private String busTypeName;
	/**
	 *票据状态
	 */
	@Dict(dictGroup="AB_BILL_STATUS",inject="billStatusName")
	private String billStatus;
	private String billStatusName;
	/**
	 *出票人
	 */
	private String payer;
	/**
	 *出票人账号
	 */
	private String payerAcc;
	/**
	 *出票人开户银行名称
	 */
	private String payerBankName;
	/**
	 *出票人开户银行行号
	 */
	private String payerBankNo;
	/**
	 *收票人全称
	 */
	private String payee;
	/**
	 *收票人账号
	 */
	private String payeeAcc;
	/**
	 *收票人开户银行名称
	 */
	private String payeeBankName;
	/**
	 *收票人开户银行行号
	 */
	private String payeeBankno;
	/**
	 *承兑人
	 */
	private String acceptor;
	/**
	 *承兑人账号
	 */
	private String acceptorAcc;
	/**
	 *承兑人开户行行号
	 */
	private String acceptorBankNo;
	/**
	 *承兑人开户行名称
	 */
	private String acceptorBankName;
	/**
	 * 出票日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="fb.st_Date")
	private Date startDate;
	/**
	 *发票号
	 */
	private String invcNo;
	/**
	 *实付金额
	 */
	private BigDecimal realpayAmt;
	/**
	 *赎回实付金额,
	 */
	private BigDecimal redmrealpayAmt;
	/**
	 *不得转让标记EM00可再转让EM01不得转让
	 */
	@Dict(dictGroup="AB_BANEM",inject="banendrsmtmkName")
	private String banendrsmtmk;
	private String banendrsmtmkName;
	/**
	 *手续费
	 */
	private BigDecimal promsfee;
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplyCode() {
		return applyCode;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public BigDecimal getFinanceAmt() {
		return financeAmt;
	}
	public void setFinanceAmt(BigDecimal financeAmt) {
		this.financeAmt = financeAmt;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getStDate() {
		return stDate;
	}
	public void setStDate(Date stDate) {
		this.stDate = stDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getApplyStatusName() {
		return applyStatusName;
	}
	public void setApplyStatusName(String applyStatusName) {
		this.applyStatusName = applyStatusName;
	}
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	public String getBizName() {
		return bizName;
	}
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrname() {
		return currname;
	}
	public void setCurrname(String currname) {
		this.currname = currname;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSrvrId() {
		return srvrId;
	}
	public void setSrvrId(String srvrId) {
		this.srvrId = srvrId;
	}
	public String getUpimage() {
		return upimage;
	}
	public void setUpimage(String upimage) {
		this.upimage = upimage;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getUpsuccess() {
		return upsuccess;
	}
	public void setUpsuccess(String upsuccess) {
		this.upsuccess = upsuccess;
	}
	public String getSuccessname() {
		return successname;
	}
	public void setSuccessname(String successname) {
		this.successname = successname;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getFirmarginAcctid() {
		return firmarginAcctid;
	}
	public void setFirmarginAcctid(String firmarginAcctid) {
		this.firmarginAcctid = firmarginAcctid;
	}
	public BigDecimal getFirmarginAmt() {
		return firmarginAmt;
	}
	public void setFirmarginAmt(BigDecimal firmarginAmt) {
		this.firmarginAmt = firmarginAmt;
	}
	public BigDecimal getFirmarginPerctge() {
		return firmarginPerctge;
	}
	public void setFirmarginPerctge(BigDecimal firmarginPerctge) {
		this.firmarginPerctge = firmarginPerctge;
	}
	public String getBacmarginAcctid() {
		return bacmarginAcctid;
	}
	public void setBacmarginAcctid(String bacmarginAcctid) {
		this.bacmarginAcctid = bacmarginAcctid;
	}
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	public String getFinPurpose() {
		return finPurpose;
	}
	public void setFinPurpose(String finPurpose) {
		this.finPurpose = finPurpose;
	}
	public String getApplyDesc() {
		return applyDesc;
	}
	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public String getPayerBankNo() {
		return payerBankNo;
	}
	public void setPayerBankNo(String payerBankNo) {
		this.payerBankNo = payerBankNo;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public String getPayeeBankno() {
		return payeeBankno;
	}
	public void setPayeeBankno(String payeeBankno) {
		this.payeeBankno = payeeBankno;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getAcceptorAcc() {
		return acceptorAcc;
	}
	public void setAcceptorAcc(String acceptorAcc) {
		this.acceptorAcc = acceptorAcc;
	}
	public String getAcceptorBankNo() {
		return acceptorBankNo;
	}
	public void setAcceptorBankNo(String acceptorBankNo) {
		this.acceptorBankNo = acceptorBankNo;
	}
	public String getAcceptorBankName() {
		return acceptorBankName;
	}
	public void setAcceptorBankName(String acceptorBankName) {
		this.acceptorBankName = acceptorBankName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getInvcNo() {
		return invcNo;
	}
	public void setInvcNo(String invcNo) {
		this.invcNo = invcNo;
	}
	public BigDecimal getRealpayAmt() {
		return realpayAmt;
	}
	public void setRealpayAmt(BigDecimal realpayAmt) {
		this.realpayAmt = realpayAmt;
	}
	public BigDecimal getRedmrealpayAmt() {
		return redmrealpayAmt;
	}
	public void setRedmrealpayAmt(BigDecimal redmrealpayAmt) {
		this.redmrealpayAmt = redmrealpayAmt;
	}
	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}
	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
	}
	public BigDecimal getPromsfee() {
		return promsfee;
	}
	public void setPromsfee(BigDecimal promsfee) {
		this.promsfee = promsfee;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getBanendrsmtmkName() {
		return banendrsmtmkName;
	}
	public void setBanendrsmtmkName(String banendrsmtmkName) {
		this.banendrsmtmkName = banendrsmtmkName;
	}
	public String getPoolNo() {
		return poolNo;
	}
	public void setPoolNo(String poolNo) {
		this.poolNo = poolNo;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBusinessTypeName() {
		return businessTypeName;
	}
	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}
	public String getBillType() {
		return billType;
	}
	public String getBillStatusName() {
		return billStatusName;
	}
	public void setBillStatusName(String billStatusName) {
		this.billStatusName = billStatusName;
	}
	@Override
	public String toString() {
		return "AbPoolFinaApplyBookVO{" +
				"id='" + id + '\'' +
				", applyCode='" + applyCode + '\'' +
				", custId='" + custId + '\'' +
				", corpId='" + corpId + '\'' +
				", corpCode='" + corpCode + '\'' +
				", corpName='" + corpName + '\'' +
				", bpId='" + bpId + '\'' +
				", poolNo='" + poolNo + '\'' +
				", poolName='" + poolName + '\'' +
				", financeAmt=" + financeAmt +
				", billType=" + billType +
				", billTypeName='" + billTypeName + '\'' +
				", applyDate=" + applyDate +
				", stDate=" + stDate +
				", endDate=" + endDate +
				", applyStatus='" + applyStatus + '\'' +
				", applyStatusName='" + applyStatusName + '\'' +
				", bizCode='" + bizCode + '\'' +
				", bizName='" + bizName + '\'' +
				", remark='" + remark + '\'' +
				", currency='" + currency + '\'' +
				", currname='" + currname + '\'' +
				", businessType='" + businessType + '\'' +
				", businessTypeName='" + businessTypeName + '\'' +
				", businessName='" + businessName + '\'' +
				", type='" + type + '\'' +
				", typeName='" + typeName + '\'' +
				", srvrId='" + srvrId + '\'' +
				", upimage='" + upimage + '\'' +
				", imageName='" + imageName + '\'' +
				", upsuccess='" + upsuccess + '\'' +
				", successname='" + successname + '\'' +
				", contractCode='" + contractCode + '\'' +
				", contractId='" + contractId + '\'' +
				", contractName='" + contractName + '\'' +
				", firmarginAcctid='" + firmarginAcctid + '\'' +
				", firmarginAmt=" + firmarginAmt +
				", firmarginPerctge=" + firmarginPerctge +
				", bacmarginAcctid='" + bacmarginAcctid + '\'' +
				", acctId='" + acctId + '\'' +
				", finPurpose='" + finPurpose + '\'' +
				", applyDesc='" + applyDesc + '\'' +
				", returnReason='" + returnReason + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", bookId='" + bookId + '\'' +
				", origNo='" + origNo + '\'' +
				", billMoney=" + billMoney +
				", busType='" + busType + '\'' +
				", busTypeName='" + busTypeName + '\'' +
				", billStatus='" + billStatus + '\'' +
				", billStatusName='" + billStatusName + '\'' +
				", payer='" + payer + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", payerBankNo='" + payerBankNo + '\'' +
				", payee='" + payee + '\'' +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payeeBankno='" + payeeBankno + '\'' +
				", acceptor='" + acceptor + '\'' +
				", acceptorAcc='" + acceptorAcc + '\'' +
				", acceptorBankNo='" + acceptorBankNo + '\'' +
				", acceptorBankName='" + acceptorBankName + '\'' +
				", startDate=" + startDate +
				", invcNo='" + invcNo + '\'' +
				", realpayAmt=" + realpayAmt +
				", redmrealpayAmt=" + redmrealpayAmt +
				", banendrsmtmk='" + banendrsmtmk + '\'' +
				", banendrsmtmkName='" + banendrsmtmkName + '\'' +
				", promsfee=" + promsfee +
				'}';
	}
}
