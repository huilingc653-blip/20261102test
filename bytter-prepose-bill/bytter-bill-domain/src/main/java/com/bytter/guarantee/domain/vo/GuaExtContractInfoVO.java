package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaExtContractInfoVO
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/5/29 17:43
 */
public class GuaExtContractInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 8518844566933081156L;
    private String id;
    /** 单位ID */
    @BtCompositeSearch(fieldName = "b.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    /** 单位名称 */
    @Excel(name = "*融资主体")
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpName;
    /** 担保编号 */
    @BtCompositeSearch(fieldName = "b.gua_no")
    private String guaNo;
    /** 担保合同类型 */
    private String guaTypeName;
    /** 币别 */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "b.cur_Id")
    private String curId;
    /** 币别名称 */
    @Excel(name = "*币别")
    private String curName;
    /** 业务类型 0-被担保合同 1-对外担保合同 */
    @BtCompositeSearch(fieldName = "b.BUSINESS_TYPE")
    private String bussinessType;
    /** 担保单位 */
    @Excel(name = "*担保单位")
    @BtCompositeSearch(fieldName = "b.gua_person")
    private String guaPerson;
    /** 债权人 */
    @Excel(name = "*债权人")
    @BtCompositeSearch(fieldName = "b.creditor_person")
    private String creditorPerson;
    /** 债务人 */
    @BtCompositeSearch(fieldName = "b.debtor_person")
    private String debtorPerson;
    /** 债务人名称 */
    @Excel(name = "*债务人")
    @BtCompositeSearch(fieldName = "b.debtor_person")
    private String debtorPersonName;
    /** 合同编号 */
    @Excel(name = "*合同编号")
    @BtCompositeSearch(fieldName = "b.contract_code")
    private String contractCode;
    /** 担保金额 */
    @Excel(name = "*担保金额")
    @BtCompositeSearch(fieldName = "b.gua_money")
    private BigDecimal guaMoney;
    /** 占用额度 */
    private BigDecimal occupyMoney;
    /** 可用额度 */
    private BigDecimal availMoney;
    /** 担保开始日期 */
    @Excel(name = "*担保开始日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 担保结束日期 */
    @Excel(name = "*担保结束日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 担保范围 */
    private String warr;
    /** 备注 */
    private String rmk;
    /** 状态 */
    @Excel(name = "*状态")
    @BtCompositeSearch(fieldName = "b.status")
    @Dict(dictGroup = "guaContract.status", inject = "statusName")
    private Integer status;
    private String statusName;
    /** 是否已变更 */
    private String isChange;
    /** 附件ID */
    private String attachmentId;
    /** 创建日期 */
    private Date createDate;
    /** 创建人登录名称 */
    private String createBy;
    /** 更新日期 */
    @Excel(name = "*最后一次变更完成时间", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 抵押信息 */
    private String pDids;
    /** 质押信息 */
    private String pZids;
    /** 担保方式Code */
    private String guaWayCode;
    /**
     * 担保方式明细
     */
    private List<GuaContractWayVO> guaDtls;
    /** 担保对接合同管理信息 */
    /** 对接的合同id */
    private String conId;
    /** 对接的合同类型id */
    private String conTypeId;
    /** 是否集团内担保单位 */
    private String innerGua;
    /** 提款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date drawMoneyDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getGuaNo() {
        return guaNo;
    }
    public void setGuaNo(String guaNo) {
        this.guaNo = guaNo;
    }
    public String getGuaTypeName() {
        return guaTypeName;
    }
    public void setGuaTypeName(String guaTypeName) {
        this.guaTypeName = guaTypeName;
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
    public String getBussinessType() {
        return bussinessType;
    }
    public void setBussinessType(String bussinessType) {
        this.bussinessType = bussinessType;
    }
    public String getGuaPerson() {
        return guaPerson;
    }
    public void setGuaPerson(String guaPerson) {
        this.guaPerson = guaPerson;
    }
    public String getCreditorPerson() {
        return creditorPerson;
    }
    public void setCreditorPerson(String creditorPerson) {
        this.creditorPerson = creditorPerson;
    }
    public String getDebtorPerson() {
        return debtorPerson;
    }
    public void setDebtorPerson(String debtorPerson) {
        this.debtorPerson = debtorPerson;
    }
    public String getDebtorPersonName() {
        return debtorPersonName;
    }
    public void setDebtorPersonName(String debtorPersonName) {
        this.debtorPersonName = debtorPersonName;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public BigDecimal getGuaMoney() {
        return guaMoney;
    }
    public void setGuaMoney(BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
    public BigDecimal getOccupyMoney() {
        return occupyMoney;
    }
    public void setOccupyMoney(BigDecimal occupyMoney) {
        this.occupyMoney = occupyMoney;
    }
    public BigDecimal getAvailMoney() {
        return availMoney;
    }
    public void setAvailMoney(BigDecimal availMoney) {
        this.availMoney = availMoney;
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
    public String getWarr() {
        return warr;
    }
    public void setWarr(String warr) {
        this.warr = warr;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getIsChange() {
        return isChange;
    }
    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
    public String getGuaWayCode() {
        return guaWayCode;
    }
    public void setGuaWayCode(String guaWayCode) {
        this.guaWayCode = guaWayCode;
    }
    public List<GuaContractWayVO> getGuaDtls() {
        return guaDtls;
    }
    public void setGuaDtls(List<GuaContractWayVO> guaDtls) {
        this.guaDtls = guaDtls;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public String getInnerGua() {
        return innerGua;
    }
    public void setInnerGua(String innerGua) {
        this.innerGua = innerGua;
    }
    public Date getDrawMoneyDate() {
        return drawMoneyDate;
    }
    public void setDrawMoneyDate(Date drawMoneyDate) {
        this.drawMoneyDate = drawMoneyDate;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
