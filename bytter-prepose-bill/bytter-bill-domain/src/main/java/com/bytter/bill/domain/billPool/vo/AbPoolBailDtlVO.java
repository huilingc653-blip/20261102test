package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolBailDtlVO
 * @Description: 保证金管理VO
 * @author wangzy
 * @date 2020/1/10 18:08
 *
 */
@SuppressWarnings("serial")
public class AbPoolBailDtlVO  implements Serializable,  IBusinessKey {
    /**保证金流水Id*/
    private String id;
    /**保证金信息Id*/
    private String bailId;
    
    /**存取类型*/
    @Dict(dictGroup = "AB_POOL_BAIL_TRANS_TYPE", inject = "typeName")
    private String type;
    /**存取类型名称*/
    @BtCompositeSearch(fieldName="dtl.type")
    private String typeName;
    /**保证金余额*/
    private BigDecimal bailMoney;
    /**保证金日期*/
    private Date bailDate;
    /**存取金额*/
    @BtCompositeSearch(fieldName="dtl.money")
    private BigDecimal money;
    /**申请日期*/
    @BtCompositeSearch(fieldName="dtl.app_date")
    private Date appDate;
    /**存取账号*/
    @BtCompositeSearch(fieldName="dtl.opp_acc")
    private String oppAcc;
    /**存取账户名称*/
    private String oppAccName;
    /**存取账户行号*/
    private String oppBankNo;
    /**存取账户行名*/
    private String oppBankName;
    /**审批状态*/
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName="dtl.status")
    private Integer status;
    private String statusName;
    /**审批状态*/
    @Dict(dictGroup = "AB_POOL_OPT_STATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName="dtl.opt_status")
    private String optStatus;
    private String optStatusName;
    /**创建人ID*/
    private String createId;
    /**创建时间*/
    private Date createDate;
    /**创建人ID*/
    private String updateId;
    /**更新时间*/
    private Date updateDate;
    /**保证金账户*/
    private String bankAcc;
    /**单位*/
    @BtCompositeSearch(fieldName="pb.corp_id")
    private String corpId;
    private String corpCodeAndName;
    /**票据池*/
    @BtCompositeSearch(fieldName="p.id")
    private String pbCodeAndName;
    
    /**银行返回代码*/
    private String returnCode;
    /**银行返回信息*/
    private String returnMsg;
    private String businessKey;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpCodeAndName() {
        return corpCodeAndName;
    }
    public void setCorpCodeAndName(String corpCodeAndName) {
        this.corpCodeAndName = corpCodeAndName;
    }
    public String getPbCodeAndName() {
        return pbCodeAndName;
    }
    public void setPbCodeAndName(String pbCodeAndName) {
        this.pbCodeAndName = pbCodeAndName;
    }
    public String getOppAcc() {
        return oppAcc;
    }
    public void setOppAcc(String oppAcc) {
        this.oppAcc = oppAcc;
    }
    public Date getBailDate() {
        return bailDate;
    }
    public void setBailDate(Date bailDate) {
        this.bailDate = bailDate;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getBailId() {
        return bailId;
    }
    public void setBailId(String bailId) {
        this.bailId = bailId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public String getOppBankName() {
        return oppBankName;
    }
    public void setOppBankName(String oppBankName) {
        this.oppBankName = oppBankName;
    }
    public String getOppBankNo() {
        return oppBankNo;
    }
    public void setOppBankNo(String oppBankNo) {
        this.oppBankNo = oppBankNo;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Date getAppDate() {
        return appDate;
    }
    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public BigDecimal getBailMoney() {
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
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
    
}
