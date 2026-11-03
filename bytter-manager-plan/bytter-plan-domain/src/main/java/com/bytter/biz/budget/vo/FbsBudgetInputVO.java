package com.bytter.biz.budget.vo;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 计划上报VO-固定上报
 * @author huangx
 *
 */
public class FbsBudgetInputVO implements Serializable {

	private String id;
	/**单位ID*/
	private String corpId;
	/**编制时间*/
	private Date enrolDate;
	/**计划开始时间*/
	private Date beginDate;
	/**计划结束时间*/
	private Date endDate;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private String timeType;
	/**需审批金额*/
	private BigDecimal checkMoney;
	/**状态*/
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**编制消息*/
	private String msg;

	/**明细ID*/
	private String[] detailId;
	/**科目代码*/
	private String[] itemCode;
	/**科目名称*/
	private String[] itemName;
	/**上报金额*/
	private BigDecimal[] bMoney;
	/**上报金额*/
	private BigDecimal[] btempMoney;

	/**追加ID*/
	private String superaddId;
	/**追加明细ID*/
	private String[] superaddDtlId;
	private String target;
	/**附件ID*/
	private String attachmentId;
	private String isCollect;
	/**币别*/
	private String curId;
	/**汇率*/
	private BigDecimal exchangeRate;
	/**备注*/
	private String[] rmk;
	/**
	 * 默认tab页签
	 */
	private String layId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Date getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(Date enrolDate) {
		this.enrolDate = enrolDate;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public BigDecimal getCheckMoney() {
		return checkMoney;
	}
	public void setCheckMoney(BigDecimal checkMoney) {
		this.checkMoney = checkMoney;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String[] getDetailId() {
		return detailId;
	}
	public void setDetailId(String[] detailId) {
		this.detailId = detailId;
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
	public BigDecimal[] getbMoney() {
		return bMoney;
	}
	public void setbMoney(BigDecimal[] bMoney) {
		this.bMoney = bMoney;
	}
	public BigDecimal[] getBtempMoney() {
		return btempMoney;
	}
	public void setBtempMoney(BigDecimal[] btempMoney) {
		this.btempMoney = btempMoney;
	}
	public String getSuperaddId() {
		return superaddId;
	}
	public void setSuperaddId(String superaddId) {
		this.superaddId = superaddId;
	}
	public String[] getSuperaddDtlId() {
		return superaddDtlId;
	}
	public void setSuperaddDtlId(String[] superaddDtlId) {
		this.superaddDtlId = superaddDtlId;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal[] getBMoney() {
		return bMoney;
	}
	public void setBMoney(BigDecimal[] bMoney) {
		this.bMoney = bMoney;
	}
	public String[] getRmk() {
		return rmk;
	}
	public void setRmk(String[] rmk) {
		this.rmk = rmk;
	}
	public String getLayId() {
		return layId;
	}
	public void setLayId(String layId) {
		this.layId = layId;
	}
}
