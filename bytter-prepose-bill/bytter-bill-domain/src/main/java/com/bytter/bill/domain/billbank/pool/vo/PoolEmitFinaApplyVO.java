package com.bytter.bill.domain.billbank.pool.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 出票融资申请信息
 * 
 */
public class PoolEmitFinaApplyVO extends PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 申请业务ID
	 */
	private String busId;
	/**
	 * 票据池明细ID
	 */
	private String pdId;
	
	/**
	 * 融资申请业务编号
	 */
	private String busAppNo;
	
	/**
     * 客户标识号
     */
    private String custId;
	/** 票据类型 bill_type number */
	private Integer billType;
	/** 不得转让标记 banendrsmtmk varchar2(8) */
	private String banendrsmtmk;
	/** 流通介质 bill_flag number */
	private Integer billFlag;
	/** 票据金额 bill_money number(15,2) */
	private BigDecimal billMoney;
	/** 出票日期 start_date date */
	private Date startDate;
	/** 到期日期 end_date date */
	private Date endDate;
	/** 出票人 payer varchar2(240) */
	private String payer;
	/** 出票人账号 payeracc varchar2(30) */
	private String payeracc;
	/** 出票人开户银行名称 payer_bank_name varchar2(240) */
	private String payerBankName;
	/** 出票人开户银行行号 payerbankno varchar2(30) */
	private String payerbankno;
	/** 收票人 payee varchar2(240) 240 */
	private String payee;
	/** 收票人账号 payeeacc varchar2(30) */
	private String payeeacc;
	/** 收票人开户银行名称 payee_bank_name varchar2(240) */
	private String payeeBankName;
	/** 收票人开户银行行号 payeebankno varchar2(30) */
	private String payeebankno;
	/** 承兑人 acceptor varchar2(240) */
	private String acceptor;
	/** 承兑人账号 acceptoracc varchar2(30) */
	private String acceptoracc;
	/** 承兑人开户行行号 acceptorbankno varchar2(30) */
	private String acceptorbankno;
	/** 承兑人开户行名称 acceptor_bank_name varchar2(240) */
	private String acceptorBankName;
	/** 承兑合同号 a_contract_no varchar2(250) */
	private String acontractNo;
	/** 发票号 a_invcnb varchar2(250) 250 */
	private String ainvcnb;
	/**
	 * 摘要-融资用途
	 */
	private String abs;
	/**
	 * 备注
	 */
	private String rmk;
	
	/**
	 * 交易日期
	 */
	private Date tradeDate;
	
	/** 票据号 */
	private String origNo;
	
	/**
     *融资金额
     */
    private BigDecimal financeAmt;
    
    /**
     *申请日期
     */
    private Date applyDate;
    
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
     *货币种类 01：人民币
     */
    private String currency;
    /**
     *货币种类名称
     */
    private String currName;
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
     *退回原因
     */
    private String returnReason;
    
    /**
     *融资用途
     */
    private String finPurpose;
    /**
     *申请说明
     */
    private String applyDesc;
    
    /**
     * 出票融资申请详细数据
     */
    private List<AbPoolFinaApplyDtlVo> finaApplyDtlVo;
    
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getBusAppNo() {
		return busAppNo;
	}
	public void setBusAppNo(String busAppNo) {
		this.busAppNo = busAppNo;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}
	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
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
	public String getAcceptorbankno() {
		return acceptorbankno;
	}
	public void setAcceptorbankno(String acceptorbankno) {
		this.acceptorbankno = acceptorbankno;
	}
	public String getAcceptorBankName() {
		return acceptorBankName;
	}
	public void setAcceptorBankName(String acceptorBankName) {
		this.acceptorBankName = acceptorBankName;
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
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getPdId() {
		return pdId;
	}
	public void setPdId(String pdId) {
		this.pdId = pdId;
	}
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrName() {
		return currName;
	}
	public void setCurrName(String currName) {
		this.currName = currName;
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
	
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
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
	/**
     * 出票融资申请详细数据
     */
	public List<AbPoolFinaApplyDtlVo> getFinaApplyDtlVo() {
		return finaApplyDtlVo;
	}
	/**
     * 出票融资申请详细数据
     */
	public void setFinaApplyDtlVo(List<AbPoolFinaApplyDtlVo> finaApplyDtlVo) {
		this.finaApplyDtlVo = finaApplyDtlVo;
	}
	@Override
	public String toString() {
		return "PoolEmitFinaApplyVO [busId=" + busId + ", pdId=" + pdId
				+ ", busAppNo=" + busAppNo + ", custId=" + custId
				+ ", billType=" + billType + ", banendrsmtmk=" + banendrsmtmk
				+ ", billFlag=" + billFlag + ", billMoney=" + billMoney
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", payer=" + payer + ", payeracc=" + payeracc
				+ ", payerBankName=" + payerBankName + ", payerbankno="
				+ payerbankno + ", payee=" + payee + ", payeeacc=" + payeeacc
				+ ", payeeBankName=" + payeeBankName + ", payeebankno="
				+ payeebankno + ", acceptor=" + acceptor + ", acceptoracc="
				+ acceptoracc + ", acceptorbankno=" + acceptorbankno
				+ ", acceptorBankName=" + acceptorBankName + ", acontractNo="
				+ acontractNo + ", ainvcnb=" + ainvcnb + ", abs=" + abs
				+ ", rmk=" + rmk + ", tradeDate=" + tradeDate + ", origNo="
				+ origNo + ", financeAmt=" + financeAmt + ", applyDate="
				+ applyDate + ", applyStatus=" + applyStatus
				+ ", applyStatusName=" + applyStatusName + ", bizCode="
				+ bizCode + ", bizName=" + bizName + ", currency=" + currency
				+ ", currName=" + currName + ", businessType=" + businessType
				+ ", businessName=" + businessName + ", type=" + type
				+ ", srvrId=" + srvrId + ", returnReason=" + returnReason
				+ ", finPurpose=" + finPurpose + ", applyDesc=" + applyDesc
				+ ", finaApplyDtlVo=" + finaApplyDtlVo + "]";
	}
}
