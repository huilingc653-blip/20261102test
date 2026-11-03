package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**   
 * @Title: Entity
 * @Description: 资金计划主表VO
 * @author onlineGenerator
 * @date 2017-07-17 15:20:33
 * @version 1.0
 *
 */
public class FbsBudgetVO implements Serializable,  IBusinessKey {
	private String id;
	/**单位ID*/
	//@Corp(inject="corpName")
	@BtCompositeSearch(fieldName = "fbs.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName = "fbs.corp_id")
	private String corpName;
	/**编制时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "fbs.enrol_date")
	private Date enrolDate;
	/**计划开始时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "fbs.begin_date")
	private Date beginDate;
	private String beginDateStr;
	/**计划结束时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "fbs.end_date")
	private Date endDate;
	private String endDateStr;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	@BtCompositeSearch(fieldName = "fbs.time_type")
	private String timeType;
	/**上级计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private String parentTimeType;
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
	private List<FbsBudgetDtlAbsVO> dtlList = new ArrayList<FbsBudgetDtlAbsVO>();
	/**编制消息*/
	private String msg;
	/**追加ID*/
	private String superaddId;
	/**附件ID*/
	private String attachmentId;
	/**期次*/
	private String preiodNameTemp;
	/**币别*/
	//@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	private String curName;
	/**汇率*/
	private BigDecimal exchangeRate;
	/**币别是否允许编辑*/
	private String isEditCur;
	/**计划执行规则*/
	private String processRule;
	/**执行比例*/
	private BigDecimal processRatio;
	
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
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
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
	public String getBeginDateStr() {
		return beginDateStr;
	}
	public void setBeginDateStr(String beginDateStr) {
		this.beginDateStr = beginDateStr;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getParentTimeType() {
		return parentTimeType;
	}
	public void setParentTimeType(String parentTimeType) {
		this.parentTimeType = parentTimeType;
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
	public List<FbsBudgetDtlAbsVO> getDtlList() {
		return dtlList;
	}
	public void setDtlList(List<FbsBudgetDtlAbsVO> dtlList) {
		this.dtlList = dtlList;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSuperaddId() {
		return superaddId;
	}
	public void setSuperaddId(String superaddId) {
		this.superaddId = superaddId;
	}
	
	public String getAttachmentId() {
		return attachmentId;
	}
	
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getPreiodNameTemp() {
		return preiodNameTemp;
	}
	public void setPreiodNameTemp(String preiodNameTemp) {
		this.preiodNameTemp = preiodNameTemp;
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
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getIsEditCur() {
		return isEditCur;
	}
	public void setIsEditCur(String isEditCur) {
		this.isEditCur = isEditCur;
	}
	public String getProcessRule() {
		return processRule;
	}
	public void setProcessRule(String processRule) {
		this.processRule = processRule;
	}
	public BigDecimal getProcessRatio() {
		return processRatio;
	}
	public void setProcessRatio(BigDecimal processRatio) {
		this.processRatio = processRatio;
	}
	@Override
	public String getBusinessKey() {
		return this.id;
	}
}
