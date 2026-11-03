package com.bytter.bill.domain.billPool.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;
/**
 * @author wangzy
 * @Title: AbPoolEmitFinaApplyVO
 * @Description: 融资申请VO
 * @date 2020/2/11 8:58
 */
@SuppressWarnings("serial")
@ExcelTarget("AbPoolEmitFinaApplyVO")
public class AbPoolEmitFinaApplyVO extends ExcelModel implements Serializable,  IBusinessKey {
    private String id;
    /**
     * 融资申请Id
     */
    private String dtlId;
    /**
     * 流水表Id
     */
    private String aid;
    /**
     * 合同信息Id
     */
    private String conId;
    /**
     * 票据号
     */
    private String origNo;
    /**
     * 合同类型Id
     */
    private String conFtype;
    /**
     * 合同类型Id
     */
    private String conName;
    /**
     * 单位
     */
    private String corpCodeAndName;
    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "vv.corp_Id")
    private String corpId;
    @Excel(name = "所属单位代码", isImportField = "true")
//	@NotNull(message = AccebillConstant.LANG_ERROR_CORPCODE_ISNOTNULL)
    private String corpCode;
    @Excel(name = "所属单位", isImportField = "true")
    //	@NotNull(message = AccebillConstant.ACCEBILL_BILLPOOL_ERRORMSG_CORPNAME_ISNOTEXIST)
    private String corpName;
    /**
     * 票据类型
     */
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    private String billTypeName;
    /**
     * 票据介质
     */
    @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    private String billFlagName;
    /**
     * 票据金额
     */
    @BtCompositeSearch(fieldName = "v.bill_Money")
    @Excel(name = "*票据金额", isImportField = "true", numFormat = "##########0.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal billMoney;
    /**
     * 申请人账号
     */
    @Excel(name = "*申请人账号", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_BILLPOOL_ERRORMSG_PAYERACC_ISNOTEXIST)
    private String payeracc;
    /**
     * 申请单号
     */
    @BtCompositeSearch(fieldName = "dtl.APP_NO")
    private String appNo;
    /**
     * 申请人
     */
    private String payer;
    /**
     * 申请人开户银行行号
     */
    private String payerbankno;
    /**
     * 申请人开户银行名称
     */
    private String payerBankName;
    /**
     * 融资用途
     */
    @BtCompositeSearch(fieldName = "vv.abs")
    @Excel(name = "*融资用途", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_BILLPOOL_ERRORMSG_ABS_ISNOTEXIST)
    private String abs;
    /**不得转让标记EM00可再转让EM01不得转让*/
    @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
    private String banendrsmtmk;
    @Excel(name = "不得转让标记", isImportField = "true")
    private String banendrsmtmkName;
    /**
     * 出票日期
     */
    @BtCompositeSearch(fieldName = "v.start_Date")
    @Excel(name = "*出票日期", format = "yyyy-MM-dd", isImportField = "true")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日期
     */
    @Excel(name = "*到期日期", format = "yyyy-MM-dd", isImportField = "true")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 收票人
     */
    @Excel(name = "*收票人", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_PAYEE_ISNOTNULL)
    private String payee;
    /**
     * 收票人账号
     */
    @Excel(name = "*收票人账号", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_PAYEEACC_BILLFLAG11_ISNOTNULL)
    private String payeeacc;
    /**
     * 收票人开户银行名称
     */
    @Excel(name = "*收票人开户银行名称", isImportField = "true")
    @NotNull(message = AccebillConstant.MESSAGE_PAYEEBANKNAME_BILLFLAG11_ISNOTNULL)
    private String payeeBankName;
    /**
     * 收票人开户银行行号
     */
    @Excel(name = "*收票人开户银行行号", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_PAYEE_ISNOTNULL)
    private String payeebankno;
    /**
     * 承兑人
     */
    @Excel(name = "*承兑人", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_ACCEPTOR_ISNOTNULL)
    private String acceptor;
    /**
     * 承兑人账号
     */
    @Excel(name = "*承兑人账号", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORACC_BILLFLAG11_ISNOTNULL)
    private String acceptoracc;
    /**
     * 承兑人开户行名称
     */
    @Excel(name = "*承兑人开户行名称", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNAME_BILLFLAG11_ISNOTNULL)
    private String acceptorBankName;
    /**
     * 承兑人开户行行号
     */
    @Excel(name = "*承兑人开户行行号", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNO_BILLFLAG11_ISNOTNULL)
    private String acceptorbankno;
    /**
     * 票据池
     */
    @BtCompositeSearch(fieldName = "p.id")
    @Excel(name = "*票据池", isImportField = "true")
    @NotNull(message = AccebillConstant.ACCEBILL_BILLPOOL_ERRORMSG_PBCODEANDNAME_ISNOTEXIST)
    private String pbCodeAndName;
    /**
     * 票据池Id
     */
    private String bpId;
    /**
     * 合同信息
     */
    private Clob conInfo;
    /**
     * 合同文件Id
     */
    private String fileId;
    /**
     * 合同状态
     */
    private String conStatus;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    /**
     * 审批状态
     */
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName = "dtl.status")
    private Integer status;
    private String statusName;
    /**
     * 指令状态
     */
    @Dict(dictGroup = "AB_POOL_DTL_OPT_STATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName = "dtl.opt_status")
    private String optStatus;
    private String optStatusName;
    /**
     * 指令状态
     */
    @Dict(dictGroup = "AB_POOL_OPT_STATUS", inject = "conOptStatusName")
    private String conOptStatus;
    @BtCompositeSearch(fieldName = "abCon.OPT_STATUS")
    private String conOptStatusName;
    private String businessKey;
    private String returnCode;
    @Dict(dictGroup = "AB_POOL_CON_OP_TYPE", inject = "conOpTypeName")
    private String conOpType;
    @BtCompositeSearch(fieldName = "abCon.STATUS")
    private String conOpTypeName;
    private String returnMsg;
    @Override
    public String getBeanName() {
        return "abPoolEmitFinaApplyService";
    }
    /**
     * 业务日期
     */
    private Date businessDate;
    /**外部系统id编码*/
    private String voucherId;
    /**开票数据来源*/
    @Dict(dictGroup = "AB_EMITBILL_SOURCE", inject = "sourceName")
    private String source;
    private String sourceName;
    /**
     * 付款单号
     */
    private String payNo;
    /**
     * 付款单id
     */
    private String payId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public String getConFtype() {
        return conFtype;
    }
    public void setConFtype(String conFtype) {
        this.conFtype = conFtype;
    }
    public String getConName() {
        return conName;
    }
    public void setConName(String conName) {
        this.conName = conName;
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
    public String getAbs() {
        return abs;
    }
    public void setAbs(String abs) {
        this.abs = abs;
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
    public Clob getConInfo() {
        return conInfo;
    }
    public void setConInfo(Clob conInfo) {
        this.conInfo = conInfo;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getConStatus() {
        return conStatus;
    }
    public void setConStatus(String conStatus) {
        this.conStatus = conStatus;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getCheckboxHide() {
        // 审批列表、合同类型不为空、指令状态为失败时可选择
        if (StringUtils.isNotEmpty(this.getBusinessKey()) || StringUtils.isNotEmpty(this.conOpType)
                || AbPoolConstant.AB_POOL_OPT_STATUS_1.equals(this.getOptStatus())) {
            return "0";
        }
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
    public String getConOpType() {
        return conOpType;
    }
    public void setConOpType(String conOpType) {
        this.conOpType = conOpType;
    }
    public String getConOpTypeName() {
        return conOpTypeName;
    }
    public void setConOpTypeName(String conOpTypeName) {
        this.conOpTypeName = conOpTypeName;
    }
    public String getConOptStatus() {
        return conOptStatus;
    }
    public void setConOptStatus(String conOptStatus) {
        this.conOptStatus = conOptStatus;
    }
    public String getConOptStatusName() {
        return conOptStatusName;
    }
    public void setConOptStatusName(String conOptStatusName) {
        this.conOptStatusName = conOptStatusName;
    }
    public Date getBusinessDate() {
        return businessDate;
    }
    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }
    public String getVoucherId() {
        return voucherId;
    }
    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public String getPayNo() {
        return payNo;
    }
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    @Override
    public String toString() {
        return "AbPoolEmitFinaApplyVO{" +
                "id='" + id + '\'' +
                ", dtlId='" + dtlId + '\'' +
                ", aid='" + aid + '\'' +
                ", conId='" + conId + '\'' +
                ", origNo='" + origNo + '\'' +
                ", conFtype='" + conFtype + '\'' +
                ", conName='" + conName + '\'' +
                ", corpCodeAndName='" + corpCodeAndName + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", corpName='" + corpName + '\'' +
                ", billType=" + billType +
                ", billTypeName='" + billTypeName + '\'' +
                ", billFlag=" + billFlag +
                ", billFlagName='" + billFlagName + '\'' +
                ", billMoney=" + billMoney +
                ", payeracc='" + payeracc + '\'' +
                ", appNo='" + appNo + '\'' +
                ", payer='" + payer + '\'' +
                ", payerbankno='" + payerbankno + '\'' +
                ", payerBankName='" + payerBankName + '\'' +
                ", abs='" + abs + '\'' +
                ", banendrsmtmk='" + banendrsmtmk + '\'' +
                ", banendrsmtmkName='" + banendrsmtmkName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", payee='" + payee + '\'' +
                ", payeeacc='" + payeeacc + '\'' +
                ", payeeBankName='" + payeeBankName + '\'' +
                ", payeebankno='" + payeebankno + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", acceptoracc='" + acceptoracc + '\'' +
                ", acceptorBankName='" + acceptorBankName + '\'' +
                ", acceptorbankno='" + acceptorbankno + '\'' +
                ", pbCodeAndName='" + pbCodeAndName + '\'' +
                ", bpId='" + bpId + '\'' +
                ", conInfo=" + conInfo +
                ", fileId='" + fileId + '\'' +
                ", conStatus='" + conStatus + '\'' +
                ", checkboxHide='" + checkboxHide + '\'' +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", optStatusName='" + optStatusName + '\'' +
                ", conOptStatus='" + conOptStatus + '\'' +
                ", conOptStatusName='" + conOptStatusName + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", conOpType='" + conOpType + '\'' +
                ", conOpTypeName='" + conOpTypeName + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", businessDate=" + businessDate +
                ", voucherId='" + voucherId + '\'' +
                ", source='" + source + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", payNo='" + payNo + '\'' +
                ", payId='" + payId + '\'' +
                '}';
    }
}
