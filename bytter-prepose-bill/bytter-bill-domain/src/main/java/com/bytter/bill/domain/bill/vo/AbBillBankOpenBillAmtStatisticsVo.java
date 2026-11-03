package com.bytter.bill.domain.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 银承开票金额统计表VO
 *
 * @author fangcf
 * @date 2025-10-28
 */
@Data
public class AbBillBankOpenBillAmtStatisticsVo {

    //区域（二级单位id）
    private String secondLevCorpId;
    //区域（二级单位）
    private String secondLevCorpName;
    //二级单位排序
    private Integer sort;
    //序号
    private Integer serialNum;
    //单位id
    private String corpId;
    //单位名称
    private String corpName;
    //单位代码
    private String corpCode;
    // 所有父单位id
    private String parendIds;

    //银行名称：账号的所属银行
    private String bankName;

    //银行类型
    private String bankTypeId;

    //授信总额；取自授信模块的授信总额
    private BigDecimal ratMoneySum;

    //其中：敞口：取授信品种为【银行承兑汇票（敞口）】额度
    private BigDecimal openMoneySum;

    //其中：低风险：取授信品种为【银行承兑汇票（低风险）】额度
    private BigDecimal lowRiskMoneySum;

    //合同到期日：授信合同到期日；
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date contractEndDate;

    //查询日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date queryDate;
    //查询开始日期
    private String startQueryDate;
    //查询结束日期
    private String endQueryDate;

    //流贷金额
    private BigDecimal loanMoney;

    //本月止未到期开票金额
    private BigDecimal unexpiredBillMoney;

    //本月末授信剩余额度
    private BigDecimal leftQuota;

    //本月止保证金余额
    private BigDecimal unexpiredBailMoney;

    //支付总额
    private BigDecimal payTotalMoney;

    //其中：电汇总额
    private BigDecimal ebankMoney;

    //其中：开票总额
    private BigDecimal openBillTotalMoney;

    //敞口：三个月
    private BigDecimal openBillThreeMonth;

    //敞口：六个月
    private BigDecimal openBillSixMonth;

    //低风险：三个月
    private BigDecimal lowRiskThreeMonth;

    //低风险：六个月
    private BigDecimal lowRiskSixMonth;

    //商承：三个月
    private BigDecimal businessBillThreeMonth;

    //商承：六个月
    private BigDecimal businessBillSixMonth;

    // 开票占比
    private BigDecimal openBillPercent;

    //6个月银承占比
    private BigDecimal sixMonthBankBillPercent;

    //敞口额度已使用总额（票面口径）
    private BigDecimal useBillMoneyByBill;

    //敞口额度已使用总额（银行口径）
    private BigDecimal useBillMoneyByBank;

    //本月未到期（其中：敞口）
    private BigDecimal openUnexpiredBillMoney;

    //本月未到期（其中：低风险）
    private BigDecimal lowRiskUnexpiredBillMoney;

    public AbBillBankOpenBillAmtStatisticsVo() {
    }

    public AbBillBankOpenBillAmtStatisticsVo(String secondLevCorpId, String secondLevCorpName, Integer sort) {
        this.secondLevCorpId = secondLevCorpId;
        this.secondLevCorpName = secondLevCorpName;
        this.sort = sort;
    }
}
