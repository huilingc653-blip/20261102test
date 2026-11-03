package com.bytter.ras.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bytter.core.base.entity.IdEntity;

/**
 * @Title: Entity
 * @Description: 稽核任务配置
 * @author onlineGenerator
 * @date 2019-05-15 15:43:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAS_AUDIT_TASK", schema = "")
public class RasAuditTask extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 任务编号 */
	private String code;
	/** 任务名称 */
	private String name;
	/** 缴款方式:多个,隔开001现金002支付宝003微信004银联POS机005收钱吧006其它 */
	private String payType;
	private String payTypeName;
	/** 有效标志：0，禁用 1，启用 */
	private String validSign;
	/** 启动日期 */
	private Date startDate;
	/** 最后执行日期 */
	private Date lastDate;
	/** 执行状态:S成功F失败R处理中 */
	private String runStatus;
	/** 执行信息 */
	private String runMsg;
	/** 定时器ID */
	private String taskId;
	/** 定时器表达式 */
	private String taskCron;
	/** 备注 */
	private String rmk;
	/** 创建人名称 */
	private String createName;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	private Date createDate;
	/** 创建人名称 */
	private String updateName;
	/** 创建人登录名称 */
	private String updateBy;
	/** 创建日期 */
	private Date updateDate;

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "pay_Type")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "valid_Sign")
	public String getValidSign() {
		return validSign;
	}

	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}

	@Column(name = "rmk")
	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	@Column(name = "CREATE_NAME")
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Column(name = "CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_NAME")
	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@Column(name = "UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "start_Date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "last_Date")
	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	@Column(name = "run_Status")
	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	@Column(name = "run_Msg")
	public String getRunMsg() {
		return runMsg;
	}

	public void setRunMsg(String runMsg) {
		this.runMsg = runMsg;
	}

	@Column(name = "task_Id")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "task_Cron")
	public String getTaskCron() {
		return taskCron;
	}

	public void setTaskCron(String taskCron) {
		this.taskCron = taskCron;
	}
	@Transient
	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	@Override
	public String toString() {
		return "RasAuditTask [code=" + code + ", name=" + name + ", payType=" + payType + ", validSign=" + validSign
				+ ", startDate=" + startDate + ", lastDate=" + lastDate + ", runStatus=" + runStatus + ", runMsg="
				+ runMsg + ", taskId=" + taskId + ", taskCron=" + taskCron + ", rmk=" + rmk + ", createName="
				+ createName + ", createBy=" + createBy + ", createDate=" + createDate + ", updateName=" + updateName
				+ ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", getName()=" + getName()
				+ ", getPayType()=" + getPayType() + ", getValidSign()=" + getValidSign() + ", getRmk()=" + getRmk()
				+ ", getCreateName()=" + getCreateName() + ", getCreateBy()=" + getCreateBy() + ", getCreateDate()="
				+ getCreateDate() + ", getUpdateName()=" + getUpdateName() + ", getUpdateBy()=" + getUpdateBy()
				+ ", getUpdateDate()=" + getUpdateDate() + ", getCode()=" + getCode() + ", getStartDate()="
				+ getStartDate() + ", getLastDate()=" + getLastDate() + ", getRunStatus()=" + getRunStatus()
				+ ", getRunMsg()=" + getRunMsg() + ", getTaskId()=" + getTaskId() + ", getTaskCron()=" + getTaskCron()
				+ "]";
	}
}
