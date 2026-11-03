package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 授信调剂视图
 * 
 * @author liyh
 *
 */
public class RatBkrationReliefListVO implements Serializable, IBusinessKey {
    /** 授信调剂ID */
    private String id;
    /** 授信种类 */
    private String ratType;
    /**
     * 调剂编号
     */
    private String refCode;
    /** 授信编号 */
    @BtCompositeSearch(fieldName = "rb.rat_code")
    private String ratCode;
    /** 单位代码 */
    private String corpId;
    @BtCompositeSearch(fieldName = "rb.corp_id")
    private String corpName;
    /** 币种 */
//    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    private String bankName;
    /** 调剂单位 */
    @BtCompositeSearch(fieldName = "rb.relief_corp")
    private String reliefCorp;
    private String reliefCorpName;
    /** 调剂额度编号 */
    private String reliefCode;
    /** 调剂额度品种 */
    private String reliefItemFlag;
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "reliefItemName")
    private String reliefItemCode;
    private String reliefItemName;
    /** 被调剂单位 */
    private String byReliefCorp;
    private String byReliefCorpName;
    /** 被调剂额度编号 */
    private String byReliefCode;
    /** 被调剂额度品种 */
    private String byReliefItemFlag;
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "byReliefItemName")
    private String byReliefItemCode;
    private String byReliefItemName;
    /** 被调剂金额 */
    private java.math.BigDecimal reliefMoney;
    /** 被调剂敞口金额 */
    private java.math.BigDecimal reliefOpenMoney;
    /** 开始日期 */
    private Date begindate;
    /** 状态 */
    private Integer status;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    @BtCompositeSearch(fieldName = "rb.create_date")
    private Date sDate;
    @BtCompositeSearch(fieldName = "rb.create_date")
    private Date eDate;
    @BtCompositeSearch(fieldName = "rb.create_Date")
    private Date startDate1;
    @BtCompositeSearch(fieldName = "rb.create_Date")
    private Date startDate2;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getReliefCorp() {
        return reliefCorp;
    }
    public void setReliefCorp(String reliefCorp) {
        this.reliefCorp = reliefCorp;
    }
    public String getReliefCorpName() {
        return reliefCorpName;
    }
    public void setReliefCorpName(String reliefCorpName) {
        this.reliefCorpName = reliefCorpName;
    }
    public String getReliefCode() {
        return reliefCode;
    }
    public void setReliefCode(String reliefCode) {
        this.reliefCode = reliefCode;
    }
    public String getReliefItemCode() {
        return reliefItemCode;
    }
    public void setReliefItemCode(String reliefItemCode) {
        this.reliefItemCode = reliefItemCode;
    }
    public String getReliefItemName() {
        return reliefItemName;
    }
    public void setReliefItemName(String reliefItemName) {
        this.reliefItemName = reliefItemName;
    }
    public String getByReliefCorp() {
        return byReliefCorp;
    }
    public void setByReliefCorp(String byReliefCorp) {
        this.byReliefCorp = byReliefCorp;
    }
    public String getByReliefCorpName() {
        return byReliefCorpName;
    }
    public void setByReliefCorpName(String byReliefCorpName) {
        this.byReliefCorpName = byReliefCorpName;
    }
    public String getByReliefCode() {
        return byReliefCode;
    }
    public void setByReliefCode(String byReliefCode) {
        this.byReliefCode = byReliefCode;
    }
    public String getByReliefItemCode() {
        return byReliefItemCode;
    }
    public void setByReliefItemCode(String byReliefItemCode) {
        this.byReliefItemCode = byReliefItemCode;
    }
    public String getByReliefItemName() {
        return byReliefItemName;
    }
    public void setByReliefItemName(String byReliefItemName) {
        this.byReliefItemName = byReliefItemName;
    }
    public java.math.BigDecimal getReliefMoney() {
        return reliefMoney;
    }
    public void setReliefMoney(java.math.BigDecimal reliefMoney) {
        this.reliefMoney = reliefMoney;
    }
    public java.math.BigDecimal getReliefOpenMoney() {
        return reliefOpenMoney;
    }
    public void setReliefOpenMoney(java.math.BigDecimal reliefOpenMoney) {
        this.reliefOpenMoney = reliefOpenMoney;
    }
    public Date getBegindate() {
        return begindate;
    }
    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getReliefItemFlag() {
        return reliefItemFlag;
    }
    public void setReliefItemFlag(String reliefItemFlag) {
        this.reliefItemFlag = reliefItemFlag;
    }
    public String getByReliefItemFlag() {
        return byReliefItemFlag;
    }
    public void setByReliefItemFlag(String byReliefItemFlag) {
        this.byReliefItemFlag = byReliefItemFlag;
    }
    public String getRefCode() {
        return refCode;
    }
    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getsDate() {
        return sDate;
    }
    public void setsDate(Date sDate) {
        this.sDate = sDate;
    }
    public Date geteDate() {
        return eDate;
    }
    public void seteDate(Date eDate) {
        this.eDate = eDate;
    }
    public Date getStartDate1() {
        return startDate1;
    }
    public void setStartDate1(Date startDate1) {
        this.startDate1 = startDate1;
    }
    public Date getStartDate2() {
        return startDate2;
    }
    public void setStartDate2(Date startDate2) {
        this.startDate2 = startDate2;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
