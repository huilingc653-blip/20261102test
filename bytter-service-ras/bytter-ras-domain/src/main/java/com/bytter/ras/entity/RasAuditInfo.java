package com.bytter.ras.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bytter.core.base.entity.IdEntity;

/**
 * @author zhenghj
 * @projectName T2
 * @description: 营收稽核稽核信息数据库实体类
 * @date 2019/5/15
 */
@Entity
@Table(name = "RAS_AUDIT_INFO", schema = "")
public class RasAuditInfo extends IdEntity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它 */
	private String payType;
	/** 门店id */
	private String shopId;
	/** 稽核码 */
	private String auditNo;
	/** 稽核类型:0自动稽核1手工稽核 */
	private String type;
	/** 营收笔数 */
	private Integer revenueSize;
	/** 实收笔数 */
	private Integer receivedSize;
	/** 营收总额 */
	private BigDecimal revenueMoney;
	/** 实收总额 */
	private BigDecimal receivedMoney;
	/** 差异金额：营收总额减去实收总额 */
	private BigDecimal lessMoney;
	/** 状态:3已稽核4差异稽核5稽核完成-1稽核打回 */
	private Integer status;
	/** 是否发送消息：1为是0和空为否 */
	private Integer isMsg;
	/**
	 * 规则：多个以,隔开01订单号02交易日期03交易流水04门店名称05单位名称06现金汇总稽核07其他方式汇总稽核
	 */
	private String rules;
	/** 备注 */
	private String rmk;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建人名称 */
	private String createName;
	/** 创建日期 */
	private Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新人名称 */
	private String updateName;
	/** 更新日期 */
	private Date updateDate;
	/** 尾款阈值 */
	private BigDecimal ecMoney;
	
	/** 缺少金额 */
	private BigDecimal leeEcMoney;
	
	/**单位ID**/
	private String corpId;
	
	/**门店名称**/
	private String shopName;
	
	/**多缴款还是少缴款信息**/
	private String ecInfo;
	
	/**营收数据ID**/
	private String revenueId;
	
	/**实收数据ID**/
	private String receivedId;

	@Column(name = "PAY_TYPE", nullable = true, length = 10)
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "SHOP_ID", nullable = true, length = 32)
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	@Column(name = "AUDIT_NO", nullable = true, length = 50)
	public String getAuditNo() {
		return auditNo;
	}

	public void setAuditNo(String auditNo) {
		this.auditNo = auditNo;
	}

	@Column(name = "TYPE", nullable = true, length = 200)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "REVENUE_SIZE", nullable = true, length = 6)
	public Integer getRevenueSize() {
		return revenueSize;
	}

	public void setRevenueSize(Integer revenueSize) {
		this.revenueSize = revenueSize;
	}

	@Column(name = "RECEIVED_SIZE", nullable = true, length = 6)
	public Integer getReceivedSize() {
		return receivedSize;
	}

	public void setReceivedSize(Integer receivedSize) {
		this.receivedSize = receivedSize;
	}

	@Column(name = "REVENUE_MONEY", nullable = true, scale = 2, length = 15)
	public BigDecimal getRevenueMoney() {
		return revenueMoney;
	}

	public void setRevenueMoney(BigDecimal revenueMoney) {
		this.revenueMoney = revenueMoney;
	}

	@Column(name = "RECEIVED_MONEY", nullable = true, scale = 2, length = 15)
	public BigDecimal getReceivedMoney() {
		return receivedMoney;
	}

	public void setReceivedMoney(BigDecimal receivedMoney) {
		this.receivedMoney = receivedMoney;
	}

	@Column(name = "LESS_MONEY", nullable = true, scale = 2, length = 15)
	public BigDecimal getLessMoney() {
		return lessMoney;
	}

	public void setLessMoney(BigDecimal lessMoney) {
		this.lessMoney = lessMoney;
	}

	@Column(name = "STATUS", nullable = true, length = 6)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "RMK", nullable = true, length = 200)
	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	@Column(name = "CREATE_NAME", nullable = true, length = 50)
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Column(name = "CREATE_BY", nullable = true, length = 50)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "CREATE_DATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_NAME", nullable = true, length = 50)
	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@Column(name = "UPDATE_BY", nullable = true, length = 50)
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "UPDATE_DATE", nullable = true)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "END_CHECK_MONEY", nullable = false)
	public BigDecimal getEcMoney() {
		return ecMoney;
	}

	public void setEcMoney(BigDecimal ecMoney) {
		this.ecMoney = ecMoney;
	}

	@Column(name = "IS_MSG")
	public Integer getIsMsg() {
		return isMsg;
	}

	public void setIsMsg(Integer isMsg) {
		this.isMsg = isMsg;
	}

	@Column(name = "RULES")
	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}
	
	@Transient
	public String getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(String revenueId) {
		this.revenueId = revenueId;
	}

	@Transient
	public String getReceivedId() {
		return receivedId;
	}

	public void setReceivedId(String receivedId) {
		this.receivedId = receivedId;
	}

	@Transient
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@Transient
	public String getEcInfo() {
		return ecInfo;
	}

	public void setEcInfo(String ecInfo) {
		this.ecInfo = ecInfo;
	}

	@Transient
	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	@Transient
	public BigDecimal getLeeEcMoney() {
		return leeEcMoney;
	}

	public void setLeeEcMoney(BigDecimal leeEcMoney) {
		this.leeEcMoney = leeEcMoney;
	}

	@Override
	public String toString() {
		return "RasAuditInfo{" + "payType='" + payType + '\'' + ", shopId='" + shopId + '\'' + ", auditNo='" + auditNo
				+ '\'' + ", type='" + type + '\'' + ", revenueSize=" + revenueSize + ", receivedSize=" + receivedSize
				+ ", revenueMoney=" + revenueMoney + ", receivedMoney=" + receivedMoney + ", lessMoney=" + lessMoney
				+ ", status=" + status + ", rmk='" + rmk + '\'' + ", createBy='" + createBy + '\'' + ", createName='"
				+ createName + '\'' + ", createDate=" + createDate + ", updateBy='" + updateBy + '\'' + ", updateName='"
				+ updateName + '\'' + ", updateDate=" + updateDate + '}';
	}
}
