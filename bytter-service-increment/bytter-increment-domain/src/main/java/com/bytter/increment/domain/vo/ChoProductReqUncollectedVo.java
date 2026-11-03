package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
/**
 * @Title: VO
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Data
public class ChoProductReqUncollectedVo implements java.io.Serializable{
    private static final long serialVersionUID = -2517739530280005301L;
    /**业务单号*/
    private String billNo;
    /**登记产品Id*/
    private String reqId;
    /**单位ID*/
    @BtCompositeSearch(fieldName="pr.corp_Id")
    private String corpId;
    @BtCompositeSearch(fieldName="pr.corp_Id")
    private String corpName;
    /**银行类别*/
    @BtCompositeSearch(fieldName = "pr.bank_Type_Id")
    private String bankTypeId;
    @BtCompositeSearch(fieldName = "pr.bank_type_id")
    private String bankTypeName;
    /**购买银行*/
    @BtCompositeSearch(fieldName = "pr.bank_name")
    private String bankName;
    /**产品类型*/
    @BtCompositeSearch(fieldName = "pr.product_type")
    private String productType;
    /**产品代码*/
    @BtCompositeSearch(fieldName = "pr.product_Code")
    private String productCode;
    /**产品名称*/
    @BtCompositeSearch(fieldName = "pt.product_Name")
    private String productName;
    /**理财账号*/
    @BtCompositeSearch(fieldName = "pr.bank_Acc")
    private String bankAcc;
    /**购买金额*/
    @BtCompositeSearch(fieldName = "pr.buy_Money")
    private BigDecimal buyMoney;
    /**币别*/
    @BtCompositeSearch(fieldName = "pr.cur_Id")
    private String curId;
    @BtCompositeSearch(fieldName = "pr.cur_Id")
    private String curName;
    /**期限*/
    @BtCompositeSearch(fieldName = "pr.day_limit")
    private Integer dayLimit;
    /**起息日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "pr.interest_start_date")
    private Date interestStartDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName = "pr.deadline_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date deadlineDate;
    /**计算截止日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date calEndDate;
    private String calEndDateStr;
    /**计算天数*/
    private Integer calDay;
    /**参考收益率*/
    @BtCompositeSearch(fieldName = "pr.refer_income_rate")
    private BigDecimal referIncomeRate;
    /**参考收益*/
    @BtCompositeSearch(fieldName = "pr.refer_income_money")
    private BigDecimal referIncomeMoney;
    /**应收利息*/
    private BigDecimal receivableMoney;
    /**实收利息*/
    private BigDecimal actualMoney;
    /**应收未收利息*/
    private BigDecimal uncollectedMoney;
    /**申购费*/
    @BtCompositeSearch(fieldName = "prd.sub_fee_money")
    private BigDecimal subFeeMoney;
    /**赎回费*/
    @BtCompositeSearch(fieldName = "prd.redeem_charge_money")
    private BigDecimal redeemChargeMoney;
    /**其他手续费*/
    @BtCompositeSearch(fieldName = "prd.charge_money")
    private BigDecimal chargeMoney;
    /**收益条款:360-360天;365-365天*/
    @BtCompositeSearch(fieldName = "pr.income_clause")
    private Integer incomeClause;
    /**赎回日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "prd.redeem_date")
    private Date redeemDate;
    /**持仓总额（App专用）*/
    private BigDecimal buyTotal;
    /**参考收益总额（App专用）*/
    private BigDecimal referIncomeTotal;
    /**业务id(app报表专用)*/
    @BtCompositeSearch(fieldName = "pr.id")
    private String id;
    /**时间刻度*/
    private String sign;
    /**状态 0全部 1已到期 2未到期*/
    private String isExpireStr;
    /**app数据集合*/
    private Map<String,BigDecimal> totalMap;
    /**购买日期*/
    @BtCompositeSearch(fieldName = "pr.buy_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date buyDate;
    /**
     * 理财类型
     */
    @BtCompositeSearch(fieldName = "pt.FINANCIAL_TYPE")
    // @Dict(dictGroup = "financial.type", inject = "financialTypeName")
    private String financialType;
    @BtCompositeSearch(fieldName = "pt.FINANCIAL_TYPE")
    private String financialTypeName;
    /**
     * 付息方式
     */
    @BtCompositeSearch(fieldName = "pr.PAY_WAY")
    // @Dict(dictGroup = "financial.payWay", inject = "payWayName")
    private String payWay;
    private String payWayName;
    /**
     * 合同号
     */
    @BtCompositeSearch(fieldName = "pr.CONTRACT_NO")
    private String contractNo;
    /**
     * 收益类型
     */
    @BtCompositeSearch(fieldName = "pr.INCOME_TYPE")
    private String incomeType;
    /**
     * 到期是否续存
     */
    @BtCompositeSearch(fieldName = "pr.IS_STORE")
    private String isStore;
    /**
     * 起始合同利率
     */
    @BtCompositeSearch(fieldName = "pr.START_CONTRACT_RATE")
    private BigDecimal startContractRate;
    /**
     * 结束合同利率
     */
    @BtCompositeSearch(fieldName = "pr.END_CONTRACT_RATE")
    private BigDecimal endContractRate;
    /**
     * 购买份额
     */
    private BigDecimal portion;
    /**
     * 已赎回金额
     */
    @BtCompositeSearch(fieldName = "prd.redeem_money")
    private BigDecimal redeemedMoney;
    /**
     * 待赎回金额
     */
    private BigDecimal waitRedeemMoney;
    /**
     * 已赎回份额
     */
    private BigDecimal redeemedPortion;
    /**
     * 待赎回份额
     */
    private BigDecimal waitRedeemPortion;
    /**
     * 实际收益（非保本）
     */
    private BigDecimal actualIncome;
    /**
     * 持有成本
     */

    @BtCompositeSearch(fieldName = "case when pn.buy_money is null then 0 else pn.buy_money end")
    private BigDecimal holdMoney;
    /**
     * 日净值
     */
    @BtCompositeSearch(fieldName = "pn.daily_net_val")
    private BigDecimal dailyNetVal;
    /**
     * 已赎回本金
     */
    private BigDecimal redeemedPrincipal;
    /**
     * 是否维护日净值
     */
    private String isMaintainVal;
    /**
     * 结束参考收益金额
     */
    private BigDecimal endReferIncomeMoney;
    /**
     * 结束参考收益率
     */
    private BigDecimal endReferIncomeRate;
}
