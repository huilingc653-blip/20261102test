package com.bytter.biz.erp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Erp代发工资单据vo
 *
 * @author: fangcf
 * @date: 2026-5-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErpNisWageVo implements Serializable {

    /**
     * 主键id
     */
    private String id;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 数据来源
     */
    private String dataSource;
    /**
     * 批次号/单据号码
     */
    private String wageNo;
    /**
     * 工资册标题
     */
    private String wageTitle;
    /**
     * 单位
     */
    private String corpCode;
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    private String payerAccNo;
    /**
     * 付款账号名称
     */
    private String payerAccName;
    /**
     * 付款开户行
     */
    private String payerBankName;
    /**
     * 付款开户行行号
     */
    private String payerBankCode;
    /**
     * 账号所在省
     */
    private String payerProv;
    /**
     * 账号所在市
     */
    private String payerCity;
    /**
     * 币别
     */
    private String curCode;
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 接口代码
     */
    private String bifCode;
    private String bifCodeId;
    /**
     * 金额
     */
    private BigDecimal wages;
    /**
     * 工资发放日期 String
     */
    private String wageDay;
    /**
     * 工资发放日期 Date
     */
    private Date erpWageDay;

    /**
     * 明细
     */
    private List<ErpNisWageDetailVo> nisWageDetailList;


}
