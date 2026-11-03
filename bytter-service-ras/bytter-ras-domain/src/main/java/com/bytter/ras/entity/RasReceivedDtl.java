package com.bytter.ras.entity;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bytter.core.base.entity.IdEntity;


/**
 * 
* @Title:  实收数据明细entity
* @Package com.bytter.ras.entity   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午4:26:53   
* @version V1.0
 */
@Entity
@Table(name = "RAS_RECEIVED_DTL", schema = "")
public class RasReceivedDtl extends  IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/**门店ID*/
    private String shopId;
	/**交易流水号*/
	private String serialNo;
	/**实收订单号*/
	private String orderNo;
	/**交易日期*/
	private Date transDate;
	/**实收金额*/
	private BigDecimal money;
	/**优惠金额*/
	private BigDecimal yhMoney;
	/**交易金额*/
	private BigDecimal transMoney;
	/**缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它*/
	private String payType;
	/**客户名称*/
	private String custName;
	/**客户账号*/
	private String custAcc;
	/**收款账号*/
	private String payeeAcc;
	/**收款户名*/
	private String payeeAccName;
	/**收款账号开户行*/
	private String payeebankName;
	/**备注*/
	private String rmk;
	/**认领标记: 0待认领1已认领*/
	private Integer claimType;
	/**状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成0待取认-2作废*/
	private Integer status;  
	/**合计标记：1为是0和空为否*/
	private Integer isSum;
	/**稽核ID**/
	private String auditId;       
	/**创建人名称*/
    private String  createName;
    /**创建人登录名称*/
    private String  createBy;   
    /**创建日期*/
    private Date  createDate;   
    /**更新人名称*/
    private String  updateName;   
    /**更新人登录名称*/
    private String  updateBy;     
    /**更新日期*/
    private Date  updateDate;
	/**数据来源*/
	private String  dateSource;
    /**用来做线程锁定*/
    private String unId;  
    /**YYYYMMDD的日期格式字符串 (预留字段* 时间格式)*/
    private String strTransDate;    
    
	@Column(name ="SHOP_ID",nullable=true,length=32)
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	@Column(name ="SERIAL_NO",nullable=true,length=120)
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	@Column(name ="ORDER_NO",nullable=true,length=120)
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@Column(name ="TRANS_DATE",nullable=true)
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	@Column(name ="MONEY",nullable=true)
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	@Column(name ="YH_MONEY",nullable=true)
	public BigDecimal getYhMoney() {
		return yhMoney;
	}
	public void setYhMoney(BigDecimal yhMoney) {
		this.yhMoney = yhMoney;
	}
	@Column(name ="TRANS_MONEY",nullable=true)
	public BigDecimal getTransMoney() {
		return transMoney;
	}
	public void setTransMoney(BigDecimal transMoney) {
		this.transMoney = transMoney;
	}
	@Column(name ="PAY_TYPE",nullable=true,length=10)
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name ="CUST_NAME",nullable=true,length=500)
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	@Column(name ="CUST_ACC",nullable=true,length=500)
	public String getCustAcc() {
		return custAcc;
	}
	public void setCustAcc(String custAcc) {
		this.custAcc = custAcc;
	}
	@Column(name ="PAYEE_ACC",nullable=true,length=500)
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=500)
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	@Column(name ="PAYEE_BANK_NAME",nullable=true,length=500)
	public String getPayeebankName() {
		return payeebankName;
	}
	public void setPayeebankName(String payeebankName) {
		this.payeebankName = payeebankName;
	}
	@Column(name ="RMK",nullable=true,length=500)
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name ="CLAIM_TYPE",nullable=true,length=6)
	public Integer getClaimType() {
		return claimType;
	}
	public void setClaimType(Integer claimType) {
		this.claimType = claimType;
	}
	@Column(name ="STATUS",nullable=true,length=6)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name ="AUDIT_ID",nullable=true,length=32)
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name ="UPDATE_DATE",nullable=true)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name ="DATE_SOURCE",nullable=true)
	public String getDateSource() {
		return dateSource;
	}

	public void setDateSource(String dateSource) {
		this.dateSource = dateSource;
	}
	@Column(name ="IS_SUM")
	public Integer getIsSum() {
		return isSum;
	}
	public void setIsSum(Integer isSum) {
		this.isSum = isSum;
	}
	@Column(name ="UN_ID")
	public String getUnId() {
		return unId;
	}
	public void setUnId(String unId) {
		this.unId = unId;
	}
	@Column(name ="STR_TRANS_DATE",nullable=true,length=8)
	public String getStrTransDate() {
		return strTransDate;
	}
	public void setStrTransDate(String strTransDate) {
		this.strTransDate = strTransDate;
	}

}
