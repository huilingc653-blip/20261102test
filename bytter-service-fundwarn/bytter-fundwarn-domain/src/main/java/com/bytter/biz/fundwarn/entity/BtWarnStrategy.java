package com.bytter.biz.fundwarn.entity;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * @Title: Entity
 * @Description: 预警策略主信息实体
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_WARN_STRATEGY", schema = "")
@SuppressWarnings("serial")
public class BtWarnStrategy extends IdEntity implements java.io.Serializable {
  /**所属网点ID*/
  private String netId;
  /**策略代码*/
  private String code;
  /**策略名称*/
  private String name;
  /**预警币别*/
  private String curId;
  /**
   * 预警类型(balOver余额超标、eqCorpPay相同公司付款、bankAccPay大额账户支出、inflowOver净流入透支、balDown账户余额持续下降、
   * outPay系统外付款、expendInflow支出户流入、cashOver库存现金超标、depositLoanOver存贷比超标、cashPay大额现金支出)
   * */
  private String typeCode;
  /**预警维度(corp单位、bankAcc银行账户、fcAcc内部账户)*/
  private String dimensionCode;
  /** 状态(1启动0停止)*/
  private Integer status;
  /** 开始数值*/
  private java.math.BigDecimal beginNum;
  /** 结束数值*/
  private java.math.BigDecimal endNum;
  /** 开始数值比较运算符（10＞、11＞＝、20＜、21＜＝）*/
  private String beginOperator;
  /** 结束数值比较运算符（10＞、11＞＝、20＜、21＜＝）*/
  private String endOperator;
  /** 数值间运算符(01或者、02并且)*/
  private String numOperator;
  /** 定时器ID*/
  private String taskId;
  /** 定时器表达式*/
  private String taskCron;
  /** 扫描数据提前期*/
  private Integer forwardTime;
  /** 执行时间*/
  private java.util.Date runDate;
  /** 创建人名称*/
  private String createName;
  /** 创建人登录名称*/
  private String createBy;
  /** 创建日期*/
  private java.util.Date createDate;
  /** 更新人名称*/
  private String updateName;
  /** 更新人登录名称*/
  private String updateBy;
  /** 更新日期*/
  private java.util.Date updateDate;
  /** 预警级别*/
  private String warnLevel;
	private ICurrentUser currentUser;
	@Transient
	public ICurrentUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(ICurrentUser currentUser) {
		this.currentUser = currentUser;
	}
  @Column(name = "NET_ID",nullable = true,length = 32)
  public String getNetId() {
    return netId;
  }
  public void setNetId(String netId) {
    this.netId = netId;
  }
  @Column(name = "CODE",nullable = true,length = 20)
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  @Column(name = "NAME",nullable = true,length = 200)
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  @Column(name = "CUR_ID",nullable = true,length = 32)
  public String getCurId() {
    return curId;
  }
  public void setCurId(String curId) {
    this.curId = curId;
  }
  @Column(name = "TYPE_CODE",nullable = true,length = 20)
  public String getTypeCode() {
    return typeCode;
  }
  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }
  @Column(name = "DIMENSION_CODE",nullable = true,length = 20)
  public String getDimensionCode() {
    return dimensionCode;
  }
  public void setDimensionCode(String dimensionCode) {
    this.dimensionCode = dimensionCode;
  }
  @Column(name = "STATUS",nullable = true,length = 1)
  public Integer getStatus() {
    return status;
  }
  public void setStatus(Integer status) {
    this.status = status;
  }
  @Column(name = "BEGIN_NUM",nullable = true,scale = 2,length = 15)
  public java.math.BigDecimal getBeginNum() {
    return beginNum;
  }
  public void setBeginNum(java.math.BigDecimal beginNum) {
    this.beginNum = beginNum;
  }
  @Column(name = "END_NUM",nullable = true,scale = 2,length = 15)
  public java.math.BigDecimal getEndNum() {
    return endNum;
  }
  public void setEndNum(java.math.BigDecimal endNum) {
    this.endNum = endNum;
  }
  @Column(name = "BEGIN_OPERATOR",nullable = true,length = 5)
  public String getBeginOperator() {
    return beginOperator;
  }
  public void setBeginOperator(String beginOperator) {
    this.beginOperator = beginOperator;
  }
  @Column(name = "END_OPERATOR",nullable = true,length = 5)
  public String getEndOperator() {
    return endOperator;
  }
  public void setEndOperator(String endOperator) {
    this.endOperator = endOperator;
  }
  @Column(name = "NUM_OPERATOR",nullable = true,length = 5)
  public String getNumOperator() {
    return numOperator;
  }
  public void setNumOperator(String numOperator) {
    this.numOperator = numOperator;
  }
  @Column(name = "TASK_ID",nullable = true,length = 32)
  public String getTaskId() {
    return taskId;
  }
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }
  @Column(name = "TASK_CRON",nullable = true,length = 100)
  public String getTaskCron() {
    return taskCron;
  }
  public void setTaskCron(String taskCron) {
    this.taskCron = taskCron;
  }
  @Column(name = "FORWARD_TIME",nullable = true,length = 10)
  public Integer getForwardTime() {
    return forwardTime;
  }
  public void setForwardTime(Integer forwardTime) {
    this.forwardTime = forwardTime;
  }
  @Column(name = "RUN_DATE",nullable = true)
  public java.util.Date getRunDate() {
    return runDate;
  }
  public void setRunDate(java.util.Date runDate) {
    this.runDate = runDate;
  }
  @Column(name = "CREATE_NAME",nullable = true,length = 50)
  public String getCreateName() {
    return createName;
  }
  public void setCreateName(String createName) {
    this.createName = createName;
  }
  @Column(name = "CREATE_BY",nullable = true,length = 50)
  public String getCreateBy() {
    return createBy;
  }
  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }
  @Column(name = "CREATE_DATE",nullable = true)
  public java.util.Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(java.util.Date createDate) {
    this.createDate = createDate;
  }
  @Column(name = "UPDATE_NAME",nullable = true,length = 50)
  public String getUpdateName() {
    return updateName;
  }
  public void setUpdateName(String updateName) {
    this.updateName = updateName;
  }
  @Column(name = "UPDATE_BY",nullable = true,length = 50)
  public String getUpdateBy() {
    return updateBy;
  }
  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }
  @Column(name = "UPDATE_DATE",nullable = true)
  public java.util.Date getUpdateDate() {
    return updateDate;
  }
  public void setUpdateDate(java.util.Date updateDate) {
    this.updateDate = updateDate;
  }
  @Column(name = "WARN_LEVEL")
  public String getWarnLevel() {
    return warnLevel;
  }
  public void setWarnLevel(String warnLevel) {
    this.warnLevel = warnLevel;
  }
  @Override
  public String toString() {
    return "BtWarnStrategy{" +
            "netId='" + netId + '\'' +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", curId='" + curId + '\'' +
            ", typeCode='" + typeCode + '\'' +
            ", dimensionCode='" + dimensionCode + '\'' +
            ", status=" + status +
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
