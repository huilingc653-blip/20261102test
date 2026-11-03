package com.bytter.guarantee.domain.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author luxy
 * @version 1.0
 * @Title:GuaContractInfo
 * @Description:担保合同信息实体类
 * @date 2018年12月11日 下午6:29:07
 */
@Entity
@Table(name = "GUA_CONTRACT_INFO", schema = "")
@SuppressWarnings("serial")
public class GuaContractInfo extends IdEntity implements java.io.Serializable {
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 担保编号
     */
    private String guaNo;
    /**
     * 担保合同类型
     */
    private String guaType;
    /**
     * 币别
     */
    private String curId;
    /**
     * 业务类型 0-被担保合同 1-对外担保合同
     */
    private String businessType;
    /**
     * 担保单位
     */
    private String guaPerson;
    /**
     * 债权人
     */
    private String creditorPerson;
    /**
     * 债务人
     */
    private String debtorPerson;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 担保金额
     */
    private java.math.BigDecimal guaMoney;
    /**
     * 担保开始日期
     */
    private Date startDate;
    /**
     * 担保结束日期
     */
    private Date endDate;
    /**
     * 担保范围
     */
    private String warr;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否已变更
     */
    private String isChange;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 附件ID
     */
    private String attachmentId;
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
     * 创建人
     */
    private String createName;
    /** 担保对接合同管理信息 */
    /**
     * 对接的合同id
     */
    private String conId;
    /**
     * 对接的合同类型id
     */
    private String conTypeId;
    /**
     * 是否集团内担保单位
     */
    private String innerGua;
    /**
     * 提款日期
     */
    private Date drawMoneyDate;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保编号
     */
    @Column(name = "GUA_NO")
    public String getGuaNo() {
        return guaNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 担保编号
     */
    public void setGuaNo(String guaNo) {
        this.guaNo = guaNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保合同类型
     */
    @Column(name = "GUA_TYPE")
    public String getGuaType() {
        return guaType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 担保合同类型
     */
    public void setGuaType(String guaType) {
        this.guaType = guaType;
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
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 业务类型
     */
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 业务类型
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保单位
     */
    @Column(name = "GUA_PERSON")
    public String getGuaPerson() {
        return guaPerson;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 担保单位
     */
    public void setGuaPerson(String guaPerson) {
        this.guaPerson = guaPerson;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 债权人
     */
    @Column(name = "CREDITOR_PERSON")
    public String getCreditorPerson() {
        return creditorPerson;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 债权人
     */
    public void setCreditorPerson(String creditorPerson) {
        this.creditorPerson = creditorPerson;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 债务人
     */
    @Column(name = "DEBTOR_PERSON")
    public String getDebtorPerson() {
        return debtorPerson;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 债务人
     */
    public void setDebtorPerson(String debtorPerson) {
        this.debtorPerson = debtorPerson;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 合同编号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 合同编号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 担保金额
     */
    @Column(name = "GUA_MONEY")
    public java.math.BigDecimal getGuaMoney() {
        return guaMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 担保金额
     */
    public void setGuaMoney(java.math.BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
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
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 结束日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保范围
     */
    @Column(name = "WARR")
    public String getWarr() {
        return warr;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 担保范围
     */
    public void setWarr(String warr) {
        this.warr = warr;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
     * 是否已变更
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否已变更
     */
    @Column(name = "IS_CHANGE")
    public String getIsChange() {
        return isChange;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否已变更
     */
    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 合同类型id
     */
    @Column(name = "CON_TYPE_ID")
    public String getConTypeId() {
        return conTypeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 合同类型id
     */
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 合同id
     */
    @Column(name = "CON_ID")
    public String getConId() {
        return conId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 合同id
     */
    public void setConId(String conId) {
        this.conId = conId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否集团内担保单位
     */
    @Column(name = "INNER_GUA")
    public String getInnerGua() {
        return innerGua;
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
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否集团内担保单位
     */
    public void setInnerGua(String innerGua) {
        this.innerGua = innerGua;
    }
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    @Column(name = "DRAWMONEY_DATE")
    public Date getDrawMoneyDate() {
        return drawMoneyDate;
    }
    public void setDrawMoneyDate(Date drawMoneyDate) {
        this.drawMoneyDate = drawMoneyDate;
    }
}
