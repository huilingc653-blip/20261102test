package com.bytter.biz.report.vo;
import com.bytter.poi.annotation.Excel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 下拨金额分析表（报表）
 *
 * @author wangzx
 * @date 2017-1-23
 */
public class DownAllocationAnalysisListVO implements Serializable {
	/**分组标识*/
	private String groupKey;
	/** 指令编号*/
	@Excel(name ="指令编号")
	private String billNo;
	/**付款单号*/
	@Excel(name="付款单号")
	private String cbsBillNo;
	/**付款单ID*/
	private String cbsBillId;
	/**网点ID*/
	private String netId;
	/**下拨单位ID*/
	private String corpId;
	/**下拨单位代码*/
	private String corpCode;
	/**下拨单位名称*/
	@Excel(name ="下拨单位")
	private String corpName;
	/**付款账号*/
	@Excel(name ="付款账号")
	private String payerAcc;
	/**付款人*/
	private String payerAccName;
	/**付款开户行*/
	private String payerBank;
	/**收款账号*/
	@Excel(name ="收款账号")
	private String payeeAcc;
	/**收款人*/
	@Excel(name ="付款人")
	private String payeeAccName;
	/**收款开户行*/
	@Excel(name ="收款开户行")
	private String payeeBank;
	/**币别*/
	private String curId;
	@Excel(name ="币别")
	private String curName;
	/**金额*/
	@Excel(name ="金额",suffix=(".00"))
	private BigDecimal payMoney;
	/**用途*/
	@Excel(name ="用途")
	private String purpose;
	/**申请日期*/
	private Date createDate;
	/**付款方式*/
	private String voucherType;
	/**付款方式名称*/
	@Excel(name ="结算类型")
	private String voucherName;
	/**状态*/
	@Excel(name ="状态")
	private Integer status;
	private String statusStr;
	/**银行名称*/
	private String bankName;
	/**余额*/
	private String bal;
	/**银行列表*/
	private List<Map<String,Object>> bankList = new ArrayList<Map<String, Object>>();
	/**个数*/
	private Integer count;
	/**期望支付日期*/
	private Date wishPayDay;
	/**创建人登录名称*/
	private String 	createBy;
	/**加急标志*/
	private String isQuick;
	/**行数*/
	private Integer rowSize;
	/**合计*/
	private BigDecimal total;
	/**指令来源*/
	private String voucherFrom;
	
	public String getCbsBillId() {
		return cbsBillId;
	}
	public void setCbsBillId(String cbsBillId) {
		this.cbsBillId = cbsBillId;
	}
	public String getCbsBillNo() {
		return cbsBillNo;
	}
	public void setCbsBillNo(String cbsBillNo) {
		this.cbsBillNo = cbsBillNo;
	}
	public String getVoucherFrom() {
		return voucherFrom;
	}
	public void setVoucherFrom(String voucherFrom) {
		this.voucherFrom = voucherFrom;
	}
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
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
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
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
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBal() {
		return bal;
	}
	public void setBal(String bal) {
		this.bal = bal;
	}
	public List<Map<String, Object>> getBankList() {
		return bankList;
	}
	public void setBankList(List<Map<String, Object>> bankList) {
		this.bankList = bankList;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getWishPayDay() {
		return wishPayDay;
	}
	public void setWishPayDay(Date wishPayDay) {
		this.wishPayDay = wishPayDay;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getIsQuick() {
		return isQuick;
	}
	public void setIsQuick(String isQuick) {
		this.isQuick = isQuick;
	}
	public Integer getRowSize() {
		return rowSize;
	}
	public void setRowSize(Integer rowSize) {
		this.rowSize = rowSize;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
