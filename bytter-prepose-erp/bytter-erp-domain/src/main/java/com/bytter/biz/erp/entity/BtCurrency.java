package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 币别表
 *
 * @TableName bt_currency
 */
@Data
public class BtCurrency implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 币别编码
     */
    private String curCode;

    /**
     * 币别名称
     */
    private String curName;

    /**
     * 英文简码
     */
    private String englishCode;

    /**
     * 汇率
     */
    private BigDecimal curExrate;

    /**
     * 币别符号
     */
    private String curSign;

    /**
     * 是否本位币
     */
    private String naturalSign;

    /**
     * 人行编码
     */
    private String pbScaleCode;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 有效标志
     */
    private String validSign;

    /**
     *
     */
    private Date createDate;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private Date updateDate;

    /**
     *
     */
    private String updateBy;

    /**
     * 是否系统初始化
     */
    private String isSystem;

    /**
     *
     */
    private String nonworkDayRule;

    /**
     *
     */
    private String nonworkDay;

    /**
     *
     */
    private String sysCurId;
    /**
     * 租户id
     */
    private String tenantId;
}
