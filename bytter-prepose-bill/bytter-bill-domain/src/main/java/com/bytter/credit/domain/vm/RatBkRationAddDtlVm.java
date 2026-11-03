package com.bytter.credit.domain.vm;
import lombok.Data;
import java.math.BigDecimal;
/**
 * 授信追加明细DTO
 * 
 * @author liyh
 *
 */
@Data
public class RatBkRationAddDtlVm extends BaseBkRationDtlVm {
    /** 追加授信金额 */
    private BigDecimal addMoney;
    /** 追加敞口金额 */
    private BigDecimal addOpenMoney;
    /** 最高金额 */
    private BigDecimal maxMoney;
    /** 追加后最高金额 */
    private BigDecimal addMaxMoney;
    /** 已分配授信金额 */
    private BigDecimal superDistributeMoney;
    /** 已分配授信敞口金额 */
    private BigDecimal superDistributeOpenMoney;
    /** 追加金额 */
    private BigDecimal superAddMoney;
    /** 追加敞口金额 */
    private BigDecimal superAddOpenMoney;
    /** 追加最高金额 */
    private BigDecimal superAddMaxMoney;
    /** 是否新增加授信 **/
    private String isNowAdd;
    /**
     * 第一个综合授信品种
     */
    private String firstComp = "0";
    /** 保证金比例 */
    private BigDecimal assurepercent;
    /** 利率 */
    private BigDecimal interestrate;
    /**
     * 剩余额度
     */
    private BigDecimal leftMoney;
    /** 剩余敞口额度 */
    private BigDecimal leftOpenMoney;
    /** 剩余最高金额 */
    private BigDecimal leftMaxMoney;
    /**
     * 追加类型
     */
    private String flag;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
}
