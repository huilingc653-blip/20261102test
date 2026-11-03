package com.bytter.report.domain.cms.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行授信余额表VO
 *
 * @author fangcf
 * @date 2025-07-01
 */
@Data
public class BankCreditBalanceVO extends BankCreditBalanceDetailVO implements Serializable {

    /**
     * 区域（二级单位）
     */
    private String secondLevCorpId;
    /**
     * 区域（二级单位名称）
     */
    private String secondLevCorpName;
    /**
     * 二级单位排序
     */
    private Integer sort;
    /**
     * 序号
     */
    private Integer serialNum;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 单位代码
     */
    private String corpCode;
    /**
     * 所有父id
     */
    private String parendIds;
    /**
     * 银行类型
     */
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 银行授信额度
     */
    private BigDecimal bankCreditBal;
    /**
     * 保证金比例
     */
    private BigDecimal bondRatio;
    /**
     * 开票手续费
     */
    private BigDecimal openBillRate;
    /**
     * 保证金利率
     */
    private String bondRate;
    /**
     * 是否能正常使用：1-是，0-否
     */
    private String isUsed;
    /**
     * 已使用银承额度：授信占用表中“票据类”结束日期大于查询开始日期的额度
     */
    private BigDecimal usedBillBal;
    /**
     * 流动资金贷款
     */
    private BigDecimal ldzjLoanBal;
    /**
     * 并购贷款
     */
    private BigDecimal bgLoanBal;
    /**
     * 剩余额度
     */
    private BigDecimal surplusBal;
    /**
     * 可使用额度
     */
    private BigDecimal availBal;
    /**
     * 本周计划开票金额
     */
    private BigDecimal currWeekBillMoney;
    /**
     * 下周计划开票金额
     */
    private BigDecimal nextWeekBillMoney;
    /**
     * 备注
     */
    private String rmk;
}
