package com.bytter.biz.finacial.report.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class BalanceQueryVo implements java.io.Serializable {
	/**
	 * 账号
	 */
	private String account;
	/**网点**/
	private java.lang.String netId;
	/**网点名称**/
	private java.lang.String netName;
	/**单位**/
	private java.lang.String corpId;
	/**单位名称**/
	private java.lang.String corpName;
	/**账户状态**/
	private java.lang.String accState;
	/**开户种类**/
	private java.lang.String accType;
	/**科目名称**/
	private java.lang.String subjectId;
	/**科目名称**/
	private java.lang.String subjectName;
	/**账号**/
	private java.lang.String blnctrAcc;
	/**账户名称**/
	private java.lang.String accName;
	/**币别**/
	private java.lang.String curId;
	private String curName;
	/**借贷方向**/
	private java.lang.Integer balanceWay;
	private java.lang.String balanceName;
	/**冻结金额**/
	private BigDecimal freezeMoney;
	/**记账余额**/
	private BigDecimal onlineBalance;
	/**复核余额**/
	private BigDecimal checkBalance;
	/**未复核金额**/
	private BigDecimal noCheckBalance;
	/**记账笔数**/
	private java.lang.Integer tallyCnt;
	/**复核笔数**/
	private java.lang.Integer checkCnt;
	/**维度**/
	private java.lang.String sumType;
	/**银行类型**/
	private java.lang.String bankType;
	/**银行类型名称**/
	private java.lang.String typeName;
	/**小计**/
	private java.lang.String isSum;
	/**总计**/
	private java.lang.String isTotal;
	/**是否有上级**/
	private java.lang.String hasFather;
	/**查询日期*/
	private Date gedCloseDate;
	private Date ledCloseDate;
	/**昨日余额**/
	private BigDecimal yesterBalance;
	/**借方发生额**/
	private BigDecimal debitMoney;
	/**贷方发生额**/
	private BigDecimal creditMoney;
	/**期末余额**/
	private BigDecimal balance;
	private String parentId;
	private String subjectCode;
	private String fatherId;
	private List<BalanceQueryVo> subBalBeans;
	public java.lang.String getBankType() {
		return bankType;
	}
	public void setBankType(java.lang.String bankType) {
		this.bankType = bankType;
	}
	public java.lang.String getAccState() {
		return accState;
	}
	public void setAccState(java.lang.String accState) {
		this.accState = accState;
	}
	public java.lang.String getAccType() {
		return accType;
	}
	public void setAccType(java.lang.String accType) {
		this.accType = accType;
	}
	public java.lang.String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(java.lang.String subjectCode) {
		this.subjectId = subjectCode;
	}
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.String getBlnctrAcc() {
		return blnctrAcc;
	}
	public void setBlnctrAcc(java.lang.String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public java.lang.String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	public java.lang.Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(java.lang.Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public java.lang.Integer getTallyCnt() {
		if(tallyCnt == null){
			return new Integer(0);
		}
		return tallyCnt;
	}
	public void setTallyCnt(java.lang.Integer tallyCnt) {
		this.tallyCnt = tallyCnt;
	}
	public java.lang.Integer getCheckCnt() {
		if(checkCnt == null){
			return new Integer(0);
		}
		return checkCnt;
	}
	public void setCheckCnt(java.lang.Integer checkCnt) {
		this.checkCnt = checkCnt;
	}
	public java.lang.String getSumType() {
		return sumType;
	}
	public void setSumType(java.lang.String sumType) {
		this.sumType = sumType;
	}
	public java.lang.String getNetName() {
		return netName;
	}
	public void setNetName(java.lang.String netName) {
		this.netName = netName;
	}
	public List<BalanceQueryVo> getSubBalBeans() {
		return subBalBeans;
	}
	public void setSubBalBeans(List<BalanceQueryVo> subBalBeans) {
		this.subBalBeans = subBalBeans;
	}
	public java.lang.String getTypeName() {
		return typeName;
	}
	public void setTypeName(java.lang.String typeName) {
		this.typeName = typeName;
	}
	public java.lang.String getIsSum() {
		return isSum;
	}
	public void setIsSum(java.lang.String isSum) {
		this.isSum = isSum;
	}
	public BigDecimal getFreezeMoney() {
		if(freezeMoney==null){
			return BigDecimal.ZERO;
		}
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public BigDecimal getOnlineBalance() {
		if(onlineBalance==null){
			return BigDecimal.ZERO;
		}
		return onlineBalance;
	}
	public void setOnlineBalance(BigDecimal onlineBalance) {
		this.onlineBalance = onlineBalance;
	}
	public BigDecimal getCheckBalance() {
		if(checkBalance==null){
			return BigDecimal.ZERO;
		}
		return checkBalance;
	}
	public void setCheckBalance(BigDecimal checkBalance) {
		this.checkBalance = checkBalance;
	}
	public BigDecimal getNoCheckBalance() {
		if(noCheckBalance==null){
			return BigDecimal.ZERO;
		}
		return noCheckBalance;
	}
	public void setNoCheckBalance(BigDecimal noCheckBalance) {
		this.noCheckBalance = noCheckBalance;
	}
	public java.lang.String getIsTotal() {
		return isTotal;
	}
	public void setIsTotal(java.lang.String isTotal) {
		this.isTotal = isTotal;
	}
	public java.lang.String getHasFather() {
		return hasFather;
	}
	public void setHasFather(java.lang.String hasFather) {
		this.hasFather = hasFather;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getBalanceName() {
		return balanceName;
	}
	public void setBalanceName(java.lang.String balanceName) {
		this.balanceName = balanceName;
	}
	public Date getGedCloseDate() {
		return gedCloseDate;
	}
	public void setGedCloseDate(Date gedCloseDate) {
		this.gedCloseDate = gedCloseDate;
	}
	public Date getLedCloseDate() {
		return ledCloseDate;
	}
	public void setLedCloseDate(Date ledCloseDate) {
		this.ledCloseDate = ledCloseDate;
	}
	public BigDecimal getYesterBalance() {
		if(yesterBalance == null){
			return BigDecimal.ZERO;
		}
		return yesterBalance;
	}
	public void setYesterBalance(BigDecimal yesterBalance) {
		this.yesterBalance = yesterBalance;
	}
	public BigDecimal getDebitMoney() {
		if(debitMoney == null){
			return BigDecimal.ZERO;
		}
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		if(creditMoney == null){
			return BigDecimal.ZERO;
		}
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public BigDecimal getBalance() {
		if(balance == null){
			return BigDecimal.ZERO;
		}
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getFatherId() {
		return fatherId;
	}
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
}
