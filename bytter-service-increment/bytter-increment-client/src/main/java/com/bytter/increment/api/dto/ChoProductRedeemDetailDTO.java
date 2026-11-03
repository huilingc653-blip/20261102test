package com.bytter.increment.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 赎回明细表
 */
@Data
public class ChoProductRedeemDetailDTO implements Serializable {

    /**产品登记单号*/
    private String reqId;
    /**赎回Id*/
    private String cprId;
    /**赎回金额*/
    private BigDecimal redeemMoney;
    /**赎回日期*/
    private Date redeemDate;
    /**到账日期*/
    private Date transferDate;
    /**收益金额*/
    private BigDecimal incomeMoney;
    /**实际收益率*/
    private BigDecimal incomeRate;
    /**预计参考利率*/
    private String expectRate;
    /**备注*/
    private String rmk;
    /**申购费*/
    private BigDecimal subFeeMoney;
    /**赎回费*/
    private BigDecimal redeemChargeMoney;
    /**其他手续费*/
    private BigDecimal chargeMoney;
    /**银行流水认领组件挂接确认*/
    private String collectionConfirm;

    /**
     * 赎回日净值
     */
    private BigDecimal redeemDailyNetVal;

    /**
     * 赎回份额
     */
    private BigDecimal redeemPortion;
}
