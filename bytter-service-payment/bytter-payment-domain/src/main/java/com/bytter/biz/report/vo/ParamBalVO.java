package com.bytter.biz.report.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author zhuop
 * @Description 内部、银行账户余额对比分析情况
 * @date 2018.6.8
 */
public class ParamBalVO implements Serializable,Comparable<ParamBalVO>{
	/** 网点ID */
	private String netId;
	/** 网点名称 */
	private String netName;
	/** 单位ID */
	private String corpId;
	/** 单位名称 */
	private String corpName;
	/** 币别ID */
	private String curId;
	/** 币别名称 */
	private String curName;
	/** 内账账号 */
	private String accountAcc;
	/** 内部账号名称 */
	private String accountName;
	/** 内部账号余额 */
	private BigDecimal balance;
	/** 内部账号冻结金额 */
	private BigDecimal freezeMoney;
	/** 内部账号可用余额 */
	private BigDecimal useMoney;
	/** 银行账号余额详情 */
	private List<InternalAndBankAccBalVO>  internal;
	/** 合并条数 */
	private Integer num;
	/** 合计金额 */
	private BigDecimal sumMoney;
	/** 是否在线  0-离线 1-在线 */
	private String isOnline;
	/**
	 * 银行账户余额
	 */
	private BigDecimal bal;
	/**
	 * 银行账户冻结金额
	 */
	private BigDecimal frzBal;
	/**
	 * 银行账户可用余额
	 */
	private BigDecimal useBal;
	@Override
	public boolean equals(Object obj){
		if(this==obj){ 
			System.out.println("this==obj"+(this==obj));
			return true ;    
		}    
		if(!(obj instanceof ParamBalVO)){ 
			System.out.println("!(obj instanceof ParamBalVO)"+(!(obj instanceof ParamBalVO)));
			return false ;    
		}    
		ParamBalVO param=(ParamBalVO)obj;
		return this.corpId.equals(param.corpId) && this.curId.equals(param.curId) && this.accountAcc.equals(param.accountAcc);
	}
	
	@Override
	public int hashCode(){
		String str=corpId + curId + accountAcc;
		return str.hashCode();
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
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
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getAccountAcc() {
		return accountAcc;
	}
	public void setAccountAcc(String accountAcc) {
		this.accountAcc = accountAcc;
	}
	public List<InternalAndBankAccBalVO> getInternal() {
		return internal;
	}
	public void setInternal(List<InternalAndBankAccBalVO> internal) {
		this.internal = internal;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public BigDecimal getUseMoney() {
		return useMoney;
	}
	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}
	public BigDecimal getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	public BigDecimal getFrzBal() {
		return frzBal;
	}
	public void setFrzBal(BigDecimal frzBal) {
		this.frzBal = frzBal;
	}
	public BigDecimal getUseBal() {
		return useBal;
	}
	public void setUseBal(BigDecimal useBal) {
		this.useBal = useBal;
	}
	@Override
	public int compareTo(ParamBalVO o) {
		if(o.getAccountAcc()!=null && this.getAccountAcc() !=null){
			Integer num=o.getAccountAcc().compareTo(this.getAccountAcc());
			Integer num2=num==0?o.getIsOnline().compareTo(this.getIsOnline()):num;
			return num2;
		}else{
			return -1;
		}
		
	}
}
