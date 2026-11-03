package com.bytter.ras.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.entity.RasShop;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.ICurrentUser;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Title: Entity
 * @Description: 稽核任务执行配置
 * @author onlineGenerator
 * @date 2019-05-15 15:43:05
 * @version 1.0
 *
 */
public class RasAuditTaskVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String lastId;
	/** 任务编号 */
    @BtCompositeSearch(fieldName="code")
	private String code;
	/** 任务名称 */
    @BtCompositeSearch(fieldName="name")
	private String name;
	/** 缴款方式:多个,隔开001现金002支付宝003微信004银联POS机005收钱吧006其它 */
    @BtCompositeSearch(fieldName="pay_Type")
	private String payType;
	private String payTypeName;
	private String[] payTypeArray;
	private List<RasPayTypeVO> rasPayTypeVO=new ArrayList<>();
	/** 有效标志：0，禁用 1，启用 */
    @Dict(dictGroup = RasConstant.RAS_VALID_SIGN_DICT, inject = "validSignName")
	private String validSign;
    @BtCompositeSearch(fieldName="valid_Sign")
	private String validSignName;
	/** 启动日期 */
    @BtCompositeSearch(fieldName="start_Date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	/** 最后执行日期 */
    @BtCompositeSearch(fieldName="last_Date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastDate;
	/** 执行状态:S成功F失败R处理中 */
    @Dict(dictGroup = RasConstant.RAS_RUN_STATUS_DICT, inject = "runStatusName")
	private String runStatus;
    @BtCompositeSearch(fieldName="run_Status")
	private String runStatusName;
	/** 执行信息 */
    @BtCompositeSearch(fieldName="run_Msg")
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
    @BtCompositeSearch(fieldName="create_Date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/** 创建人名称 */
    @BtCompositeSearch(fieldName="update_By")
	private String updateName;
	/** 创建人登录名称 */
	private String updateBy;
	/** 创建日期 */
    @BtCompositeSearch(fieldName="update_Date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;

    /**
     * 操作类型 addOp,updateOp,taskOp
     */
	private String opType;
    
    private ICurrentUser user;
    
    /**
     * 实收数据起始日期
     */
    private Date startReceivedDate;
    
    /**
     * 营收数据起始日期
     */
	private Date startRevenueDate;

	private List<RasAuditStrategy> auditStrategyList;
	
	private List<RasShop> shopList;

	private String unId;

	private Map<String,RasShop> shopMap=new HashMap<>();
	
	private Map<String,List<RasAuditStrategy>> payTypeMap=new HashMap<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getValidSign() {
		return validSign;
	}

	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
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
		if(StringUtils.isNotEmpty(this.updateBy)){
			return "("+this.updateBy+")"+updateName;
		}
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public String getRunMsg() {
		return runMsg;
	}

	public void setRunMsg(String runMsg) {
		this.runMsg = runMsg;
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

	public String getPayTypeName() {
		if(this.payTypeName==null&&this.rasPayTypeVO!=null&&this.rasPayTypeVO.size()>0){
			StringBuilder payTypeName=new StringBuilder();
			for(RasPayTypeVO info : rasPayTypeVO){
				if(payTypeName.length()>0){
					payTypeName.append(RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
				}
				payTypeName.append(info.getPayTypeName());
			}
			setPayTypeName(payTypeName.toString());
		}
		return this.payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String[] getPayTypeArray() {
		return payTypeArray;
	}

	public void setPayTypeArray(String[] payTypeArray) {
		this.payTypeArray = payTypeArray;
	}

	public List<RasPayTypeVO> getRasPayTypeVO() {
		return rasPayTypeVO;
	}

	public void setRasPayTypeVO(List<RasPayTypeVO> rasPayTypeVO) {
		this.rasPayTypeVO = rasPayTypeVO;
	}

	public String getValidSignName() {
		return validSignName;
	}

	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}

	public String getRunStatusName() {
		return runStatusName;
	}

	public void setRunStatusName(String runStatusName) {
		this.runStatusName = runStatusName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}

	public ICurrentUser getUser() {
		return user;
	}

	public void setUser(ICurrentUser user) {
		this.user = user;
	}

	public Date getStartReceivedDate() {
		return startReceivedDate;
	}

	public void setStartReceivedDate(Date startReceivedDate) {
		this.startReceivedDate = startReceivedDate;
	}

	public Date getStartRevenueDate() {
		return startRevenueDate;
	}

	public void setStartRevenueDate(Date startRevenueDate) {
		this.startRevenueDate = startRevenueDate;
	}

	public List<RasAuditStrategy> getAuditStrategyList() {
		return auditStrategyList;
	}

	public void setAuditStrategyList(List<RasAuditStrategy> auditStrategyList) {
		this.auditStrategyList = auditStrategyList;
	}

	public List<RasShop> getShopList() {
		return shopList;
	}

	public void setShopList(List<RasShop> shopList) {
		this.shopList = shopList;
	}

	public String getUnId() {
		return unId;
	}

	public void setUnId(String unId) {
		this.unId = unId;
	}

	public Map<String, RasShop> getShopMap() {
		return shopMap;
	}

	public void setShopMap(Map<String, RasShop> shopMap) {
		this.shopMap = shopMap;
	}

	public Map<String, List<RasAuditStrategy>> getPayTypeMap() {
		return payTypeMap;
	}

	public void setPayTypeMap(Map<String, List<RasAuditStrategy>> payTypeMap) {
		this.payTypeMap = payTypeMap;
	}

	@Override
	public String toString() {
		return "RasAuditTaskVO [id=" + id + ",lastId=" + lastId + ", code=" + code + ", name=" + name + ", payType=" + payType
				+ ", payTypeName=" + payTypeName + ", payTypeArray=" + Arrays.toString(payTypeArray) + ", rasPayTypeVO="
				+ rasPayTypeVO + ", validSign=" + validSign + ", validSignName=" + validSignName + ", startDate="
				+ startDate + ", lastDate=" + lastDate + ", runStatus=" + runStatus + ", runStatusName=" + runStatusName
				+ ", runMsg=" + runMsg + ", taskId=" + taskId + ", taskCron=" + taskCron + ", rmk=" + rmk
				+ ", createName=" + createName + ", createBy=" + createBy + ", createDate=" + createDate
				+ ", updateName=" + updateName + ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", opType="
				+ opType + ", getName()=" + getName() + ", getPayType()=" + getPayType() + ", getValidSign()="
				+ getValidSign() + ", getRmk()=" + getRmk() + ", getCreateName()=" + getCreateName()
				+ ", getCreateBy()=" + getCreateBy() + ", getCreateDate()=" + getCreateDate() + ", getUpdateName()="
				+ getUpdateName() + ", getUpdateBy()=" + getUpdateBy() + ", getUpdateDate()=" + getUpdateDate()
				+ ", getCode()=" + getCode() + ", getStartDate()=" + getStartDate() + ", getLastDate()=" + getLastDate()
				+ ", getRunStatus()=" + getRunStatus() + ", getRunMsg()=" + getRunMsg() + ", getTaskId()=" + getTaskId()
				+ ", getTaskCron()=" + getTaskCron() + ", getPayTypeName()=" + getPayTypeName() + ", getPayTypeArray()="
				+ Arrays.toString(getPayTypeArray()) + ", getRasPayTypeVO()=" + getRasPayTypeVO()
				+ ", getValidSignName()=" + getValidSignName() + ", getRunStatusName()=" + getRunStatusName()
				+ ", getId()=" + getId() + ", getOpType()=" + getOpType() + "]";
	}

}
