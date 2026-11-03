package com.bytter.bill.domain.billPool.vo;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 
　 * @Title: 质押托管转换VO
　 * @author jy.cao
  * @Description:AbPoolPdTstVO.java
　 * @date 2020年3月5日
　 * @version 1.0
 */
public class AbPoolPdTstVO  implements java.io.Serializable,  IBusinessKey{
    private String id;
    /** 从表id**/
    private String viceId;
    
    /** 主表ID**/
    private String vouId;
    /** 业务流水表**/
    private String accId;
    
    /** 票据池业务流水表**/
    private String apdId;
    
    /** 审批业务流水ID**/
    private String businessKey;
    
	/**
     * 出入池状态
     * 0或空	未入池
     * 1	已入池
     * 2	入池在途
     */
    private String poolSatatus;
    /**
     * 从表ID
     */
    private String vvId;
    /**
     * 票据池ID
     */
    private String bpId;
    /**
     * 业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款
     */
    @Dict(dictGroup = "AB_BILL_INSBRIEFID_TEST", inject = "briefIdTypeName")
    private String briefId;
    private String briefIdTypeName;
    /**
     * 申请单号
     */
    private String appNo;
    /**
     * 票据号码
     */
    @BtCompositeSearch(fieldName="V.orig_no")
    private String origNo;
    /**
     * 出入池方式：1质押2托管
     */
    @BtCompositeSearch(fieldName="apd.inout_type")
    @Dict(dictGroup = "AB_POOL_INOUT_TYPE", inject = "inoutTypeName")
    private String inoutType;
    private String inoutTypeName;
    
    private String validSign;
    
    private String backBriefId;
    
