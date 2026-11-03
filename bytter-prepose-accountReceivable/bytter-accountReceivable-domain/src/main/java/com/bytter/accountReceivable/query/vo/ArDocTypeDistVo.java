package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author bytter
 * @Date 2025/6/26 下午12:25
 * @Version 1.0
 **/
@Data
public class ArDocTypeDistVo {
    /**
     * 业务类型名称
     */
    private String name;
    /**
     * 占比：该业务类型对应的应收余额占总额的占比
     */
    private String value;
    /**
     * 币种id，用于中间环节计算
     */
    private String curId;
    /**
     * 应收余额，用于中间环节计算
     */
    private BigDecimal writeOffBal;
}
