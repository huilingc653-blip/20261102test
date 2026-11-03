package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;

import java.math.BigDecimal;

/**
 * @Title: VO
 * @Description: 单位敞口上报明细表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
public class FexExposureDetailVO implements java.io.Serializable {
    /**ID*/
    private String id;
    /**敞口上报ID*/
    private String exposureId;
    /**币别ID*/
    @BtCompositeSearch(fieldName = "t.cur_Id")
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    private String curId;
    private String curName;
    private String englishCode;
    /**科目代码*/
    private String itemCode;
    /**科目名称*/
    private String itemName;
    /**上级科目*/
    private String superItemCode;
    private String superItemName;
    /**资金流向 1,资金流入 -1,资金流出*/
    private Integer sign;
    /**科目内是否自然对冲  1-是 0-否*/
    private String isNaturalHedge;
    /**填报金额*/
    private BigDecimal fillMoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExposureId() {
        return exposureId;
    }

    public void setExposureId(String exposureId) {
        this.exposureId = exposureId;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
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

    public String getSuperItemCode() {
        return superItemCode;
    }

    public void setSuperItemCode(String superItemCode) {
        this.superItemCode = superItemCode;
    }

    public String getSuperItemName() {
        return superItemName;
    }

    public void setSuperItemName(String superItemName) {
        this.superItemName = superItemName;
    }

    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public String getIsNaturalHedge() {
        return isNaturalHedge;
    }

    public void setIsNaturalHedge(String isNaturalHedge) {
        this.isNaturalHedge = isNaturalHedge;
    }

    public BigDecimal getFillMoney() {
        return fillMoney;
    }

    public void setFillMoney(BigDecimal fillMoney) {
        this.fillMoney = fillMoney;
    }
}
