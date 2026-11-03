package com.bytter.biz.budget.spi.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.bytter.core.bpm.IBusinessKey;
/**   
 * @Title: Entity
 * @Description: 代发工资VO
 * @author onlineGenerator
 * @date 2017-10-31 15:20:33
 * @version 1.0
 *
 */
public class NisWageVO  implements Serializable,IBusinessKey{
	
	private String id;
	/**单据号码*/
	private String wageNo;
	/**工资册标题*/
	private String wageTitle;
	/**单位*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	private String payerAcc;
	/**付款账号名称*/
	private String payerAccName;
	/**付款开户行*/
	private String payerBank;
	/**付款开户行行号*/
	private String payerBankCode;
	/**账号所在省*/
	private String payerProv;
	/**账号所在市*/
	private String payerCity;
	/**币别*/
	private String curId;
	/**币别名称*/
	private String curName;
	/**接口代码*/
	private String bifCode;
	/**金额*/
	private BigDecimal wages;
	/**工资发放日期*/
	private Date wageDay;
	/**期望支付日期*/
	private Date wishPayday;
	/**附言*/
	private String addWord;
	/**状态*/
	private Integer status;
	/**交易状态*/
	private String voucherStat;
	/**附件ID*/
	private String attachmentId;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**业务类别id*/
	private String busTypeId;
	/**业务类别*/
	private String busType;
	/**业务类别编码*/
	private String busCode;
	/**业务类别名称*/
	private String busTypeName;
	/**erp中间表ID*/
	private String erpId;
	
	/**明细*/
    private List<NisWageDetailVO> nisWageDetailList;
	
	/**计划科目*/
	private String[] itemCode;
	/**计划科目名称*/
	private String[] itemName;
	/**执行金额*/
	private BigDecimal[] processMoney;
	/**汇率*/
	private BigDecimal[] exchangeRate;
	/**执行金额（折合本身币别）*/
	private BigDecimal[] processCurMoney;
	private String itemCodeImp;
	/**是否挂计划*/
	private String isBudget;
	private String bankTypeId;
	private String bankTypeName;
	private String bankPrefix;
	private String bankNamePrefix;
	/**成功总笔数*/
	private Integer successNum;
	/**失败总笔数*/
	private Integer failNum;
	/**待确认总笔数*/
	private Integer waitNum;
	/**成功总金额*/
	private BigDecimal successWages;
	/**失败总金额*/
	private BigDecimal failWages;
	/**待确认总金额*/
	private BigDecimal waitWages;
	/**账户余额*/
	private BigDecimal balance;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWageNo() {
		return wageNo;
	}
	public void setWageNo(String wageNo) {
		this.wageNo = wageNo;
	}
	public String getWageTitle() {
		return wageTitle;
	}
	public void setWageTitle(String wageTitle) {
		this.wageTitle = wageTitle;
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
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerAccName() {
		return payerAccName;
	}
	public void setPayerAccName(String payerAccName) {
		this.payerAccName = payerAccName;
	}
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
	}
	public String getPayerBankCode() {
		return payerBankCode;
	}
	public void setPayerBankCode(String payerBankCode) {
		this.payerBankCode = payerBankCode;
	}
	public String getPayerProv() {
		return payerProv;
	}
	public void setPayerProv(String payerProv) {
		this.payerProv = payerProv;
	}
	public String getPayerCity() {
		return payerCity;
	}
	public void setPayerCity(String payerCity) {
		this.payerCity = payerCity;
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
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public BigDecimal getWages() {
		return wages;
	}
	public void setWages(BigDecimal wages) {
		this.wages = wages;
	}
	public Date getWageDay() {
		return wageDay;
	}
	public void setWageDay(Date wageDay) {
		this.wageDay = wageDay;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public List<NisWageDetailVO> getNisWageDetailList() {
		return nisWageDetailList;
	}
	public void setNisWageDetailList(List<NisWageDetailVO> nisWageDetailList) {
		this.nisWageDetailList = nisWageDetailList;
	}
	
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public String[] getItemCode() {
		return itemCode;
	}
	public void setItemCode(String[] itemCode) {
		this.itemCode = itemCode;
	}
	public String[] getItemName() {
		return itemName;
	}
	public void setItemName(String[] itemName) {
		this.itemName = itemName;
	}
	public BigDecimal[] getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(BigDecimal[] processMoney) {
		this.processMoney = processMoney;
	}
	public BigDecimal[] getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal[] exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal[] getProcessCurMoney() {
		return processCurMoney;
	}
	public void setProcessCurMoney(BigDecimal[] processCurMoney) {
		this.processCurMoney = processCurMoney;
	}
	public String getBusTypeId() {
		return busTypeId;
	}
	public void setBusTypeId(String busTypeId) {
		this.busTypeId = busTypeId;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}
	public String getErpId() {
		return erpId;
	}
	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	public String getBankPrefix() {
		return bankPrefix;
	}
	public void setBankPrefix(String bankPrefix) {
		this.bankPrefix = bankPrefix;
	}
	public String getBankNamePrefix() {
		return bankNamePrefix;
	}
	public void setBankNamePrefix(String bankNamePrefix) {
		this.bankNamePrefix = bankNamePrefix;
	}
	public Integer getSuccessNum() {
		return successNum;
	}
	public void setSuccessNum(Integer successNum) {
		this.successNum = successNum;
	}
	public Integer getFailNum() {
		return failNum;
	}
	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}
	public BigDecimal getSuccessWages() {
		return successWages;
	}
	public void setSuccessWages(BigDecimal successWages) {
		this.successWages = successWages;
	}
	public BigDecimal getFailWages() {
		return failWages;
	}
	public void setFailWages(BigDecimal failWages) {
		this.failWages = failWages;
	}
	public Integer getWaitNum() {
		return waitNum;
	}
	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}
	public BigDecimal getWaitWages() {
		return waitWages;
	}
	public void setWaitWages(BigDecimal waitWages) {
		this.waitWages = waitWages;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getItemCodeImp() {
		return itemCodeImp;
	}
	public void setItemCodeImp(String itemCodeImp) {
		this.itemCodeImp = itemCodeImp;
	}
	public String getIsBudget() {
		return isBudget;
	}
	public void setIsBudget(String isBudget) {
		this.isBudget = isBudget;
	}
}
