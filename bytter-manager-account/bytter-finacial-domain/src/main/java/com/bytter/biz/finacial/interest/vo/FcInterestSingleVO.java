package com.bytter.biz.finacial.interest.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_INTEREST_SINGLE
 * @date 2020-12-02 14:34:54
 */
public class FcInterestSingleVO implements Serializable {
    private String id;
    /**
     * 网点
     */
    @BtCompositeSearch(fieldName = "t.net_id")
    private String netId;
    private String netName;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpId;
    private String corpName;
    /**
     * 币别
     */
    @BtCompositeSearch(fieldName = "t.cur_id")
    private String curId;
    private String curName;
    /**
     * 计息日期
     */
    @JsonFormat(pattern = DateUtils.PATTERN_CLASSICAL_SIMPLE)
    @BtCompositeSearch(fieldName = "t.interest_time")
    private Date interestTime;
    /**账号*/
    @BtCompositeSearch(fieldName = "t.account")
    private String account;
    private String accName;
    /**账户类别*/
    @BtCompositeSearch(fieldName = "t.acc_type")
    private String accType;
    private String accTypeName;
    /**起息日期*/
    @JsonFormat(pattern = DateUtils.PATTERN_CLASSICAL_SIMPLE)
    @BtCompositeSearch(fieldName = "t.start_date")
    private Date startDate;
    /**止息日期*/
    @JsonFormat(pattern = DateUtils.PATTERN_CLASSICAL_SIMPLE)
    @BtCompositeSearch(fieldName = "t.end_date")
    private Date endDate;
    /**计息金额*/
    @BtCompositeSearch(fieldName = "t.money")
    private BigDecimal money;
    /**利息*/
    @BtCompositeSearch(fieldName = "t.interest")
    private BigDecimal interest;
    /**记账日期*/
    @JsonFormat(pattern = DateUtils.PATTERN_CLASSICAL_SIMPLE)
    @BtCompositeSearch(fieldName = "t.tally_date")
    private Date tallyDate;
    /**流水号*/
    @BtCompositeSearch(fieldName = "t.order_no")
    private String orderNo;
    /**
     * 业务全局id
     */
    protected String busGlobalId;
    /**还款方式*/
    @BtCompositeSearch(fieldName = "t.pay_way")
    private String payWay;
    /**业务状态*/
    @BtCompositeSearch(fieldName = "t.status")
    private Integer status;
    /**附件张数*/
    @BtCompositeSearch(fieldName = "t.affix_amount")
    private Integer affixAmount;
    /**创建用户代码*/
    @BtCompositeSearch(fieldName = "t.create_by")
    private String createBy;
    /**复核人代码*/
    @BtCompositeSearch(fieldName = "t.checker_code")
    private String checkerCode;
    /**复核日期*/
    @JsonFormat(pattern = DateUtils.PATTERN_CLASSICAL_SIMPLE)
    @BtCompositeSearch(fieldName = "t.check_date")
    private Date checkDate;
    /**创建时间*/
    @JsonFormat(pattern = DateUtils.PATTERN_CLASSICAL)
    @BtCompositeSearch(fieldName = "t.create_date")
    private Date createDate;
    /**最后更新用户代码*/
    @BtCompositeSearch(fieldName = "t.update_by")
    private String updateBy;
    /**最后更新时间*/
    @JsonFormat(pattern = DateUtils.PATTERN_CLASSICAL)
    @BtCompositeSearch(fieldName = "t.update_date")
    private Date updateDate;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    @BtCompositeSearch(fieldName = "t.pool_sign")
    private String poolSign;
    /**
     * 操作类型 interest-计息 submit-提交 check-复核 back-打回
     */
    private String opType;
    /**
     * 界面标志 0-新增页面 1-修改页面 2-复核页面 3-查看页面
     */
    private Integer pageFlag;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Date getInterestTime() {
        return interestTime;
    }
    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getAccTypeName() {
        return accTypeName;
    }
    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getPayWay() {
        return payWay;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getAffixAmount() {
        return affixAmount;
    }
    public void setAffixAmount(Integer affixAmount) {
        this.affixAmount = affixAmount;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCheckerCode() {
        return checkerCode;
    }
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
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
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    public String getOpType() {
        return opType;
    }
    public void setOpType(String opType) {
        this.opType = opType;
    }
    public Integer getPageFlag() {
        return pageFlag;
    }
    public void setPageFlag(Integer pageFlag) {
        this.pageFlag = pageFlag;
    }
    public String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
}
