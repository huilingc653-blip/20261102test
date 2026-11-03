package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolLimitApplycheckVO
 * @Description: 申请额度核准AbPoolLimitApplycheckVO
 * @author jinyong.cao
 * @date 2020/1/21 08:11:46
 *
 */
public class AbPoolLimitApplyCheckVO implements Serializable,  IBusinessKey{
	private String id;
	
	private String ids;
	
	@BtCompositeSearch(fieldName="ap.corp_id")
    @Corp(inject="corpName")
	private String corpId;
	
	/**
     * 票据池单位
     */
    @BtCompositeSearch(fieldName="apc.corp_name")
    private String corpName;
    /**
     * 票据池单位代码
     */
    private String corpCode;
	@BtCompositeSearch(fieldName = "ap.BP_ID")
	private String bpId;
	@BtCompositeSearch(fieldName = "ap.BUSTYPE_ID")
	private String busTypeId;
	/**
     * 额度种类
     */
	@BtCompositeSearch(fieldName = "ap.limt_Type")
	@Dict(dictGroup = "AB_POOL_LIMT_TYPE", inject = "limtTypeName")
	private String limtType;
	private String limtTypeName;
	/**
     * 審批流產狀態
     */
	@BtCompositeSearch(fieldName = "ap.status")
	@Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
	private String status;
	private String statusName;
	/**
     * 指令狀態
     */
	@BtCompositeSearch(fieldName = "ap.opt_status")
	@Dict(dictGroup = "AB_POOL_OPT_STATUS", inject = "opStatusName")
	private String opStatus;
	private String opStatusName;
	 /**
     * 申领额度
     */
	@BtCompositeSearch(fieldName = "ap.apply_money")
	private BigDecimal applyMoney;
	 /**
     * 业务类型代码
     */
	private String bustypeCode;
	 /**
     * 业务类型名称
     */
	private String bustypeName;
	/**
     * 上一级别业务名称
     */
	private String parentBusName;
	/**
	 * 申请单位
	 */
	private String apcName;
	/**
	 * 上一级单位名称
	 */
	private String parentCorpName;
	/**
	 * 单位当前级
	 */
	private Integer corpLevel;
	 /**
     * 创建日期
     */
	 @BtCompositeSearch(fieldName = "ap.CREATE_TIME")
    private Date createTime;
	@BtCompositeSearch(fieldName = "ap.END_DATE")
    private Date endDate;
    /**
     * 备注
     */
	private String rmk;
	/**
	 * 票据池
	 */
	private String bpName;
	/**
	 * 核准类型：1通过0不通过
	 */
    @Dict(dictGroup = "AB_POOL_CHECK_TYPE", inject = "checkTypeName")
    private String checkType;
    private String checkTypeName;
	/**
	 * 核准信息
	 */
	private String checkMsg;
	/**
	 *
	 */
	private String returnCode;
	/**
	 *
	 */
	@BtCompositeSearch(fieldName = "APBE.RE_MSG")
	private String returnMsg;
	/**交易日期*/
	@BtCompositeSearch(fieldName = "ap.CREATE_TIME")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date transDate;
    /**是否隐藏复选框 1为是*/
	private String checkboxHide;
    private String businessKey;
	@Override
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLimtType() {
		return limtType;
	}
	public void setLimtType(String limtType) {
		this.limtType = limtType;
	}
	public String getLimtTypeName() {
		return limtTypeName;
	}
	public void setLimtTypeName(String limtTypeName) {
		this.limtTypeName = limtTypeName;
	}
	public BigDecimal getApplyMoney() {
		return applyMoney;
	}
	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}
	public String getBustypeCode() {
		return bustypeCode;
	}
	public void setBustypeCode(String bustypeCode) {
		this.bustypeCode = bustypeCode;
	}
	public String getBustypeName() {
		return bustypeName;
	}
	public void setBustypeName(String bustypeName) {
		this.bustypeName = bustypeName;
	}
	public String getParentBusName() {
		return parentBusName;
	}
	public void setParentBusName(String parentBusName) {
		this.parentBusName = parentBusName;
	}
	public String getParentCorpName() {
		return parentCorpName;
	}
	public void setParentCorpName(String parentCorpName) {
		this.parentCorpName = parentCorpName;
	}
	public Integer getCorpLevel() {
		return corpLevel;
	}
	public void setCorpLevel(Integer corpLevel) {
		this.corpLevel = corpLevel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getBpName() {
		return bpName;
	}
	public void setBpName(String bpName) {
		this.bpName = bpName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getOpStatus() {
		return opStatus;
	}
	public void setOpStatus(String opStatus) {
		this.opStatus = opStatus;
	}
	public String getOpStatusName() {
		return opStatusName;
	}
	public void setOpStatusName(String opStatusName) {
		this.opStatusName = opStatusName;
	}
	public String getApcName() {
		return apcName;
	}
	public void setApcName(String apcName) {
		this.apcName = apcName;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	public String getBusTypeId() {
		return busTypeId;
	}
	public void setBusTypeId(String busTypeId) {
		this.busTypeId = busTypeId;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckMsg() {
		return checkMsg;
	}
	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
            //审批列表查询
            return "0";
        }
		if(this.getStatus()!=null
				&& (this.getStatus().equals("11")
                || (this.getStatus().equals("95")) && this.getOpStatus().equals("16")
                || (this.getStatus().equals("95")) && this.getOpStatus().equals("19")
				)){
			checkboxHide="1";
		}else{
			checkboxHide="0";
		}
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public String getCheckTypeName() {
		return checkTypeName;
	}
	public void setCheckTypeName(String checkTypeName) {
		this.checkTypeName = checkTypeName;
	}
}
