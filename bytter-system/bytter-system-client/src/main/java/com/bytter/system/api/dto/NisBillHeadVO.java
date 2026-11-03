package com.bytter.system.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@ApiModel("付款办理NisBillHeadVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NisBillHeadVO implements Serializable {
    //期望支付时间
    @ApiModelProperty(value = "期望支付时间")
    private Date endWishPayday;
    //是否支付
    @ApiModelProperty(value = "是否支付")
    private String showIsPayed;
    //来源于ERP的付款单，付款账号是否允许编辑
    @ApiModelProperty(value = "来源于ERP的付款单，付款账号是否允许编辑")
    private String editPayerAcc;
    //是否启用限额管理  1-是 0-否
    @ApiModelProperty(value = "是否启用限额管理  1-是 0-否")
    private String isOpenLimit;
    //是否启用待付款池	  1-是 0-否
    @ApiModelProperty(value = "是否启用待付款池   1-是 0-否")
    private String enablePendingPaymentPool;
    //未产生资金需求付款单付款控制 0-不提醒，1-只提醒不控制，2-控制
    @ApiModelProperty(value = "未产生资金需求付款单付款控制 0-不提醒，1-只提醒不控制，2-控制")
    private String demandControl;
    //  支付结算参数配置：isEnableSplit 是否启用拆分
    @ApiModelProperty(value = "支付结算参数配置：isEnableSplit 是否启用拆分")
    private String isEnableSplit;
    //支付结算参数配置：isEnableMerge 是否启用合并
    @ApiModelProperty(value = "支付结算参数配置：isEnableMerge 是否启用合并")
    private String isEnableMerge;
    // 收款认领管理参数配置：budgetSel-是否挂接资金计划
    @ApiModelProperty(value = "收款认领管理参数配置：budgetSel-是否挂接资金计划")
    private String budgetSel;
    /**
     *
     * 是否挂预算
     */
    @ApiModelProperty(value = "是否挂预算")
    private String isBudget;
    private String payItem;
    /*** 费用报销参数配置：expFbsin-报销计划挂接环节  */
    @ApiModelProperty(value = "费用报销参数配置：expFbsin-报销计划挂接环节 ")
    private String expFbsin;
    @ApiModelProperty(value = "支付结算参数配置：accountOver-内部账户余额不足")
    private String accountOver;
    @ApiModelProperty(value = "支付结算参数配置：bankaccOver-银行账户余额不足")
    private String bankaccOver;
    /**
     * 设置系统客商是否可以从其他业务层手动录入
     * 0-否,1-是
     */
    @ApiModelProperty(value = "设置系统客商是否可以从其他业务层手动录入 0-否,1-是")
    private String externalCorp;
    /**
     * 支付结算参数配置：allowDifferentBillMoney-允许付款金额与票据金额不一致
     */
    @ApiModelProperty(value = "支付结算参数配置：allowDifferentBillMoney-允许付款金额与票据金额不一致")
    private String allowDifferentBillMoney;
    /*** 支付结算参数配置：balControlType-付款余额控制方式  */
    @ApiModelProperty(value = "支付结算参数配置：balControlType-付款余额控制方式")
    private String balControlType;


}
