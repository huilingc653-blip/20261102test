package com.bytter.increment.api.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 */
@Data
public class ChoProductReqDTO implements Serializable,IBusinessKey {
    private static final long serialVersionUID = -7507521276870939344L;
    private String id;
    /**业务单号*/
    private String billNo;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "pr.corp_Id")
    private String corpId;
    private String corpName;
    /**银行类别*/
    @BtCompositeSearch(fieldName = "pr.bank_Type_Id")
    private String bankTypeId;
    private String bankTypeName;
    /**产品类型*/
    @BtCompositeSearch(fieldName = "pr.product_Type")
    private String productType;
    /**产品代码*/
    @BtCompositeSearch(fieldName = "pr.product_Code")
    private String productCode;
    /**产品名称*/
    @BtCompositeSearch(fieldName = "pt.product_Name")
    private String productName;
    /**购买银行ID*/
    @BtCompositeSearch(fieldName = "pr.bank_Info_Id")
    private String bankInfoId;
    /**购买银行*/
    private String bankName;
    /**理财账号ID*/
    @BtCompositeSearch(fieldName = "pr.bank_Acc_Id")
    private String bankAccId;
    /**理财账号*/
    @BtCompositeSearch(fieldName = "pr.bank_Acc")
    private String bankAcc;
    /**币别*/
    @BtCompositeSearch(fieldName = "pr.cur_Id")
    private String curId;
    private String curName;
    /**金额*/
    @BtCompositeSearch(fieldName = "pr.buy_Money")
    private BigDecimal buyMoney;
    /**收益条款:360-360天;365-365天*/
    @BtCompositeSearch(fieldName = "pr.income_Clause")
    private Integer incomeClause;
    /**购买日期*/
    @BtCompositeSearch(fieldName = "pr.buy_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date buyDate;
    /**期限:期限=到期日期-起息日期,单位:天*/
    @BtCompositeSearch(fieldName = "pr.day_limit")
    private Integer dayLimit;
    /**起息日期*/
    @BtCompositeSearch(fieldName = "pr.interest_start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date interestStartDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName = "pr.deadline_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date deadlineDate;
    /**兑付日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date cashDate;
    /**参考收益率*/
    @BtCompositeSearch(fieldName = "pr.refer_income_rate")
    private BigDecimal referIncomeRate;
    /**申购费*/
    private BigDecimal subFeeMoney;
    /**参考收益=购买金额*参考收益率*期限;360（除以360还是365以收益条款为准）*/
    private BigDecimal referIncomeMoney;
    /**其他手续费*/
    private BigDecimal chargeMoney;
    /**是否可提前赎回 0-否 1-是*/
    @BtCompositeSearch(fieldName = "pr.is_move_up")
    private String isMoveUp;
    /**提前赎回条约*/
    private String moveUpRmk;
    /**备注*/
    private String rmk;
    /**附件ID*/
    private String attachmentId;
    /**状态*/
    @BtCompositeSearch(fieldName = "pr.status")
    // @Dict(dictGroup = "choBankFinance.status", inject = "statusName")
    private Integer status;
    private String statusName;
    /**创建人登录姓名*/
    private String createBy;
    /**创建日期*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**金额单位*/
    private Integer moneyUnit;
    /**消息提醒标志*/
    private String noticeSign;
    /**资金来源:1 自由资金 2 募集资金 3 银行贷款*/
    private Integer capitalSource;
    /**是否保本 0-否 1-是*/
    private String isGuaranteed;
    /**已赎回金额*/
    private BigDecimal redeemedMoney;
    /**待赎回金额*/
    private BigDecimal waitRedeemMoney;
    /**
     * 理财类型
     */
    @BtCompositeSearch(fieldName = "pt.FINANCIAL_TYPE")
    // @Dict(dictGroup = "financial.type", inject = "financialTypeName")
    private String financialType;
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
     * 理财登记申购费
     */
    private BigDecimal reqSubFeeMoney;
    /**
     * 理财登记其他手续费
     */
    private BigDecimal reqChargeMoney;
    /**
     * 赎回费
     */
    private BigDecimal redeemChargeMoney;
    /**
     * 理财赎回表id
     */
    private String redeemId;
    /**
     * 日净值
     */
    private BigDecimal dailyNetVal;
    /**
     * 份额
     */
    private BigDecimal portion;
    /**
     * 赎回日净值
     */
    private BigDecimal redeemDailyNetVal;
    /**
     * 赎回份额
     */
    private BigDecimal redeemPortion;
    /**
     * 业务状态
     */
    @BtCompositeSearch(fieldName = "pr.biz_status")
    private Integer bizStatus;
    /**
     * 最新净值
     */
    private BigDecimal latestNetVal;
    /**
     * 最新日期
     */
    private Date latestDate;
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
     * 是否为购买申请
     */
    private String isBuyApply;
    /**
     * 购买申请状态
     */
    @BtCompositeSearch(fieldName = "pr.buy_apply_status")
    private Integer buyApplyStatus;
    /**
     * 是否隐藏复选框 1为是
     */
    private String checkboxHide;
    /** 审批业务流水ID**/
    private String businessKey;
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public String getCheckboxHide() {
        if (this.getBuyApplyStatus() == null) {
            //审批列表查询
            return "0";
        }
        if (CommonConstant.STATUE_WAIT_CHECK.equals(this.getBuyApplyStatus())) {
            return "1";
        } else {
            return "0";
        }
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
}
