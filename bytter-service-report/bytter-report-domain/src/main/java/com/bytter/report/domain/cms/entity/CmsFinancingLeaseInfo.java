package com.bytter.report.domain.cms.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 融资租赁表
 * @date 2022-04-02
 */
@Entity
@Table(name = "CMS_FINANCING_LEASE_INFO", schema = "")
public class CmsFinancingLeaseInfo extends IdEntity implements java.io.Serializable {
    /**
     * 融资租赁单号
     */
    private String leaseNo;
    /**
     * 融资租赁类型
     */
    private String leaseType;
    /**
     * 融资租赁合同编号
     */
    private String leaseCode;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 原币币种名称
     */
    private String oriCurName;
    /**
     * 境内/境外
     */
    private String isDomestic;
    /**
     * 买卖合同编号
     */
    private String payCode;
    /**
     * 承租人
     */
    private String corpId;
    /**
     * 出租人
     */
    private String lessor;
    /**
     * 供货商
     */
    private String supplier;
    /**
     * 租赁物
     */
    private String leaseItem;
    /**
     * 原币币种
     */
    private String oriCurid;
    /**
     * 原币金额
     */
    private BigDecimal oriMoney;
    /**
     * 内含报酬率
     */
    private BigDecimal incRate;
    /**
     * 人名币金额
     */
    private BigDecimal rmbMoney;
    /**
     * 起租日
     */
    private Date beginDate;
    /**
     * 到期日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 租赁期限
     */
    private Integer dayLimit;
    /**
     * 期限单位
     */
    private String ternUnit;
    /**
     * 到期处理
     */
    private String dueProcess;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 浮动方式
     */
    private String floatWay;
    /**
     * 二级浮动方式
     */
    private String floatWayNext;
    /**
     * 浮动日期
     */
    private Date floatDate;
    /**
     * 利率调整生效日期
     */
    private Date effectiveDate;
    /**
     * 定价基准利率类型
     */
    private String referenceRateType;
    /**
     * 利率期限
     */
    private String repayCycle;
    /**
     * 点数或者比例值
     */
    private BigDecimal pointsProportionText;
    /**
     * 点数或者比例
     */
    private String pointsProportionSelect;
    /**
     * 报价日期
     */
    private Date quoDate;
    /**
     * 利率
     */
    private BigDecimal rate;
    /**
     * 年度利率天数
     */
    private String rateBase;
    /**
     * 计息方式
     */
    private String interestWay;
    /**
     * 租金计算方式
     */
    private String rentCalWay;
    /**
     * 付息频率
     */
    private String interestFre;
    /**
     * 本金支付频率
     */
    private String paymoneyFre;
    /**
     * 费用支付频率
     */
    private String paymentFre;
    /**
     * 租金起始支付日
     */
    private Date beginPaymoneyDate;
    /**
     * 说明
     */
    private String ins;
    /**
     * 是否挂保证金
     */
    private String isHookBond;
    /**
     * 保证金账户
     */
    private String bondBankAcc;
    /**
     * 保证金币种
     */
    private String bondCurId;
    /**
     * 展期次数
     */
    private Integer expendedCount;
    /**
     * 续租次数
     */
    private Integer continueCount;
    /**
     * 保证金比例
     */
    private BigDecimal bailmoneyRate;
    /**
     * 保证金原币金额
     */
    private BigDecimal oriBailmoney;
    /**
     * 保证金人名币金额
     */
    private BigDecimal rmbBailmoney;
    /**
     * 保证金利率
     */
    private BigDecimal bondRate;
    /**
     * 保证金合同
     */
    private String bailmoneyContract;
    /**
     * 是否挂担保
     */
    private String isHangGuarantee;
    /**
     * 担保合同编号
     */
    private String guaContract;
    /**
     * 担保人
     */
    private String guaMan;
    /**
     * 担保方式
     */
    private String guaWay;
    /**
     * 担保备注
     */
    private String guaRmk;
    /**
     * 是否挂抵质押物
     */
    private String isRelatePledge;
    /**
     * 附件ID
     */
    private String uploadId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 控制字段
     */
    private String controlStatus;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    @Column(name = "LEASE_NO")
    public String getLeaseNo() {
        return leaseNo;
    }
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    @Column(name = "LEASE_TYPE")
    public String getLeaseType() {
        return leaseType;
    }
    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }
    @Column(name = "LEASE_CODE")
    public String getLeaseCode() {
        return leaseCode;
    }
    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }
    @Column(name = "ISDOMESTIC")
    public String getIsDomestic() {
        return isDomestic;
    }
    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
    }
    @Column(name = "PAY_CODE")
    public String getPayCode() {
        return payCode;
    }
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "LESSOR")
    public String getLessor() {
        return lessor;
    }
    public void setLessor(String lessor) {
        this.lessor = lessor;
    }
    @Column(name = "SUPPLIER")
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    @Column(name = "LEASE_ITEM")
    public String getLeaseItem() {
        return leaseItem;
    }
    public void setLeaseItem(String leaseItem) {
        this.leaseItem = leaseItem;
    }
    @Column(name = "ORI_CURID")
    public String getOriCurid() {
        return oriCurid;
    }
    public void setOriCurid(String oriCurid) {
        this.oriCurid = oriCurid;
    }
    @Column(name = "ORI_MONEY")
    public BigDecimal getOriMoney() {
        return oriMoney;
    }
    public void setOriMoney(BigDecimal oriMoney) {
        this.oriMoney = oriMoney;
    }
    @Column(name = "INC_RATE")
    public BigDecimal getIncRate() {
        return incRate;
    }
    public void setIncRate(BigDecimal incRate) {
        this.incRate = incRate;
    }
    @Column(name = "RMB_MONEY")
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    @Column(name = "BEGIN_DATE")
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "DAY_LIMIT")
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    @Column(name = "TERN_UNIT")
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    @Column(name = "DUE_PROCESS")
    public String getDueProcess() {
        return dueProcess;
    }
    public void setDueProcess(String dueProcess) {
        this.dueProcess = dueProcess;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "FLOAT_WAY")
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    @Column(name = "FLOAT_WAY_NEXT")
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    @Column(name = "FLOAT_DATE")
    public Date getFloatDate() {
        return floatDate;
    }
    public void setFloatDate(Date floatDate) {
        this.floatDate = floatDate;
    }
    @Column(name = "EFFECTIVE_DATE")
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    @Column(name = "POINTS_PROPORTION_SELECT")
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    @Column(name = "QUO_DATE")
    public Date getQuoDate() {
        return quoDate;
    }
    public void setQuoDate(Date quoDate) {
        this.quoDate = quoDate;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "RATE_BASE")
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    @Column(name = "RENT_CAL_WAY")
    public String getRentCalWay() {
        return rentCalWay;
    }
    public void setRentCalWay(String rentCalWay) {
        this.rentCalWay = rentCalWay;
    }
    @Column(name = "INTEREST_FRE")
    public String getInterestFre() {
        return interestFre;
    }
    public void setInterestFre(String interestFre) {
        this.interestFre = interestFre;
    }
    @Column(name = "PAYMONEY_FRE")
    public String getPaymoneyFre() {
        return paymoneyFre;
    }
    public void setPaymoneyFre(String paymoneyFre) {
        this.paymoneyFre = paymoneyFre;
    }
    @Column(name = "PAYMENT_FRE")
    public String getPaymentFre() {
        return paymentFre;
    }
    public void setPaymentFre(String paymentFre) {
        this.paymentFre = paymentFre;
    }
    @Column(name = "BEGIN_PAYMONEY_DATE")
    public Date getBeginPaymoneyDate() {
        return beginPaymoneyDate;
    }
    public void setBeginPaymoneyDate(Date beginPaymoneyDate) {
        this.beginPaymoneyDate = beginPaymoneyDate;
    }
    @Column(name = "INS")
    public String getIns() {
        return ins;
    }
    public void setIns(String ins) {
        this.ins = ins;
    }
    @Column(name = "IS_HOOK_BOND")
    public String getIsHookBond() {
        return isHookBond;
    }
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    @Column(name = "BOND_BANKACC")
    public String getBondBankAcc() {
        return bondBankAcc;
    }
    public void setBondBankAcc(String bondBankAcc) {
        this.bondBankAcc = bondBankAcc;
    }
    @Column(name = "BOND_CUR_ID")
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    @Column(name = "EXPENDED_COUNT")
    public Integer getExpendedCount() {
        return expendedCount;
    }
    public void setExpendedCount(Integer expendedCount) {
        this.expendedCount = expendedCount;
    }
    @Column(name = "CONTINUE_COUNT")
    public Integer getContinueCount() {
        return continueCount;
    }
    public void setContinueCount(Integer continueCount) {
        this.continueCount = continueCount;
    }
    @Column(name = "BAILMONEY_RATE")
    public BigDecimal getBailmoneyRate() {
        return bailmoneyRate;
    }
    public void setBailmoneyRate(BigDecimal bailmoneyRate) {
        this.bailmoneyRate = bailmoneyRate;
    }
    @Column(name = "ORI_BAILMONEY")
    public BigDecimal getOriBailmoney() {
        return oriBailmoney;
    }
    public void setOriBailmoney(BigDecimal oriBailmoney) {
        this.oriBailmoney = oriBailmoney;
    }
    @Column(name = "RMB_BAILMONEY")
    public BigDecimal getRmbBailmoney() {
        return rmbBailmoney;
    }
    public void setRmbBailmoney(BigDecimal rmbBailmoney) {
        this.rmbBailmoney = rmbBailmoney;
    }
    @Column(name = "BOND_RATE")
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    @Column(name = "BAILMONEY_CONTRACT")
    public String getBailmoneyContract() {
        return bailmoneyContract;
    }
    public void setBailmoneyContract(String bailmoneyContract) {
        this.bailmoneyContract = bailmoneyContract;
    }
    @Column(name = "IS_HANG_GUARANTEE")
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    @Column(name = "GUA_CONTRACT")
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    @Column(name = "GUA_MAN")
    public String getGuaMan() {
        return guaMan;
    }
    public void setGuaMan(String guaMan) {
        this.guaMan = guaMan;
    }
    @Column(name = "GUA_WAY")
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    @Column(name = "GUA_RMK")
    public String getGuaRmk() {
        return guaRmk;
    }
    public void setGuaRmk(String guaRmk) {
        this.guaRmk = guaRmk;
    }
    @Column(name = "IS_RELATE_PLEDGE")
    public String getIsRelatePledge() {
        return isRelatePledge;
    }
    public void setIsRelatePledge(String isRelatePledge) {
        this.isRelatePledge = isRelatePledge;
    }
    @Column(name = "UPLOAD_ID")
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "CONTROL_STATUS")
    public String getControlStatus() {
        return controlStatus;
    }
    public void setControlStatus(String controlStatus) {
        this.controlStatus = controlStatus;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "ORI_CUR_NAME")
    public String getOriCurName() {
        return oriCurName;
    }
    public void setOriCurName(String oriCurName) {
        this.oriCurName = oriCurName;
    }
}
