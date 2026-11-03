package com.bytter.biz.wage.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class NisWageUpdateVO implements Serializable {
    /**
     * 代发工资
     */
    private NisWageVO nisWagePage;
    /**
     * 代发工资参数配置：bankaccOver-银行账户余额不足
     */
    private String bankaccOver;
    /**
     * 期望支付时间
     */
    private String wishPayday;
    /**
     *
     */
    private String user;
    /**
     * 代发工资参数配置：budgetSel-是否挂接资金计划
     */
    private String budgetSel;
    /**
     * 代发工资参数配置：payItem-默认走的计划科目
     */
    private String payItem;

    /**
     * 资金计划
     */
    private String businessName;
    /**
     * 执行系统名称：nis-结算管理
     */
    private String processSysname;
    /**
     * 是否启用限额管理  1-是 0-否
     */
    private String isOpenLimit;
    /**
     * 是否支付
     */
    private String isPay;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 退单类型
     */
    private String chargeBackType;
    /**
     * 可用余额
     */
    private BigDecimal bal;
    /**
     * 代发工资数量
     */
    private Integer count;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 代发工资参数配置：wagesVerify-是否显示明细金额
     */
    private String   wagesVerify;
    /**
     * 创造者
     */
    private String   creator;

}