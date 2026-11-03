package com.bytter.letter.domain.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeInfo
 * @Description 保函申请实体类
 * @date 2019年1月7日
 */
@Entity
@Table(name = "GUA_GUARANTEE_INFO", schema = "")
@SuppressWarnings("serial")
public class GuaGuaranteeInfo extends IdEntity implements java.io.Serializable {
    /**
     * 保函编号
     */
    private String guaranteeNo;
    /**
     * 保函类型
     */
    private String guaranteeType;
    /**
     * 保函申请人
     */
    private String corpId;
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
     * 银行名称
     */
    private String bankName;
    /**
     * 保函开立人
     */
    private String openCorp;
    /**
     * 保函受益人
     */
    private String incomeCorp;
    /**
     * 币别
     */
    private String curId;
    /**
     * 保函金额
     */
    private BigDecimal guaranteeMoney;
    /**
     * 保函性质
     */
    private String guaranteeAttr;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 保函事项
     */
    private String guaItem;
    /**
     * 保函预计结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expectEndDate;
    /**
     * 保函开始日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    /**
     * 保函结束日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    /**
     * 注销日期
     */
    private Date logoutDate;
    /**
     * 转开行
     */
    private String turnBank;
    /**
     * 状态
     */
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
     * 授信模式
     */
    private String ratmodel;
    /**
     * 被担保单位
     */
    private String guaranteeedCorp;
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
    private String bondCurId;
    /**
     * 保证金合同
     */
    private String bondContract;
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
     * 保证金利率
     */
    private BigDecimal bondRate;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保函编号
     */
    @Column(name = "GUARANTEE_NO")
    public String getGuaranteeNo() {
        return guaranteeNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保函编号
     */
    public void setGuaranteeNo(String guaranteeNo) {
        this.guaranteeNo = guaranteeNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保函类型
     */
    @Column(name = "GUARANTEE_TYPE")
    public String getGuaranteeType() {
        return guaranteeType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保函类型
     */
    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保函申请人
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保函申请人
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 开立人类型
     */
    @Column(name = "OPEN_CORP_TYPE")
    public String getOpenCorpType() {
        return openCorpType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 开立人类型
     */
    public void setOpenCorpType(String openCorpType) {
        this.openCorpType = openCorpType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 开立方式
     */
    @Column(name = "OPEN_WAY")
    public String getOpenWay() {
        return openWay;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 开立方式
     */
    public void setOpenWay(String openWay) {
        this.openWay = openWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 开立类型
     */
    @Column(name = "OPEN_TYPE")
    public String getOpenType() {
        return openType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 开立类型
     */
    public void setOpenType(String openType) {
        this.openType = openType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保函开立人
     */
    @Column(name = "OPEN_CORP")
    public String getOpenCorp() {
        return openCorp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保函开立人
     */
    public void setOpenCorp(String openCorp) {
        this.openCorp = openCorp;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保函受益人
     */
    @Column(name = "INCOME_CORP")
    public String getIncomeCorp() {
        return incomeCorp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保函受益人
     */
    public void setIncomeCorp(String incomeCorp) {
        this.incomeCorp = incomeCorp;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 币别
     */
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 币别
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保函金额
     */
    @Column(name = "GUARANTEE_MONEY")
    public BigDecimal getGuaranteeMoney() {
        return guaranteeMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保函金额
     */
    public void setGuaranteeMoney(BigDecimal guaranteeMoney) {
        this.guaranteeMoney = guaranteeMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保函性质
     */
    @Column(name = "GUARANTEE_ATTR")
    public String getGuaranteeAttr() {
        return guaranteeAttr;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保函性质
     */
    public void setGuaranteeAttr(String guaranteeAttr) {
        this.guaranteeAttr = guaranteeAttr;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保函事项
     */
    @Column(name = "GUA_ITEM")
    public String getGuaItem() {
        return guaItem;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保函事项
     */
    public void setGuaItem(String guaItem) {
        this.guaItem = guaItem;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 保函预计结束时间
     */
    @Column(name = "EXPECT_END_DATE")
    public Date getExpectEndDate() {
        return expectEndDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 保函预计结束时间
     */
    public void setExpectEndDate(Date expectEndDate) {
        this.expectEndDate = expectEndDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 转开行
     */
    @Column(name = "TURN_BANK")
    public String getTurnBank() {
        return turnBank;
    }
    /**
     * 方法:设置java.lang.String
     *
     * @return: java.lang.String 转开行
     */
    public void setTurnBank(String turnBank) {
        this.turnBank = turnBank;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 费率
     */
    @Column(name = "HANDLE_RATE")
    public BigDecimal getHandleRate() {
        return handleRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return java.math.BigDecimal 费率
     */
    public void setHandleRate(BigDecimal handleRate) {
        this.handleRate = handleRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 手续费
     */
    @Column(name = "HANDLE_CHARGE")
    public BigDecimal getHandleCharge() {
        return handleCharge;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return java.math.BigDecimal 手续费
     */
    public void setHandleCharge(BigDecimal handleCharge) {
        this.handleCharge = handleCharge;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金比例
     */
    @Column(name = "BOND_PERCENT")
    public BigDecimal getBondPercent() {
        return bondPercent;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金比例
     */
    public void setBondPercent(BigDecimal bondPercent) {
        this.bondPercent = bondPercent;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金金额
     */
    @Column(name = "BOND_MONEY")
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金金额
     */
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保证金账户
     */
    @Column(name = "BOND_ACC")
    public String getBondAcc() {
        return bondAcc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保证金账户
     */
    public void setBondAcc(String bondAcc) {
        this.bondAcc = bondAcc;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 开始日期
     */
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 开始日期
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 结束日期
     */
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    @Column(name = "LOGOUT_DATE")
    public Date getLogoutDate() {
        return logoutDate;
    }
    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 结束日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @return: java.lang.Integer 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 附件ID
     */
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 附件ID
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
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
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
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
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
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
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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
     * @return: java.lang.String 被担保单位
     */
    @Column(name = "GUARANTEEED_CORP", length = 2000)
    public String getGuaranteeedCorp() {
        return guaranteeedCorp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 被担保单位
     */
    public void setGuaranteeedCorp(String guaranteeedCorp) {
        this.guaranteeedCorp = guaranteeedCorp;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 领取人
     */
    @Column(name = "RECEIVER")
    public String getReceiver() {
        return receiver;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 领取人
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 领取日期
     */
    @Column(name = "RECEIVE_DATE")
    public Date getReceiveDate() {
        return receiveDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 领取日期
     */
    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保证金币别
     */
    @Column(name = "BOND_CURID")
    public String getBondCurId() {
        return bondCurId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保证金币别
     */
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否折合本位币
     */
    @Column(name = "IS_RMB_RAT")
    public String getIsRmbRat() {
        return isRmbRat;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 是否折合本位币
     */
    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
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
     * @return: java.lang.String 是否挂接保证金
     */
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保证金合同
     */
    @Column(name = "BOND_CONTRACT")
    public String getBondContract() {
        return bondContract;
    }
    /**
     * 方法: 设置ava.lang.String
     *
     * @return: java.lang.String 保证金合同
     */
    public void setBondContract(String bondContract) {
        this.bondContract = bondContract;
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
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否启用敞口授信
     */
    @Column(name = "IS_OPEN_RAT")
    public String getIsOpenRat() {
        return isOpenRat;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 是否启用敞口授信
     */
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 失败原因
     */
    @Column(name = "FAIL_REASON")
    public String getFailReason() {
        return failReason;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 失败原因
     */
    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
