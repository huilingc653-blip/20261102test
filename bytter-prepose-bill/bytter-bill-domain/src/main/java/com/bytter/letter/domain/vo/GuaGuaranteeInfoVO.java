package com.bytter.letter.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeInfo
 * @Description 保函申请VO
 * @date 2019年1月7日
 */
public class GuaGuaranteeInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 8568844566933081156L;
    private String id;
    /**
     * 保函编号
     */
    @Excel(name = "*保函编号")
    @BtCompositeSearch(fieldName = "b.guarantee_no")
    private String guaranteeNo;
    /**
     * 保函类型
     */
    @Excel(name = "*保函类型")
    @BtCompositeSearch(fieldName = "b.guarantee_type")
    private String guaranteeType;
    /**
     * 保函申请单位
     */
    @BtCompositeSearch(fieldName = "b.corp_id")
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
    private String openCorp;
    @Excel(name = "*保函开立人")
    private String openCorpName;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 保函受益人
     */
    @Excel(name = "*保函受益人")
    @BtCompositeSearch(fieldName = "b.income_corp")
    private String incomeCorp;
    /**
     * 币别
     */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "b.cur_Id")
    private String curId;
    /**
     * 币别名称
     */
    @Excel(name = "*币别")
    private String curName;
    /**
     * 保函金额
     */
    @Excel(name = "*保函金额")
    @BtCompositeSearch(fieldName = "b.guarantee_money")
    private BigDecimal guaranteeMoney;
    /**
     * 保函性质
     */
    private String guaranteeAttr;
    /**
     * 保函事项
     */
    private String guaItem;
    /**
     * 保函预计结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expectEndDate;
    /**
     * 保函开始日期
     */
    @Excel(name = "*保函开始日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 保函结束日期
     */
    @Excel(name = "*保函结束日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 转开行
     */
    private String turnBank;
    /**
     * 状态
     */
    @Excel(name = "*状态")
    @BtCompositeSearch(fieldName = "b.status")
    private Integer status;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 费率
     */
    private BigDecimal handleRate;
    /**
     * 手续费
     */
    private BigDecimal handleCharge;
    /**
     * 保证金比例
     */
    private BigDecimal bondPercent;
    /**
     * 保证金金额
     */
    private BigDecimal bondMoney;
    /**
     * 保证金账户
     */
    private String bondAcc;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 变更次数
     */
    private Integer changeCount;
    /**
     * 注销日期
     */
    private Date logoutDate;
    /**
     * 授信模式
     */
    private String ratmodel;
    /**
     * 被担保单位
     */
    private String guaranteeedCorp;
    private String guaranteeedCorpShow;
    /**
     * 领取人
     */
    private String receiver;
    /**
     * 领取时间
     */
    private Date receiveDate;
    /**
     * 保证金币别
     */
    @Currency(inject = "bondCurName")
    private String bondCurId;
    /**
     * 保证金币别
     */
    private String bondCurName;
    /**
     * 是否折合本位币
     */
    private String isRmbRat;
    /**
     * 是否挂接保证金
     */
    private String isHookBond;
    /**
     * 是否启用敞口授信
     */
    private String isOpenRat;
    /**
     * 保证金id
     */
    private String bondId;
    /**
     * 修改前挂接保证金Id
     */
    private String bondIdPre;
    /**
     * 保证金合同
     */
    private String bondContract;
    /**
     * 授信品种
     */
    private String ratItemCode;
    /**
     * 授信币别
     */
    private String ratCurId;
    /**
     * 本位币标识
     */
    private String naturalSign;
    /**
     * 保证金利率
     */
    private BigDecimal bondRate;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 合同编号
     */
    private String contractNo;
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
    public BigDecimal getGuaranteeMoney() {
        return guaranteeMoney;
    }
    public void setGuaranteeMoney(BigDecimal guaranteeMoney) {
        this.guaranteeMoney = guaranteeMoney;
    }
    public String getGuaranteeAttr() {
        return guaranteeAttr;
    }
    public void setGuaranteeAttr(String guaranteeAttr) {
        this.guaranteeAttr = guaranteeAttr;
    }
    public String getGuaItem() {
        return guaItem;
    }
    public void setGuaItem(String guaItem) {
        this.guaItem = guaItem;
    }
    public Date getExpectEndDate() {
        return expectEndDate;
    }
    public void setExpectEndDate(Date expectEndDate) {
        this.expectEndDate = expectEndDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getTurnBank() {
        return turnBank;
    }
    public void setTurnBank(String turnBank) {
        this.turnBank = turnBank;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public BigDecimal getHandleRate() {
        return handleRate;
    }
    public void setHandleRate(BigDecimal handleRate) {
        this.handleRate = handleRate;
    }
    public BigDecimal getHandleCharge() {
        return handleCharge;
    }
    public void setHandleCharge(BigDecimal handleCharge) {
        this.handleCharge = handleCharge;
    }
    public BigDecimal getBondPercent() {
        return bondPercent;
    }
    public void setBondPercent(BigDecimal bondPercent) {
        this.bondPercent = bondPercent;
    }
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public String getBondAcc() {
        return bondAcc;
    }
    public void setBondAcc(String bondAcc) {
        this.bondAcc = bondAcc;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
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
    public Integer getChangeCount() {
        return changeCount;
    }
    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public Date getLogoutDate() {
        return logoutDate;
    }
    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }
    public String getRatmodel() {
        return ratmodel;
    }
    public void setRatmodel(String ratmodel) {
        this.ratmodel = ratmodel;
    }
    public String getGuaranteeedCorp() {
        return guaranteeedCorp;
    }
    public void setGuaranteeedCorp(String guaranteeedCorp) {
        this.guaranteeedCorp = guaranteeedCorp;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public Date getReceiveDate() {
        return receiveDate;
    }
    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public String getIsRmbRat() {
        return isRmbRat;
    }
    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
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
    public String getBondId() {
        return bondId;
    }
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    public String getBondContract() {
        return bondContract;
    }
    public void setBondContract(String bondContract) {
        this.bondContract = bondContract;
    }
    public String getBondIdPre() {
        return bondIdPre;
    }
    public void setBondIdPre(String bondIdPre) {
        this.bondIdPre = bondIdPre;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public String getRatItemCode() {
        return ratItemCode;
    }
    public void setRatItemCode(String ratItemCode) {
        this.ratItemCode = ratItemCode;
    }
    public String getRatCurId() {
        return ratCurId;
    }
    public void setRatCurId(String ratCurId) {
        this.ratCurId = ratCurId;
    }
    public String getNaturalSign() {
        return naturalSign;
    }
    public void setNaturalSign(String naturalSign) {
        this.naturalSign = naturalSign;
    }
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getFailReason() {
        return failReason;
    }
    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getGuaranteeedCorpShow() {
        return guaranteeedCorpShow;
    }

    public void setGuaranteeedCorpShow(String guaranteeedCorpShow) {
        this.guaranteeedCorpShow = guaranteeedCorpShow;
    }
}
