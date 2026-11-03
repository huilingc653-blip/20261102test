package com.bytter.erp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhenghj
 * ERP应付票据-接口参数对象
 * 2019-06-17
 */
@SuppressWarnings("serial")
public class ErpAbEmitBillDTO implements Serializable {

	/**持票单位*/
	private String corpId;
	/**票据号*/
	private String origNo;
	/**票据金额*/
	private BigDecimal billMoney;
	/**出票人*/
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户银行名称*/
	private String payerBankName;
	/**出票人开户银行行号*/
	private String payerbankno;
	/**收票人全称*/
	private String payee;
	/**收票人账号*/
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**承兑人账号 */
	private String acceptoracc;
	/**承兑人开户行名称*/
	private String acceptorBankName;
	/**承兑人开户行行号*/
	private String acceptorbankno;
	/**承兑人开户行地址*/
	private String acceptorbankaddress;
	/**开出方式：0  对外开出；1 对内自开*/
	private String insideSing;
	/**前手*/
	private String cnAccrecords;
	/**业务归属日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date businessDate;
	/**票据类型：10 银票、11 商票*/
	private Integer billType;
	/**流通介质：10 纸票、11 电票*/
	private Integer billFlag;
	/**出票日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/**承兑协议编号 */
	private String accptncagrmtnb;
	/**交易合同号*/
	private String acontractNo;
	/**不得转让标记EM00可再转让EM01不得转让*/
	private String banendrsmtmk;
	/**开票信用等级*/
	private String payerCdtRatgs;
	/**开票评级机构*/
	private String payerCdtRatgGgcy;
	/**开票评级到期日*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date payerCdtRatgDuedt;
	/**发票号*/
	private String ainvcnb;
	/**保证金比例*/
	private BigDecimal bailpercent;
	/**保证金金额*/
	private BigDecimal bailmoney;
	/**保证金利率 */
	private BigDecimal bailrate;
	/**缴存保证金账户*/
	private String bailAcc;
	/**外部系统id编码*/
	private String voucherId;
	/**手续费 */
	private BigDecimal feemoney;
	/**摘要*/
	private String abs;
	/**备注*/
	private String rmk;

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
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

	public String getAcceptorbankaddress() {
		return acceptorbankaddress;
	}

	public void setAcceptorbankaddress(String acceptorbankaddress) {
		this.acceptorbankaddress = acceptorbankaddress;
	}

	public String getInsideSing() {
		return insideSing;
	}

	public void setInsideSing(String insideSing) {
		this.insideSing = insideSing;
	}

	public String getCnAccrecords() {
		return cnAccrecords;
	}

	public void setCnAccrecords(String cnAccrecords) {
		this.cnAccrecords = cnAccrecords;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
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

	public String getAccptncagrmtnb() {
		return accptncagrmtnb;
	}

	public void setAccptncagrmtnb(String accptncagrmtnb) {
		this.accptncagrmtnb = accptncagrmtnb;
	}

	public String getAcontractNo() {
		return acontractNo;
	}

	public void setAcontractNo(String acontractNo) {
		this.acontractNo = acontractNo;
	}

	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}

	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
	}

	public String getPayerCdtRatgs() {
		return payerCdtRatgs;
	}

	public void setPayerCdtRatgs(String payerCdtRatgs) {
		this.payerCdtRatgs = payerCdtRatgs;
	}

	public String getPayerCdtRatgGgcy() {
		return payerCdtRatgGgcy;
	}

	public void setPayerCdtRatgGgcy(String payerCdtRatgGgcy) {
		this.payerCdtRatgGgcy = payerCdtRatgGgcy;
	}

	public Date getPayerCdtRatgDuedt() {
		return payerCdtRatgDuedt;
	}

	public void setPayerCdtRatgDuedt(Date payerCdtRatgDuedt) {
		this.payerCdtRatgDuedt = payerCdtRatgDuedt;
	}

	public String getAinvcnb() {
		return ainvcnb;
	}

	public void setAinvcnb(String ainvcnb) {
		this.ainvcnb = ainvcnb;
	}

	public BigDecimal getBailpercent() {
		return bailpercent;
	}

	public void setBailpercent(BigDecimal bailpercent) {
		this.bailpercent = bailpercent;
	}

	public BigDecimal getBailmoney() {
		return bailmoney;
	}

	public void setBailmoney(BigDecimal bailmoney) {
		this.bailmoney = bailmoney;
	}

	public BigDecimal getBailrate() {
		return bailrate;
	}

	public void setBailrate(BigDecimal bailrate) {
		this.bailrate = bailrate;
	}

	public String getBailAcc() {
		return bailAcc;
	}

	public void setBailAcc(String bailAcc) {
		this.bailAcc = bailAcc;
	}

	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public BigDecimal getFeemoney() {
		return feemoney;
	}

	public void setFeemoney(BigDecimal feemoney) {
		this.feemoney = feemoney;
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
		return "ErpAbEmitBillDTO{" +
				"corpId='" + corpId + '\'' +
				", origNo='" + origNo + '\'' +
				", billMoney=" + billMoney +
				", payer='" + payer + '\'' +
				", payeracc='" + payeracc + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", payerbankno='" + payerbankno + '\'' +
				", payee='" + payee + '\'' +
				", payeeacc='" + payeeacc + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payeebankno='" + payeebankno + '\'' +
				", acceptoracc='" + acceptoracc + '\'' +
				", acceptorBankName='" + acceptorBankName + '\'' +
				", acceptorbankno='" + acceptorbankno + '\'' +
				", acceptorbankaddress='" + acceptorbankaddress + '\'' +
				", insideSing='" + insideSing + '\'' +
				", cnAccrecords='" + cnAccrecords + '\'' +
				", businessDate=" + businessDate +
				", billType=" + billType +
				", billFlag=" + billFlag +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", accptncagrmtnb='" + accptncagrmtnb + '\'' +
				", acontractNo='" + acontractNo + '\'' +
				", banendrsmtmk='" + banendrsmtmk + '\'' +
				", payerCdtRatgs='" + payerCdtRatgs + '\'' +
				", payerCdtRatgGgcy='" + payerCdtRatgGgcy + '\'' +
				", payerCdtRatgDuedt=" + payerCdtRatgDuedt +
				", ainvcnb='" + ainvcnb + '\'' +
				", bailpercent=" + bailpercent +
				", bailmoney=" + bailmoney +
				", bailrate=" + bailrate +
				", bailAcc='" + bailAcc + '\'' +
				", voucherId='" + voucherId + '\'' +
				", feemoney=" + feemoney +
				", abs='" + abs + '\'' +
				", rmk='" + rmk + '\'' +
				'}';
	}
}
