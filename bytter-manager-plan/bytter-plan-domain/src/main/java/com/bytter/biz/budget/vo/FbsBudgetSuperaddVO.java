package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.bytter.biz.budget.entity.FbsBudgetSuperaddDetail;
import com.bytter.core.annotation.Corp;
/**
 * @author huangx
 * @date 2017-08-03
 */
public class FbsBudgetSuperaddVO implements Serializable {
	
	private String id;
	/**计划ID*/
	private String budgetId;
	/**单位ID*/
	//@Corp(inject="corpName")
	private String corpId;
	private String corpName;
	/**需审批金额*/
	private BigDecimal checkMoney;
	/**状态*/
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**计划明细*/
	private List<FbsBudgetSuperaddDetail> dtlList = new ArrayList<FbsBudgetSuperaddDetail>();
	/**编制消息*/
	private String msg;
	/**附件ID*/
	private String attachmentId;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private String timeType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(String budgetId) {
		this.budgetId = budgetId;
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
	public List<FbsBudgetSuperaddDetail> getDtlList() {
		return dtlList;
	}
	public void setDtlList(List<FbsBudgetSuperaddDetail> dtlList) {
		this.dtlList = dtlList;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
}
