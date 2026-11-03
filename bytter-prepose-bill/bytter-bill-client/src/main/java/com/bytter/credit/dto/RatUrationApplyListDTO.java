package com.bytter.credit.dto;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
    /**
 * 授信占用DTO
 *
 * @author liyh
 */
public class RatUrationApplyListDTO implements Serializable {
    private String id;
    /**
     * 录入单号
     */
    private String oriRatCode;
    /**
     * 业务名称
     */
    private String type;
    private String typeName;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 额度类型
     */
    private String useType;
    /**
     * 占用授信类型
     */
    private String ratType;
    /**
     * 授信额度编号
     */
    private String ratCode;
    /**
     * 授信分配编号
     */
    private String disCode;
    /**
     * 品种标识
     */
    private String itemFlag;
    /**
     * 品种标识
     */
    private String itemCode;
    /**
     * 币别
     */
    private String curCode;
    /**
     * 占用金额
     */
    private BigDecimal money;
    /**
     * 敞口金额
     */
    private BigDecimal openMoney;
    /**
     * 备注
     */
    private String remark;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 开始日期
     */
    private java.util.Date beginDate;
    /**
     * 结束日期
     */
    private java.util.Date endDate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 合同号
     */
    private String contactNo;
    /**
     * 票据号
     */
    private String billNo;
    /**
     * 信用证号
     */
    private String lcNo;
    /**
     * 贷款方式
     */
    private String creditType;
    /**
     * 担保编号
     */
    private String gurCode;
    /**
     * 利率
     */
    private BigDecimal interestrate;
    /**
     * 汇率
     */
    private BigDecimal exchange;
    /**
     * 保证金比例
     */
    private BigDecimal assurepercent;
    /**
     * 占用明细
     */
    private List<RatUrationApplyDtlDTO> applyDtls;
    /**
     * 占用明细lenght
     */
    private Integer applyDtlSize;
    /**
     * 单品种
     */
    private String singItemCode;
    private String singItemName;
    /**
     * 剩余额度
     */
    private BigDecimal leftMoney;
    /**
     * 占用期限
     */
    private Integer applyDays;
    /**
     * 业务金额
     */
    private BigDecimal billMoney;
    /**
     * 业务币种(必填)
     */
//    @Currency(inject = "initCurName")
    private String initCurCode;
    private String initCurName;
    /**
     * 业务名称id
     */
    private String billNameId;
    /**
     * 业务名称
     */
    private String billName;
    /**
     * 保证金金额(必填)
     */
    private BigDecimal bondMoney;
    /**
     * 保证金币种(必填)
     */
//    @Currency(inject = "bondCurName")
    private String bondCurId;
    private String bondCurName;
    /**
     * 是否启用授信敞口(必填，根据授信基础参数设置，需给默认值)
     */
    private String isOpenRat;
    /**
     * 担保方式(必填)
     */
    private String guaWay;
    /**
     * 担保备注
     */
    private String guaRmk;
    /**
     * 额度类型 0-不循环 1-循环
     */
    private String ratCreditType;
    /**
     * 附件ID
     */
    private String attachmentId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getUseType() {
        return useType;
    }
    public void setUseType(String useType) {
        this.useType = useType;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public java.util.Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getLcNo() {
        return lcNo;
    }
    public void setLcNo(String lcNo) {
        this.lcNo = lcNo;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    public String getGurCode() {
        return gurCode;
    }
    public void setGurCode(String gurCode) {
        this.gurCode = gurCode;
    }
    public BigDecimal getExchange() {
        return exchange;
    }
    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }
    public BigDecimal getInterestrate() {
        return interestrate;
    }
    public void setInterestrate(BigDecimal interestrate) {
        this.interestrate = interestrate;
    }
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    public List<RatUrationApplyDtlDTO> getApplyDtls() {
        return applyDtls;
    }
    public void setApplyDtls(List<RatUrationApplyDtlDTO> applyDtls) {
        this.applyDtls = applyDtls;
    }
    public String getSingItemCode() {
        return singItemCode;
    }
    public void setSingItemCode(String singItemCode) {
        this.singItemCode = singItemCode;
    }
    public String getSingItemName() {
        return singItemName;
    }
    public void setSingItemName(String singItemName) {
        this.singItemName = singItemName;
    }
    public Integer getApplyDtlSize() {
        return applyDtlSize;
    }
    public void setApplyDtlSize(Integer applyDtlSize) {
        this.applyDtlSize = applyDtlSize;
    }
    public String getOriRatCode() {
        return oriRatCode;
    }
    public void setOriRatCode(String oriRatCode) {
        this.oriRatCode = oriRatCode;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public Integer getApplyDays() {
        return applyDays;
    }
    public void setApplyDays(Integer applyDays) {
        this.applyDays = applyDays;
    }
    public String getInitCurCode() {
        return initCurCode;
    }
    public void setInitCurCode(String initCurCode) {
        this.initCurCode = initCurCode;
    }
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getBillNameId() {
        return billNameId;
    }
    public void setBillNameId(String billNameId) {
        this.billNameId = billNameId;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public String getGuaRmk() {
        return guaRmk;
    }
    public void setGuaRmk(String guaRmk) {
        this.guaRmk = guaRmk;
    }
    public String getInitCurName() {
        return initCurName;
    }
    public void setInitCurName(String initCurName) {
        this.initCurName = initCurName;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public String getRatCreditType() {
        return ratCreditType;
    }
    public void setRatCreditType(String ratCreditType) {
        this.ratCreditType = ratCreditType;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
