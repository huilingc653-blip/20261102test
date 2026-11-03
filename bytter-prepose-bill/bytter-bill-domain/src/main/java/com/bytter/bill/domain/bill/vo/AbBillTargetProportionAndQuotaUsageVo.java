package com.bytter.bill.domain.bill.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 银承占比及额度使用情况统计表数据VO
 *
 * @author fangcf
 * @date 2025-09-12
 */
@Data
public class AbBillTargetProportionAndQuotaUsageVo {

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
    // 所有父单位id
    private String parendIds;
    //仓库编码
    private String unitStoreMaintenanceId;
    //仓库名称
    private String unitStoreMaintenanceName;
    //目标
    private BigDecimal targetProportion;
    //实际
    private BigDecimal actual;
    //差异
    private BigDecimal difference;
    //提升比例
    private BigDecimal increaseProportion;
    //银行额度
    private BigDecimal bankCredit;
    //本月止未到期开票金额
    private BigDecimal unexpiredBillMoney;
    //本月末授信剩余额度
    private BigDecimal leftQuota;
    //本月货款支付总额
    private BigDecimal totalPay;
    //本月货款电汇金额
    private BigDecimal eBankPay;
    //本月总开票金额
    private BigDecimal openBillPay;
    //上月总开票金额
    private BigDecimal preOpenBillPay;
    //上月货款电汇金额
    private BigDecimal preEBankPay;
    //上月实际
    private BigDecimal preActual;
    // 查询日期，年月
    private String ym;
}
