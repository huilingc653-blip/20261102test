package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author shenc
 * @version 1.0
 * @Title: VO
 * @Description: 理财产品
 * @date 2019-03-26 14:03:30
 */
@Data
public class ChoProductRedeemVo implements java.io.Serializable,  IBusinessKey {
    private static final long serialVersionUID = 6800194292372933909L;
    private String id;
    /**
     * 业务单号
     */
    private String billNo;
    /**
     * 产品登记单号
     */
    private String reqId;
    /**
     * 单位Id
     */
    @BtCompositeSearch(fieldName = "prd.corp_Id")
    private String corpId;

    @BtCompositeSearch(fieldName = "sc.code")
    private String corpCode;

    @BtCompositeSearch(fieldName = "sc.name")
    private String corpName;
    /**
     * 银行类别
     */
    @BtCompositeSearch(fieldName = "pt.bank_Type_Id")
    private String bankTypeId;

    @BtCompositeSearch(fieldName = "pr.bank_type_name")
    private String bankTypeName;
    /**
     * 购买银行ID
     */
    @BtCompositeSearch(fieldName = "pr.bank_Info_Id")
    private String bankInfoId;

    @BtCompositeSearch(fieldName = "pr.bank_name")
    private String bankName;
    /**
     * 产品类型
     */
    @BtCompositeSearch(fieldName = "pt.product_Type")
    private String productType;
    /**
     * 产品代码
     */
    @BtCompositeSearch(fieldName = "pt.product_Code")
    private String productCode;
    /**
     * 产品名称
     */
    @BtCompositeSearch(fieldName = "pt.product_Name")
    private String productName;
    /**
     * 理财账号
     */
    @BtCompositeSearch(fieldName = "pr.bank_acc")
    private String bankAcc;
    /**
     * 金额
     */
    @BtCompositeSearch(fieldName = "pr.buy_Money")
    private BigDecimal buyMoney;
    /**
     * 币别
     */
    @BtCompositeSearch(fieldName = "pt.cur_Id")
    private String curId;

    @BtCompositeSearch(fieldName = "cur.cur_name")
    private String curName;
    /**
     * 起息日期
     */
    @BtCompositeSearch(fieldName = "pr.interest_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "pr.deadline_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /**
     * 赎回金额
     */
    @BtCompositeSearch(fieldName = "prd.redeem_money")
    private BigDecimal redeemMoney;
    /**
     * 赎回日期
     */
    @BtCompositeSearch(fieldName = "prd.redeem_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date redeemDate;
    /**
     * 到账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transferDate;
    /**
     * 收益金额
     */
    @BtCompositeSearch(fieldName = "prd.income_money")
    private BigDecimal incomeMoney;
    /**
     * 实际收益率
     */
    @BtCompositeSearch(fieldName = "prd.income_rate")
    private BigDecimal incomeRate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 申购费
     */
    @BtCompositeSearch(fieldName = "prd.sub_fee_money")
    private BigDecimal subFeeMoney;
    /**
     * 赎回费
     */
    @BtCompositeSearch(fieldName = "prd.redeem_charge_money")
    private BigDecimal redeemChargeMoney;
    /**
     * 其他手续费
     */
    @BtCompositeSearch(fieldName = "prd.charge_money")
    private BigDecimal chargeMoney;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "prd.status")
    private Integer status;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 购买日期
     */
    @BtCompositeSearch(fieldName = "pr.buy_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyDate;
    /**
     * 期限:期限=到期日期-起息日期,单位:天
     */
    private Integer dayLimit;
    /**
     * 收益条款:360-360天;365-365天
     */
    @BtCompositeSearch(fieldName = "pr.income_Clause")
    private Integer incomeClause;
    /**
     * 兑付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date cashDate;
    /**
     * 参考收益率
     */
    @BtCompositeSearch(fieldName = "case when pr.END_REFER_INCOME_RATE is not null then pr.END_REFER_INCOME_RATE when pr.REFER_INCOME_RATE is not null then pr.REFER_INCOME_RATE else 0 end")
    private BigDecimal referIncomeRate;
    /**
     * 参考收益=购买金额*参考收益率*期限;360（除以360还是365以收益条款为准）
     */
    @BtCompositeSearch(fieldName = "case when pr.END_REFER_INCOME_MONEY is not null then pr.END_REFER_INCOME_MONEY when pr.REFER_INCOME_MONEY is not null then pr.REFER_INCOME_MONEY else 0 end")
    private BigDecimal referIncomeMoney;
    /**
     * 银行流水认领组件挂接确认
     */
    private String collectionConfirm;
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
     * 是否可提前赎回 0-否 1-是
     */
    @BtCompositeSearch(fieldName = "pr.is_move_up")
    private String isMoveUp;
    /**
     * 资金来源:1 自由资金 2 募集资金 3 银行贷款
     */
    private Integer capitalSource;
    /**
     * 申购费
     */
    @BtCompositeSearch(fieldName = "pr.sub_fee_money")
    private BigDecimal reqSubFeeMoney;
    /**
     * 其他手续费
     */
    @BtCompositeSearch(fieldName = "pr.charge_money")
    private BigDecimal reqChargeMoney;
    /**
     * 理财续期id
     */
    private String renewalId;
    /**
     * 是否隐藏 1：隐藏
     */
    private String checkboxHide;
    /**
     * 续期状态
     */
    @BtCompositeSearch(fieldName = "pw.status")
    private Integer renewalStatus;
    /**
     * 赎回日净值
     */
    private BigDecimal redeemDailyNetVal;
    /**
     * 赎回份额
     */
    private BigDecimal redeemPortion;
    /**
     * 日净值
     */
    private BigDecimal dailyNetVal;
    /**
     * 份额
     */
    private BigDecimal portion;
    /**
     * 是否维护日净值
     */
    private String isMaintainVal;
    /**续存模式 1-本金续存 2-本息续存*/
    private Integer certificateWay;

    /**提前赎回条约*/
    private String moveUpRmk;

    public String getCheckboxHide() {
        if (StringUtils.isEmpty(checkboxHide) && AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())) {
            return "1";
        } else {
            return "0";
        }
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