    /**
     * 创建人名称
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人名称
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**持票单位*/
    @BtCompositeSearch(fieldName="P.corp_id")
    @Corp(inject="corpName")
    private String corpId;
    /**
     * 票据池单位
     */
    @BtCompositeSearch(fieldName="s.code")
    private String corpName;
    /**
     * 票据池单位代码
     */
    private String corpCode;
    /**票据类型：10 银票、11 商票*/
    @BtCompositeSearch(fieldName="V.bill_type")
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    @BtCompositeSearch(fieldName="V.bill_type")
    private String  billTypeName;
    /**流通介质：10 纸票、11 电票*/
    @BtCompositeSearch(fieldName="V.bill_flag")
    @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
    private Integer billFlag;
    @BtCompositeSearch(fieldName="V.bill_flag")
    private String billFlagName;
    /**票据金额*/
    @BtCompositeSearch(fieldName="V.bill_money")
    private BigDecimal billMoney;
    /**出票日期*/
    @BtCompositeSearch(fieldName="V.start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName="V.end_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    /**出票人*/
    @BtCompositeSearch(fieldName="V.payer")
    private String payer;
    /**收票人全称*/
    @BtCompositeSearch(fieldName="V.payee")
    private String payee;
    /**承兑人*/
    @BtCompositeSearch(fieldName="V.acceptor")
    private String acceptor;
    /**票据池ID*/
    @BtCompositeSearch(fieldName="ap.id")
    private String abPoolId;
    /**票据池名称**/
    private String abPoolName;
    /**票据状态*/
    @BtCompositeSearch(fieldName="apd.status")
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    private Integer status;
    @BtCompositeSearch(fieldName="apd.status")
    private String statusName;
    /**
     * 指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2
     */
    @BtCompositeSearch(fieldName="apd.opt_status")
    @Dict(dictGroup = "AB_POOL_DTL_OPT_STATUS", inject = "optStatusName")
    private String optStatus;
    @BtCompositeSearch(fieldName="apd.opt_status")
    private String optStatusName;
    
    /**是否隐藏复选框 1为是*/
	private String checkboxHide;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVvId() {
        return vvId;
    }
    public void setVvId(String vvId) {
        this.vvId = vvId;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getBriefId() {
        return briefId;
    }
    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }
    public String getAppNo() {
        return appNo;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public String getInoutType() {
        return inoutType;
    }
    public void setInoutType(String inoutType) {
        this.inoutType = inoutType;
    }
    public Integer getStatus() {
    	//判断入池8018申请业务类型
//    	if(!StringUtils.isBlank(this.getBriefId())&&this.getBriefId().equals("8018") && this.validSign.equals("1")){
//    		return 0;
//    	}
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getOptStatus() {
    	//判断入池8018申请业务类型
//    	if(!StringUtils.isBlank(this.getBriefId())&&this.getBriefId().equals("8018") && this.validSign.equals("1")){
//    		return null;
//    	}
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public Integer getBillType() {
        return billType;
    }
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    public String getBillTypeName() {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }
    public Integer getBillFlag() {
        return billFlag;
    }
    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }
    public String getBillFlagName() {
        return billFlagName;
    }
    public void setBillFlagName(String billFlagName) {
        this.billFlagName = billFlagName;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    public String getAbPoolId() {
        return abPoolId;
    }
    public void setAbPoolId(String abPoolId) {
        this.abPoolId = abPoolId;
    }
    public String getAbPoolName() {
        return abPoolName;
    }
    public void setAbPoolName(String abPoolName) {
        this.abPoolName = abPoolName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getOptStatusName() {
        return optStatusName;
    }
    public void setOptStatusName(String optStatusName) {
        this.optStatusName = optStatusName;
    }
    public String getViceId() {
        return viceId;
    }
    public void setViceId(String viceId) {
        this.viceId = viceId;
    }
    public String getPoolSatatus() {
        return poolSatatus;
    }
    public void setPoolSatatus(String poolSatatus) {
        this.poolSatatus = poolSatatus;
    }
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	
    public String getVouId() {
		return vouId;
	}
	public void setVouId(String vouId) {
		this.vouId = vouId;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getInoutTypeName() {
		return inoutTypeName;
	}
	public void setInoutTypeName(String inoutTypeName) {
		this.inoutTypeName = inoutTypeName;
	}
	public String getCheckboxHide() {
		if(StringUtils.isNotEmpty(this.getBusinessKey())){
            //审批列表查询
            return "0";
        }
		if(this.getStatus()!=0 && this.getStatus()==-1){
			return "0";
		}
		
		if(this.getStatus()!=0 
				&& (this.getBriefId().equals(AbPoolConstant.AB_POOL_BRIEF_8118) || this.getBriefId().equals(AbPoolConstant.AB_POOL_BRIEF_8218))
				&& null!=this.getOptStatus() &&this.getOptStatus().equals("19") && this.getStatus().equals(AbPoolConstant.AB_POOL_STATUS95)
				&& this.getValidSign().equals(AbPoolConstant.AB_POLL_VALID_SIGN_1)
				){
			checkboxHide="0";
		}else if(this.getStatus()!=0 
				     ||(AbPoolConstant.AB_POOL_STATUS11.equals(this.getStatus())
				     ||(
	                 (AbPoolConstant.AB_POOL_STATUS11.equals(this.getStatus())
	                && StringUtils.isEmpty(this.getOptStatus())
	                || (AbPoolConstant.AB_POOL_STATUS95.equals(this.getStatus())
	                && StringUtils.isNotEmpty(this.getOptStatus()) && !AbPoolConstant.AB_POOL_OPT_STATUS_2.equals(this.getOptStatus())))))){
			checkboxHide="1";
		}else{
			checkboxHide="0";
		}
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public String getApdId() {
		return apdId;
	}
	public void setApdId(String apdId) {
		this.apdId = apdId;
	}
	public String getBriefIdTypeName() {
		return briefIdTypeName;
	}
	public void setBriefIdTypeName(String briefIdTypeName) {
		this.briefIdTypeName = briefIdTypeName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getBackBriefId() {
		return backBriefId;
	}
	public void setBackBriefId(String backBriefId) {
		this.backBriefId = backBriefId;
	}
}
