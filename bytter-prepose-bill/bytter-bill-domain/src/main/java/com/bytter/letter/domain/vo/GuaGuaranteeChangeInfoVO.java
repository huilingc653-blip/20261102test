package com.bytter.letter.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
    /**
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeInfoVO
 * @Description 保函变更VO
 * @date 2019年1月9日
 */
public class GuaGuaranteeChangeInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 8568844566933081156L;
    private String id;
    /**
     * 保函ID
     */
    private String guaraId;
    /**
     * 保函编号
     */
    @Excel(name = "*保函编号")
    @BtCompositeSearch(fieldName = "a.guarantee_no")
    private String guaranteeNo;
    /**
     * 保函变更申请单编号
     */
    @Excel(name = "*保函变更单编号")
    @BtCompositeSearch(fieldName = "b.guara_change_no")
    private String guaraChangeNo;
    /**
     * 变更次数
     */
    @Excel(name = "*变更次数")
    private Integer changeCount;
    /**
     * 保函类型
     */
    private String guaranteeType;
    /**
     * 保函类型名称
     */
    private String guaranteeTypeName;
    /**
     * 保函申请人
     */
    @BtCompositeSearch(fieldName = "a.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    /**
     * 保函申请单位名称
     */
    @Excel(name = "*保函申请单位")
    private String corpName;
    /**
     * 开立人类型
     */
    private String openCorpType;
    /**
     * 开立方式
     */
    private String openWay;
    /**
     * 开立类型
     */
    private String openType;
    /**
     * 保函开立人
     */
    @BtCompositeSearch(fieldName = "a.open_corp")
    private String openCorp;
    /**
     * 保函开立人名称
     */
    @BtCompositeSearch(fieldName = "a.BANK_NAME")
    private String openCorpName;
    /**
     * 保函受益人
     */
    @Excel(name = "*保函受益人")
    @BtCompositeSearch(fieldName = "a.income_corp")
    private String incomeCorp;
    /**
     * 币别
     */
//    @Currency(inject = "curName")
    private String curId;
    /**
     * 币别名称
     */
    @Excel(name = "*币别")
    @BtCompositeSearch(fieldName = "a.cur_Id")
    private String curName;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 保函金额调整类型
     */
    private String guaChangeType;
    /**
     * 保函调整金额
     */
    private java.math.BigDecimal guaChangeMoney;
    /**
     * 调整后保函金额
     */
    private java.math.BigDecimal guaranteeMoney;
    /**
     * 保函性质
     */
    private String guaranteeAttr;
    /** 保函事项 */
    // private java.lang.String guaItem;
    /**
     * 保函预计结束时间
     */
    private java.util.Date expectEndDate;
    /**
     * 保函开始日期
     */
    private java.util.Date startDate;
    /**
     * 保函结束日期
     */
    private java.util.Date endDate;
    /** 转开行 */
    // private java.lang.String turnBank;
    /**
     * 状态
     */
    @Excel(name = "*状态")
    @BtCompositeSearch(fieldName = "b.status")
    private Integer status;
    /** 附件ID */
    // private java.lang.String attachmentId;
    /** 费率 */
    // private java.math.BigDecimal handleRate;
    /**
     * 手续费
     */
    private java.math.BigDecimal handleCharge;
    /** 保证金比例 */
    // private java.math.BigDecimal bondPercent;
    /**
     * 保函保证金调整类型
     */
    private String bondChangeType;
    /**
     * 保函保证金调整金额
     */
    private java.math.BigDecimal bondChangeMoney;
    /**
     * 调整后保证金金额
     */
    private java.math.BigDecimal bondMoney;
    /**
     * 保证金账户
     */
    private String bondAcc;
    /**
     * 申请变更日期
     */
    @Excel(name = "*保函变更申请日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.change_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date changeDate;
    /**
     * 修改条款
     */
    private String changeTerm;
    /**
     * 修改原因
     */
    private String changeReason;
    /**
     * 经办人
     */
    private String operator;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /** 以下是原保函的字段信息 */
    /**
     * 原保函结束时间
     */
    private java.util.Date endDateOld;
    /**
     * 原保函预计结束时间
     */
    private java.util.Date expectEndDateOld;
    /**
     * 原保函金额
     */
    private java.math.BigDecimal guaranteeMoneyOld;
    /**
     * 原手续费
     */
    private java.math.BigDecimal handleChargeOld;
    /**
     * 原保证金
     */
    private java.math.BigDecimal bondMoneyOld;
    private String ratmodel;
    /**
     * 是否挂接保证金
     */
    private String isHookBond;
    /**
     * 是否启用敞口授信
     */
    private String isOpenRat;
    /**
     * 保证金合同
     */
    private String bondContract;
    /**
     * 保证金币别
     */
//    @Currency(inject = "bondCurName")
    private String bondCurId;
    /**
     * 保证金币别
     */
    private String bondCurName;
    /**
     * 是否折合本位币
     */
    private String isRmbRat;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGuaranteeNo() {
        return guaranteeNo;
    }
    public void setGuaranteeNo(String guaranteeNo) {
        this.guaranteeNo = guaranteeNo;
    }
    public String getGuaranteeType() {
        return guaranteeType;
    }
    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getOpenCorpType() {
        return openCorpType;
    }
    public void setOpenCorpType(String openCorpType) {
        this.openCorpType = openCorpType;
    }
    public String getOpenWay() {
        return openWay;
    }
    public void setOpenWay(String openWay) {
        this.openWay = openWay;
    }
    public String getOpenType() {
        return openType;
    }
    public void setOpenType(String openType) {
        this.openType = openType;
    }
    public String getOpenCorp() {
        return openCorp;
    }
    public void setOpenCorp(String openCorp) {
        this.openCorp = openCorp;
    }
    public String getIncomeCorp() {
        return incomeCorp;
    }
    public void setIncomeCorp(String incomeCorp) {
        this.incomeCorp = incomeCorp;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public java.math.BigDecimal getGuaranteeMoney() {
        return guaranteeMoney;
    }
    public void setGuaranteeMoney(java.math.BigDecimal guaranteeMoney) {
        this.guaranteeMoney = guaranteeMoney;
    }
    public java.util.Date getExpectEndDate() {
        return expectEndDate;
    }
    public void setExpectEndDate(java.util.Date expectEndDate) {
        this.expectEndDate = expectEndDate;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public java.math.BigDecimal getHandleCharge() {
        return handleCharge;
    }
    public void setHandleCharge(java.math.BigDecimal handleCharge) {
        this.handleCharge = handleCharge;
    }
    public java.math.BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(java.math.BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getOpenCorpName() {
        return openCorpName;
    }
    public void setOpenCorpName(String openCorpName) {
        this.openCorpName = openCorpName;
    }
    public String getGuaraId() {
        return guaraId;
    }
    public void setGuaraId(String guaraId) {
        this.guaraId = guaraId;
    }
    public String getGuaraChangeNo() {
        return guaraChangeNo;
    }
    public void setGuaraChangeNo(String guaraChangeNo) {
        this.guaraChangeNo = guaraChangeNo;
    }
    public Integer getChangeCount() {
        return changeCount;
    }
    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }
    public String getGuaChangeType() {
        return guaChangeType;
    }
    public void setGuaChangeType(String guaChangeType) {
        this.guaChangeType = guaChangeType;
    }
    public java.math.BigDecimal getGuaChangeMoney() {
        return guaChangeMoney;
    }
    public void setGuaChangeMoney(java.math.BigDecimal guaChangeMoney) {
        this.guaChangeMoney = guaChangeMoney;
    }
    public String getBondChangeType() {
        return bondChangeType;
    }
    public void setBondChangeType(String bondChangeType) {
        this.bondChangeType = bondChangeType;
    }
    public java.math.BigDecimal getBondChangeMoney() {
        return bondChangeMoney;
    }
    public void setBondChangeMoney(java.math.BigDecimal bondChangeMoney) {
        this.bondChangeMoney = bondChangeMoney;
    }
    public java.util.Date getChangeDate() {
        return changeDate;
    }
    public void setChangeDate(java.util.Date changeDate) {
        this.changeDate = changeDate;
    }
    public String getChangeTerm() {
        return changeTerm;
    }
    public void setChangeTerm(String changeTerm) {
        this.changeTerm = changeTerm;
    }
    public String getChangeReason() {
        return changeReason;
    }
    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getGuaranteeAttr() {
        return guaranteeAttr;
    }
    public void setGuaranteeAttr(String guaranteeAttr) {
        this.guaranteeAttr = guaranteeAttr;
    }
    public java.util.Date getEndDateOld() {
        return endDateOld;
    }
    public void setEndDateOld(java.util.Date endDateOld) {
        this.endDateOld = endDateOld;
    }
    public java.util.Date getExpectEndDateOld() {
        return expectEndDateOld;
    }
    public void setExpectEndDateOld(java.util.Date expectEndDateOld) {
        this.expectEndDateOld = expectEndDateOld;
    }
    public java.math.BigDecimal getGuaranteeMoneyOld() {
        return guaranteeMoneyOld;
    }
    public void setGuaranteeMoneyOld(java.math.BigDecimal guaranteeMoneyOld) {
        this.guaranteeMoneyOld = guaranteeMoneyOld;
    }
    public java.math.BigDecimal getHandleChargeOld() {
        return handleChargeOld;
    }
    public void setHandleChargeOld(java.math.BigDecimal handleChargeOld) {
        this.handleChargeOld = handleChargeOld;
    }
    public java.math.BigDecimal getBondMoneyOld() {
        return bondMoneyOld;
    }
    public void setBondMoneyOld(java.math.BigDecimal bondMoneyOld) {
        this.bondMoneyOld = bondMoneyOld;
    }
    public String getRatmodel() {
        return ratmodel;
    }
    public void setRatmodel(String ratmodel) {
        this.ratmodel = ratmodel;
    }
    public String getGuaranteeTypeName() {
        return guaranteeTypeName;
    }
    public void setGuaranteeTypeName(String guaranteeTypeName) {
        this.guaranteeTypeName = guaranteeTypeName;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getIsHookBond() {
        return isHookBond;
    }
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    public String getBondAcc() {
        return bondAcc;
    }
    public void setBondAcc(String bondAcc) {
        this.bondAcc = bondAcc;
    }
    public String getBondContract() {
        return bondContract;
    }
    public void setBondContract(String bondContract) {
        this.bondContract = bondContract;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public String getIsRmbRat() {
        return isRmbRat;
    }
    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
    }
}
