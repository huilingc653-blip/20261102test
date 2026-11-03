package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 单位敞口上报明细表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
@Entity
@Table(name = "FEX_EXPOSURE_DETAIL", schema = "")
public class FexExposureDetail extends IdEntity implements java.io.Serializable {
    /**敞口上报ID*/
    private String exposureId;
    /**币别ID*/
    private String curId;
    /**科目代码*/
    private String itemCode;
    /**科目名称*/
    private String itemName;
    /**资金流向 1,资金流入 -1,资金流出*/
    private Integer sign;
    /**科目内是否自然对冲  1-是 0-否*/
    private String isNaturalHedge;
    /**填报金额*/
    private BigDecimal fillMoney;

    @Column(name ="EXPOSURE_ID")
    public String getExposureId() {
        return exposureId;
    }

    public void setExposureId(String exposureId) {
        this.exposureId = exposureId;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="ITEM_CODE",nullable=false,length=40)
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Column(name ="ITEM_NAME",nullable=true,length=100)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Column(name ="SIGN",nullable=true,length=5)
    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    @Column(name ="IS_NATURAL_HEDGE",nullable=false,length=10)
    public String getIsNaturalHedge() {
        return isNaturalHedge;
    }

    public void setIsNaturalHedge(String isNaturalHedge) {
        this.isNaturalHedge = isNaturalHedge;
    }

    @Column(name ="FILL_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getFillMoney() {
        return fillMoney;
    }

    public void setFillMoney(BigDecimal fillMoney) {
        this.fillMoney = fillMoney;
    }
}
