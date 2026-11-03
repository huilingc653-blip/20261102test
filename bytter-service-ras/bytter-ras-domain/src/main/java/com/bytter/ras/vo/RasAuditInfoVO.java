package com.bytter.ras.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.entity.RasShop;
import com.bytter.core.base.entity.ICurrentUser;
//import com.bytter.core.injections.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Title: 稽核处理VO类
 * @author zhengg
 * @date 2019-05-21
 * @version 1.0
 *
 */
public class RasAuditInfoVO  implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 缴款方式:001现金002支付宝003微信004银联POS机005收钱吧006其它 */
	private String payType;
	private String payTypeName;
	private List<RasPayTypeVO> rasPayTypeVO=new ArrayList<>();
	private List<String> payTypeList;
	/** 单位id */
	private String corpId;
	/** 单位id */
	private List<String> corpIdList;
	/** 门店id */
	private String shopId;
	/** 门店列表 */
	private List<String> shopIdList;
	private Map<String,RasShop> shopMap=new HashMap<>();
	private Map<String,List<RasAuditStrategy>> payTypeMap=new HashMap<>();
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
	/** 数据来源 */
	private String dataSource;
	/** 数据来源 */
	private List<String> dataSourceList;
	/** 起始金额 */
	private BigDecimal moneyStart;
	/** 结束金额 */
	private BigDecimal moneyEnd;
	/** 交易日期 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date transDate;
	/** 营收交易日期 */
	private Date revenueTransDateStart;
	/** 营收交易日期 */
	private Date revenueTransDateEnd;
	/** 实收交易日期 */
	private Date receivedTransDateStart;
	/** 实收交易日期 */
	private Date receivedTransDateEnd;
	
	/**营收数据ID**/
	private String revenueId;
	
	/**实收数据ID**/
	private String receivedId;
	
	/**
	 * 操作类型：manual手工稽核,less差异稽核,taskOp 自动稽核
	 */
	private String opType;
    
    private ICurrentUser user;

	private List<RasAuditStrategy> auditStrategyList;
	
	private String unId;
	
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getAuditNo() {
		return auditNo;
	}

	public void setAuditNo(String auditNo) {
		this.auditNo = auditNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRevenueSize() {
		return revenueSize;
	}

	public void setRevenueSize(Integer revenueSize) {
		this.revenueSize = revenueSize;
	}

	public Integer getReceivedSize() {
		return receivedSize;
	}

	public void setReceivedSize(Integer receivedSize) {
		this.receivedSize = receivedSize;
	}

	public BigDecimal getRevenueMoney() {
		return revenueMoney;
	}

	public void setRevenueMoney(BigDecimal revenueMoney) {
		this.revenueMoney = revenueMoney;
	}

	public BigDecimal getReceivedMoney() {
		return receivedMoney;
	}

	public void setReceivedMoney(BigDecimal receivedMoney) {
		this.receivedMoney = receivedMoney;
	}

	public BigDecimal getLessMoney() {
		return lessMoney;
	}

	public void setLessMoney(BigDecimal lessMoney) {
		this.lessMoney = lessMoney;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsMsg() {
		return isMsg;
	}

	public void setIsMsg(Integer isMsg) {
		this.isMsg = isMsg;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
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

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public BigDecimal getEcMoney() {
		return ecMoney;
	}

	public void setEcMoney(BigDecimal ecMoney) {
		this.ecMoney = ecMoney;
	}

	public String getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(String revenueId) {
		this.revenueId = revenueId;
	}

	public String getReceivedId() {
		return receivedId;
	}

	public void setReceivedId(String receivedId) {
		this.receivedId = receivedId;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public BigDecimal getMoneyStart() {
		return moneyStart;
	}

	public void setMoneyStart(BigDecimal moneyStart) {
		this.moneyStart = moneyStart;
	}

	public BigDecimal getMoneyEnd() {
		return moneyEnd;
	}

	public void setMoneyEnd(BigDecimal moneyEnd) {
		this.moneyEnd = moneyEnd;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public Date getRevenueTransDateStart() {
		return revenueTransDateStart;
	}

	public void setRevenueTransDateStart(Date revenueTransDateStart) {
		this.revenueTransDateStart = revenueTransDateStart;
	}

	public Date getRevenueTransDateEnd() {
		return revenueTransDateEnd;
	}

	public void setRevenueTransDateEnd(Date revenueTransDateEnd) {
		this.revenueTransDateEnd = revenueTransDateEnd;
	}

	public Date getReceivedTransDateStart() {
		return receivedTransDateStart;
	}

	public void setReceivedTransDateStart(Date receivedTransDateStart) {
		this.receivedTransDateStart = receivedTransDateStart;
	}

	public Date getReceivedTransDateEnd() {
		return receivedTransDateEnd;
	}

	public void setReceivedTransDateEnd(Date receivedTransDateEnd) {
		this.receivedTransDateEnd = receivedTransDateEnd;
	}

	public List<String> getCorpIdList() {
		return corpIdList;
	}

	public void setCorpIdList(List<String> corpIdList) {
		this.corpIdList = corpIdList;
	}

	public List<String> getShopIdList() {
		return shopIdList;
	}

	public void setShopIdList(List<String> shopIdList) {
		this.shopIdList = shopIdList;
	}

	public ICurrentUser getUser() {
		return user;
	}

	public void setUser(ICurrentUser user) {
		this.user = user;
	}

	public List<String> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<String> payTypeList) {
		this.payTypeList = payTypeList;
	}

	public List<String> getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(List<String> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	public List<RasPayTypeVO> getRasPayTypeVO() {
		return rasPayTypeVO;
	}

	public void setRasPayTypeVO(List<RasPayTypeVO> rasPayTypeVO) {
		this.rasPayTypeVO = rasPayTypeVO;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public List<RasAuditStrategy> getAuditStrategyList() {
		return auditStrategyList;
	}

	public void setAuditStrategyList(List<RasAuditStrategy> auditStrategyList) {
		this.auditStrategyList = auditStrategyList;
	}

	public Map<String, RasShop> getShopMap() {
		return shopMap;
	}

	public void setShopMap(Map<String, RasShop> shopMap) {
		this.shopMap = shopMap;
	}

	public String getUnId() {
		return unId;
	}

	public void setUnId(String unId) {
		this.unId = unId;
	}

	public Map<String, List<RasAuditStrategy>> getPayTypeMap() {
		return payTypeMap;
	}

	public void setPayTypeMap(Map<String, List<RasAuditStrategy>> payTypeMap) {
		this.payTypeMap = payTypeMap;
	}

	@Override
	public String toString() {
		return "RasAuditInfoVO [payType=" + payType + ", corpId=" + corpId + ", shopId=" + shopId + ", auditNo="
				+ auditNo + ", type=" + type + ", revenueSize=" + revenueSize + ", receivedSize=" + receivedSize
				+ ", revenueMoney=" + revenueMoney + ", receivedMoney=" + receivedMoney + ", lessMoney=" + lessMoney
				+ ", status=" + status + ", isMsg=" + isMsg + ", rules=" + rules + ", rmk=" + rmk + ", createBy="
				+ createBy + ", createName=" + createName + ", createDate=" + createDate + ", updateBy=" + updateBy
				+ ", updateName=" + updateName + ", updateDate=" + updateDate + ", ecMoney=" + ecMoney + ", dataSource="
				+ dataSource + ", moneyStart=" + moneyStart + ", moneyEnd=" + moneyEnd + ", transDate=" + transDate
				+ ", revenueTransDateStart=" + revenueTransDateStart + ", revenueTransDateEnd=" + revenueTransDateEnd
				+ ", receivedTransDateStart=" + receivedTransDateStart + ", receivedTransDateEnd="
				+ receivedTransDateEnd + ", revenueId=" + revenueId + ", receivedId=" + receivedId + ", opType="
				+ opType + ", getPayType()=" + getPayType() + ", getShopId()=" + getShopId() + ", getAuditNo()="
				+ getAuditNo() + ", getType()=" + getType() + ", getRevenueSize()=" + getRevenueSize()
				+ ", getReceivedSize()=" + getReceivedSize() + ", getRevenueMoney()=" + getRevenueMoney()
				+ ", getReceivedMoney()=" + getReceivedMoney() + ", getLessMoney()=" + getLessMoney() + ", getStatus()="
				+ getStatus() + ", getIsMsg()=" + getIsMsg() + ", getRules()=" + getRules() + ", getRmk()=" + getRmk()
				+ ", getCreateBy()=" + getCreateBy() + ", getCreateName()=" + getCreateName() + ", getCreateDate()="
				+ getCreateDate() + ", getUpdateBy()=" + getUpdateBy() + ", getUpdateName()=" + getUpdateName()
				+ ", getUpdateDate()=" + getUpdateDate() + ", getEcMoney()=" + getEcMoney() + ", getRevenueId()="
				+ getRevenueId() + ", getReceivedId()=" + getReceivedId() + ", getOpType()=" + getOpType()
				+ ", getCorpId()=" + getCorpId() + ", getDataSource()=" + getDataSource() + ", getMoneyStart()="
				+ getMoneyStart() + ", getMoneyEnd()=" + getMoneyEnd() + ", getTransDate()=" + getTransDate()
				+ ", getRevenueTransDateStart()=" + getRevenueTransDateStart() + ", getRevenueTransDateEnd()="
				+ getRevenueTransDateEnd() + ", getReceivedTransDateStart()=" + getReceivedTransDateStart()
				+ ", getReceivedTransDateEnd()=" + getReceivedTransDateEnd() + "]";
	}
	
}
