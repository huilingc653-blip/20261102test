package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: VO
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Data
public class ChoProductReqQueryVo implements java.io.Serializable{
    private static final long serialVersionUID = -1243220207243230225L;
    private String id;
    /**业务单号*/
    private String billNo;
    /**登记产品Id*/
    private String reqId;
    /**单位ID*/
    // @Corp(inject = "corpName")
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
    /**产品代码*/
    @BtCompositeSearch(fieldName = "pr.product_Code")
    private String productCode;
    /**产品名称*/
    @BtCompositeSearch(fieldName = "pt.product_Name")
    private String productName;
    /**产品类型*/
    @BtCompositeSearch(fieldName = "pr.product_Type")
    private String productType;
    /**收益条款:360-360天;365-365天*/
    @BtCompositeSearch(fieldName = "pr.income_Clause")
    private Integer incomeClause;
    /**理财账号*/
    @BtCompositeSearch(fieldName = "pr.bank_Acc")
    private String bankAcc;
    /**购买金额*/
    @BtCompositeSearch(fieldName = "pr.buy_Money")
    private BigDecimal buyMoney;
    /**币别*/
    // @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "pr.cur_Id")
    private String curId;

    @BtCompositeSearch(fieldName = "pr.cur_name")
    private String curName;
    /**赎回日期*/
    @BtCompositeSearch(fieldName = "prd.redeem_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date redeemDate;
    /**已赎回金额*/
    private BigDecimal redeemedMoney;
    /**待赎回金额*/
    private BigDecimal waitRedeemMoney;
    /**起息日期*/
    @BtCompositeSearch(fieldName = "pr.interest_start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date interestStartDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName = "pr.deadline_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date deadlineDate;
    /**参考收益率*/
    @BtCompositeSearch(fieldName = "pr.refer_income_rate")
    private BigDecimal referIncomeRate;
    /**参考收益=购买金额*参考收益率*期限;360（除以360还是365以收益条款为准）*/
    @BtCompositeSearch(fieldName = "pr.refer_income_money")
    private BigDecimal referIncomeMoney;
    /**平均收益率*/
    private BigDecimal averageIncomeRate;
    /**实际收益率*/
    @BtCompositeSearch(fieldName = "prd.income_rate")
    private BigDecimal incomeRate;
    /**收益金额*/
    private BigDecimal incomeMoney;
    /**申购费*/
    private BigDecimal subFeeMoney;
    /**赎回费*/
    private BigDecimal redeemChargeMoney;
    /**其他手续费*/
    private BigDecimal chargeMoney;
    /**是否可提前赎回 0-否 1-是*/
    @BtCompositeSearch(fieldName = "pr.is_move_up")
    private String isMoveUp;
    /**状态*/
    @BtCompositeSearch(fieldName = "case when pr.biz_status is null then pr.status else pr.biz_status end")
    private Integer status;
    /**最新一条赎回记录的状态*/
    @BtCompositeSearch(fieldName = "pr.redeem_status")
    private Integer redeemStatus;
    /**金额单位*/
    private Integer moneyUnit;
    /**资金来源:1 自由资金 2 募集资金 3 银行贷款*/
    @BtCompositeSearch(fieldName = "pr.capital_source")
    private Integer capitalSource;
    /**是否保本 0-否 1-是*/
    private String isGuaranteed;
    /**银行流水认领组件挂接确认*/
    private String collectionConfirm;
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

    @BtCompositeSearch(fieldName = "pr.PAY_WAY")
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
     * 业务状态
     */
    @BtCompositeSearch(fieldName = "pr.biz_status")
    private String bizStatus;
    /**
     * 购买份额
     */
    private BigDecimal portion;
    /**
     * 已赎回份额
     */
    private BigDecimal redeemedPortion;
    /**
     * 待赎回份额
     */
    private BigDecimal waitRedeemPortion;
    /**
     * 持有收益率
     */
    private BigDecimal holdIncomeRate;
    /**
     * 实际收益（非保本）
     */
    private BigDecimal actualIncome;
    /**
     * 年化收益率
     */
    private BigDecimal annualizedIncomeRate;
    /**
     * 年化收益
     */
    private BigDecimal annualizedIncome;
    /**
     * 余额
     */
    private BigDecimal bal;
    /**
     * 持有成本
     */
    @BtCompositeSearch(fieldName = "pn.buy_money")
    private BigDecimal holdMoney;
    /**
     * 净值维护日期
     */
    private Date netValDate;
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
    /**
     * 资金来源
     */
    @BtCompositeSearch(fieldName = "pr.capital_source")
    private String capitalSourceStr;
    /**
     * 期限
     */
    private Integer dayLimit;
}
