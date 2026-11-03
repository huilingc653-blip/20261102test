package com.bytter.biz.fundwarn.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
/**
 * @Description: 预警策略主信息VO
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 */
public class BtWarnStrategyVO implements Serializable,  IBusinessKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**所属网点*/
	@BtCompositeSearch(fieldName="s.net_Id")
	private String netId;
	/**所属网点名称*/
	@Excel(name = "网点名称")
	private String netName;
	/**
	 * 策略代码
	 */
	@BtCompositeSearch(fieldName = "s.code")
	private String code;
	/**
	 * 策略名称
	 */
	@Excel(name = "策略名称")
	@BtCompositeSearch(fieldName = "s.name")
	private String name;
	/**
	 * 预警币别ID
	 */
	@BtCompositeSearch(fieldName = "s.cur_id")
	private String curId;
	/**
	 * 预警币别
	 */
	@BtCompositeSearch(fieldName = "bc.cur_name")
	private String curName;
	/**
	 * 预警类型(balOver余额超标、eqCorpPay相同公司付款、bankAccPay大额账户支出、inflowOver净流入透支、balDown账户余额持续下降、
	 * outPay系统外付款、expendInflow支出户流入、cashOver库存现金超标、depositLoanOver存贷比超标、cashPay大额现金支出)
	 * */
	@BtCompositeSearch(fieldName = "s.type_code")
	private String typeCode;

	@BtCompositeSearch(fieldName = "s.type_code")
	private String typeCodeName;

	/**
	 * 预警维度(corp单位、bankAcc银行账户、fcAcc内部账户)
	 */
	@BtCompositeSearch(fieldName = "s.dimension_code")
	private String dimensionCode;

	@BtCompositeSearch(fieldName = "s.dimension_code")
	private String dimensionCodeName;
	/**
	 * 状态(1启动0停止)
	 */
	@BtCompositeSearch(fieldName = "s.status")
	private Integer status;

	@BtCompositeSearch(fieldName = "s.status")
	private String statusName;
	/**
	 * 开始数值
	 */
	private java.math.BigDecimal beginNum;
	/**
	 * 结束数值
	 */
	private java.math.BigDecimal endNum;
	/**
	 * 开始数值比较运算符（10＞、11＞＝、20＜、21＜＝）
	 */
	private String beginOperator;
	/**
	 * 结束数值比较运算符（10＞、11＞＝、20＜、21＜＝）
	 */
	private String endOperator;
	/** 数值间运算符(01或者、02并且)*/
	private String numOperator;
	/** 定时器ID*/
	private String taskId;
	/** 定时器表达式*/
	private String taskCron;
	/** 扫描数据提前期*/
	private Integer forwardTime;
	/**
	 * 执行时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date runDate;
	/**创建人名称*/
	private String createName;
	/**
	 * 创建人登录名称
	 */
	private String createBy;
	/**
	 * 创建日期
	 */
	@BtCompositeSearch(fieldName = "s.create_date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**
	 * 更新人名称
	 * */
	private String updateName;
	/**
	 * 更新人登录名称
	 */
	private String updateBy;
	/**
	 * 更新日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName = "s.update_date")
	private Date updateDate;
	/** 预警级别*/
	@BtCompositeSearch(fieldName = "s.warn_level")
	private String warnLevel;
	private Map<String,Object> dataMap;
    private ICurrentUser currentUser;
    
	public ICurrentUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(ICurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeCodeName() {
		return typeCodeName;
	}
	public void setTypeCodeName(String typeCodeName) {
		this.typeCodeName = typeCodeName;
	}
	public String getDimensionCode() {
		return dimensionCode;
	}
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}
	public String getDimensionCodeName() {
		return dimensionCodeName;
	}
	public void setDimensionCodeName(String dimensionCodeName) {
		this.dimensionCodeName = dimensionCodeName;
	}
	public String getBeginOperator() {
		return beginOperator;
	}
	public void setBeginOperator(String beginOperator) {
		this.beginOperator = beginOperator;
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
	public String getEndOperator() {
		return endOperator;
	}
	public void setEndOperator(String endOperator) {
		this.endOperator = endOperator;
	}
	public Date getRunDate() {
		return runDate;
	}
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
	public java.math.BigDecimal getBeginNum() {
		return beginNum;
	}
	public void setBeginNum(java.math.BigDecimal beginNum) {
		this.beginNum = beginNum;
	}
	public java.math.BigDecimal getEndNum() {
		return endNum;
	}
	public void setEndNum(java.math.BigDecimal endNum) {
		this.endNum = endNum;
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
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getNumOperator() {
		return numOperator;
	}
	public void setNumOperator(String numOperator) {
		this.numOperator = numOperator;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskCron() {
		return taskCron;
	}
	public void setTaskCron(String taskCron) {
		this.taskCron = taskCron;
	}
	public Integer getForwardTime() {
		return forwardTime;
	}
	public void setForwardTime(Integer forwardTime) {
		this.forwardTime = forwardTime;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public String getWarnLevel() {
		return warnLevel;
	}
	public void setWarnLevel(String warnLevel) {
		this.warnLevel = warnLevel;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	@Override
	public String toString() {
		return "BtWarnStrategyVO{" +
				"id='" + id + '\'' +
				", netId='" + netId + '\'' +
				", netName='" + netName + '\'' +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", typeCode='" + typeCode + '\'' +
				", typeCodeName='" + typeCodeName + '\'' +
				", dimensionCode='" + dimensionCode + '\'' +
				", dimensionCodeName='" + dimensionCodeName + '\'' +
				", status=" + status +
				", statusName='" + statusName + '\'' +
				", beginNum=" + beginNum +
				", endNum=" + endNum +
				", beginOperator='" + beginOperator + '\'' +
				", endOperator='" + endOperator + '\'' +
				", numOperator='" + numOperator + '\'' +
				", taskId='" + taskId + '\'' +
				", taskCron='" + taskCron + '\'' +
				", forwardTime=" + forwardTime +
				", runDate=" + runDate +
				", createName='" + createName + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateName='" + updateName + '\'' +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				'}';
	}
}
