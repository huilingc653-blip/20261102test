package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 理财产品净值维护实体
 *
 * @author ouyj
 */
@Entity
@Table(name = "CHO_PRODUCT_NETVAL")
public class ChoProductNetVal extends IdEntity implements Serializable {
    /**
     * 登记id
     */
    private String reqId;
    /**
     * 赎回id
     */
    private String redeemId;
    /**
     * 日期
     */
    private Date netValDate;
    /**
     * 购买日期
     */
    private BigDecimal buyMoney;
    /**
     * 购买份额
     */
    private BigDecimal buyPortion;
    /**
     * 日净值
     */
    private BigDecimal dailyNetVal;
    /**
     * 每万份收益
     */
    private BigDecimal tenThousandIncome;
    /**
     * 当日收益
     */
    private BigDecimal dayIncome;
    /**
     * 实际收益
     */
    private BigDecimal actualIncome;
    /**
     * 余额
     */
    private BigDecimal bal;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 是否详情页
     */
    private String isView;
    @Transient
    public String getIsView() {
        return isView;
    }
    public void setIsView(String isView) {
        this.isView = isView;
    }
    @Column(name = "REQ_ID")
    public String getReqId() {
        return reqId;
    }
    public void setReqId(String reqId) {
        this.reqId = reqId;
    }
    @Column(name = "REDEEM_ID")
    public String getRedeemId() {
        return redeemId;
    }
    public void setRedeemId(String redeemId) {
        this.redeemId = redeemId;
    }
    @Column(name = "NET_VAL_DATE")
    public Date getNetValDate() {
        return netValDate;
    }
    public void setNetValDate(Date netValDate) {
        this.netValDate = netValDate;
    }
    @Column(name = "BUY_MONEY")
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }
    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }
    @Column(name = "BUY_PORTION")
    public BigDecimal getBuyPortion() {
        return buyPortion;
    }
    public void setBuyPortion(BigDecimal buyPortion) {
        this.buyPortion = buyPortion;
    }
    @Column(name = "DAILY_NET_VAL")
    public BigDecimal getDailyNetVal() {
        return dailyNetVal;
    }
    public void setDailyNetVal(BigDecimal dailyNetVal) {
        this.dailyNetVal = dailyNetVal;
    }
    @Column(name = "TEN_THOUSAND_INCOME")
    public BigDecimal getTenThousandIncome() {
        return tenThousandIncome;
    }
    public void setTenThousandIncome(BigDecimal tenThousandIncome) {
        this.tenThousandIncome = tenThousandIncome;
    }
    @Column(name = "DAY_INCOME")
    public BigDecimal getDayIncome() {
        return dayIncome;
    }
    public void setDayIncome(BigDecimal dayIncome) {
        this.dayIncome = dayIncome;
    }
    @Column(name = "ACTUAL_INCOME")
    public BigDecimal getActualIncome() {
        return actualIncome;
    }
    public void setActualIncome(BigDecimal actualIncome) {
        this.actualIncome = actualIncome;
    }
    @Column(name = "BAL")
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true, length = 6)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true, length = 6)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
