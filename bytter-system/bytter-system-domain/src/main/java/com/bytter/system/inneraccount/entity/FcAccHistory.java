package com.bytter.system.inneraccount.entity;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.constants.InnerAccountConstant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_ACC_HISTORY
 * @date 2017-06-07 14:30:19
 */
@Entity
@Table(name = "FC_ACC_HISTORY", schema = "")
@SuppressWarnings("serial")
public class FcAccHistory extends IdEntity implements java.io.Serializable {
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 结算中心账号
     */
    private String blnctrAcc;
    /**
     * 账户名称
     */
    private String accName;
    /**
     * 网点ID
     */
    private String netId;
    /**
     * 开户单位ID
     */
    private String corpId;
    /**
     * 币别代码
     */
    private String curId;
    /**
     * 科目代码
     */
    private String subjectId;
    /**
     * 账户类别
     */
    private String accType;
    /**
     * 账户属性
     */
    private String attributeId;
    /**
     * 账户性质
     */
    private String natureCodeId;
    /**
     * 银行代码
     */
    private String bankCode;
    /**
     * 余额方向
     */
    private Integer balanceWay;
    /**
     * 计息标志
     */
    private String interestSign;
    /**
     * 起息日期
     */
    private Date interestStartDate;
    /**
     * 利率
     */
    private BigDecimal interestRate;
    /**
     * 透支标志
     */
    private String overdraftSign;
    /**
     * 透支利率
     */
    private BigDecimal overdraftRate;
    /**
     * 透支金额
     */
    private BigDecimal overdraftMoney;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 到期日期
     */
    private Date deadlineDate;
    /**
     * 展期到期日期
     */
    private Date delayDeadlineDate;
    /**
     * 逾期利率
     */
    private BigDecimal overdueRate;
    /**
     * 扣息活期存款账户
     */
    private String accForDeduct;
    /**
     * 扣息方式
     */
    private String deductMode;
    /**
     * 通知类型
     */
    private Integer notifyType;
    /**
     * 账户状态
     */
    private String accState;
    /**
     * 协定存款标志
     */
    private String agreedDepositSign;
    /**
     * 预提标志
     */
    private String predrawSign;
    /**
     * 是否产生对账数据---对账标志
     */
    private String ifCompare;
    /**
     * 往来账号
     */
    private String accId;
    /**
     * 账号启用日期
     */
    private Date accStartDate;
    /**
     * 经办人代码
     */
    private String operatorCode;
    /**
     * 开户日期
     */
    private Date accOpenTime;
    /**
     * 复核员代码
     */
    private String checkerCode;
    /**
     * 复核日期
     */
    private Date checkTime;
    /**
     * 区域代码
     */
    private String areaCode;
    /**
     * 行业代码
     */
    private String vocationCode;
    /**
     * 助记码
     */
    private String shortcut;
    /**
     * 联行网点
     */
    private String unionNet;
    /**
     * 展期利率
     */
    private BigDecimal delayRate;
    /**
     * 联行账号
     */
    private String unionAcc;
    /**
     * 委托贷款帐号
     */
    private String precationAcc;
    /**
     * 委托贷款方
     */
    private String precationCorp;
    /**
     * 委托存款的手续费率
     */
    private BigDecimal poundageRate;
    /**
     * 手续费收取账户
     */
    private String chargeAcc;
    /**
     * 办理方式
     */
    private Integer transactWay;
    /**
     * 销户日期
     */
    private Date cancelDate;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 打回原因
     */
    private String checkBackReason;
    /**
     * 到期单位
     */
    private String endUnit;
    /**
     * 到期续存方式
     */
    private Integer certificateWay;
    /**
     * 是否收取手续费
     */
    private String handleCharge;
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
    private Integer accAccNoRule;
    /**
     * 贷款账号
     */
    private String accCussent;
    private String fcaccId;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    private String poolSign;
    /**
     * 关联活期账号
     */
    private String relationAcc;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 利率种类 0-基准利率，1-基础利率
     */
    private String referenceRateType;
    /**
     * 利息收入支出户
     */
    private String interestInExp;
    /**
     * 应收应付利息户
     */
    private String interestRecPay;
    /**
     * 计息账号
     */
    private String interestAcc;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  扣息活期存款账户
     */
    @Column(name = "ACC_FOR_DEDUCT", nullable = true, length = 50)
    public String getAccForDeduct() {
        return this.accForDeduct;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  扣息活期存款账户
     */
    public void setAccForDeduct(String accForDeduct) {
        this.accForDeduct = accForDeduct;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String
     */
    @Column(name = "FCACC_ID", nullable = false, length = 32)
    public String getFcaccId() {
        return fcaccId;
    }
    public void setFcaccId(String fcaccId) {
        this.fcaccId = fcaccId;
    }
    @Column(name = "ACC_CUSSENT", nullable = true, length = 50)
    public String getAccCussent() {
        return accCussent;
    }
    public void setAccCussent(String accCussent) {
        this.accCussent = accCussent;
    }
    @Transient
    public Integer getAccAccNoRule() {
        return accAccNoRule;
    }
    public void setAccAccNoRule(Integer accAccNoRule) {
        this.accAccNoRule = accAccNoRule;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  结算中心账号
     */
    @Column(name = "BLNCTR_ACC", nullable = false, length = 50)
    public String getBlnctrAcc() {
        return this.blnctrAcc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  结算中心账号
     */
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账户名称
     */
    @Column(name = "ACC_NAME", nullable = false, length = 80)
    public String getAccName() {
        return this.accName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账户名称
     */
    public void setAccName(String accName) {
        this.accName = accName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  网点ID
     */
    @Column(name = "NET_ID", nullable = false, length = 32)
    public String getNetId() {
        return this.netId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  网点ID
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  开户单位ID
     */
    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  开户单位ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  币别代码
     */
    @Column(name = "CUR_ID", nullable = false, length = 32)
    public String getCurId() {
        return this.curId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  币别代码
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  科目代码
     */
    @Column(name = "SUBJECT_ID", nullable = false, length = 32)
    public String getSubjectId() {
        return this.subjectId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  科目代码
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账户类别
     */
    @Column(name = "ACC_TYPE", nullable = true, length = 32)
    public String getAccType() {
        return this.accType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账户类别
     */
    public void setAccType(String accType) {
        this.accType = accType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账户属性
     */
    @Column(name = "ATTRIBUTE_ID", nullable = true, length = 32)
    public String getAttributeId() {
        return this.attributeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账户属性
     */
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账户性质
     */
    @Column(name = "NATURE_CODE_ID", nullable = true, length = 32)
    public String getNatureCodeId() {
        return this.natureCodeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账户性质
     */
    public void setNatureCodeId(String natureCodeId) {
        this.natureCodeId = natureCodeId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  银行代码
     */
    @Column(name = "BANK_CODE", nullable = true, length = 20)
    public String getBankCode() {
        return this.bankCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  银行代码
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  余额方向
     */
    @Column(name = "BALANCE_WAY", nullable = false, length = 1)
    public Integer getBalanceWay() {
        return this.balanceWay;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  余额方向
     */
    public void setBalanceWay(Integer balanceWay) {
        this.balanceWay = balanceWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  计息标志
     */
    @Column(name = "INTEREST_SIGN", nullable = false, length = 1)
    public String getInterestSign() {
        return this.interestSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  计息标志
     */
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  起息日期
     */
    @Column(name = "INTEREST_START_DATE", nullable = true)
    public Date getInterestStartDate() {
        return this.interestStartDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  起息日期
     */
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  利率
     */
    @Column(name = "INTEREST_RATE", nullable = true, scale = 6, length = 8)
    public BigDecimal getInterestRate() {
        return this.interestRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  利率
     */
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  透支标志
     */
    @Column(name = "OVERDRAFT_SIGN", nullable = true, length = 1)
    public String getOverdraftSign() {
        return this.overdraftSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  透支标志
     */
    public void setOverdraftSign(String overdraftSign) {
        this.overdraftSign = overdraftSign;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  透支利率
     */
    @Column(name = "OVERDRAFT_RATE", nullable = true, scale = 6, length = 8)
    public BigDecimal getOverdraftRate() {
        return this.overdraftRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  透支利率
     */
    public void setOverdraftRate(BigDecimal overdraftRate) {
        this.overdraftRate = overdraftRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  透支金额
     */
    @Column(name = "OVERDRAFT_MONEY", nullable = true, scale = 2, length = 14)
    public BigDecimal getOverdraftMoney() {
        return this.overdraftMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  透支金额
     */
    public void setOverdraftMoney(BigDecimal overdraftMoney) {
        this.overdraftMoney = overdraftMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  合同编号
     */
    @Column(name = "CONTRACT_NO", nullable = true, length = 20)
    public String getContractNo() {
        return this.contractNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  合同编号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  到期日期
     */
    @Column(name = "DEADLINE_DATE", nullable = true)
    public Date getDeadlineDate() {
        return this.deadlineDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  到期日期
     */
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  展期到期日期
     */
    @Column(name = "DELAY_DEADLINE_DATE", nullable = true)
    public Date getDelayDeadlineDate() {
        return this.delayDeadlineDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  展期到期日期
     */
    public void setDelayDeadlineDate(Date delayDeadlineDate) {
        this.delayDeadlineDate = delayDeadlineDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  逾期利率
     */
    @Column(name = "OVERDUE_RATE", nullable = true, scale = 6, length = 8)
    public BigDecimal getOverdueRate() {
        return this.overdueRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  逾期利率
     */
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  扣息方式
     */
    @Column(name = "DEDUCT_MODE", nullable = true, length = 1)
    public String getDeductMode() {
        return this.deductMode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  扣息方式
     */
    public void setDeductMode(String deductMode) {
        this.deductMode = deductMode;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  通知类型
     */
    @Column(name = "NOTIFY_TYPE", nullable = true, length = 5)
    public Integer getNotifyType() {
        return this.notifyType;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  通知类型
     */
    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账户状态
     */
    @Column(name = "ACC_STATE", nullable = false, length = 1)
    public String getAccState() {
        return this.accState;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账户状态
     */
    public void setAccState(String accState) {
        this.accState = accState;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  协定存款标志
     */
    @Column(name = "AGREED_DEPOSIT_SIGN", nullable = true, length = 1)
    public String getAgreedDepositSign() {
        return this.agreedDepositSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  协定存款标志
     */
    public void setAgreedDepositSign(String agreedDepositSign) {
        this.agreedDepositSign = agreedDepositSign;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  预提标志
     */
    @Column(name = "PREDRAW_SIGN", nullable = true, length = 1)
    public String getPredrawSign() {
        return this.predrawSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  预提标志
     */
    public void setPredrawSign(String predrawSign) {
        this.predrawSign = predrawSign;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  是否产生对账数据
     */
    @Column(name = "IF_COMPARE", nullable = true, length = 1)
    public String getIfCompare() {
        if ((InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(this.accType)
                || InnerAccountConstant.FCACC_BANK_DEMAND.equals(this.accType)) && StringUtils.isBlank(ifCompare)) {
            this.ifCompare = InnerAccountConstant.BOOLEAN_TYPE_YES;
        } else if (StringUtils.isBlank(ifCompare)) {
            this.ifCompare = InnerAccountConstant.BOOLEAN_TYPE_NO;
        }
        return this.ifCompare;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  是否产生对账数据
     */
    public void setIfCompare(String ifCompare) {
        this.ifCompare = ifCompare;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  往来账号
     */
    @Column(name = "ACC_ID", nullable = true, length = 32)
    public String getAccId() {
        return this.accId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  往来账号
     */
    public void setAccId(String accId) {
        this.accId = accId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  账号启用日期
     */
    @Column(name = "ACC_START_DATE", nullable = true)
    public Date getAccStartDate() {
        return this.accStartDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  账号启用日期
     */
    public void setAccStartDate(Date accStartDate) {
        this.accStartDate = accStartDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  经办人代码
     */
    @Column(name = "OPERATOR_CODE", nullable = true, length = 20)
    public String getOperatorCode() {
        return this.operatorCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  经办人代码
     */
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  开户日期
     */
    @Column(name = "ACC_OPEN_TIME", nullable = false)
    public Date getAccOpenTime() {
        return this.accOpenTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  开户日期
     */
    public void setAccOpenTime(Date accOpenTime) {
        this.accOpenTime = accOpenTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  复核员代码
     */
    @Column(name = "CHECKER_CODE", nullable = true, length = 20)
    public String getCheckerCode() {
        return this.checkerCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  复核员代码
     */
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  复核日期
     */
    @Column(name = "CHECK_TIME", nullable = true)
    public Date getCheckTime() {
        return this.checkTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  复核日期
     */
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  区域代码
     */
    @Column(name = "AREA_CODE", nullable = true, length = 4)
    public String getAreaCode() {
        return this.areaCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  区域代码
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  行业代码
     */
    @Column(name = "VOCATION_CODE", nullable = true, length = 4)
    public String getVocationCode() {
        return this.vocationCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  行业代码
     */
    public void setVocationCode(String vocationCode) {
        this.vocationCode = vocationCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  助记码
     */
    @Column(name = "SHORTCUT", nullable = true, length = 80)
    public String getShortcut() {
        return this.shortcut;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  助记码
     */
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  联行网点
     */
    @Column(name = "UNION_NET", nullable = true, length = 4)
    public String getUnionNet() {
        return this.unionNet;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  联行网点
     */
    public void setUnionNet(String unionNet) {
        this.unionNet = unionNet;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  展期利率
     */
    @Column(name = "DELAY_RATE", nullable = true, scale = 6, length = 8)
    public BigDecimal getDelayRate() {
        return this.delayRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  展期利率
     */
    public void setDelayRate(BigDecimal delayRate) {
        this.delayRate = delayRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  联行账号
     */
    @Column(name = "UNION_ACC", nullable = true, length = 50)
    public String getUnionAcc() {
        return this.unionAcc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  联行账号
     */
    public void setUnionAcc(String unionAcc) {
        this.unionAcc = unionAcc;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  委托贷款帐号
     */
    @Column(name = "PRECATION_ACC", nullable = true, length = 50)
    public String getPrecationAcc() {
        return this.precationAcc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  委托贷款帐号
     */
    public void setPrecationAcc(String precationAcc) {
        this.precationAcc = precationAcc;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  委托贷款方
     */
    @Column(name = "PRECATION_CORP", nullable = true, length = 32)
    public String getPrecationCorp() {
        return this.precationCorp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  委托贷款方
     */
    public void setPrecationCorp(String precationCorp) {
        this.precationCorp = precationCorp;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  委托存款的手续费率
     */
    @Column(name = "POUNDAGE_RATE", nullable = true, scale = 6, length = 8)
    public BigDecimal getPoundageRate() {
        return this.poundageRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  委托存款的手续费率
     */
    public void setPoundageRate(BigDecimal poundageRate) {
        this.poundageRate = poundageRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  手续费收取账户
     */
    @Column(name = "CHARGE_ACC", nullable = true, length = 50)
    public String getChargeAcc() {
        return this.chargeAcc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  手续费收取账户
     */
    public void setChargeAcc(String chargeAcc) {
        this.chargeAcc = chargeAcc;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  办理方式
     */
    @Column(name = "TRANSACT_WAY", nullable = true, length = 2)
    public Integer getTransactWay() {
        return this.transactWay;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  办理方式
     */
    public void setTransactWay(Integer transactWay) {
        this.transactWay = transactWay;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  销户日期
     */
    @Column(name = "CANCEL_DATE", nullable = true)
    public Date getCancelDate() {
        return this.cancelDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  销户日期
     */
    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  有效标志
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return this.validSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  有效标志
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  打回原因
     */
    @Column(name = "CHECK_BACK_REASON", nullable = true, length = 150)
    public String getCheckBackReason() {
        return this.checkBackReason;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  打回原因
     */
    public void setCheckBackReason(String checkBackReason) {
        this.checkBackReason = checkBackReason;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  到期单位
     */
    @Column(name = "END_UNIT", nullable = true, length = 1)
    public String getEndUnit() {
        return this.endUnit;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  到期单位
     */
    public void setEndUnit(String endUnit) {
        this.endUnit = endUnit;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  到期续存方式
     */
    @Column(name = "CERTIFICATE_WAY", nullable = true, length = 2)
    public Integer getCertificateWay() {
        return this.certificateWay;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  到期续存方式
     */
    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  是否收取手续费
     */
    @Column(name = "HANDLE_CHARGE", nullable = true, length = 1)
    public String getHandleCharge() {
        return this.handleCharge;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  是否收取手续费
     */
    public void setHandleCharge(String handleCharge) {
        this.handleCharge = handleCharge;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人登录名称
     */
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人登录名称
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "POOL_SIGN", nullable = true, length = 1)
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    @Column(name = "RELATION_ACC")
    public String getRelationAcc() {
        return relationAcc;
    }
    public void setRelationAcc(String relationAcc) {
        this.relationAcc = relationAcc;
    }
    @Column(name = "rmk")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    @Column(name = "INTEREST_IN_EXP")
    public String getInterestInExp() {
        return interestInExp;
    }
    public void setInterestInExp(String interestInExp) {
        this.interestInExp = interestInExp;
    }
    @Column(name = "INTEREST_REC_PAY")
    public String getInterestRecPay() {
        return interestRecPay;
    }
    public void setInterestRecPay(String interestRecPay) {
        this.interestRecPay = interestRecPay;
    }
    @Transient
    public String getInterestAcc() {
        return interestAcc;
    }
    public void setInterestAcc(String interestAcc) {
        this.interestAcc = interestAcc;
    }
}
