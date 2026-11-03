package com.bytter.guarantee.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.guarantee.dto.RepayPlanDTO;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractInfoVO 担保合同信息VO
 * @Description
 * @date 2018年12月15日
 */
public class GuaContractInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 8518844566933081156L;
    private String id;
    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "b.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    /**
     * 单位名称
     */
    @Excel(name = "*融资主体")
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpName;
    /**
     * 担保编号
     */
    @BtCompositeSearch(fieldName = "b.gua_no")
    private String guaNo;
    /**
     * 担保合同类型
     */
    private String guaTypeName;
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
     * 业务类型 0-被担保合同 1-对外担保合同
     */
    @BtCompositeSearch(fieldName = "b.BUSINESS_TYPE")
    private String businessType;
    /**
     * 担保单位
     */
    @Excel(name = "*担保单位")
    @BtCompositeSearch(fieldName = "b.gua_person")
    private String guaPerson;
    /**
     * 债权人
     */
    @Excel(name = "*债权人")
    @BtCompositeSearch(fieldName = "b.creditor_person")
    private String creditorPerson;
    /**
     * 债务人
     */
    @BtCompositeSearch(fieldName = "b.debtor_person")
//    @Corp(inject = "debtorPersonName")
    private String debtorPerson;
    /**
     * 债务人名称
     */
    @Excel(name = "*债务人")
    @BtCompositeSearch(fieldName = "b.debtor_person")
    private String debtorPersonName;
    /**
     * 合同编号
     */
    @Excel(name = "*合同编号")
    @BtCompositeSearch(fieldName = "b.contract_code")
    private String contractCode;
    /**
     * 担保金额
     */
    @Excel(name = "*担保金额")
    @BtCompositeSearch(fieldName = "b.gua_money")
    private java.math.BigDecimal guaMoney;
    /**
     * 占用额度
     */
    private java.math.BigDecimal occupyMoney;
    /**
     * 可用额度
     */
    private java.math.BigDecimal availMoney;
    /**
     * 担保开始日期
     */
    @Excel(name = "*担保开始日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 担保结束日期
     */
    @Excel(name = "*担保结束日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @Excel(name = "*状态")
    @BtCompositeSearch(fieldName = "b.status")
    @Dict(dictGroup = "guaContract.status", inject = "statusName")
    private Integer status;
    private String statusName;
    /**
     * 是否已变更
     */
    private String isChange;
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
    @BtCompositeSearch(fieldName = "b.create_Name")
    private String createBy;
    /**
     * 创建人名称
     */
    private String createName;
	/**
     * 创建人名称
     */
    @BtCompositeSearch(fieldName = "b.create_Name")
	private String operator;
    /**
     * 更新日期
     */
    @Excel(name = "*最后一次变更完成时间", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 抵押信息
     */
    private String pDids;
    /**
     * 质押信息
     */
    private String pZids;
    /**
     * 抵押金额
     */
    private java.math.BigDecimal money0;
    /**
     * 质押金额
     */
    private java.math.BigDecimal money1;
    /**
     * 保证金额
     */
    private java.math.BigDecimal money2;
    /**
     * 担保方式Code
     */
    private String guaWayCode;
    /**
     * 担保方式明细
     */
    private List<GuaContractWayVO> guaDtls;
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
     * 明细
     */
    private List<RepayPlanDTO> repayMents;
    /**
     * 提款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date drawMoneyDate;
    /**
     * 还款计划id
     */
    private String planId;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 单位代码
     */
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
    public String getGuaTypeName() {
        return guaTypeName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 担保合同类型
     */
    public void setGuaTypeName(String guaTypeName) {
        this.guaTypeName = guaTypeName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 币别
     */
    @Column(name = "CUR_ID", nullable = false, length = 32)
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
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 更新日期
     */
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
    public String getUpdateBy() {
        return updateBy;
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
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String id
     */
    public String getId() {
        return id;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 融资单位名称
     */
    public String getCorpName() {
        return corpName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 融资单位名称
     */
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 币别名称
     */
    public String getCurName() {
        return curName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 币别名称
     */
    public void setCurName(String curName) {
        this.curName = curName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 债务人名称
     */
    public String getDebtorPersonName() {
        return debtorPersonName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 债务人名称
     */
    public void setDebtorPersonName(String debtorPersonName) {
        this.debtorPersonName = debtorPersonName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @param: java.lang.String
     * 合同id
     */
    public String getConId() {
        return conId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 合同id
     */
    public void setConId(String conId) {
        this.conId = conId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @param: java.lang.String
     * 合同类型id
     */
    public String getConTypeId() {
        return conTypeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 合同类型id
     */
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    /**
     * 以下是页面中的附加信息
     */
    public String getpDids() {
        return pDids;
    }
    public void setpDids(String pDids) {
        this.pDids = pDids;
    }
    public String getpZids() {
        return pZids;
    }
    public void setpZids(String pZids) {
        this.pZids = pZids;
    }
    public java.math.BigDecimal getMoney0() {
        return money0;
    }
    public void setMoney0(java.math.BigDecimal money0) {
        this.money0 = money0;
    }
    public java.math.BigDecimal getMoney1() {
        return money1;
    }
    public void setMoney1(java.math.BigDecimal money1) {
        this.money1 = money1;
    }
    public java.math.BigDecimal getMoney2() {
        return money2;
    }
    public void setMoney2(java.math.BigDecimal money2) {
        this.money2 = money2;
    }
    public java.math.BigDecimal getOccupyMoney() {
        return occupyMoney;
    }
    public void setOccupyMoney(java.math.BigDecimal occupyMoney) {
        this.occupyMoney = occupyMoney;
    }
    public java.math.BigDecimal getAvailMoney() {
        return availMoney;
    }
    public void setAvailMoney(java.math.BigDecimal availMoney) {
        this.availMoney = availMoney;
    }
    public String getIsChange() {
        return isChange;
    }
    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }
    /**
     * 附加信息结束
     */
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public List<GuaContractWayVO> getGuaDtls() {
        return guaDtls;
    }
    public void setGuaDtls(List<GuaContractWayVO> guaDtls) {
        this.guaDtls = guaDtls;
    }
    public String getGuaWayCode() {
        return guaWayCode;
    }
    public void setGuaWayCode(String guaWayCode) {
        this.guaWayCode = guaWayCode;
    }
    public String getInnerGua() {
        return innerGua;
    }
    public void setInnerGua(String innerGua) {
        this.innerGua = innerGua;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public List<RepayPlanDTO> getRepayMents() {
        return repayMents;
    }
    public void setRepayMents(List<RepayPlanDTO> repayMents) {
        this.repayMents = repayMents;
    }
    public Date getDrawMoneyDate() {
        return drawMoneyDate;
    }
    public void setDrawMoneyDate(Date drawMoneyDate) {
        this.drawMoneyDate = drawMoneyDate;
    }
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
}
