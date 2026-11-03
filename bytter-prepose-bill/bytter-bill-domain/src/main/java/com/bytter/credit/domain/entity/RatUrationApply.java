package com.bytter.credit.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 授信占用申请表
 * @date 2017-08-19 11:36:28
 */
@Entity
@Table(name = "RAT_URATION_APPLY", schema = "")
@SuppressWarnings("serial")
public class RatUrationApply extends IdEntity implements java.io.Serializable {
    /**
     * 系统品种种类
     */
    private String type;
    /**
     * 申请编号
     */
    private String applyCode;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 额度类型
     */
    private String useType;
    /**
     * 占用授信类型
     */
    private String ratType;
    /**
     * 授信额度编号
     */
    private String ratCode;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 授信分配编号
     */
    private String disCode;
    /**
     * 授信品种
     */
    private String itemCode;
    /**
     * 品种标识
     */
    private String itemFlag;
    /**
     * 币别
     */
    private String curCode;
    /**
     * 是否折合本位币
     */
    private String isCur;
    /**
     * 占用金额
     */
    private BigDecimal money;
    /**
     * 敞口金额
     */
    private BigDecimal openMoney;
    /**
     * 备注
     */
    private String remark;
    /**
     * 开始日期
     */
    private java.util.Date beginDate;
    /**
     * 结束日期
     */
    private java.util.Date endDate;
    /**
     * 占用到期日期
     */
    private java.util.Date scheduleenddate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 合同号
     */
    private String contactNo;
    /**
     * 票据号
     */
    private String billNo;
    /**
     * 信用证号
     */
    private String lcNo;
    /**
     * 贷款方式
     */
    private String creditType;
    /**
     * 担保编号
     */
    private String gurCode;
    /**
     * 利率
     */
    private BigDecimal interestrate;
    /**
     * 汇率
     */
    private BigDecimal exchange;
    /**
     * 保证金比例
     */
    private BigDecimal assurepercent;
    /**
     * 通过日期
     */
    private java.util.Date passDate;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 业务金额
     */
    private BigDecimal billMoney;
    /**
     * 业务币种
     */
    private String initCurCode;
    /**
     * 业务名称
     */
    private String billName;
    /**
     * 保证金金额
     */
    private BigDecimal bondMoney;
    /**
     * 保证金币种
     */
    private String bondCurId;
    /**
     * 是否启用授信敞口
     */
    private String isOpenRat;
    /**
     * 担保方式
     */
    private String guaWay;
    /**
     * 担保备注
     */
    private String guaRmk;
    /**
     * 额度类型 0-不循环 1-循环
     */
    private String ratCreditType;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 系统品种
     */
    @Column(name = "TYPE")
    public String getType() {
        return this.type;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer
     * 系统品种
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 申请编号
     */
    @Column(name = "APPLY_CODE")
    public String getApplyCode() {
        return this.applyCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 申请编号
     */
    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
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
     * @return: java.lang.String 额度类型
     */
    @Column(name = "USE_TYPE")
    public String getUseType() {
        return this.useType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 额度类型
     */
    public void setUseType(String useType) {
        this.useType = useType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 占用授信类型
     */
    @Column(name = "RAT_TYPE")
    public String getRatType() {
        return this.ratType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 占用授信类型
     */
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信额度编号
     */
    @Column(name = "RAT_CODE")
    public String getRatCode() {
        return this.ratCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信额度编号
     */
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信分配编号
     */
    @Column(name = "DIS_CODE")
    public String getDisCode() {
        return this.disCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信分配编号
     */
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信品种
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return this.itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信品种
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 品种标识
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return this.itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 品种标识
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 币别
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 币别
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否折合本位币
     */
    @Column(name = "IS_CUR")
    public String getIsCur() {
        return this.isCur;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否折合本位币
     */
    public void setIsCur(String isCur) {
        this.isCur = isCur;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 占用金额
     */
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 占用金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 敞口金额
     */
    @Column(name = "OPEN_MONEY")
    public BigDecimal getOpenMoney() {
        return this.openMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 敞口金额
     */
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 备注
     */
    @Column(name = "REMARK")
    public String getRemark() {
        return this.remark;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 开始日期
     */
    @Column(name = "BEGIN_DATE")
    public java.util.Date getBeginDate() {
        return this.beginDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 开始日期
     */
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 结束日期
     */
    @Column(name = "END_DATE")
    public java.util.Date getEndDate() {
        return this.endDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 结束日期
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 占用到期日期
     */
    @Column(name = "SCHEDULEENDDATE")
    public java.util.Date getScheduleenddate() {
        return this.scheduleenddate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 占用到期日期
     */
    public void setScheduleenddate(java.util.Date scheduleenddate) {
        this.scheduleenddate = scheduleenddate;
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
     * @return: java.lang.String 合同号
     */
    @Column(name = "CONTACT_NO")
    public String getContactNo() {
        return this.contactNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 合同号
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 票据号
     */
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return this.billNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 票据号
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 信用证号
     */
    @Column(name = "LC_NO")
    public String getLcNo() {
        return this.lcNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 信用证号
     */
    public void setLcNo(String lcNo) {
        this.lcNo = lcNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 贷款方式
     */
    @Column(name = "CREDIT_TYPE")
    public String getCreditType() {
        return this.creditType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 贷款方式
     */
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保编号
     */
    @Column(name = "GUR_CODE")
    public String getGurCode() {
        return this.gurCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 担保编号
     */
    public void setGurCode(String gurCode) {
        this.gurCode = gurCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 利率
     */
    @Column(name = "INTERESTRATE")
    public BigDecimal getInterestrate() {
        return this.interestrate;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 利率
     */
    public void setInterestrate(BigDecimal interestrate) {
        this.interestrate = interestrate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 汇率
     */
    @Column(name = "EXCHANGE")
    public BigDecimal getExchange() {
        return this.exchange;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 汇率
     */
    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保证金比例
     */
    @Column(name = "ASSUREPERCENT")
    public BigDecimal getAssurepercent() {
        return this.assurepercent;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 保证金比例
     */
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 通过日期
     */
    @Column(name = "PASS_DATE")
    public java.util.Date getPassDate() {
        return this.passDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 通过日期
     */
    public void setPassDate(java.util.Date passDate) {
        this.passDate = passDate;
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
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
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
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "BILL_MONEY")
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 业务币种
     */
    @Column(name = "INIT_CUR_CODE")
    public String getInitCurCode() {
        return initCurCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 业务币种
     */
    public void setInitCurCode(String initCurCode) {
        this.initCurCode = initCurCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 业务名称
     */
    @Column(name = "BILL_NAME")
    public String getBillName() {
        return billName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 业务名称
     */
    public void setBillName(String billName) {
        this.billName = billName;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保证金金额
     */
    @Column(name = "BOND_MONEY")
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保证金金额
     */
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保证金币种
     */
    @Column(name = "BOND_CURID")
    public String getBondCurId() {
        return bondCurId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保证金币种
     */
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否启用授信敞口
     */
    @Column(name = "IS_OPEN_RAT")
    public String getIsOpenRat() {
        return isOpenRat;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 是否启用授信敞口
     */
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保方式
     */
    @Column(name = "GUA_WAY")
    public String getGuaWay() {
        return guaWay;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 担保方式
     */
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保备注
     */
    @Column(name = "GUA_RMK")
    public String getGuaRmk() {
        return guaRmk;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 担保备注
     */
    public void setGuaRmk(String guaRmk) {
        this.guaRmk = guaRmk;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信额度类型
     */
    @Column(name = "RAT_CREDIT_TYPE")
    public String getRatCreditType() {
        return ratCreditType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信额度类型
     */
    public void setRatCreditType(String ratCreditType) {
        this.ratCreditType = ratCreditType;
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
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
