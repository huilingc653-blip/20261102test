package com.bytter.credit.domain.vo;

import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.handler.ExcelModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行授信年度表
 *
 * @author loveCamille
 */
@Getter
@Setter
public class BankCreditPlanTraceVO extends ExcelModel implements Serializable, IBusinessKey {

    private String id;

    private String creditType;

    private BigDecimal money;

    private BigDecimal maxMoney;

    private String bankCode;

    private String bankTypeId;

    /**
     * 大区
     */
    private String areaName;

    /**
     * 单位id
     */
    //@BtCompositeSearch(fieldName = "t1.corp_id")
    private String corpId;
    /**
     * 单位名
     */
    private String corpName;

    /**
     * 年度
     */
    //@BtCompositeSearch(fieldName = "t1.year")
    private Integer year;


    /*================= 年度融资规划（万元） =================*/
    /**
     * 年采购量
     */
    private BigDecimal yearPurchase;
    /**
     * 并购投资
     */
    private BigDecimal mergeAcquisitionInvest;
    /**
     * 授信额度需求
     */
    private BigDecimal creditRequire;



    /*================= 计划授信（万元） =================*/
    /**
     * 银行合作情况
     */
    private String cooperationType;
    /**
     * 授信银行名称
     */
    private String planCreditBankName;
    /**
     * 授信额度
     */
    private BigDecimal planCreditLimit;
    /**
     * 额度变动情况
     */
    private BigDecimal creditChange;



    /*================= 授信情况 =================*/
    /**
     * 银行额度
     */
    private BigDecimal bankLimit;
    /**
     * 授信银行名称
     */
    private String creditBankName;
    /**
     * 合同开始日期
     */
    private String contractStartEndDate;

    /**
     * 综合额度
     */
    private BigDecimal comprehensiveLimit;
    /**
     * 流贷额度
     */
    private BigDecimal flowLoanLimit;
    /**
     * 银承额度
     */
    private BigDecimal bankAcceptanceBillLimit;
    /**
     * 其他：默认为 <code>无</code>
     */
    private String otherLimit = "无";
    /**
     * 待分配子公司额度：默认为 <code>-</code>
     */
    private String allocatedSubsidiaryLimit = "-";

    /**
     * 保证金比率
     */
    private BigDecimal marginRatio;
    /**
     * 开票手续费
     */
    private BigDecimal invoiceFee;
    /**
     * 保证金利率
     */
    private String marginRate;
    /**
     * 是否可正常使用
     */
    private String isUsed;



    /*================= 额度使用情况（万元） =================*/
    /**
     * 本期已使用额度
     */
    private BigDecimal currentPeriodUsedLimit;
    /**
     * 本期可使用额度
     */
    private BigDecimal currentPeriodAvailableLimit;
    /**
     * 本年目前实际额度缺口
     */
    private BigDecimal currentPeriodLimitGap;

    /**
     * 上年同期-已使用额度
     */
    private BigDecimal lastPeriodUsedLimit;

    /**
     * 本年已使用额度
     */
    private BigDecimal currentYearUsedLimit;
    /**
     * 本年可使用额度
     */
    private BigDecimal currentYearAvailableLimit;
    /**
     * 本年目前实际额度缺口
     */
    private BigDecimal currentYearLimitGap;

    /**
     * 上年-已使用额度
     */
    private BigDecimal lastYearUsedLimit;



    /**
     * 授信完成率
     */
    private BigDecimal creditCompleteRate;



    /**
     * 解决措施
     */
    private String solveMeasure;



    /**
     * 存在的困难
     */
    private String existDifficulty;



    @Override
    public String getBusinessKey() {
        return getId();
    }

}
