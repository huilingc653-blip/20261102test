package com.bytter.biz.transfer.swap.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * Created by zhangxb on 2018/12/3.
 */
public class ErpHisDtlVO implements Serializable {
    private String id;
    @BtCompositeSearch(fieldName = "h.status")
    private Long status;
    private String templateId;
    private String typeId;
    private String typeName;
    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "t.trans_time")
    private Date transTime;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "acc.corp_id")
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 本方账号
     */
    @BtCompositeSearch(fieldName = "t.bank_acc")
    private String bankAcc;
    /**
     * 本方账户名
     */
    @BtCompositeSearch(fieldName = "t.acc_name")
    private String accName;
    /**
     * 本方账户开户行
     */
    private String bankName;
    /**
     * 对方账号
     */
    @BtCompositeSearch(fieldName = "t.opp_acc_no")
    private String oppAccNo;
    /**
     * 对方账户名
     */
    @BtCompositeSearch(fieldName = "t.opp_acc_name")
    private String oppAccName;
    /**
     * 对方账户开户行
     */
    private String oppAccBank;
    /**
     * 币别ID
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "t.cur_id")
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 银行类型ID
     */
    @BtCompositeSearch(fieldName = "acc.bank_type_id")
    private String bankTypeId;
    /**
     * 银行类型
     */
    private String bankTypeName;
    /**
     * 借贷标志
     */
    @BtCompositeSearch(fieldName = "t.cd_sign")
    private String cdSign;
    /**
     * 交易金额
     */
    @BtCompositeSearch(fieldName = "t.amt")
    private BigDecimal amt;
    /**
     * 摘要
     */
    @BtCompositeSearch(fieldName = "t.remark")
    private String remark;
    /**
     * 流水单号
     */
    private String dtlId;
    /**
     * Erp反馈编码
     */
    @BtCompositeSearch(fieldName = "h.send_back_no")
    private String sendBackNo;
    private String sendReason;
    private String opName;
    private Date opDate;
    private String extField0;
    private String extField1;
    private String extField2;
    private String extField3;
    private String extField4;
    private String extField5;
    private String extField6;
    private String extField7;
    private String extField8;
    private String extField9;
    List<ErpHisDtlComFieldVO> erpHisDtlComFields;
    public List<ErpHisDtlComFieldVO> getErpHisDtlComFields() {
        return erpHisDtlComFields;
    }
    public void setErpHisDtlComFields(List<ErpHisDtlComFieldVO> erpHisDtlComFields) {
        this.erpHisDtlComFields = erpHisDtlComFields;
    }
    public Long getStatus() {
        return status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
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
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getOppAccNo() {
        return oppAccNo;
    }
    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public String getOppAccBank() {
        return oppAccBank;
    }
    public void setOppAccBank(String oppAccBank) {
        this.oppAccBank = oppAccBank;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    public BigDecimal getAmt() {
        return amt;
    }
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
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
    public String getExtField0() {
        return extField0;
    }
    public void setExtField0(String extField0) {
        this.extField0 = extField0;
    }
    public String getExtField1() {
        return extField1;
    }
    public void setExtField1(String extField1) {
        this.extField1 = extField1;
    }
    public String getExtField2() {
        return extField2;
    }
    public void setExtField2(String extField2) {
        this.extField2 = extField2;
    }
    public String getExtField3() {
        return extField3;
    }
    public void setExtField3(String extField3) {
        this.extField3 = extField3;
    }
    public String getExtField4() {
        return extField4;
    }
    public void setExtField4(String extField4) {
        this.extField4 = extField4;
    }
    public String getExtField5() {
        return extField5;
    }
    public void setExtField5(String extField5) {
        this.extField5 = extField5;
    }
    public String getExtField6() {
        return extField6;
    }
    public void setExtField6(String extField6) {
        this.extField6 = extField6;
    }
    public String getExtField7() {
        return extField7;
    }
    public void setExtField7(String extField7) {
        this.extField7 = extField7;
    }
    public String getExtField8() {
        return extField8;
    }
    public void setExtField8(String extField8) {
        this.extField8 = extField8;
    }
    public String getExtField9() {
        return extField9;
    }
    public void setExtField9(String extField9) {
        this.extField9 = extField9;
    }
}
