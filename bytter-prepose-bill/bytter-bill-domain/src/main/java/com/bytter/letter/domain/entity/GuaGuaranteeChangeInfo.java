package com.bytter.letter.domain.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeInfo
 * @Description 保函信息变更实体类
 * @date 2019年1月9日
 */
@Entity
@Table(name = "GUA_GUARANTEE_CHANGE_INFO", schema = "")
@SuppressWarnings("serial")
public class GuaGuaranteeChangeInfo extends IdEntity implements java.io.Serializable {
    /**
     * 保函申请ID
     */
    private String guaraId;
    /**
     * 保函变更申请单编号
     */
    private String guaraChangeNo;
    /**
     * 变更次数
     */
    private Integer changeCount;
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
     * 新保函预计结束时间
     */
    private java.util.Date expectEndDate;
    /**
     * 新保函开始日期
     */
    private java.util.Date startDate;
    /**
     * 新保函结束日期
     */
    private java.util.Date endDate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 手续费
     */
    private java.math.BigDecimal handleCharge;
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
     * 申请变更日期
     */
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
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保函金额
     */
    @Column(name = "GUARANTEE_MONEY")
    public java.math.BigDecimal getGuaranteeMoney() {
        return guaranteeMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 保函金额
     */
    public void setGuaranteeMoney(java.math.BigDecimal guaranteeMoney) {
        this.guaranteeMoney = guaranteeMoney;
    }
    /**
     * 取得 java.lang.String 保函申请ID
     *
     * @return
     */
    @Column(name = "GUARA_ID")
    public String getGuaraId() {
        return guaraId;
    }
    /**
     * 设置 java.lang.String
     *
     * @param guaraId 保函申请ID
     */
    public void setGuaraId(String guaraId) {
        this.guaraId = guaraId;
    }
    /**
     * java.lang.String 取得保函变更申请单编号
     *
     * @return
     */
    @Column(name = "GUARA_CHANGE_NO")
    public String getGuaraChangeNo() {
        return guaraChangeNo;
    }
    /**
     * 设置java.lang.String
     *
     * @param guaraChangeNo 保函变更申请单编号
     */
    public void setGuaraChangeNo(String guaraChangeNo) {
        this.guaraChangeNo = guaraChangeNo;
    }
    /**
     * 取得java.lang.String 变更次数
     *
     * @return
     */
    @Column(name = "CHANGE_COUNT")
    public Integer getChangeCount() {
        return changeCount;
    }
    /**
     * 设置java.lang.String
     *
     * @param changeCount 变更次数
     */
    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }
    /**
     * 取得java.lang.String 保函金额调整类型
     *
     * @return
     */
    @Column(name = "GUA_CHANGE_TYPE")
    public String getGuaChangeType() {
        return guaChangeType;
    }
    /**
     * 设置java.lang.String
     *
     * @param guaChangeType 保函金额调整类型
     */
    public void setGuaChangeType(String guaChangeType) {
        this.guaChangeType = guaChangeType;
    }
    /**
     * 取得java.math.BigDecimal 保函调整金额
     *
     * @return
     */
    @Column(name = "GUA_CHANGE_MONEY")
    public java.math.BigDecimal getGuaChangeMoney() {
        return guaChangeMoney;
    }
    /**
     * 设置java.math.BigDecimal
     *
     * @param guaChangeMoney 保函调整金额
     */
    public void setGuaChangeMoney(java.math.BigDecimal guaChangeMoney) {
        this.guaChangeMoney = guaChangeMoney;
    }
    /**
     * 取得 java.lang.String 保函保证金调整类型
     *
     * @return
     */
    @Column(name = "BOND_CHANGE_TYPE")
    public String getBondChangeType() {
        return bondChangeType;
    }
    /**
     * 设置java.lang.String
     *
     * @param bondChangeType 保函保证金调整类型
     */
    public void setBondChangeType(String bondChangeType) {
        this.bondChangeType = bondChangeType;
    }
    /**
     * 取得java.math.BigDecimal 保函保证金调整金额
     *
     * @return
     */
    @Column(name = "BOND_CHANGE_MONEY")
    public java.math.BigDecimal getBondChangeMoney() {
        return bondChangeMoney;
    }
    /**
     * 设置java.math.BigDecimal
     *
     * @param bondChangeMoney 保函保证金调整金额
     */
    public void setBondChangeMoney(java.math.BigDecimal bondChangeMoney) {
        this.bondChangeMoney = bondChangeMoney;
    }
    /**
     * 取得java.util.Date 保函变更日期
     *
     * @return
     */
    @Column(name = "CHANGE_DATE")
    public java.util.Date getChangeDate() {
        return changeDate;
    }
    /**
     * 设置java.util.Date
     *
     * @param changeDate 保函变更日期
     */
    public void setChangeDate(java.util.Date changeDate) {
        this.changeDate = changeDate;
    }
    /**
     * 取得java.lang.String 修改条款
     *
     * @return
     */
    @Column(name = "CHANGE_TERM")
    public String getChangeTerm() {
        return changeTerm;
    }
    /**
     * 设置java.lang.String
     *
     * @param changeTerm 修改条款
     */
    public void setChangeTerm(String changeTerm) {
        this.changeTerm = changeTerm;
    }
    /**
     * 取得 java.lang.String 修改原因
     *
     * @return
     */
    @Column(name = "CHANGE_REASON")
    public String getChangeReason() {
        return changeReason;
    }
    /**
     * 设置java.lang.String
     *
     * @param changeReason 修改原因
     */
    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
    /**
     * 取得java.lang.String 经办人
     *
     * @return
     */
    @Column(name = "OPERATOR")
    public String getOperator() {
        return operator;
    }
    /**
     * 设置java.lang.String 经办人
     *
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 保函预计结束时间
     */
    @Column(name = "EXPECT_END_DATE")
    public java.util.Date getExpectEndDate() {
        return expectEndDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 保函预计结束时间
     */
    public void setExpectEndDate(java.util.Date expectEndDate) {
        this.expectEndDate = expectEndDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 费率
     */
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 手续费
     */
    @Column(name = "HANDLE_CHARGE")
    public java.math.BigDecimal getHandleCharge() {
        return handleCharge;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return java.math.BigDecimal 手续费
     */
    public void setHandleCharge(java.math.BigDecimal handleCharge) {
        this.handleCharge = handleCharge;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金金额
     */
    @Column(name = "BOND_MONEY")
    public java.math.BigDecimal getBondMoney() {
        return bondMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return java.math.BigDecimal 保证金金额
     */
    public void setBondMoney(java.math.BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保证金账户
     */
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 开始日期
     */
    @Column(name = "START_DATE")
    public java.util.Date getStartDate() {
        return startDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 开始日期
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 结束日期
     */
    @Column(name = "END_DATE")
    public java.util.Date getEndDate() {
        return endDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 结束日期
     */
    public void setEndDate(java.util.Date endDate) {
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
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return createDate;
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
    public java.util.Date getUpdateDate() {
        return updateDate;
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
}
