package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhenghj
 * @Title: 额度分配VO
 * @version 1.0
 * @Description: 额度分配Vo
 * @date 2020-1-13
 */
@SuppressWarnings("serial")
public class AbPoolLimitAllotVO implements java.io.Serializable,  IBusinessKey {
    /**
     * 额度分配Id
     */
    private String id;
    /**单位ID**/
    @BtCompositeSearch(fieldName = "APLA.corp_id")
    private String corpId;
    private String corpCode;
    private String corpName;
    /**父级单位ID**/
    private String parentId;
    private String parentName;
    /**
     * 票据池id
     */
    @BtCompositeSearch(fieldName = "APLA.BP_Id")
    private String bpId;
    /**
     * 票据池名称
     */
    @BtCompositeSearch(fieldName = "AP.BP_NAME")
    private String bpName;
    
    /**
     * 额度类型
     */
    @Dict(dictGroup = "AB_POOL_LIMIT_OP_TYPE", inject = "typeName")
    private String type;
    
    /**
     * 额度类型名称
     */
    @BtCompositeSearch(fieldName = "APLA.type")
    private String typeName;
    /**
     * 业务类型ID
     */
    private String busTypeId;
    /**
     * 业务类型名称
     */
    private String busTypeName;
    /**
     * 额度种类:1：低风险， 2：非低风险
     */
    @Dict(dictGroup = "AB_POOL_LIMT_TYPE", inject = "limtTypeName")
    @BtCompositeSearch(fieldName = "APLA.LIMT_TYPE")
    private  String limtType;
    private  String limtTypeName;
    /**
     * 总额度
     */
    @BtCompositeSearch(fieldName = "APLA.POOL_MONEY")
    private BigDecimal poolMoney;
    /**
     * 可用额度
     */
    @BtCompositeSearch(fieldName = "APLA.VALID_MONEY")
    private BigDecimal validMoney;
    /**
     * 已用额度
     */
    @BtCompositeSearch(fieldName = "APLA.LOCK_MONEY")
    private BigDecimal lockMoney;
    /**
     * 申请额度
     */
    @BtCompositeSearch(fieldName = "APLA.APPLY_MONEY")
    private BigDecimal applyMoney;
    /**
     * 到期日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "APLA.END_DATE")
    private Date endDate;
    /**
     * 指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3
     */
    @Dict(dictGroup = "AB_POOL_OPT_STATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName = "APLA.OPT_STATUS")
    private String optStatus;
    private String optStatusName;
    /**
     * 审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过
     */
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName = "APLA.STATUS")
    private Integer status;
    private String statusName;
    /**
     * 单位层级
     */
    private Integer bustypeLevel;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "APLA.create_Time")
    private Date createTime;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    
    private String businessKey;
    
    private String returnCode;
    @BtCompositeSearch(fieldName = "APBE.RE_MSG")
    private String returnMsg;
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
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getBpName() {
        return bpName;
    }
    public void setBpName(String bpName) {
        this.bpName = bpName;
    }
    public String getBusTypeId() {
        return busTypeId;
    }
    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
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
    public BigDecimal getPoolMoney() {
        return poolMoney;
    }
    public void setPoolMoney(BigDecimal poolMoney) {
        this.poolMoney = poolMoney;
    }
    public BigDecimal getValidMoney() {
        return validMoney;
    }
    public void setValidMoney(BigDecimal validMoney) {
        this.validMoney = validMoney;
    }
    public BigDecimal getLockMoney() {
        return lockMoney;
    }
    public void setLockMoney(BigDecimal lockMoney) {
        this.lockMoney = lockMoney;
    }
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    public String getOptStatusName() {
        return optStatusName;
    }
    public void setOptStatusName(String optStatusName) {
        this.optStatusName = optStatusName;
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
    public Integer getBustypeLevel() {
        return bustypeLevel;
    }
    public void setBustypeLevel(Integer bustypeLevel) {
        this.bustypeLevel = bustypeLevel;
    }
    public String getCheckboxHide() {
        return checkboxHide;
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getBusinessKey() {
        return id;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckTypeName() {
		return checkTypeName;
	}
	public void setCheckTypeName(String checkTypeName) {
		this.checkTypeName = checkTypeName;
	}
	public String getCheckMsg() {
		return checkMsg;
	}
	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg;
	}
	@Override
    public String toString() {
        return "AbPoolLimitAllotVO{" +
                "id='" + id + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", corpName='" + corpName + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                ", bpId='" + bpId + '\'' +
                ", bpName='" + bpName + '\'' +
                ", busTypeId='" + busTypeId + '\'' +
                ", busTypeName='" + busTypeName + '\'' +
                ", limtType='" + limtType + '\'' +
                ", limtTypeName='" + limtTypeName + '\'' +
                ", poolMoney=" + poolMoney +
                ", validMoney=" + validMoney +
                ", lockMoney=" + lockMoney +
                ", applyMoney=" + applyMoney +
                ", endDate=" + endDate +
                ", optStatus='" + optStatus + '\'' +
                ", optStatusName='" + optStatusName + '\'' +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", bustypeLevel=" + bustypeLevel +
                ", rmk='" + rmk + '\'' +
                ", createTime=" + createTime +
                ", checkboxHide='" + checkboxHide + '\'' +
                ", businessKey='" + businessKey + '\'' +
                '}';
    }
}
