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
 * @Title: AbPoolCollectionApplyVO
 * @Description: 提示付款VO
 * @author wangzy
 * @date 2020/3/11 8:58
 *
 */
@SuppressWarnings("serial")
public class AbPoolCollectionApplyVO  implements Serializable,  IBusinessKey {
    /**明细Id*/
    private String dtlId;
    /**明细Ids*/
    private String dtlIds;
    /**流水表Id*/
    private String aid;
    /**票据号*/
    @BtCompositeSearch(fieldName="coll.origNo")
    private String origNo;
    /**单位*/
    private String corpCodeAndName;
    /**单位ID*/
    @BtCompositeSearch(fieldName="coll.corpId")
    private String corpId;
    /**票据类型*/
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    @BtCompositeSearch(fieldName="coll.billType")
    private String billTypeName;
    /**票据介质*/
    @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    @BtCompositeSearch(fieldName="coll.billFlag")
    private String billFlagName;
    /**票据金额*/
    @BtCompositeSearch(fieldName="coll.billMoney")
    private BigDecimal billMoney;
    /**出票人*/
    private String payer;
    /**申请人账号*/
    private String payeracc;
    /**申请人开户银行行号*/
    private String payerbankno;
    /**申请人开户银行名称*/
    private String payerBankName;
    /**入池方式*/
    @Dict(dictGroup = "AB_POOL_INOUT_TYPE", inject = "inoutTypeName")
    @BtCompositeSearch(fieldName="coll.inoutType")
    private String inoutType;
    private String inoutTypeName;
    /**是否转让*/
    @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
    private String banendrsmtmk;
    private String banendrsmtmkName;
    /**出票日期*/
    @BtCompositeSearch(fieldName="coll.startDate")
    private Date startDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName="coll.endDate")
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
    @BtCompositeSearch(fieldName="coll.bpId")
    private String pbCodeAndName;
    /**票据池Id*/
    private String bpId;
    /** 是否隐藏多选框 1为是 */
    private String checkboxHide;
    /** 选中票据数 */
    private Integer length;
    /**审批状态*/
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName="coll.status")
    private Integer status;
    private String statusName;
    /**指令状态*/
    @Dict(dictGroup = "AB_POOL_DTL_OPT_STATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName="coll.optStatus")
    private String optStatus;
    private String optStatusName;
    /**清算方式：SM00 开启线上清算、SM01 关闭线上清算*/
    @Dict(dictGroup = "AB_STTLMMK", inject ="sttlmmkName")
    private String sttlmmk;
    private String sttlmmkName;
    /**预约日期*/
    private Date businessDate;
    /**
     * 逾期理由
     */
    private String rsn;
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
    public String getDtlIds() {
        return dtlIds;
    }
    public void setDtlIds(String dtlIds) {
        this.dtlIds = dtlIds;
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
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getInoutType() {
        return inoutType;
    }
    public void setInoutType(String inoutType) {
        this.inoutType = inoutType;
    }
    public String getInoutTypeName() {
        return inoutTypeName;
    }
    public void setInoutTypeName(String inoutTypeName) {
        this.inoutTypeName = inoutTypeName;
    }
    public String getBanendrsmtmk() {
        return banendrsmtmk;
    }
    public void setBanendrsmtmk(String banendrsmtmk) {
        this.banendrsmtmk = banendrsmtmk;
    }
    public String getBanendrsmtmkName() {
        return banendrsmtmkName;
    }
    public void setBanendrsmtmkName(String banendrsmtmkName) {
        this.banendrsmtmkName = banendrsmtmkName;
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
    public String getPbCodeAndName() {
        return pbCodeAndName;
    }
    public void setPbCodeAndName(String pbCodeAndName) {
        this.pbCodeAndName = pbCodeAndName;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
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
    public String getCheckboxHide() {
        if(StringUtils.isNotEmpty(this.getBusinessKey())){
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
    public String getSttlmmk() {
        return sttlmmk;
    }
    public void setSttlmmk(String sttlmmk) {
        this.sttlmmk = sttlmmk;
    }
    public String getSttlmmkName() {
        return sttlmmkName;
    }
    public void setSttlmmkName(String sttlmmkName) {
        this.sttlmmkName = sttlmmkName;
    }
    public Date getBusinessDate() {
        return businessDate;
    }
    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }
    public String getRsn() {
        return rsn;
    }
    public void setRsn(String rsn) {
        this.rsn = rsn;
    }
}
