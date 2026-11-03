package com.bytter.finance.domain.directFinance.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-09 16:39
 * @Version 1.0
 */
@Entity
@Table(name = "DF_INTERMEDIATE_COST", schema = "")
@SuppressWarnings("serial")
public class DfIntermediateCost extends IdEntity implements Serializable {
    /**
     * 直融业务id
     */
    private String directFinanceId;
    /**
     * 费用类型id
     */
    private String costTypeId;
    /**
     * 费率
     */
    private BigDecimal rate;
    /**
     * 预计费用
     */
    private BigDecimal advanceFees;
    @Column(name = "DIRECT_FINANCE_ID")
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    @Column(name = "COST_TYPE_ID")
    public String getCostTypeId() {
        return costTypeId;
    }
    public void setCostTypeId(String costTypeId) {
        this.costTypeId = costTypeId;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "ADVANCE_FEES")
    public BigDecimal getAdvanceFees() {
        return advanceFees;
    }
    public void setAdvanceFees(BigDecimal advanceFees) {
        this.advanceFees = advanceFees;
    }
}
