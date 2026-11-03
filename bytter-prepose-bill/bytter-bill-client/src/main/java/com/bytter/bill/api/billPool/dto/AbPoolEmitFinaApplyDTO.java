package com.bytter.bill.api.billPool.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;
/**
 * @author zhenghj
 * @Title: AbPoolEmitFinaApplyDTO
 * @Description: 融资申请VO
 * @date 2022/7/27
 */
public class AbPoolEmitFinaApplyDTO implements Serializable {
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
    private String corpId;
    private String corpCode;
    private String corpName;
    /**
     * 票据类型
     */
    private Integer billType;
    private String billTypeName;
    /**
     * 票据介质
     */
    private Integer billFlag;
    private String billFlagName;
    /**
     * 票据金额
     */
    private BigDecimal billMoney;
    /**
     * 申请人账号
     */
    private String payeracc;
    /**
     * 申请单号
     */
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
    private String abs;
    /**
     * 不得转让标记EM00可再转让EM01不得转让
     */
    private String banendrsmtmk;
    private String banendrsmtmkName;
    /**
     * 出票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 收票人
     */
    private String payee;
    /**
     * 收票人账号
     */
    private String payeeacc;
    /**
     * 收票人开户银行名称
     */
    private String payeeBankName;
    /**
     * 收票人开户银行行号
     */
    private String payeebankno;
    /**
     * 承兑人
     */
    private String acceptor;
    /**
     * 承兑人账号
     */
    private String acceptoracc;
    /**
     * 承兑人开户行名称
     */
    private String acceptorBankName;
    /**
     * 承兑人开户行行号
     */
    private String acceptorbankno;
    /**
     * 票据池
     */
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
     * 审批状态
     */
    private Integer status;
    private String statusName;
    /**
     * 指令状态
     */
    private String optStatus;
    private String optStatusName;
    /**
     * 指令状态
     */
    private String conOptStatus;
    private String conOptStatusName;
    private String returnCode;
    private String conOpType;
    private String conOpTypeName;
    private String returnMsg;
    /**
     * 业务日期
     */
    private Date businessDate;
    /**外部系统id编码*/
    private String voucherId;
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
    @Override
    public String toString() {
        return "AbPoolEmitFinaApplyDTO{" +
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
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", optStatusName='" + optStatusName + '\'' +
                ", conOptStatus='" + conOptStatus + '\'' +
                ", conOptStatusName='" + conOptStatusName + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", conOpType='" + conOpType + '\'' +
                ", conOpTypeName='" + conOpTypeName + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", businessDate=" + businessDate +
                ", voucherId='" + voucherId + '\'' +
                '}';
    }
}
