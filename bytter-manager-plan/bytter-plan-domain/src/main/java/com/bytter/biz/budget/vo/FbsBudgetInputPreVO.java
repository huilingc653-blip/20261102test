package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.Currency;
import com.bytter.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 计划上报VO
 * @author onlineGenerator
 * @date 2017-07-14 15:06:43
 * @version 1.0
 *
 */
public class FbsBudgetInputPreVO implements Serializable{
	/**单位ID*/
	private String corpId;
	/**单位编码*/
	private String corpCode;
	/**单位名称*/
	private String corpName;
	/**上级单位ID*/
    private String parentCorpId;
    /**单位类型 0-网点  1-单位*/
    private Integer corpType;
    /**计划上报ID*/
	private String id;
    /**编制时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date enrolDate;
	private String enrolDateStr;
    /**计划开始时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date beginDate;
	private String beginDateStr;
	/**计划结束时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	private String endDateStr;
	private String showEndDate;
	/**状态*/
	private Integer status;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private String timeType;
	/**显示过滤 1-全部 2-本期 3-下期 4-本期录入 5-下期录入*/
	private String showType;
	private String target;
	/**追加ID*/
	private String superaddId;
	/**是否是汇总 0-否 1-是*/
	private String isCollect;
	/**待汇总子单位*/
	private Integer childCorp;
	/**计划控制 0-否 1-是*/
	private Integer ctlBudget;
	/**计划汇总 0-否 1-是*/
    private Integer collectFlag;
	/**是否只能查看*/
	private Boolean view;
	/**父周期上报累计收支净额*/
	private BigDecimal netAmount;
	/**本周期执行累计收支净额*/
	private BigDecimal processNetAmount;
	/**币别
	 * 在service 填充
	 * */
	//@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	private String curName;
	/**汇率*/
	private BigDecimal exchangeRate;
	/**
	 * 虚拟单位
	 */
	private String fictitiousCorp;
	/**
	 * 默认tab页签
	 */
	private String layId;
	
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
	public String getParentCorpId() {
		return parentCorpId;
	}
	public void setParentCorpId(String parentCorpId) {
		this.parentCorpId = parentCorpId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getCorpType() {
		return corpType;
	}
	public void setCorpType(Integer corpType) {
		this.corpType = corpType;
	}
	public Date getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(Date enrolDate) {
		this.enrolDate = enrolDate;
		if(null != enrolDate){
			this.enrolDateStr = DateUtils.formatDate(enrolDate);
		}
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
		if(null !=beginDate){
			this.beginDateStr = DateUtils.formatDate(beginDate);
		}
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		if(null != endDate){
			this.endDateStr = DateUtils.formatDate(endDate);
		}
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
    
	public String getEnrolDateStr() {
		return enrolDateStr;
	}
	public void setEnrolDateStr(String enrolDateStr) {
		this.enrolDateStr = enrolDateStr;
	}
	public String getBeginDateStr() {
		return beginDateStr;
	}
	public void setBeginDateStr(String beginDateStr) {
		this.beginDateStr = beginDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSuperaddId() {
		return superaddId;
	}
	public void setSuperaddId(String superaddId) {
		this.superaddId = superaddId;
	}
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public Integer getChildCorp() {
		return childCorp;
	}
	public void setChildCorp(Integer childCorp) {
		this.childCorp = childCorp;
	}
	public Integer getCtlBudget() {
		return ctlBudget;
	}
	public void setCtlBudget(Integer ctlBudget) {
		this.ctlBudget = ctlBudget;
	}
	public Integer getCollectFlag() {
		return collectFlag;
	}
	public void setCollectFlag(Integer collectFlag) {
		this.collectFlag = collectFlag;
	}
	public Boolean getView() {
		return view;
	}
	public void setView(Boolean view) {
		this.view = view;
	}
	public BigDecimal getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}
	public BigDecimal getProcessNetAmount() {
		return processNetAmount;
	}
	public void setProcessNetAmount(BigDecimal processNetAmount) {
		this.processNetAmount = processNetAmount;
	}
	public String getShowEndDate() {
		return showEndDate;
	}
	public void setShowEndDate(String showEndDate) {
		this.showEndDate = showEndDate;
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
	public String getFictitiousCorp() {
		return fictitiousCorp;
	}
	public void setFictitiousCorp(String fictitiousCorp) {
		this.fictitiousCorp = fictitiousCorp;
	}
	public String getLayId() {
		return layId;
	}
	public void setLayId(String layId) {
		this.layId = layId;
	}
}
