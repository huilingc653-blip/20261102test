package com.bytter.credential.domain.international.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @ClassName LcReceiveLetterReportVO
 * @Description TODO
 * @Author fusb
 * @Date 2020/4/15 16:54
 * @Version 1.0
 **/
public class LcReceiveLetterReportVO  {
    /**
     *主键id
     */
    private String id;
    /**
    *单位
    */
    @BtCompositeSearch(fieldName = "lc.corp_Id")
//    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "lc.corp_name")
    private String corpName;
    /**
     *信用证类型
     */
    @BtCompositeSearch(fieldName = "lc.lc_Inter")
    private String lcInter;
    /**
     *信用证号
     */
    @BtCompositeSearch(fieldName = "lc.credit_No")
    private String creditNo;
    /**
     *合同号
     */
    private String contractNo;
    /**
     *开证人
     */
    private String createBy;
    private String createName;
    /**
     *开证银行
     */
    @BtCompositeSearch(fieldName = "lc.opening_bank_name")
    private String openingBankName;
    /**
     *开证金额
     */
    private BigDecimal creditMoney;
    /**
     *币种
     */
    @BtCompositeSearch(fieldName = "lc.issuing_Cur_Id")
//    @Currency(inject = "issuingCurName")
    private String issuingCurId;
    private String issuingCurName;
    /**
     *登记日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lc.create_Date")
    private Date createDate;
    /**
     *信用证有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lc.expiry_Date")
    private Date expiryDate;
    /**
     *已交单金额
     */
    private BigDecimal sumBillMoney;
    /**
     *收款合计金额
     */
//    @BtCompositeSearch(fieldName = "li.sum_incoming_money")
    private BigDecimal sumIncomingMoney;
    /**
     *状态
     */
    private Integer status;
    /**
     * 附件id
     */
    private String attachmentId;
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
    public String getLcInter() {
        return lcInter;
    }
    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getOpeningBankName() {
        return openingBankName;
    }
    public void setOpeningBankName(String openingBankName) {
        this.openingBankName = openingBankName;
    }
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public String getIssuingCurId() {
        return issuingCurId;
    }
    public void setIssuingCurId(String issuingCurId) {
        this.issuingCurId = issuingCurId;
    }
    public String getIssuingCurName() {
        return issuingCurName;
    }
    public void setIssuingCurName(String issuingCurName) {
        this.issuingCurName = issuingCurName;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public BigDecimal getSumBillMoney() {
        return sumBillMoney;
    }
    public void setSumBillMoney(BigDecimal sumBillMoney) {
        this.sumBillMoney = sumBillMoney;
    }
    public BigDecimal getSumIncomingMoney() {
        return sumIncomingMoney;
    }
    public void setSumIncomingMoney(BigDecimal sumIncomingMoney) {
        this.sumIncomingMoney = sumIncomingMoney;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
