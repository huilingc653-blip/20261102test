package com.bytter.bill.api.bill.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Description: 收票登记暂存-接口参数对象
 * @author zhenghj
 * @date 2019-06-13 14:42:25
 * @version 1.0
 *
 */
public class AbReceiveBillDTO  implements Serializable{
	/**票据类型：10 银票、11 商票*/
	private Integer billType;
	/**流通介质：10 纸票、11 电票*/
	private Integer billFlag;
	/**持票单位*/
	private String corpId;
	/**持票单位账号 */
	private String bankAcc;
	/**持票单位银行行号*/
	private String bankCode;
	/**持票单位银行名称*/
	private String bankName;
	/**出票人*/
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户银行名称*/
	private String payerBankName;
	/**出票人开户银行行号 */
	private String payerbankno;
	/**承兑人*/
	private String acceptor;
	/**承兑人账号 */
	private String acceptoracc;
	/**承兑人开户行名称 */
	private String acceptorBankName;
	/**承兑人开户行行号 */
	private String acceptorbankno;
	/**票据号码*/
	private String origNo;
	/**票据金额*/
	private BigDecimal billMoney;
	/**手续费*/
	private BigDecimal feemoney;
	/**出票日期*/
	private Date startDate;
	/**到期日期*/
	private Date endDate;
	/**申请日期 */
	private Date applyDate;
	/**业务归属日期 */
	private Date businessDate;
	/**收票人全称*/
	private String payee;
	/**收票人账号*/
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**票据状态*/
	private Integer status;
	/**所属库存*/
	private String enterStock;
	/**票据来源*/
	private String billSource;
	/** 内部业务标志：0 否；1 是 */
	private Integer insideSing;
	/**不得转让标记 */
	private String banendrsmtmk;
	/**承兑合同号 */
	private String acontractNo;
	/**发票号 */
	private String ainvcnb;
	/**前手*/
	private String cnAccrecords;
	/**摘要*/
	private String abs;
	/**备注*/
	private String rmk;
	/** 出票标志 */
	private String newStmIssuofpprInd;
	/** 是否允许分包 */
	private String blgrdbagcrclPrmtInd;
	/** 子票区间开始序号 */
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	private String sbbllRngEndSn;
	/** 标准金额 */
	private BigDecimal blStdAmt;
	/** 出票人统一识别号码 */
	private String blRmtrUnnIdNo;
	/** 收票人统一信用代码 */
	private String blRcvpymtpsUnnIdNo;
	/** 承兑人统一信用代码 */
	private String blAcptrUnnIdNo;
	private String ebillBlCrclIndc;//EBILL_BL_CRCL_INDC 流通标志
	public String getNewStmIssuofpprInd() {
		return newStmIssuofpprInd;
	}
	public void setNewStmIssuofpprInd(String newStmIssuofpprInd) {
		this.newStmIssuofpprInd = newStmIssuofpprInd;
	}
	public String getBlgrdbagcrclPrmtInd() {
		return blgrdbagcrclPrmtInd;
	}
	public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
		this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
	}
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}
	public BigDecimal getBlStdAmt() {
		return blStdAmt;
	}
	public void setBlStdAmt(BigDecimal blStdAmt) {
		this.blStdAmt = blStdAmt;
	}
	public String getBlRmtrUnnIdNo() {
		return blRmtrUnnIdNo;
	}
	public void setBlRmtrUnnIdNo(String blRmtrUnnIdNo) {
		this.blRmtrUnnIdNo = blRmtrUnnIdNo;
	}
	public String getBlRcvpymtpsUnnIdNo() {
		return blRcvpymtpsUnnIdNo;
	}
	public void setBlRcvpymtpsUnnIdNo(String blRcvpymtpsUnnIdNo) {
		this.blRcvpymtpsUnnIdNo = blRcvpymtpsUnnIdNo;
	}
	public String getBlAcptrUnnIdNo() {
		return blAcptrUnnIdNo;
	}
	public void setBlAcptrUnnIdNo(String blAcptrUnnIdNo) {
		this.blAcptrUnnIdNo = blAcptrUnnIdNo;
	}
	public String getEbillBlCrclIndc() {
		return ebillBlCrclIndc;
	}
	public void setEbillBlCrclIndc(String ebillBlCrclIndc) {
		this.ebillBlCrclIndc = ebillBlCrclIndc;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayeracc() {
		return payeracc;
	}
	public void setPayeracc(String payeracc) {
		this.payeracc = payeracc;
	}
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public String getPayerbankno() {
		return payerbankno;
	}
	public void setPayerbankno(String payerbankno) {
		this.payerbankno = payerbankno;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getAcceptoracc() {
		return acceptoracc;
	}
	public void setAcceptoracc(String acceptoracc) {
		this.acceptoracc = acceptoracc;
	}
	public String getAcceptorBankName() {
		return acceptorBankName;
	}
	public void setAcceptorBankName(String acceptorBankName) {
		this.acceptorBankName = acceptorBankName;
	}
	public String getAcceptorbankno() {
		return acceptorbankno;
	}
	public void setAcceptorbankno(String acceptorbankno) {
		this.acceptorbankno = acceptorbankno;
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
	public BigDecimal getFeemoney() {
		return feemoney;
	}
	public void setFeemoney(BigDecimal feemoney) {
		this.feemoney = feemoney;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getPayeeacc() {
		return payeeacc;
	}
	public void setPayeeacc(String payeeacc) {
		this.payeeacc = payeeacc;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public String getPayeebankno() {
		return payeebankno;
	}
	public void setPayeebankno(String payeebankno) {
		this.payeebankno = payeebankno;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getEnterStock() {
		return enterStock;
	}
	public void setEnterStock(String enterStock) {
		this.enterStock = enterStock;
	}
	public String getBillSource() {
		return billSource;
	}
	public void setBillSource(String billSource) {
		this.billSource = billSource;
	}
	public Integer getInsideSing() {
		return insideSing;
	}
	public void setInsideSing(Integer insideSing) {
		this.insideSing = insideSing;
	}
	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}
	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
	}
	public String getAcontractNo() {
		return acontractNo;
	}
	public void setAcontractNo(String acontractNo) {
		this.acontractNo = acontractNo;
	}
	public String getAinvcnb() {
		return ainvcnb;
	}
	public void setAinvcnb(String ainvcnb) {
		this.ainvcnb = ainvcnb;
	}
	public String getCnAccrecords() {
		return cnAccrecords;
	}
	public void setCnAccrecords(String cnAccrecords) {
		this.cnAccrecords = cnAccrecords;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Override
	public String toString() {
		return "AbReceiveBillDTO{" +
				"billType=" + billType +
				", billFlag=" + billFlag +
				", corpId='" + corpId + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", bankCode='" + bankCode + '\'' +
				", bankName='" + bankName + '\'' +
				", payer='" + payer + '\'' +
				", payeracc='" + payeracc + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", payerbankno='" + payerbankno + '\'' +
				", acceptor='" + acceptor + '\'' +
				", acceptoracc='" + acceptoracc + '\'' +
				", acceptorBankName='" + acceptorBankName + '\'' +
				", acceptorbankno='" + acceptorbankno + '\'' +
				", origNo='" + origNo + '\'' +
				", billMoney=" + billMoney +
				", feemoney=" + feemoney +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", applyDate=" + applyDate +
				", businessDate=" + businessDate +
				", payee='" + payee + '\'' +
				", payeeacc='" + payeeacc + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payeebankno='" + payeebankno + '\'' +
				", status=" + status +
				", enterStock='" + enterStock + '\'' +
				", billSource='" + billSource + '\'' +
				", insideSing=" + insideSing +
				", banendrsmtmk='" + banendrsmtmk + '\'' +
				", acontractNo='" + acontractNo + '\'' +
				", ainvcnb='" + ainvcnb + '\'' +
				", cnAccrecords='" + cnAccrecords + '\'' +
				", abs='" + abs + '\'' +
				", rmk='" + rmk + '\'' +
				'}';
	}
}
