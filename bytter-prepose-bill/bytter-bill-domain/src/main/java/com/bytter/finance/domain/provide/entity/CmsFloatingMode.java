package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: CMS_FOLATING_MODE
 * @date 2017-08-24 11:09:43
 */
@Entity
@Table(name = "CMS_FLOATING_MODE", schema = "")
@SuppressWarnings("serial")
public class CmsFloatingMode extends IdEntity implements java.io.Serializable {
    /**
     * 发放id
     */
    private String billCode;
    /**
     * 启动日期
     */
    private Date enterDate;
    /**
     * 浮动方式
     */
    private String floatingMode;
    /**
     * 浮动方式显示值
     */
    private String floatingModeName;
    /**
     * 基础利率
     */
    private BigDecimal rate;
    /**
     * 浮动方式二级菜单
     */
    private String floatModeNext;
    /**
     * 固定利率
     */
    private String fixedRate;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 浮动点数或百分比
     */
    private BigDecimal pointsText;
    /** 基准利率类型 */
    private String referenceRateType;
    /** 贷款周期 */
    private String repayCycle;
    /**
     * 利率报价日期
     */
    private Date rateQuotedPriceDate;
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    @Column(name = "ENTER_DATE")
    public Date getEnterDate() {
        return enterDate;
    }
    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "FLOATING_MODE")
    public String getFloatingMode() {
        return floatingMode;
    }
    public void setFloatingMode(String floatingMode) {
        this.floatingMode = floatingMode;
    }
    @Column(name = "FLOAT_MODE_NEXT")
    public String getFloatModeNext() {
        return floatModeNext;
    }
    public void setFloatModeNext(String floatModeNext) {
        this.floatModeNext = floatModeNext;
    }
    @Column(name = "FLOATING_MODENAME")
    public String getFloatingModeName() {
        return floatingModeName;
    }
    public void setFloatingModeName(String floatingModeName) {
        this.floatingModeName = floatingModeName;
    }
    public String getFixedRate() {
        return fixedRate;
    }
    public void setFixedRate(String fixedRate) {
        this.fixedRate = fixedRate;
    }
    @Column(name = "POINTS_TEXT")
    public BigDecimal getPointsText() {
        return pointsText;
    }
    public void setPointsText(BigDecimal pointsText) {
        this.pointsText = pointsText;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 基准利率类型
     */
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款周期
     */
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    @Column(name = "RATE_QUOTED_PRICE_DATE")
    public Date getRateQuotedPriceDate() {
        return rateQuotedPriceDate;
    }
    public void setRateQuotedPriceDate(Date rateQuotedPriceDate) {
        this.rateQuotedPriceDate = rateQuotedPriceDate;
    }
}
