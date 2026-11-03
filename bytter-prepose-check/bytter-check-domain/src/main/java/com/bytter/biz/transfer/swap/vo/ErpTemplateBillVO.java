package com.bytter.biz.transfer.swap.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by zhouj on 2018/6/7.
 */
public class ErpTemplateBillVO implements Serializable {
    private String id;
    @BtCompositeSearch(fieldName = "b.trans_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transTime;//出票时间
    @BtCompositeSearch(fieldName = "b.maturity_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;//到期时间
    @BtCompositeSearch(fieldName = "b.BILL_OP_VESTING_DATE")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date businessDate;//业务办理日期
    @BtCompositeSearch(fieldName = "h.status")
    private Long status;
    private String templateId;
    private String templateName;
    private String typeId;
    private String typeName;
    @BtCompositeSearch(fieldName = "b.bill_type")
    private Integer billType;//票据类型
    @BtCompositeSearch(fieldName = "b.bill_flag")
    private Integer billFlag; //流通介质
    @BtCompositeSearch(fieldName = "b.operation_Id")
    private String tradeType;//业务类型
    @BtCompositeSearch(fieldName = "b.code")
    private String code;//票据号
    @BtCompositeSearch(fieldName = "b.money")
    private BigDecimal money;//票据金额
    @BtCompositeSearch(fieldName = "b.payer")
    private String payer;//付款人
    private String payerName;//付款名称
    @BtCompositeSearch(fieldName = "b.payer_bank_acc")
    private String payerAcc;//付款账号
    @BtCompositeSearch(fieldName = "b.acceptor")
    private String acceptor;//承兑人
    private String acceptorBankNo;//承兑人开户行行号
    private String acceptorAcc;//承兑人账号
    private String acceptorBankName;//承兑人开户行名称
    @BtCompositeSearch(fieldName = "b.receiver")
    private String receiver;//收款人
    private String receiverName;//收款人
    @BtCompositeSearch(fieldName = "b.receiver_bank_acc")
    private String receiverAcc;//收款人
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpId;
    private String corpName;
    private String abs;//摘要
    private String rmk;//备注
    /**
     * 已补录模板ID
     */
    private String storeId;
    /**
     * Erp反馈编码
     */
    @BtCompositeSearch(fieldName = "h.send_back_no")
    private String sendBackNo;
    private String sendReason;
    private String opName;
    private Date opDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getTransTime() {
        return transTime;
    }
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }
    public Long getStatus() {
        return status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getTradeType() {
        return tradeType;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getPayerName() {
        return payerName;
    }
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getReceiverAcc() {
        return receiverAcc;
    }
    public void setReceiverAcc(String receiverAcc) {
        this.receiverAcc = receiverAcc;
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    public Date getMaturityDate() {
        return maturityDate;
    }
    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }
    public Date getBusinessDate() {
        return businessDate;
    }
    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }
    public String getAcceptorBankNo() {
        return acceptorBankNo;
    }
    public void setAcceptorBankNo(String acceptorBankNo) {
        this.acceptorBankNo = acceptorBankNo;
    }
    public String getAcceptorAcc() {
        return acceptorAcc;
    }
    public void setAcceptorAcc(String acceptorAcc) {
        this.acceptorAcc = acceptorAcc;
    }
    public String getAcceptorBankName() {
        return acceptorBankName;
    }
    public void setAcceptorBankName(String acceptorBankName) {
        this.acceptorBankName = acceptorBankName;
    }
    public String getAbs() {
        return abs;
    }
    public void setAbs(String abs) {
        this.abs = abs;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getSendBackNo() {
        return sendBackNo;
    }
    public void setSendBackNo(String sendBackNo) {
        this.sendBackNo = sendBackNo;
    }
    public String getSendReason() {
        return sendReason;
    }
    public void setSendReason(String sendReason) {
        this.sendReason = sendReason;
    }
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
    
}
