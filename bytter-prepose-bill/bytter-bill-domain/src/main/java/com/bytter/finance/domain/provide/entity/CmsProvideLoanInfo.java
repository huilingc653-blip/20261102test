package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/*
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 借款信息表
 * @date 2017-07-20 10:45:09
 */
@Entity
@Table(name = "CMS_PROVIDE_LOAN_INFO", schema = "")
@SuppressWarnings("serial")
public class CmsProvideLoanInfo extends IdEntity implements java.io.Serializable {
    /**
     * 结算中心代码
     */
    private String netId;
    /*
     * 单位代码
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 单位编码
     */
    private String corpCode;
    /*
     * 借款流水号
     */
    private String billCode;
    /*
     * 借款合同号
     */
    private String contractNo;
    /*
     * 合同号期限
     */
    private String contractTerm;
    /*
     * 借款金额
     */
    private BigDecimal plMoney;
    /*
     * 借款期限
     */
    private Integer plTerm;
    /*
     * 期限单位
     */
    private String ternUnit;
    /*
     * 借款币种
     */
    private String curId;
    /*
     * 借款银行类别
     */
    private String bankType;
    /*
     * 借款银行
     */
    private String bankId;
    /*
     * 借款银行账号
     */
    private String plAccount;
    /*
     * 借款日期
     */
    private Date plDate;
    /*
     * 还款日期
     */
    private Date repayDate;
    /*
     * 计息财务季度
     */
    private String fiscalQuarter;
    /*
     * 银行名称
     */
    private String bankName;
    /*
     * 银行类别名称
     */
    private String bankTypeName;
    /*
     * 联行号
     */
    private String bankCode;
    /*
     * 币别名称
     */
    private String curName;
    /*
     * 计息日
     */
    private Integer expiryDate;
    /*
     * 预提日
     */
    private Integer accruedDailyDate;
    /*
     * 还款方式
     */
    private String repayType;
    /*
     * 还款方式说明
     */
    private String repayTypeDesc;
    /*
     * 借款种类
     */
    private String plClass;
    /*
     * 贷款类型
     */
    private String plGenre;
    /*
     * 担保主业务类型
     */
    private String mainPlType;
    /*
     * 担保方式
     */
    private String plType;
    /*
     * 保证金比例
     */
    private BigDecimal bailmoneyRate;
    /*
     * 保证金
     */
    private BigDecimal bailmoney;
    /*
     * 保证金利率
     */
    private BigDecimal bondRate;
    /*
     * 保证金合同
     */
    private String bailmoneyContract;
    /*
     * 担保说明
     */
    private String suretyExplain;
    /*
     * 借款利率
     */
    private BigDecimal plRate;
    /*
     * 展期利率
     */
    private BigDecimal deferredRate;
    /*
     * 逾期利率
     */
    private BigDecimal overdueRate;
    /*
     * 补录员
     */
    private String repairOp;
    /*
     * 状态
     */
    private Integer status;
    /*
     * 借款用途
     */
    private String plPurpose;
    /*
     * 备注
     */
    private String rmk;
    /*
     * 打回原因
     */
    private String refuseWhy;
    /*
     * 是否挂授信
     */
    private String isHangCredit;
    /*
     * 授信模式
     */
    private String ratmodel;
    /*
     * 额度编号
     */
    private String rationCode;
    /*
     * 占用授信代码
     */
    private String itemCode;
    /*
     * 保存当前币种转换汇率
     */
    private BigDecimal exchangerate;
    /*
     * 是否折合成本位币
     */
    private String isRmbRat;
    /**
     * 贷款期限名称
     */
    private String repayCycleName;
    /*
     * 使用人
     */
    private String users;
    /*
     * 信贷合同号
     */
    private String creditContractNo;
    /*
     * 内部合同号
     */
    private String insideContractNo;
    /*
     * 项目名称
     */
    private String projectName;
    /*
     * 付息方式
     */
    private String cplInterestPayment;
    /*
     * 中间业务费
     */
    private BigDecimal interbusexpenses;
    /*
     * 发放方式
     */
    private String loanSendOutType;
    /*
     * 跳过借款发放功能
     */
    private String acrossSend;
    /*
     * 浮动方式
     */
    private String floatWay;
    /*
     * 二级浮动方式
     */
    private String floatWayNext;
    /*
     * 点数或浮动比例
     */
    private BigDecimal pointsProportionText;
    /*
     * 点数或者比例
     */
    private String pointsProportionSelect;
    /*
     * 利率调整生效日期
     */
    private Date rateEntriesEffectDate;
    /*
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    /*
     * 计息方式
     */
    private String interestWay;
    /*
     * 基准利率类型
     */
    private String referenceRateType;
    /*
     * 贷款周期
     */
    private String repayCycle;
    /*
     * 是否跳过补录
     **/
    private String isOverRepair;
    /*
     * 利率复核生效日期
     */
    private Date ratetimeoftakeeffect;
    /*
     * 预计利息
     */
    private BigDecimal anticInterest;
    /*
     * 顾问费利率
     */
    private BigDecimal consultantFeeRate;
    /*
     * 顾问费
     */
    private BigDecimal consultantFee;
    /*
     * 第三方收益利率
     */
    private BigDecimal thirdPartyProfitRate;
    /*
     * 第三方收益
     */
    private BigDecimal thirdPartyProfit;
    /*
     * 其他利率
     */
    private BigDecimal otherFeeRate;
    /*
     * 其他费用
     */
    private BigDecimal otherFee;
    /*
     * 作废单号
     */
    private String bolishCode;
    /*
     * 利息测算基数
     */
    private String rateBase;
    /*
     * 是否勾选顾问费复选框
     */
    private String consultantCb;
    /*
     * 是否勾选第三方收益复选框
     */
    private String thirdPartyCb;
    /*
     * 是否勾其他利率复选框
     */
    private String otherFeeCb;
    /*
     * 综合成本年化利率
     */
    private BigDecimal compositeFeeRate;
    /*
     * 综合成本
     */
    private BigDecimal compositeFee;
    /*
     * 创建人名称
     */
    private String createName;
    /*
     * 创建人登录名称
     */
    private String createBy;
    /*
     * 创建日期
     */
    private Date createDate;
    /*
     * 更新人名称
     */
    private String updateName;
    /*
     * 更新人登录名称
     */
    private String updateBy;
    /*
     * 更新日期
     */
    private Date updateDate;
    private String uploadId;
    private String bizStatus;
    /*
     * 是否挂接保证金
     */
    private String isHookBond;
    /*
     * 保证金币别
     */
    private String bondCurId;
    /*
     * 保证金账户
     */
    private String bondBankAcc;
    /*
     * 是否启用授信敞口
     */
    private String isOpenRat;
    /*
     * 授信业务品种id
     */
    private String billName;
    /*
     * 是否挂接担保
     */
    private String isHangGuarantee;
    /*
     * 担保备注
     */
    private String guaranteeRmk;
    /*
     * 合同类型
     */
    private String conTypeId;
    /*
     * 调用合同id
     */
    private String conId;
    /*
     * 是否挂抵质押物
     */
    private String isRelatePledge;
    /*
     * 是否自动还款
     */
    private String isAutoPay;
    /*
     * 利率报价日期
     */
    private Date rateQuotedPriceDate;
    /*
     * 展期上浮点数
     */
    private BigDecimal deferredPoints;
    /*
     * 逾期上浮比例
     */
    private BigDecimal overdueProportion;
    /*
     * 计提方式
     */
    private String accrualType;
    /**
     * 还款账号
     */
    private String onlinePaymentAcc;

