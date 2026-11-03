package com.bytter.accountReceivable.agingAnalysis.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账龄分析表
 */
@Data
public class AgingVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //单位
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;

    /**
     *币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;

    //单据编号
    private String code;

    //客商名称
    private String externalName;

    //应收余额
    private BigDecimal writeOffBal;

    //交易合同编号
    private String contractCode;

    //0-30天,未到期金额
    private BigDecimal thirtyDay;

    //31-60天
    private BigDecimal sixtyDay;

    //61-90天
    private BigDecimal ninetyDay;

    //90天以上
    private BigDecimal overNinetyDay;

}
