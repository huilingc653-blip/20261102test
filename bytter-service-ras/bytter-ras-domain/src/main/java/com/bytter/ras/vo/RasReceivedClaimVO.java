package com.bytter.ras.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.ICurrentUser;
//import com.bytter.core.injections.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
* @Title:  实收稽核实收数据处理VO类
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: zhenghj
* @date:   2019-5-17
* @version V1.0
 */
public class RasReceivedClaimVO  implements Serializable{

	private String id;
	/**门店ID*/
	@BtCompositeSearch(fieldName = "rc.shop_id")
    private String shopId;
	/**门店名称*/
	private String shopName;
	/**交易流水号*/
	@BtCompositeSearch(fieldName = "rc.serial_No")
	private String serialNo;
	/**实收订单号*/
	@BtCompositeSearch(fieldName = "rc.order_No")
	private String orderNo;
	/**单位*/
	@BtCompositeSearch(fieldName = "c.id")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName="c.code")
	private String corpName;
	/**单位编码*/
	private String corpCode;
	/**交易日期*/
	@BtCompositeSearch(fieldName = "rc.TRANS_DATE")
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date transDate;
	private Date transDateStart;
	private Date transDateEnd;

	/**实收金额*/
	@BtCompositeSearch(fieldName = "rc.money")
	private BigDecimal money;
	/**优惠金额*/
	private BigDecimal yhMoney;
	/**交易金额*/
	@BtCompositeSearch(fieldName = "rc.trans_money")
	private BigDecimal transMoney;

	private BigDecimal endTransMoney;

	/**缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它*/
	@Dict(dictGroup = "RAS_PAY_TYPE",inject = "payTypeName")
	@BtCompositeSearch(fieldName = "rc.pay_type")
	private String payType;
	private String payTypeName;
	/**客户名称*/
	private String custName;
	/**客户账号*/
	private String custAcc;
	/**收款账号*/
	@BtCompositeSearch(fieldName = "rc.payee_acc")
	private String payeeAcc;
	/**收款户名*/
	private String payeeAccName;
	/**收款账号开户行*/
	private String payeeBankName;
	/**备注*/
	private String rmk;
	/**认领标记: 0待认领1已认领*/
	@Dict(dictGroup = "RAS_CLAIM_TYPE",inject = "claimTypeName")
	@BtCompositeSearch(fieldName = "rc.claim_type")
	private Integer claimType;
	private String claimTypeName;
	/**状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成0待取认-2作废*/
	@Dict(dictGroup = "RAS_AUDIT_STATUS",inject = "statusName")
	@BtCompositeSearch(fieldName = "rc.status")
	private Integer status;
	private String statusName;
	/**稽核ID**/
	private String auditId;
	/**数据来源*/
	@Dict(dictGroup = "RAS_AUDIT_DATASOURCE",inject = "dateSourceName")
	private String  dateSource;
	private String  dateSourceName;
	/**创建人名称*/
    private String  createName;
    /**创建人登录名称*/
    private String  createBy;
    /**创建日期*/
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date  createDate;

	private ICurrentUser user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getYhMoney() {
		return yhMoney;
	}

	public void setYhMoney(BigDecimal yhMoney) {
		this.yhMoney = yhMoney;
	}

	public BigDecimal getTransMoney() {
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustAcc() {
		return custAcc;
	}

	public void setCustAcc(String custAcc) {
		this.custAcc = custAcc;
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

	public String getPayeeBankName() {
		return payeeBankName;
	}

	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public Integer getClaimType() {
		return claimType;
	}

	public void setClaimType(Integer claimType) {
		this.claimType = claimType;
	}

	public String getClaimTypeName() {
		return claimTypeName;
	}

	public void setClaimTypeName(String claimTypeName) {
		this.claimTypeName = claimTypeName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getDateSource() {
		return dateSource;
	}

	public void setDateSource(String dateSource) {
		this.dateSource = dateSource;
	}

	public String getDateSourceName() {
		return dateSourceName;
	}

	public void setDateSourceName(String dateSourceName) {
		this.dateSourceName = dateSourceName;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
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

	public Date getTransDateStart() {
		return transDateStart;
	}

	public void setTransDateStart(Date transDateStart) {
		this.transDateStart = transDateStart;
	}

	public Date getTransDateEnd() {
		return transDateEnd;
	}

	public void setTransDateEnd(Date transDateEnd) {
		this.transDateEnd = transDateEnd;
	}

	public BigDecimal getEndTransMoney() {
		return endTransMoney;
	}

	public void setEndTransMoney(BigDecimal endTransMoney) {
		this.endTransMoney = endTransMoney;
	}

	public ICurrentUser getUser() {
		return user;
	}

	public void setUser(ICurrentUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "RasReceivedClaimVO{" +
				"id='" + id + '\'' +
				", shopId='" + shopId + '\'' +
				", shopName='" + shopName + '\'' +
				", serialNo='" + serialNo + '\'' +
				", orderNo='" + orderNo + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", corpCode='" + corpCode + '\'' +
				", transDate=" + transDate +
				", transDateStart=" + transDateStart +
				", transDateEnd=" + transDateEnd +
				", money=" + money +
				", yhMoney=" + yhMoney +
				", transMoney=" + transMoney +
				", endTransMoney=" + endTransMoney +
				", payType='" + payType + '\'' +
				", payTypeName='" + payTypeName + '\'' +
				", custName='" + custName + '\'' +
				", custAcc='" + custAcc + '\'' +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeAccName='" + payeeAccName + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", rmk='" + rmk + '\'' +
				", claimType=" + claimType +
				", claimTypeName='" + claimTypeName + '\'' +
				", status=" + status +
				", statusName='" + statusName + '\'' +
				", auditId='" + auditId + '\'' +
				", dateSource='" + dateSource + '\'' +
				", dateSourceName='" + dateSourceName + '\'' +
				", createName='" + createName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				'}';
	}
}
