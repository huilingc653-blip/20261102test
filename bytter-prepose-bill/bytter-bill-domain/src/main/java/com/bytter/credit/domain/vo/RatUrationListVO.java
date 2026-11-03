package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 授信占用申请VO
 * 
 * @author liyh
 *
 */
public class RatUrationListVO implements Serializable, IBusinessKey {
    private String id;
    /** 申请编号 */
    @BtCompositeSearch(fieldName = "ru.apply_code")
    private String applyCode;
    /** 申请编号 */
    private String urationCode;
    /** 业务名称 */
    private String type;
    /** 单位代码 */
    @BtCompositeSearch(fieldName = "ru.corp_id")
    private String corpId;
    private String corpName;
    /** 额度类型 */
    private String useType;
    /** 占用授信类型 */
    @BtCompositeSearch(fieldName = "ru.rat_type")
    private String ratType;
    /** 授信额度编号 */
    @BtCompositeSearch(fieldName = "ru.rat_code")
    private String ratCode;
    /** 授信品种 */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    private String itemName;
    /** 品种标识 */
    private String itemFlag;
    /** 币别 */
    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    /** 是否折合本位币 */
    private String isCur;
    /** 占用金额 */
    private java.math.BigDecimal money;
    /** 敞口金额 */
    private java.math.BigDecimal openMoney;
    /** 备注 */
    private String remark;
    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 占用到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleenddate;
    /** 状态 */
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(orderBy = "desc", fieldName = "ru.create_Date")
    private Date createDate;
    @BtCompositeSearch(fieldName = "ru.create_Date")
    private Date startDate1;
    @BtCompositeSearch(fieldName = "ru.create_Date")
    private Date startDate2;
    /** 额度类型 0-不循环 1-循环 */
    @Dict(dictGroup = "rat.creditType", inject = "creditTypeName")
    private String creditType;
    /** 额度类型 0-不循环 1-循环 */
    private String creditTypeName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getApplyCode() {
        return applyCode;
    }
    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }
    public String getUrationCode() {
        return urationCode;
    }
    public void setUrationCode(String urationCode) {
        this.urationCode = urationCode;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
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
    public String getIsCur() {
        return isCur;
    }
    public void setIsCur(String isCur) {
        this.isCur = isCur;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    public java.math.BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(java.math.BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getScheduleenddate() {
        return scheduleenddate;
    }
    public void setScheduleenddate(Date scheduleenddate) {
        this.scheduleenddate = scheduleenddate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    public String getCreditTypeName() {
        return creditTypeName;
    }
    public void setCreditTypeName(String creditTypeName) {
        this.creditTypeName = creditTypeName;
    }
}
