package com.bytter.biz.expense.vo;

import com.bytter.system.api.dto.CorpDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NisExpenseDetailIniVO {
    /**
     * 费用报销参数配置：budgetSel-是否挂接资金计划
     */
    private String budgetSel;
    /**
     * 费用报销参数配置：payItem-付款方式默认走的计划科目
     */
    private String payItem;
    /**
     * 费用报销参数配置：expFbsin-报销计划挂接环节
     */
    private String expFbsin;
    /**
     * 费用报销参数配置：accountOver-内部账户余额不足
     */
    private String accountOver;
    /**
     * 费用报销参数配置：bankaccOver-银行账户余额不足
     */
    private String bankaccOver;
    /**
     * 支付结算参数配置：showIsPayed-显示已付款是标识
     */
    private String showIsPayed;
    /**
     * 是否启用限额管理
     * 0-否,1-是
     */
    private String isOpenLimit;
    /**
     * 执行业务名称
     */
    private String businessName;
    /**
     * 执行系统名称：nis-结算管理
     */
    private String processSysname;
    /**
     * 费用报销详情
     */
    private NisExpenseDetailVO nisExpenseDetailPage;
    /**
     * 业务类型
     */
    private String busType;
    /**
     *  //汇总明细列表
     */
    private List<NisExpenseDetailVO> listNisExpenseDetail;
    /**
     * 支付金额
     */
    private BigDecimal payMoney;
    /**
     * 期望支付时间
     */
    private String wishPayday;
    /**
     * 单位信息
     */
    private CorpDTO corp;
    /**
     * 任务ID
     */
    private String  taskId;


}
