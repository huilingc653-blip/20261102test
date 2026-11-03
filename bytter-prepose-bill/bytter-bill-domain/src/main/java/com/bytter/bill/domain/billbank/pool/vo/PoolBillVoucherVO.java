package com.bytter.bill.domain.billbank.pool.vo;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 出票融资申请合同信息
 * 
 */
public class PoolBillVoucherVO extends PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 出入池方式：1质押2托管 */
	private String inoutType;
	
	/** 托管质押转换方式：01-质押转托管 02-托管转质押 */
	private String pdTstType;
	/** 票据号 */
	private String origNo;
	
	/** 票据类型 bill_type number */
	private Integer billType;
	/** 不得转让标记 banendrsmtmk varchar2(8)不得转让标记EM00可再转让EM01不得转让 */
	private String banendrsmtmk="EM00";
	/** 流通介质 bill_flag number */
	private Integer billFlag;
	/** 票据金额 bill_money number(15,2) */
	private BigDecimal billMoney;
	
	/** 票据融资额度 bill_money number(15,2) */
	private BigDecimal limitMoney;
	/** 出票日期 start_date date */
	private Date startDate;
	/** 到期日期 end_date date */
	private Date endDate;
	/** 出票人 payer varchar2(240) */
	private String payer;
	/** 出票人类型 payerType char(4)*/
	private String payerType;
	/** 出票人组织机构代码 char(20)*/
	private String payerCode;
	/** 出票人账号 payeracc varchar2(30) */
	private String payeracc;
	/** 出票人开户银行名称 payer_bank_name varchar2(240) */
	private String payerBankName;
	/** 出票人开户银行行号 payerbankno varchar2(30) */
	private String payerbankno;
	/** 收票人 payee varchar2(240) 240 */
	private String payee;
	/** 收票人类型 payeeType  char(4) */
	private String payeeType;
	/** 收票人账号 payeeacc varchar2(30) */
	private String payeeacc;
	/** 收票人组织机构代码 char(20) */
	private String payeeCode;
	/** 收票人开户银行名称 payee_bank_name varchar2(240) */
	private String payeeBankName;
	/** 收票人开户银行行号 payeebankno varchar2(30) */
	private String payeebankno;
	/** 承兑人 acceptor varchar2(240) */
	private String acceptor;
	/** 承兑人类型 accpType char(4)*/
	private String accpType;
	/** 承兑人账号 acceptoracc varchar2(30) */
	private String acceptoracc;
	/** 承兑人组织机构代码 char(20)*/
	private String acceptorCode;
	/** 承兑人开户行行号 acceptorbankno varchar2(30) */
	private String acceptorbankno;
	/** 承兑人开户行名称 acceptor_bank_name varchar2(240) */
	private String acceptorBankName;
	/**
	 * 备注
	 */
	private String rmk;
	
	/**
	 * 申请人名称
	 */
	private String opName;
	/**
	 * 交易日期
	 */
	private Date tradeDate;
	
	/** 重复入池标记空或0为否1为是 */
	private String isInRepeat;
	
	/** 入池次数 */
	private Integer inPoolSize;
	
	/**票据池明细表业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；8032入池撤销;001出票申请；010背书申请；011贴现申请；020提示付款*/
	private String briefId;
	/**清算方式：SM00 线上清算、SM01 线下清算*/
	private String sttlmmk="SM01";
	/**提示付款逾期原因*/
	private String rsn;
	
	/**票据状态*/
	private String billStatus;
	/**
	 * 票据ID编码
	 */
	private String billIdCode;
	/**
	 * 出质人类型 char(4)
	 */
	private String plgrTyp;
	/**
	 * 出质人名称 varchar(122)
	 */
	private String plgrNam;
	/**
	 * 出质人组织机构代码 char(20)
	 */
	private String plgrCod;
	/**
	 * 出质人账号 varchar(32)
	 */
	private String plgrAccNo;
	/**
	 * 出质人开户行行号 varchar(20)
	 */
	private String plgrBnm;
	/**
	 * 质权人类型  char(4)
	 */
	private String plgeTyp;
	/**
	 * 质权人名称 varchar(122)
	 */
	private String plgeNam;
	/**
	 * 质权人机构号 char(6)
	 */
	private String plgeInstID;
	/**
	 * 质权人组织机构代码 char(20)
	 */
	private String plgeCod;
	/**
	 * 质权人账号 varchar(32)
	 */
	private String plgeAccNo;
	/**
	 * 质权人开户行行号 varchar(20)
	 */
	private String plgeBnm;
	/**
	 *-票据质押价值 decimal(15,2)
	 */
	private String billPlgVal;
	/**
	 * 占用同业额度标识 char(1)
	 */
	private String ocpSmidyLmtFlag;
	/**
	 * 最高额质押合同编号 varchar(90)
	 */
	private String highLmtPlgCtrID;
	/**
	 * 最高额质押流水号 varchar(36)
	 */
	private String highLmtPlgSrl;
	/**
	 * -出入池状态 char(2)
	 */
	private String poolSttOfIO;
	/**
	 * 电子票据防重标识 char(1) 0:关闭,1:开启
	 */
	private String avdRepeatFlag;
	/**
	 * 是否允许客户自助出池票据 char(1) 1:是,0:否
	 */
	private String isAlwCstOutPool;
	
	/**
	 * 入池申请日期
	 */
	private Date intoApplyDate;
	
	/**
	 * 质押转托管确认状态:-1质押转托管申请失败、 0质押转托管确认失败、1质押转托管确认在途
	 */
	private String poolPledgeStatus;
	
	public String getInoutType() {
		return inoutType;
	}
	public void setInoutType(String inoutType) {
		this.inoutType = inoutType;
	}
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
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
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getPdTstType() {
		return pdTstType;
	}
	public void setPdTstType(String pdTstType) {
		//System.out.println("==============设置setPdTstType="+this.getPdTstType());
		this.pdTstType = pdTstType;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	/**票据池明细表业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款*/
	public String getBriefId() {
		return briefId;
	}
	public String getIsInRepeat() {
		return isInRepeat;
	}
	public void setIsInRepeat(String isInRepeat) {
		this.isInRepeat = isInRepeat;
	}
	public Integer getInPoolSize() {
		return inPoolSize;
	}
	public void setInPoolSize(Integer inPoolSize) {
		if(inPoolSize!=null&&inPoolSize>0){
			setIsInRepeat("1");
		}
		this.inPoolSize = inPoolSize;
	}
	public String getPayerType() {
		return payerType;
	}
	public void setPayerType(String payerType) {
		this.payerType = payerType;
	}
	public String getPayerCode() {
		return payerCode;
	}
	public void setPayerCode(String payerCode) {
		this.payerCode = payerCode;
	}
	public String getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}
	public String getPayeeCode() {
		return payeeCode;
	}
	public void setPayeeCode(String payeeCode) {
		this.payeeCode = payeeCode;
	}
	public String getAccpType() {
		return accpType;
	}
	public void setAccpType(String accpType) {
		this.accpType = accpType;
	}
	public String getAcceptorCode() {
		return acceptorCode;
	}
	public void setAcceptorCode(String acceptorCode) {
		this.acceptorCode = acceptorCode;
	}
	public String getPlgrTyp() {
		return plgrTyp;
	}
	public void setPlgrTyp(String plgrTyp) {
		this.plgrTyp = plgrTyp;
	}
	public String getPlgrNam() {
		return plgrNam;
	}
	public void setPlgrNam(String plgrNam) {
		this.plgrNam = plgrNam;
	}
	public String getPlgrCod() {
		return plgrCod;
	}
	public void setPlgrCod(String plgrCod) {
		this.plgrCod = plgrCod;
	}
	public String getPlgrAccNo() {
		return plgrAccNo;
	}
	public void setPlgrAccNo(String plgrAccNo) {
		this.plgrAccNo = plgrAccNo;
	}
	public String getPlgrBnm() {
		return plgrBnm;
	}
	public void setPlgrBnm(String plgrBnm) {
		this.plgrBnm = plgrBnm;
	}
	public String getPlgeTyp() {
		return plgeTyp;
	}
	public void setPlgeTyp(String plgeTyp) {
		this.plgeTyp = plgeTyp;
	}
	public String getPlgeNam() {
		return plgeNam;
	}
	public void setPlgeNam(String plgeNam) {
		this.plgeNam = plgeNam;
	}
	public String getPlgeInstID() {
		return plgeInstID;
	}
	public void setPlgeInstID(String plgeInstID) {
		this.plgeInstID = plgeInstID;
	}
	public String getPlgeCod() {
		return plgeCod;
	}
	public void setPlgeCod(String plgeCod) {
		this.plgeCod = plgeCod;
	}
	public String getPlgeAccNo() {
		return plgeAccNo;
	}
	public void setPlgeAccNo(String plgeAccNo) {
		this.plgeAccNo = plgeAccNo;
	}
	public String getPlgeBnm() {
		return plgeBnm;
	}
	public void setPlgeBnm(String plgeBnm) {
		this.plgeBnm = plgeBnm;
	}
	public String getBillPlgVal() {
		return billPlgVal;
	}
	public void setBillPlgVal(String billPlgVal) {
		this.billPlgVal = billPlgVal;
	}
	public String getOcpSmidyLmtFlag() {
		return ocpSmidyLmtFlag;
	}
	public void setOcpSmidyLmtFlag(String ocpSmidyLmtFlag) {
		this.ocpSmidyLmtFlag = ocpSmidyLmtFlag;
	}
	public String getHighLmtPlgCtrID() {
		return highLmtPlgCtrID;
	}
	public void setHighLmtPlgCtrID(String highLmtPlgCtrID) {
		this.highLmtPlgCtrID = highLmtPlgCtrID;
	}
	public String getHighLmtPlgSrl() {
		return highLmtPlgSrl;
	}
	public void setHighLmtPlgSrl(String highLmtPlgSrl) {
		this.highLmtPlgSrl = highLmtPlgSrl;
	}
	public String getPoolSttOfIO() {
		return poolSttOfIO;
	}
	public void setPoolSttOfIO(String poolSttOfIO) {
		this.poolSttOfIO = poolSttOfIO;
	}
	public String getAvdRepeatFlag() {
		return avdRepeatFlag;
	}
	public void setAvdRepeatFlag(String avdRepeatFlag) {
		this.avdRepeatFlag = avdRepeatFlag;
	}
	public String getIsAlwCstOutPool() {
		return isAlwCstOutPool;
	}
	public void setIsAlwCstOutPool(String isAlwCstOutPool) {
		this.isAlwCstOutPool = isAlwCstOutPool;
	}
	public void setBriefId(String briefId) {
		//System.out.println("==============设置briefid="+briefId+"  PdTstType="+this.getPdTstType());
		if(StrUtil.isEmpty(this.getPdTstType())
				|| !BillPoolConstant.PDTSTTYPE_TO_HOSTING.equals(this.getPdTstType())
				|| !BillPoolConstant.PDTSTTYPE_TO_PLEDGE.equals(this.getPdTstType())){
			if(BillPoolConstant.BRIEF_TYPE_TO_HOSTING.equals(briefId)){
				this.setPdTstType(BillPoolConstant.PDTSTTYPE_TO_HOSTING);
				//System.out.println("==============设置1PdTstType="+this.getPdTstType());
			}else if(BillPoolConstant.BRIEF_TYPE_TO_PLEDGE.equals(briefId)){
				this.setPdTstType(BillPoolConstant.PDTSTTYPE_TO_PLEDGE);
				//System.out.println("==============设置2PdTstType="+this.getPdTstType());
			}
		}
		
		this.briefId = briefId;
	}
	public String getBillIdCode() {
		return billIdCode;
	}
	public void setBillIdCode(String billIdCode) {
		this.billIdCode = billIdCode;
	}
	public String getSttlmmk() {
		return sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}
	public String getRsn() {
		return rsn;
	}
	public void setRsn(String rsn) {
		this.rsn = rsn;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public BigDecimal getLimitMoney() {
		return limitMoney;
	}
	public void setLimitMoney(BigDecimal limitMoney) {
		this.limitMoney = limitMoney;
	}
	public Date getIntoApplyDate() {
		return intoApplyDate;
	}
	public void setIntoApplyDate(Date intoApplyDate) {
		this.intoApplyDate = intoApplyDate;
	}
	public String getPoolPledgeStatus() {
		return poolPledgeStatus;
	}
	public void setPoolPledgeStatus(String poolPledgeStatus) {
		this.poolPledgeStatus = poolPledgeStatus;
	}
	@Override
	public String toString() {
		return "PoolBillVoucherVO{" +
				"inoutType='" + inoutType + '\'' +
				", pdTstType='" + pdTstType + '\'' +
				", origNo='" + origNo + '\'' +
				", billType=" + billType +
				", banendrsmtmk='" + banendrsmtmk + '\'' +
				", billFlag=" + billFlag +
				", billMoney=" + billMoney +
				", limitMoney=" + limitMoney +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", payer='" + payer + '\'' +
				", payerType='" + payerType + '\'' +
				", payerCode='" + payerCode + '\'' +
				", payeracc='" + payeracc + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", payerbankno='" + payerbankno + '\'' +
				", payee='" + payee + '\'' +
				", payeeType='" + payeeType + '\'' +
				", payeeacc='" + payeeacc + '\'' +
				", payeeCode='" + payeeCode + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", payeebankno='" + payeebankno + '\'' +
				", acceptor='" + acceptor + '\'' +
				", accpType='" + accpType + '\'' +
				", acceptoracc='" + acceptoracc + '\'' +
				", acceptorCode='" + acceptorCode + '\'' +
				", acceptorbankno='" + acceptorbankno + '\'' +
				", acceptorBankName='" + acceptorBankName + '\'' +
				", rmk='" + rmk + '\'' +
				", opName='" + opName + '\'' +
				", tradeDate=" + tradeDate +
				", isInRepeat='" + isInRepeat + '\'' +
				", inPoolSize=" + inPoolSize +
				", briefId='" + briefId + '\'' +
				", sttlmmk='" + sttlmmk + '\'' +
				", rsn='" + rsn + '\'' +
				", billStatus='" + billStatus + '\'' +
				", billIdCode='" + billIdCode + '\'' +
				", plgrTyp='" + plgrTyp + '\'' +
				", plgrNam='" + plgrNam + '\'' +
				", plgrCod='" + plgrCod + '\'' +
				", plgrAccNo='" + plgrAccNo + '\'' +
				", plgrBnm='" + plgrBnm + '\'' +
				", plgeTyp='" + plgeTyp + '\'' +
				", plgeNam='" + plgeNam + '\'' +
				", plgeInstID='" + plgeInstID + '\'' +
				", plgeCod='" + plgeCod + '\'' +
				", plgeAccNo='" + plgeAccNo + '\'' +
				", plgeBnm='" + plgeBnm + '\'' +
				", billPlgVal='" + billPlgVal + '\'' +
				", ocpSmidyLmtFlag='" + ocpSmidyLmtFlag + '\'' +
				", highLmtPlgCtrID='" + highLmtPlgCtrID + '\'' +
				", highLmtPlgSrl='" + highLmtPlgSrl + '\'' +
				", poolSttOfIO='" + poolSttOfIO + '\'' +
				", avdRepeatFlag='" + avdRepeatFlag + '\'' +
				", isAlwCstOutPool=" + isAlwCstOutPool +
				", intoApplyDate=" + intoApplyDate +
				", poolPledgeStatus=" + poolPledgeStatus +
				'}';
	}
}