    /**
     * 还款账号名称
     */
    private String onlineAccName;

    /**
     * 还款开户行名称
     */
    private String onlineBankName;

    /**
     * 还款账号id
     */
    private String onlinePaymentAccId;
    /*
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 结算中心代码
     */
    @Column(name = "NET_ID")
    public String getNetId() {
        return this.netId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 结算中心代码
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款流水号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款流水号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款合同号
     */
    @Column(name = "CONTRACT_NO")
    public String getContractNo() {
        return this.contractNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款合同号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 借款金额
     */
    @Column(name = "PL_MONEY")
    public BigDecimal getPlMoney() {
        return this.plMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 借款金额
     */
    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 借款期限
     */
    @Column(name = "PL_TERM")
    public Integer getPlTerm() {
        return this.plTerm;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer
     * 借款期限
     */
    public void setPlTerm(Integer plTerm) {
        this.plTerm = plTerm;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 期限单位
     */
    @Column(name = "TERN_UNIT")
    public String getTernUnit() {
        return this.ternUnit;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 期限单位
     */
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款币种
     */
    @Column(name = "CUR_ID")
    public String getCurId() {
        return this.curId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款币种
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款银行类别
     */
    @Column(name = "BANK_TYPE")
    public String getBankType() {
        return this.bankType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款银行类别
     */
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款银行
     */
    @Column(name = "BANK_ID")
    public String getBankId() {
        return this.bankId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款银行
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款银行账号
     */
    @Column(name = "PL_ACCOUNT")
    public String getPlAccount() {
        return this.plAccount;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款银行账号
     */
    public void setPlAccount(String plAccount) {
        this.plAccount = plAccount;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 借款日期
     */
    @Column(name = "PL_DATE")
    public Date getPlDate() {
        return this.plDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 借款日期
     */
    public void setPlDate(Date plDate) {
        this.plDate = plDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 还款日期
     */
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return this.repayDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 还款日期
     */
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 还款方式
     */
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return this.repayType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 还款方式
     */
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 还款方式说明
     */
    @Column(name = "REPAY_TYPE_DESC")
    public String getRepayTypeDesc() {
        return this.repayTypeDesc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 还款方式说明
     */
    public void setRepayTypeDesc(String repayTypeDesc) {
        this.repayTypeDesc = repayTypeDesc;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款种类
     */
    @Column(name = "PL_CLASS")
    public String getPlClass() {
        return this.plClass;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款种类
     */
    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 贷款类型
     */
    @Column(name = "PL_GENRE")
    public String getPlGenre() {
        return this.plGenre;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 贷款类型
     */
    public void setPlGenre(String plGenre) {
        this.plGenre = plGenre;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保主业务类型
     */
    @Column(name = "MAIN_PL_TYPE")
    public String getMainPlType() {
        return this.mainPlType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 担保主业务类型
     */
    public void setMainPlType(String mainPlType) {
        this.mainPlType = mainPlType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保方式
     */
    @Column(name = "PL_TYPE")
    public String getPlType() {
        return this.plType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 担保方式
     */
    public void setPlType(String plType) {
        this.plType = plType;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保证金比例
     */
    @Column(name = "BAILMONEY_RATE")
    public BigDecimal getBailmoneyRate() {
        return this.bailmoneyRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 保证金比例
     */
    public void setBailmoneyRate(BigDecimal bailmoneyRate) {
        this.bailmoneyRate = bailmoneyRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金利率
     */
    @Column(name = "BOND_RATE")
    public BigDecimal getBondRate() {
        return bondRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金利率
     */
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保证金
     */
    @Column(name = "BAILMONEY")
    public BigDecimal getBailmoney() {
        return this.bailmoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 保证金
     */
    public void setBailmoney(BigDecimal bailmoney) {
        this.bailmoney = bailmoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保说明
     */
    @Column(name = "SURETY_EXPLAIN")
    public String getSuretyExplain() {
        return this.suretyExplain;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 担保说明
     */
    public void setSuretyExplain(String suretyExplain) {
        this.suretyExplain = suretyExplain;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 借款利率
     */
    @Column(name = "PL_RATE")
    public BigDecimal getPlRate() {
        return this.plRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 借款利率
     */
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 展期利率
     */
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return this.deferredRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 展期利率
     */
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 逾期利率
     */
    @Column(name = "OVERDUE_RATE")
    public BigDecimal getOverdueRate() {
        return this.overdueRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 逾期利率
     */
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 补录员
     */
    @Column(name = "REPAIR_OP")
    public String getRepairOp() {
        return this.repairOp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 补录员
     */
    public void setRepairOp(String repairOp) {
        this.repairOp = repairOp;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer
     * 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款用途
     */
    @Column(name = "PL_PURPOSE")
    public String getPlPurpose() {
        return this.plPurpose;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款用途
     */
    public void setPlPurpose(String plPurpose) {
        this.plPurpose = plPurpose;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否挂授信
     */
    @Column(name = "IS_HANG_CREDIT")
    public String getIsHangCredit() {
        return this.isHangCredit;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否挂授信
     */
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信模式
     */
    @Column(name = "RATMODEL")
    public String getRatmodel() {
        return this.ratmodel;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信模式
     */
    public void setRatmodel(String ratmodel) {
        this.ratmodel = ratmodel;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 额度编号
     */
    @Column(name = "RATION_CODE")
    public String getRationCode() {
        return this.rationCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 额度编号
     */
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 占用授信代码
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return this.itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 占用授信代码
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保存当前币种转换汇率
     */
    @Column(name = "EXCHANGERATE")
    public BigDecimal getExchangerate() {
        return this.exchangerate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 保存当前币种转换汇率
     */
    public void setExchangerate(BigDecimal exchangerate) {
        this.exchangerate = exchangerate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否折合成本位币
     */
    @Column(name = "IS_RMB_RAT")
    public String getIsRmbRat() {
        return this.isRmbRat;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否折合成本位币
     */
    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 使用人
     */
    @Column(name = "USERS")
    public String getUsers() {
        return this.users;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 使用人
     */
    public void setUsers(String users) {
        this.users = users;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 信贷合同号
     */
    @Column(name = "CREDIT_CONTRACT_NO")
    public String getCreditContractNo() {
        return this.creditContractNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 信贷合同号
     */
    public void setCreditContractNo(String creditContractNo) {
        this.creditContractNo = creditContractNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 内部合同号
     */
    @Column(name = "INSIDE_CONTRACT_NO")
    public String getInsideContractNo() {
        return this.insideContractNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 内部合同号
     */
    public void setInsideContractNo(String insideContractNo) {
        this.insideContractNo = insideContractNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 项目名称
     */
    @Column(name = "PROJECT_NAME")
    public String getProjectName() {
        return this.projectName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 付息方式
     */
    @Column(name = "CPL_INTEREST_PAYMENT")
    public String getCplInterestPayment() {
        return this.cplInterestPayment;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 付息方式
     */
    public void setCplInterestPayment(String cplInterestPayment) {
        this.cplInterestPayment = cplInterestPayment;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 中间业务费
     */
    @Column(name = "INTERBUSEXPENSES")
    public BigDecimal getInterbusexpenses() {
        return this.interbusexpenses;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 中间业务费
     */
    public void setInterbusexpenses(BigDecimal interbusexpenses) {
        this.interbusexpenses = interbusexpenses;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 发放方式
     */
    @Column(name = "LOAN_SEND_OUT_TYPE")
    public String getLoanSendOutType() {
        return this.loanSendOutType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 发放方式
     */
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 浮动方式
     */
    @Column(name = "FLOAT_WAY")
    public String getFloatWay() {
        return this.floatWay;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 浮动方式
     */
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 点数或浮动比例
     */
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return this.pointsProportionText;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 点数或浮动比例
     */
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 点数或者比例
     */
    @Column(name = "POINTS_PROPORTION_SELECT")
    public String getPointsProportionSelect() {
        return this.pointsProportionSelect;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 点数或者比例
     */
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 利率调整生效日期
     */
    @Column(name = "RATE_ENTRIES_EFFECT_DATE")
    public Date getRateEntriesEffectDate() {
        return this.rateEntriesEffectDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 利率调整生效日期
     */
    public void setRateEntriesEffectDate(Date rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 计息方式
     */
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return this.interestWay;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 计息方式
     */
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 基准利率类型
     */
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return this.referenceRateType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 基准利率类型
     */
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 贷款周期
     */
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return this.repayCycle;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 贷款周期
     */
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    @Column(name = "IS_OVER_REPAIR")
    public String getIsOverRepair() {
        return isOverRepair;
    }
    public void setIsOverRepair(String isOverRepair) {
        this.isOverRepair = isOverRepair;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 利率复核生效日期
     */
    @Column(name = "RATETIMEOFTAKEEFFECT")
    public Date getRatetimeoftakeeffect() {
        return this.ratetimeoftakeeffect;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 利率复核生效日期
     */
    public void setRatetimeoftakeeffect(Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 预计利息
     */
    @Column(name = "ANTIC_INTEREST")
    public BigDecimal getAnticInterest() {
        return this.anticInterest;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 预计利息
     */
    public void setAnticInterest(BigDecimal anticInterest) {
        this.anticInterest = anticInterest;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 顾问费利率
     */
    @Column(name = "CONSULTANT_FEE_RATE")
    public BigDecimal getConsultantFeeRate() {
        return this.consultantFeeRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 顾问费利率
     */
    public void setConsultantFeeRate(BigDecimal consultantFeeRate) {
        this.consultantFeeRate = consultantFeeRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 顾问费
     */
    @Column(name = "CONSULTANT_FEE")
    public BigDecimal getConsultantFee() {
        return this.consultantFee;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 顾问费
     */
    public void setConsultantFee(BigDecimal consultantFee) {
        this.consultantFee = consultantFee;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 第三方收益利率
     */
    @Column(name = "THIRD_PARTY_PROFIT_RATE")
    public BigDecimal getThirdPartyProfitRate() {
        return this.thirdPartyProfitRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 第三方收益利率
     */
    public void setThirdPartyProfitRate(BigDecimal thirdPartyProfitRate) {
        this.thirdPartyProfitRate = thirdPartyProfitRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 第三方收益
     */
    @Column(name = "THIRD_PARTY_PROFIT")
    public BigDecimal getThirdPartyProfit() {
        return this.thirdPartyProfit;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 第三方收益
     */
    public void setThirdPartyProfit(BigDecimal thirdPartyProfit) {
        this.thirdPartyProfit = thirdPartyProfit;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 其他利率
     */
    @Column(name = "OTHER_FEE_RATE")
    public BigDecimal getOtherFeeRate() {
        return this.otherFeeRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 其他利率
     */
    public void setOtherFeeRate(BigDecimal otherFeeRate) {
        this.otherFeeRate = otherFeeRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 其他费用
     */
    @Column(name = "OTHER_FEE")
    public BigDecimal getOtherFee() {
        return this.otherFee;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 其他费用
     */
    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 作废单号
     */
    @Column(name = "BOLISH_CODE")
    public String getBolishCode() {
        return this.bolishCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 作废单号
     */
    public void setBolishCode(String bolishCode) {
        this.bolishCode = bolishCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 利息测算基数
     */
    @Column(name = "RATE_BASE")
    public String getRateBase() {
        return this.rateBase;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 利息测算基数
     */
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否勾选顾问费复选框
     */
    @Column(name = "CONSULTANT_CB")
    public String getConsultantCb() {
        return this.consultantCb;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否勾选顾问费复选框
     */
    public void setConsultantCb(String consultantCb) {
        this.consultantCb = consultantCb;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否勾选第三方收益复选框
     */
    @Column(name = "THIRD_PARTY_CB")
    public String getThirdPartyCb() {
        return this.thirdPartyCb;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否勾选第三方收益复选框
     */
    public void setThirdPartyCb(String thirdPartyCb) {
        this.thirdPartyCb = thirdPartyCb;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否勾其他利率复选框
     */
    @Column(name = "OTHER_FEE_CB")
    public String getOtherFeeCb() {
        return this.otherFeeCb;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否勾其他利率复选框
     */
    public void setOtherFeeCb(String otherFeeCb) {
        this.otherFeeCb = otherFeeCb;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 综合成本年化利率
     */
    @Column(name = "COMPOSITE_FEE_RATE")
    public BigDecimal getCompositeFeeRate() {
        return this.compositeFeeRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 综合成本年化利率
     */
    public void setCompositeFeeRate(BigDecimal compositeFeeRate) {
        this.compositeFeeRate = compositeFeeRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 综合成本
     */
    @Column(name = "COMPOSITE_FEE")
    public BigDecimal getCompositeFee() {
        return this.compositeFee;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 综合成本
     */
    public void setCompositeFee(BigDecimal compositeFee) {
        this.compositeFee = compositeFee;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.util.String
     *
     * @return: java.util.String 合同期限
     */
    @Column(name = "CONTRACT_TERM")
    public String getContractTerm() {
        return contractTerm;
    }
    /**
     * 方法: 设置String
     *
     * @param: String
     * 合同期限
     */
    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }
    @Column(name = "BAILMONEY_CONTRACT")
    public String getBailmoneyContract() {
        return bailmoneyContract;
    }
    public void setBailmoneyContract(String bailmoneyContract) {
        this.bailmoneyContract = bailmoneyContract;
    }
    @Column(name = "REFUSE_WHY")
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    @Column(name = "ACROSS_SEND")
    public String getAcrossSend() {
        return acrossSend;
    }
    public void setAcrossSend(String acrossSend) {
        this.acrossSend = acrossSend;
    }
    @Column(name = "FLOAT_WAY_NEXT")
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    @Column(name = "UPLOAD_ID")
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "FISCAL_QUARTER")
    public String getFiscalQuarter() {
        return fiscalQuarter;
    }
    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }
    @Column(name = "EXPIRY_DATE")
    public Integer getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }
    @Column(name = "ACCRUED_DAILY_DATE")
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
    @Column(name = "BENCHMARK_RATE")
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否挂接保证金
     */
    @Column(name = "IS_HOOK_BOND")
    public String getIsHookBond() {
        return isHookBond;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否挂接保证金
     */
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    @Column(name = "BOND_CUR_ID")
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    @Column(name = "BOND_BANK_ACC")
    public String getBondBankAcc() {
        return bondBankAcc;
    }
    public void setBondBankAcc(String bondBankAcc) {
        this.bondBankAcc = bondBankAcc;
    }
    @Column(name = "IS_OPEN_RAT")
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    @Column(name = "BILL_NAME")
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    @Column(name = "IS_HANG_GUARANTEE")
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    @Column(name = "GUARANTEE_RMK")
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 生成合同类型
     */
    @Column(name = "CON_TYPE_ID")
    public String getConTypeId() {
        return conTypeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 生成合同类型
     */
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 调用的合同id
     */
    @Column(name = "CON_ID")
    public String getConId() {
        return conId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 调用的合同id
     */
    public void setConId(String conId) {
        this.conId = conId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否挂抵质押物
     */
    @Column(name = "IS_RELATE_PLEDGE")
    public String getIsRelatePledge() {
        return isRelatePledge;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 是否挂抵质押物
     */
    public void setIsRelatePledge(String isRelatePledge) {
        this.isRelatePledge = isRelatePledge;
    }
    @Column(name = "IS_AUTO_PAY")
    public String getIsAutoPay() {
        return isAutoPay;
    }
    public void setIsAutoPay(String isAutoPay) {
        this.isAutoPay = isAutoPay;
    }
    @Column(name = "RATE_QUOTED_PRICE_DATE")
    public Date getRateQuotedPriceDate() {
        return rateQuotedPriceDate;
    }
    public void setRateQuotedPriceDate(Date rateQuotedPriceDate) {
        this.rateQuotedPriceDate = rateQuotedPriceDate;
    }
    @Column(name = "DEFERRED_POINTS")
    public BigDecimal getDeferredPoints() {
        return deferredPoints;
    }
    public void setDeferredPoints(BigDecimal deferredPoints) {
        this.deferredPoints = deferredPoints;
    }
    @Column(name = "OVERDUE_PROPORTION")
    public BigDecimal getOverdueProportion() {
        return overdueProportion;
    }
    public void setOverdueProportion(BigDecimal overdueProportion) {
        this.overdueProportion = overdueProportion;
    }
    @Column(name = "ACCRUAL_TYPE")
    public String getAccrualType() {
        return accrualType;
    }
    public void setAccrualType(String accrualType) {
        this.accrualType = accrualType;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CORP_CODE")
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    @Column(name = "REPAY_CYCLE_NAME")
    public String getRepayCycleName() {
        return repayCycleName;
    }
    public void setRepayCycleName(String repayCycleName) {
        this.repayCycleName = repayCycleName;
    }

    @Column(name ="ONLINE_PAYMENT_ACC")
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }


    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }

    @Column(name ="ONLINE_ACC_NAME")
    public String getOnlineAccName() {
        return onlineAccName;
    }

    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }

    @Column(name ="ONLINE_BANK_NAME")
    public String getOnlineBankName() {
        return onlineBankName;
    }

    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }

    @Column(name ="ONLINE_PAYMENT_ACC_ID")
    public String getOnlinePaymentAccId() {
        return onlinePaymentAccId;
    }

    public void setOnlinePaymentAccId(String onlinePaymentAccId) {
        this.onlinePaymentAccId = onlinePaymentAccId;
    }
}
