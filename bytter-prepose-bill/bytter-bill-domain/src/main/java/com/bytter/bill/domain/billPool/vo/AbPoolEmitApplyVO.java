package com.bytter.bill.domain.billPool.vo;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolEmitApplyVO
 * @Description: 出票申请VO
 * @author wangzy
 * @date 2020/2/26 16:09
 *
 */
public class AbPoolEmitApplyVO  implements Serializable,  IBusinessKey {
    /**明细Id*/
    private String dtlId;
    /**流水表Id*/
    private String aid;
    /**票据号*/
    @BtCompositeSearch(fieldName="emit.origNo")
    private String origNo;
    /**单位*/
    private String corpCodeAndName;
    /**单位ID*/
    @BtCompositeSearch(fieldName="emit.corpId")
    private String corpId;
    /**票据类型*/
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    @BtCompositeSearch(fieldName="emit.billType")
    private String billTypeName;
    /**票据介质*/
    @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    @BtCompositeSearch(fieldName="emit.billFlag")
    private String billFlagName;
    /**票据金额*/
    @BtCompositeSearch(fieldName="emit.billMoney")
    private BigDecimal billMoney;
    /**申请人账号*/
    private String payeracc;
    /**申请单号*/
    @BtCompositeSearch(fieldName="emit.appNo")
    private String appNo;
    /**申请人*/
    private String payer;
    /**申请人开户银行行号*/
    private String payerbankno;
    /**申请人开户银行名称*/
    private String payerBankName;
    /**是否转让*/
    @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
    private String banendrsmtmk;
    private String banendrsmtmkName;
    /**出票日期*/
    @BtCompositeSearch(fieldName="emit.startDate")
    private Date startDate;
    /**到期日期*/
    private Date endDate;
    /**收票人*/
    private String payee;
    /**收票人账号*/
    private String payeeacc;
    /**收票人开户银行名称*/
    private String payeeBankName;
    /**收票人开户银行行号*/
    private String payeebankno;
    /**承兑人*/
    private String acceptor;
    /**承兑人账号 */
    private String acceptoracc;
    /**承兑人开户行名称*/
    private String acceptorBankName;
    /**承兑人开户行行号*/
    private String acceptorbankno;
    /**票据池*/
    @BtCompositeSearch(fieldName="emit.bpId")
    private String pbCodeAndName;
    /**票据池Id*/
    private String bpId;
    /** 是否隐藏多选框 1为是 */
    private String checkboxHide;
    /**审批状态*/
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName="emit.status")
    private Integer status;
    private String statusName;
    /**指令状态*/
    @Dict(dictGroup = "AB_POOL_DTL_OPT_STATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName="emit.optStatus")
    private String optStatus;
    private String optStatusName;
    private String businessKey;
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    public String getAid() {
        return aid;
    }
    public void setAid(String aid) {
        this.aid = aid;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public String getCorpCodeAndName() {
        return corpCodeAndName;
    }
    public void setCorpCodeAndName(String corpCodeAndName) {
        this.corpCodeAndName = corpCodeAndName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Integer getBillType() {
        return billType;
    }
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    public Integer getBillFlag() {
        return billFlag;
    }
    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public String getPayeracc() {
        return payeracc;
    }
    public void setPayeracc(String payeracc) {
        this.payeracc = payeracc;
    }
    public String getPayerbankno() {
        return payerbankno;
    }
    public void setPayerbankno(String payerbankno) {
        this.payerbankno = payerbankno;
    }
    public String getPayerBankName() {
        return payerBankName;
    }
    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName;
    }
    public String getBanendrsmtmk() {
        return banendrsmtmk;
    }
    public void setBanendrsmtmk(String banendrsmtmk) {
        this.banendrsmtmk = banendrsmtmk;
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
    public String getAppNo() {
        return appNo;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
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
    public String getPayeeacc() {
        return payeeacc;
    }
    public void setPayeeacc(String payeeacc) {
        this.payeeacc = payeeacc;
    }
    public String getPayeeBankName() {
        return payeeBankName;
    }
    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }
    public String getPayeebankno() {
        return payeebankno;
    }
    public void setPayeebankno(String payeebankno) {
        this.payeebankno = payeebankno;
    }
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    public String getAcceptoracc() {
        return acceptoracc;
    }
    public void setAcceptoracc(String acceptoracc) {
        this.acceptoracc = acceptoracc;
    }
    public String getAcceptorBankName() {
        return acceptorBankName;
    }
    public void setAcceptorBankName(String acceptorBankName) {
        this.acceptorBankName = acceptorBankName;
    }
    public String getAcceptorbankno() {
        return acceptorbankno;
    }
    public void setAcceptorbankno(String acceptorbankno) {
        this.acceptorbankno = acceptorbankno;
    }
    public String getPbCodeAndName() {
        return pbCodeAndName;
    }
    public void setPbCodeAndName(String pbCodeAndName) {
        this.pbCodeAndName = pbCodeAndName;
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
    public String getBillTypeName() {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }
    public String getBillFlagName() {
        return billFlagName;
    }
    public void setBillFlagName(String billFlagName) {
        this.billFlagName = billFlagName;
    }
    public String getBanendrsmtmkName() {
        return banendrsmtmkName;
    }
    public void setBanendrsmtmkName(String banendrsmtmkName) {
        this.banendrsmtmkName = banendrsmtmkName;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getCheckboxHide() {
        // 审批列表、指令状态为失败时可选择
        if(StringUtils.isNotEmpty(this.getBusinessKey()) || AbPoolConstant.AB_POOL_OPT_STATUS_1.equals(this.getOptStatus())){
            //审批列表查询
            return "0";
        }
        // 审批中和审批完成的不能选择
        if (AbPoolConstant.AB_POOL_STATUS11.equals(this.getStatus()) || AbPoolConstant.AB_POOL_STATUS95.equals(this.getStatus())) {
            return "1";
        } else {
            return "0";
        }
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
    @Override
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
