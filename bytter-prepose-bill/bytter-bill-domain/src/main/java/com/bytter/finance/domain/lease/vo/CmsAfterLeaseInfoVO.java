package com.bytter.finance.domain.lease.vo;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author wanggl
 * @version 1.0
 * @Description: 融资租赁租后管理表vo
 * @date 2022-04-02
 */
public class CmsAfterLeaseInfoVO implements Serializable, IBusinessKey {
    private String id;
    private String leaseId;
    /**
     * 融资租赁单号
     */
    @BtCompositeSearch(fieldName = "linfo.lease_no")
    private String leaseNo;
    /**
     * 融资租赁类型
     */
    @BtCompositeSearch(fieldName = "linfo.lease_type")
    private String leaseType;
    /**
     * 融资租赁合同编号
     */
    @BtCompositeSearch(fieldName = "linfo.lease_code")
    private String leaseCode;
    /**
     * 境内/境外
     */
    @Dict(dictGroup = "btBankAcc.isDomesticBank", inject = "isDomesticName")
    private String isDomestic;
    private String isDomesticName;
    /**
     * 买卖合同编号
     */
    @BtCompositeSearch(fieldName = "linfo.pay_code")
    private String payCode;
    /**
     * 承租人
     */
    @BtCompositeSearch(fieldName = "linfo.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 出租人
     */
    @BtCompositeSearch(fieldName = "linfo.lessor")
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
//    @Currency(inject = "oriCurName")
    @BtCompositeSearch(fieldName = "linfo.ori_curid")
    private String oriCurid;
    private String oriCurName;
    /**
     * 原币金额
     */
    @BtCompositeSearch(fieldName = "linfo.ori_money")
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
    @BtCompositeSearch(fieldName = "linfo.begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 到期日
     */
    @BtCompositeSearch(fieldName = "linfo.end_date")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date floatDate;
    /**
     * 利率调整生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    private String bondBankacc;
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
     * 保证金Id
     */
    private String bondId;
    /**
     * 修改前挂接保证金Id
     */
    private String bondIdPre;
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
    private List<DfIntermediateCost> costTypeDtl;
    /**
     * 是否挂抵质押物
     */
    private String isRelatePledge;
    /**
     * 抵质押物id
     */
    private String[] pledgeId;
    /**
     * 占用抵质押物额度
     */
    private BigDecimal[] pledgeMoney;
    /**
     * 附件ID
     */
    private String uploadId;
    /**
     * 状态
     */
    private Integer status;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 退租时间
     */
    @BtCompositeSearch(fieldName = "r.refund_date")
    private Date refundDate;
    /**
     * 是否提前退租
     */
    @BtCompositeSearch(fieldName = "r.is_advance_back")
    private String isAdvanceBack;
    /**
     * 退租备注
     */
    @BtCompositeSearch(fieldName = "r.rmk")
    private String refundRmk;
    /**
     * 退租状态
     */
    @BtCompositeSearch(fieldName = "r.status")
    private String refundStatus;
    /**
     * 续租日期
     */
    @BtCompositeSearch(fieldName = "c.continue_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date continueDate;
    /**
     * 续租合同
     */
    @BtCompositeSearch(fieldName = "c.continue_code")
    private String continueCode;
    /**
     * 续租备注
     */
    @BtCompositeSearch(fieldName = "afc.continue_rmk")
    private String continueRmk;
    /**
     * 续租状态
     */
    @BtCompositeSearch(fieldName = "afc.continue_status")
    private String continueStatus;
    /**
     * 回购日期
     */
    private Date repoDate;
    /**
     * 回购金额
     */
    private String repoMoney;
    /**
     * 回购备注
     */
    private String repoRmk;
    /**
     * 回购状态
     */
    private String repoStatus;
    /**
     * 展期到期日期
     */
    private Date expendedDate;
    public Date getContinueDate() {
        return continueDate;
    }
    public void setContinueDate(Date continueDate) {
        this.continueDate = continueDate;
    }
    public String getContinueCode() {
        return continueCode;
    }
    public void setContinueCode(String continueCode) {
        this.continueCode = continueCode;
    }
    public String getContinueRmk() {
        return continueRmk;
    }
    public void setContinueRmk(String continueRmk) {
        this.continueRmk = continueRmk;
    }
    public String getContinueStatus() {
        return continueStatus;
    }
    public void setContinueStatus(String continueStatus) {
        this.continueStatus = continueStatus;
    }
    public Date getRefundDate() {
        return refundDate;
    }
    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }
    public String getIsAdvanceBack() {
        return isAdvanceBack;
    }
    public void setIsAdvanceBack(String isAdvanceBack) {
        this.isAdvanceBack = isAdvanceBack;
    }
    public String getRefundRmk() {
        return refundRmk;
    }
    public void setRefundRmk(String refundRmk) {
        this.refundRmk = refundRmk;
    }
    public String getRefundStatus() {
        return refundStatus;
    }
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getId() {
        return id;
    }
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLeaseNo() {
        return leaseNo;
    }
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    public String getLeaseType() {
        return leaseType;
    }
    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }
    public String getLeaseCode() {
        return leaseCode;
    }
    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }
    public String getIsDomestic() {
        return isDomestic;
    }
    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
    }
    public String getIsDomesticName() {
        return isDomesticName;
    }
    public void setIsDomesticName(String isDomesticName) {
        this.isDomesticName = isDomesticName;
    }
    public String getPayCode() {
        return payCode;
    }
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getLessor() {
        return lessor;
    }
    public void setLessor(String lessor) {
        this.lessor = lessor;
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getLeaseItem() {
        return leaseItem;
    }
    public void setLeaseItem(String leaseItem) {
        this.leaseItem = leaseItem;
    }
    public String getOriCurid() {
        return oriCurid;
    }
    public void setOriCurid(String oriCurid) {
        this.oriCurid = oriCurid;
    }
    public String getOriCurName() {
        return oriCurName;
    }
    public void setOriCurName(String oriCurName) {
        this.oriCurName = oriCurName;
    }
    public BigDecimal getOriMoney() {
        return oriMoney;
    }
    public void setOriMoney(BigDecimal oriMoney) {
        this.oriMoney = oriMoney;
    }
    public BigDecimal getIncRate() {
        return incRate;
    }
    public void setIncRate(BigDecimal incRate) {
        this.incRate = incRate;
    }
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getDueProcess() {
        return dueProcess;
    }
    public void setDueProcess(String dueProcess) {
        this.dueProcess = dueProcess;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public Date getFloatDate() {
        return floatDate;
    }
    public void setFloatDate(Date floatDate) {
        this.floatDate = floatDate;
    }
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    public Date getQuoDate() {
        return quoDate;
    }
    public void setQuoDate(Date quoDate) {
        this.quoDate = quoDate;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getRentCalWay() {
        return rentCalWay;
    }
    public void setRentCalWay(String rentCalWay) {
        this.rentCalWay = rentCalWay;
    }
    public String getInterestFre() {
        return interestFre;
    }
    public void setInterestFre(String interestFre) {
        this.interestFre = interestFre;
    }
    public String getPaymoneyFre() {
        return paymoneyFre;
    }
    public void setPaymoneyFre(String paymoneyFre) {
        this.paymoneyFre = paymoneyFre;
    }
    public String getPaymentFre() {
        return paymentFre;
    }
    public void setPaymentFre(String paymentFre) {
        this.paymentFre = paymentFre;
    }
    public Date getBeginPaymoneyDate() {
        return beginPaymoneyDate;
    }
    public void setBeginPaymoneyDate(Date beginPaymoneyDate) {
        this.beginPaymoneyDate = beginPaymoneyDate;
    }
    public String getIns() {
        return ins;
    }
    public void setIns(String ins) {
        this.ins = ins;
    }
    public String getIsHookBond() {
        return isHookBond;
    }
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    public String getBondBankacc() {
        return bondBankacc;
    }
    public void setBondBankacc(String bondBankacc) {
        this.bondBankacc = bondBankacc;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public Integer getExpendedCount() {
        return expendedCount;
    }
    public void setExpendedCount(Integer expendedCount) {
        this.expendedCount = expendedCount;
    }
    public Integer getContinueCount() {
        return continueCount;
    }
    public void setContinueCount(Integer continueCount) {
        this.continueCount = continueCount;
    }
    public String getBondId() {
        return bondId;
    }
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    public String getBondIdPre() {
        return bondIdPre;
    }
    public void setBondIdPre(String bondIdPre) {
        this.bondIdPre = bondIdPre;
    }
    public BigDecimal getBailmoneyRate() {
        return bailmoneyRate;
    }
    public void setBailmoneyRate(BigDecimal bailmoneyRate) {
        this.bailmoneyRate = bailmoneyRate;
    }
    public BigDecimal getOriBailmoney() {
        return oriBailmoney;
    }
    public void setOriBailmoney(BigDecimal oriBailmoney) {
        this.oriBailmoney = oriBailmoney;
    }
    public BigDecimal getRmbBailmoney() {
        return rmbBailmoney;
    }
    public void setRmbBailmoney(BigDecimal rmbBailmoney) {
        this.rmbBailmoney = rmbBailmoney;
    }
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    public String getBailmoneyContract() {
        return bailmoneyContract;
    }
    public void setBailmoneyContract(String bailmoneyContract) {
        this.bailmoneyContract = bailmoneyContract;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getGuaMan() {
        return guaMan;
    }
    public void setGuaMan(String guaMan) {
        this.guaMan = guaMan;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getGuaRmk() {
        return guaRmk;
    }
    public void setGuaRmk(String guaRmk) {
        this.guaRmk = guaRmk;
    }
    public List<DfIntermediateCost> getCostTypeDtl() {
        return costTypeDtl;
    }
    public void setCostTypeDtl(List<DfIntermediateCost> costTypeDtl) {
        this.costTypeDtl = costTypeDtl;
    }
    public String getIsRelatePledge() {
        return isRelatePledge;
    }
    public void setIsRelatePledge(String isRelatePledge) {
        this.isRelatePledge = isRelatePledge;
    }
    public String[] getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String[] pledgeId) {
        this.pledgeId = pledgeId;
    }
    public BigDecimal[] getPledgeMoney() {
        return pledgeMoney;
    }
    public void setPledgeMoney(BigDecimal[] pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getControlStatus() {
        return controlStatus;
    }
    public void setControlStatus(String controlStatus) {
        this.controlStatus = controlStatus;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Date getRepoDate() {
        return repoDate;
    }
    public void setRepoDate(Date repoDate) {
        this.repoDate = repoDate;
    }
    public String getRepoMoney() {
        return repoMoney;
    }
    public void setRepoMoney(String repoMoney) {
        this.repoMoney = repoMoney;
    }
    public String getRepoRmk() {
        return repoRmk;
    }
    public void setRepoRmk(String repoRmk) {
        this.repoRmk = repoRmk;
    }
    public String getRepoStatus() {
        return repoStatus;
    }
    public void setRepoStatus(String repoStatus) {
        this.repoStatus = repoStatus;
    }
    public Date getExpendedDate() {
        return expendedDate;
    }
    public void setExpendedDate(Date expendedDate) {
        this.expendedDate = expendedDate;
    }
}
