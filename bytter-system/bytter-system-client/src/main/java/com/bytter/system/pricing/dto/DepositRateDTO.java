package com.bytter.system.pricing.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Title: DepositRateDTO
 * Description: 存款利率维护主表DTO
 *
 * @author luxy
 * @since 2019/5/27 10:18
 */
public class DepositRateDTO implements Serializable {
    private String id;
    /**
     * 存款利率类型
     */
    private String depositRateType;
    /**
     * 利率
     */
    private BigDecimal rate;
    /**
     * 生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date timeOfTakeEffect;
    /**
     * 最近修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date timeOfLastModifier;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 打回说明
     */
    private String returndeScribe;
    /**
     * 币别
     */
    @Currency(inject = "curName")
    private String moneyType;
    private String curName;
    /**
     * 创建人
     **/
    private String createBy;
    /**
     * 类型编码
     */
    @BtCompositeSearch(fieldName = "t.code")
    private String rateTypeCode;
    /**
     * 类型名称
     */
    @BtCompositeSearch(fieldName = "t.period_name")
    private String rateTypeName;
    /**
     * 存款种类
     */
    private String depositClass;
    /**
     * 保存类型：1-提交 0-暂存
     */
    private String saveType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 复核人
     */
    private String checkOp;
    /**利率种类 0-基准利率，1-基础利率*/
    private String referenceRateType;
    /**存款账户标识  1-内部账户存款 2-资金池账户存款 3-银行账户存款 */
    private String depositAccSign;
    /**利率上限*/
    private BigDecimal rateUpLimit;
    /**利率下限*/
    private BigDecimal rateDownLimit;
    /**贷款期数*/
    private Integer period;
    /**贷款期数类型*/
    private Integer periodCycle;
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
    public Integer getPeriod() {
        return period;
    }
    public void setPeriod(Integer period) {
        this.period = period;
    }
    public Integer getPeriodCycle() {
        return periodCycle;
    }
    public void setPeriodCycle(Integer periodCycle) {
        this.periodCycle = periodCycle;
    }
}
