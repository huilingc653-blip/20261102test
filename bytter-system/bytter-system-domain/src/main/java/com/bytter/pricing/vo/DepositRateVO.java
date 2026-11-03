package com.bytter.pricing.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: DepositRateVO
 * @ProjectName T2_IDEA
 * @Description: 存款利率
 * @date 2019/5/23 9:39
 */
public class DepositRateVO extends ExcelModel implements Serializable,  IBusinessKey {
    private String id;
    /**存款利率类型*/
    private String depositRateType;
    /**利率*/
    @BtCompositeSearch(fieldName = "m.rate")
    private BigDecimal rate;
    /** 生效时间*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "m.time_of_take_effect")
    private Date timeOfTakeEffect;
    /**最近修改时间*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date timeOfLastModifier;
    /**状态*/
    @BtCompositeSearch(fieldName = "m.status")
    private Integer status;
    /**打回说明*/
    private String returndeScribe;
    /**币别*/
    @Currency(inject="curName")
    @BtCompositeSearch(fieldName = "m.money_type")
    private String moneyType;


    @BtCompositeSearch(fieldName = "bc.cur_name")
    private String curName;
    /**创建人**/
    @BtCompositeSearch(fieldName="m.create_By")
    private String createBy;
    /**类型编码*/
    @BtCompositeSearch(fieldName="t.code")
    private String rateTypeCode;
    /**类型名称*/
    @BtCompositeSearch(fieldName="t.period_name")
    private String rateTypeName;
    /**存款种类*/
    @BtCompositeSearch(fieldName="t.loan_class")
    private String depositClass;
    /**保存类型：1-提交 0-暂存*/
    private String saveType;
    /**备注*/
    private String remark;
    /**复核人*/
    private String checkOp;
    /**利率种类 0-基准利率，1-基础利率*/

    @BtCompositeSearch(fieldName="m.reference_rate_type")
    private String referenceRateType;
    /**存款账户标识  1-内部账户存款 2-资金池账户存款 3-银行账户存款 */
    @BtCompositeSearch(fieldName="t.deposit_acc_sign")
    private String depositAccSign;
    /**利率上限*/
    @BtCompositeSearch(fieldName="m.rate_up_limit")
    private BigDecimal rateUpLimit;
    /**利率下限*/
    @BtCompositeSearch(fieldName="m.rate_down_limit")
    private BigDecimal rateDownLimit;
    /**基准利率*/
    private BigDecimal referenceRate;
    /**基础利率*/
    private BigDecimal baseRate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDepositRateType() {
        return depositRateType;
    }
    public void setDepositRateType(String depositRateType) {
        this.depositRateType = depositRateType;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public Date getTimeOfTakeEffect() {
        return timeOfTakeEffect;
    }
    public void setTimeOfTakeEffect(Date timeOfTakeEffect) {
        this.timeOfTakeEffect = timeOfTakeEffect;
    }
    public Date getTimeOfLastModifier() {
        return timeOfLastModifier;
    }
    public void setTimeOfLastModifier(Date timeOfLastModifier) {
        this.timeOfLastModifier = timeOfLastModifier;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getReturndeScribe() {
        return returndeScribe;
    }
    public void setReturndeScribe(String returndeScribe) {
        this.returndeScribe = returndeScribe;
    }
    public String getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getRateTypeCode() {
        return rateTypeCode;
    }
    public void setRateTypeCode(String rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    public String getRateTypeName() {
        return rateTypeName;
    }
    public void setRateTypeName(String rateTypeName) {
        this.rateTypeName = rateTypeName;
    }
    public String getDepositClass() {
        return depositClass;
    }
    public void setDepositClass(String depositClass) {
        this.depositClass = depositClass;
    }
    public String getSaveType() {
        return saveType;
    }
    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Override
    public String getBusinessKey() {
        return this.id;
    }
    public String getCheckOp() {
        return checkOp;
    }
    public void setCheckOp(String checkOp) {
        this.checkOp = checkOp;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getDepositAccSign() {
        return depositAccSign;
    }
    public void setDepositAccSign(String depositAccSign) {
        this.depositAccSign = depositAccSign;
    }
    public BigDecimal getRateUpLimit() {
        return rateUpLimit;
    }
    public void setRateUpLimit(BigDecimal rateUpLimit) {
        this.rateUpLimit = rateUpLimit;
    }
    public BigDecimal getRateDownLimit() {
        return rateDownLimit;
    }
    public void setRateDownLimit(BigDecimal rateDownLimit) {
        this.rateDownLimit = rateDownLimit;
    }
    public BigDecimal getReferenceRate() {
        return referenceRate;
    }
    public void setReferenceRate(BigDecimal referenceRate) {
        this.referenceRate = referenceRate;
    }
    public BigDecimal getBaseRate() {
        return baseRate;
    }
    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }
}
