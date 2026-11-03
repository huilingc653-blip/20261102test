package com.bytter.report.domain.cms.vo;
import lombok.Data;
import java.math.BigDecimal;
/**
 * 授信录入明细DTO
 * 
 * @author liyh
 *
 */
@Data
public class RatBkRationDtlVm extends BaseBkRationDtlVm {
    /** 占用授信金额 */
    private BigDecimal disMoney;
    /** 占用敞口金额 */
    private BigDecimal disOpenMoney;
    /** 未使用授信额度 */
    private BigDecimal unUserMoney;
    /** 未使用敞口额度 */
    private BigDecimal unUserOpenMoney;
    /** 剩余授信额度 */
    private BigDecimal leftMoney;
    /** 剩余敞口额度 */
    private BigDecimal leftOpenMoney;
    /** 最大金额 */
    private BigDecimal maxMoney;
    /** 已分配最高金额 */
    private BigDecimal disMaxMoney;
    /** 剩余最高金额 */
    private BigDecimal leftMaxMoney;
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
    /** 担保条件 */
    private String assurecondition;
    /** 保证金比例 */
    private BigDecimal assurepercent;
    /** 利率 */
    private BigDecimal interestrate;
    /** 总额共享追加金额 */
    private BigDecimal totalMoneyAddMoney;
    /** 总额共享追加敞口额度 */
    private BigDecimal totalMoneyAddOpenMoney;
    /** 保证金金额 **/
    private BigDecimal bondMoney;
    /** 保证金费率 **/
    private BigDecimal procedureFeeRate;
    /** 保证金费用 **/
    private BigDecimal procedureFee;
    /**
     * 实际调剂额度
     */
    private BigDecimal reliefMoney;
    /**
     * 不能选的品种
     */
    private String disItemCodes;
    /**
     * 第一个综合授信品种
     */
    private String firstComp = "0";
    /**
     * 综合额度使用标识
     */
    private String compSign;
    /** 调剂金额 */
    private BigDecimal superReliefMoney;
    /** 被调剂金额 */
    private BigDecimal superByReliefMoney;
    /** 调剂敞口金额 */
    private BigDecimal superReliefOpenMoney;
    /** 被调剂敞口金额 */
    private BigDecimal superByReliefOpenMoney;
    /** 调剂最高金额 */
    private BigDecimal reliefMaxMoney;
    /** 被调剂最高金额 */
    private BigDecimal byReliefMaxMoney;
    /** 是否启用授信敞口(1=启用，0=不启用) */
    private String isOpenRat;
    /** 调整额度 */
    private BigDecimal adjustMoney;
    /** 调整后额度 */
    private BigDecimal adjustedMoney;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /** 冻结额度*/
    private BigDecimal freezeMoney;
}
