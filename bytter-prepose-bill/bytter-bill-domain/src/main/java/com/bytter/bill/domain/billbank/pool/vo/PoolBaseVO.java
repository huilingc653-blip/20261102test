package com.bytter.bill.domain.billbank.pool.vo;
import java.io.Serializable;
/**
 * 票据池底层VO
 * 
 */
public class PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 申请业务ID
	 */
	private String busId;
	
	/**
	 * 单位ID
	 */
	private String corpId;
	/**
	 * 单位ID
	 */
	private String corpName;
	/**
	 * 票据池ID
	 */
	private String bpId;
	
	/**
	 * 票据池编号
	 */
	private String poolNo;
	
	/**
	 * 票据池名称
	 */
	private String poolName;
	
	/**
	 * 银行对于客户机构号
	 */
	private String bankOrgCode;
	
	/**
	 * 银行对于客户机构号
	 */
	private String bankOrgName;
	
	/**
	 * 银行接口代码
	 */
	private String bifCode;
	/**
	 * 银行系统ID
	 */
	private String bankId;
	/**
	 * 银行接口申请ID
	 */
	private String applyId;
	/**
	 * 指令状态(交易失败-1，指令待发送16，交易处理中18，交易成功19，交易打回-2)
	 */
	private String optStatus;
	/**
	 * 银行接口中间状态（银行接口需要）
	 */
	private String reCode;
	/**
	 * 银行接口返回信息
	 */
	private String reMsg;
	/**
	 * 票据池质权人账号
	 */
	private String pledgeeAcc;
	/**
	 * 票据池质权人名称
	 */
	private String pledgeeName;
	/**
	 * 票据池质权人银行行号
	 */
	private String pledgeeBankNo;
	/**
	 * 票据池质权人银行行名
	 */
	private String pledgeeBankName;
	
	/**
	 * 申请人账号
	 */
	private String reqAcc;
	/**
	 * 申请人名称
	 */
	private String reqName;
	/**
	 * 申请人银行行号
	 */
	private String reqBankNo;
	/**
	 * 申请人银行行名
	 */
	private String reqBankName;
	/**
	 * 池模式:1：集团票据池（统收统支）\ 2：集团票据池（自收自用）\3：单一票据池  默认为1
	 */
	private String bpMode;
	
	/**
	 * 外围系统流水号
	 */
	private String billId;
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getOptStatus() {
		return optStatus;
	}
	public void setOptStatus(String optStatus) {
		this.optStatus = optStatus;
	}
	public String getReCode() {
		return reCode;
	}
	public void setReCode(String reCode) {
		this.reCode = reCode;
	}
	public String getReMsg() {
		return reMsg;
	}
	public void setReMsg(String reMsg) {
		this.reMsg = reMsg;
	}
	public String getPoolNo() {
		return poolNo;
	}
	public void setPoolNo(String poolNo) {
		this.poolNo = poolNo;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getBankOrgCode() {
		return bankOrgCode;
	}
	public void setBankOrgCode(String bankOrgCode) {
		this.bankOrgCode = bankOrgCode;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public String getPledgeeAcc() {
		return pledgeeAcc;
	}
	public void setPledgeeAcc(String pledgeeAcc) {
		this.pledgeeAcc = pledgeeAcc;
	}
	public String getPledgeeName() {
		return pledgeeName;
	}
	public void setPledgeeName(String pledgeeName) {
		this.pledgeeName = pledgeeName;
	}
	public String getPledgeeBankNo() {
		return pledgeeBankNo;
	}
	public void setPledgeeBankNo(String pledgeeBankNo) {
		this.pledgeeBankNo = pledgeeBankNo;
	}
	public String getPledgeeBankName() {
		return pledgeeBankName;
	}
	public void setPledgeeBankName(String pledgeeBankName) {
		this.pledgeeBankName = pledgeeBankName;
	}
	public String getReqAcc() {
		return reqAcc;
	}
	public void setReqAcc(String reqAcc) {
		this.reqAcc = reqAcc;
	}
	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	public String getReqBankNo() {
		return reqBankNo;
	}
	public void setReqBankNo(String reqBankNo) {
		this.reqBankNo = reqBankNo;
	}
	public String getReqBankName() {
		return reqBankName;
	}
	public void setReqBankName(String reqBankName) {
		this.reqBankName = reqBankName;
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
	public String getBankOrgName() {
		return bankOrgName;
	}
	public void setBankOrgName(String bankOrgName) {
		this.bankOrgName = bankOrgName;
	}
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	public String getBpMode() {
		return bpMode;
	}
	public void setBpMode(String bpMode) {
		this.bpMode = bpMode;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	@Override
	public String toString() {
		return "PoolBaseVO [busId=" + busId + ", corpId=" + corpId
				+ ", corpName=" + corpName + ", bpId=" + bpId + ", poolNo="
				+ poolNo + ", poolName=" + poolName + ", bankOrgCode="
				+ bankOrgCode + ", bankOrgName=" + bankOrgName + ", bifCode="
				+ bifCode + ", bankId=" + bankId + ", applyId=" + applyId
				+ ", optStatus=" + optStatus + ", reCode=" + reCode
				+ ", reMsg=" + reMsg + ", pledgeeAcc=" + pledgeeAcc
				+ ", pledgeeName=" + pledgeeName + ", pledgeeBankNo="
				+ pledgeeBankNo + ", pledgeeBankName=" + pledgeeBankName
				+ ", reqAcc=" + reqAcc + ", reqName=" + reqName
				+ ", reqBankNo=" + reqBankNo + ", reqBankName=" + reqBankName
				+ ", bpMode=" + bpMode + ", billId=" + billId + "]";
	}
}
