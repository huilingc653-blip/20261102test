package com.bytter.report.domain.cms.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行授信余额表分配明细VO
 *
 * @author fangcf
 * @date 2025-07-01
 */
@Data
public class BankCreditBalanceDetailVO implements Serializable {

    /**
     * 授信分配编号
     */
    private String disCode;
    /**
     * 授信品种
     */
    private String itemCode;
    /**
     * 授信品种名称
     */
    private String itemName;
    /**
     * 品种标识
     */
    private String itemFlag;
    /**
     * 综合额度使用标识
     */
    private String compSign;
    /**
     * 授信类型：1：敞口，2：低风险
     */
    private String creditType;
    /**
     * 授信金额
     */
    private BigDecimal money;
    /**
     * 最大金额
     */
    private BigDecimal maxMoney;
    /**
     * 占用金额
     */
    private BigDecimal superProcessMoney;
    /**
     * 调剂金额
     */
    private BigDecimal superReliefMoney;
    /**
     * 被调剂金额
     */
    private BigDecimal superByReliefMoney;
    /**
     * 冻结金额
     */
    private BigDecimal freezeMoney;
    /**
     * 虚拟占用金额
     */
    private BigDecimal reliefAndUrationMoney;
    /**
     * 虚拟调剂金额
     */
    private BigDecimal virtualReliefMoney;
    /**
     * 虚拟占用金额
     */
    private BigDecimal virtualUration;
    /**
     * 占用最高额度
     */
    private BigDecimal processMaxMoney;
    /**
     * 调剂最高额度
     */
    private BigDecimal reliefMaxMoney;
    /**
     * 被调剂最高额度
     */
    private BigDecimal byReliefMaxMoney;
    /**
     * 冻结最高额度
     */
    private BigDecimal freezeMaxMoney;
    /**
     * 虚拟占用最高额度
     */
    private BigDecimal virtualMaxMoney;
}
