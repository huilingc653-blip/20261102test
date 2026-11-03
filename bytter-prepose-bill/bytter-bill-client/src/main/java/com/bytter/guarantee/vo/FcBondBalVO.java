package com.bytter.guarantee.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FcBondBalVO implements Serializable {
    private String id;
    /** 保证金ID */
    private String bondId;
    /** 存入金额 */
    private java.math.BigDecimal bondMoney;
    /** 追加金额 */
    private java.math.BigDecimal addMoney;
    /** 占用金额 */
    private java.math.BigDecimal applyMoney;
    /** 在途占用金额 */
    private java.math.BigDecimal freezeMoney;
    /** 转回金额 */
    private java.math.BigDecimal backMoney;
    /** 可用余额 */
    private java.math.BigDecimal availMoney;
}
