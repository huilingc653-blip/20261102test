package com.bytter.ras.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
* @Title:  营收数据详细VO   
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午6:13:54   
* @version V1.0
 */
@ExcelTarget("RasRevenueDtlVO")
public class RasRevenueDtlVO extends ExcelModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**门店ID*/
	@BtCompositeSearch(fieldName = "nue.shop_id")
    private String shopId;
	/**门店名称*/
    @Excel(name = "*门店名称")
	@BtCompositeSearch(fieldName = "rs.name")
	private String shopName;
	/**交易流水号*/
    @Excel(name = "交易流水号")
	@BtCompositeSearch(fieldName = "nue.serial_no")
	private String serialNo;
	/**营收订单号*/
	@BtCompositeSearch(fieldName = "nue.ORDER_NO")
    @Excel(name = "营收订单号")
	private String orderNo;
	/**单位*/
	@BtCompositeSearch(fieldName = "rs.corp_id")
	private String corpId;
	/**单位代码*/
	private String corpCode;
	/**单位名称*/
	@BtCompositeSearch(fieldName = "sc.code")
	private String corpName;
	
	/**交易日期*/
	@BtCompositeSearch(fieldName = "nue.TRANS_DATE")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date transDate;
	
	@BtCompositeSearch(fieldName = "nue.TRANS_DATE")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date revenueTransDate;

    @Excel(name = "*交易日期",format = "yyyy-MM-dd HH:mm:ss",isTryTransferDate = true)
	@JsonFormat(pattern="yyyy-MM-dd")
    private Date transConvertDate;
    
	/**营收金额*/
	@BtCompositeSearch(fieldName = "nue.MONEY")
    @Excel(name = "*营收金额")
	private BigDecimal money;
	/**缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它*/
	@Dict(dictGroup = "RAS_PAY_TYPE",inject = "payTypeName")
	@BtCompositeSearch(fieldName = "pay_type")
	private String payType;
	@BtCompositeSearch(fieldName = "pay_type")
	@Excel(name = "*缴款方式")
	private String payTypeName;
	/**客户名称*/
	@BtCompositeSearch(fieldName = "nue.cust_Name")
	@Excel(name = "客户名称")
	private String custName;
	/**客户账号*/
	@BtCompositeSearch(fieldName = "nue.cust_Acc")
	@Excel(name = "客户账号")
	private String custAcc;
	/**备注*/
	@Excel(name = "备注")
	private String rmk;
	/**状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成0待取认-2作废*/
	@Dict(dictGroup = "RAS_AUDIT_STATUS",inject = "statusName")
	@BtCompositeSearch(fieldName = "nue.status")
	private Integer status;
	@BtCompositeSearch(fieldName = "nue.status")
	private String statusName;
	/**稽核ID**/
	private String auditId;       
	/**创建人名称*/
    private String  createName;
    /**创建人登录名称*/
    private String  createBy;   
    /**创建日期*/
	@BtCompositeSearch(fieldName = "nue.create_date")
    private Date  createDate;
    /**更新人名称*/
    private String  updateName;   
    /**更新人登录名称*/
    private String  updateBy;     
    /**更新日期*/
    private Date  updateDate;
    /**YYYYMMDD的日期格式字符串 (预留字段* 时间格式)*/
    private String strTransDate;

   	/**合计标记：1为是0和空为否*/
   	private Integer isSum;
   	
	public Integer getIsSum() {
		return isSum;
	}
	public void setIsSum(Integer isSum) {
		this.isSum = isSum;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
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
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
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
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		if(StringUtils.isNotEmpty(corpCode)
				&&StringUtils.isNotEmpty(corpName)){
			return "("+corpCode+")"+corpName;
		}
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public Date getTransConvertDate() {
		return transConvertDate;
	}
	public void setTransConvertDate(Date transConvertDate) {
		this.transConvertDate = transConvertDate;
	}
		
	public RasRevenueDtlVO(){
		
	}
	public Date getRevenueTransDate() {
		return revenueTransDate;
	}
	public void setRevenueTransDate(Date revenueTransDate) {
		this.revenueTransDate = revenueTransDate;
	}
	
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getStrTransDate() {
		return strTransDate;
	}
	public void setStrTransDate(String strTransDate) {
		this.strTransDate = strTransDate;
	}
	
	@Override
	public String toString() {
		return "RasRevenueDtlVO [shopId=" + shopId + ", serialNo=" + serialNo
				+ ", orderNo=" + orderNo + ", transDate=" + transDate
				+ ", money=" + money + ", payType=" + payType + ", custName="
				+ custName + ", custAcc=" + custAcc + ", rmk=" + rmk
				+ ", status=" + status
				+ ", auditId=" + auditId + ", createName=" + createName
				+ ", createBy=" + createBy + ", createDate=" + createDate
				+ ", updateName=" + updateName + ", updateBy=" + updateBy
				+ ", updateDate=" + updateDate + "]";
	}
	 @Override
	 public String getBeanName() {
	    return "rasRevenueService";
	 }
	
	
}
