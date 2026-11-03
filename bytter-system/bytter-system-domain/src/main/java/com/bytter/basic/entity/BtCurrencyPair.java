package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 货币对管理
 * @date 2020-1-19 11:14:22
 */
@Entity
@Table(name = "BT_CURRENCY_PAIR")
public class BtCurrencyPair extends IdEntity implements Serializable {
    /**
     * 币种1
     */
    private String curId1;
    /**
     * 币种2
     */
    private String curId2;
    /**
     * 标准货币
     */
    private String baseCurId;
    /**
     * 汇率示例
     */
    private BigDecimal exchangeRate;
    /**
     * 有效标志
     */
    private String validSign;
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
    @Column(name = "CUR_ID_1", nullable = false, length = 32)
    public String getCurId1() {
        return curId1;
    }
    public void setCurId1(String curId1) {
        this.curId1 = curId1;
    }
    @Column(name = "CUR_ID_2", nullable = false, length = 32)
    public String getCurId2() {
        return curId2;
    }
    public void setCurId2(String curId2) {
        this.curId2 = curId2;
    }
    @Column(name = "BASE_CUR_ID", nullable = false, length = 32)
    public String getBaseCurId() {
        return baseCurId;
    }
    public void setBaseCurId(String baseCurId) {
        this.baseCurId = baseCurId;
    }
    @Column(name = "EXCHANGE_RATE", nullable = true, scale = 4, length = 12)
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
