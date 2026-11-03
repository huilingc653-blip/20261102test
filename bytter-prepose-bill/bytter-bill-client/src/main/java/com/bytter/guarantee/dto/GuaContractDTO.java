package com.bytter.guarantee.dto;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Title: GuaContractDTO
 *
 * @author luxy
 * @since 2019/7/29 14:05
 */
public class GuaContractDTO implements Serializable {
    private String id;
    /** 单位ID */
//    @Corp(inject = "corpName")
    private String corpId;
    /** 单位名称 */
    private String corpName;
    /** 担保编号 */
    private String guaNo;
    /** 担保合同类型 */
    private String guaTypeName;
    /** 币别 */
//    @Currency(inject = "curName")
    private String curId;
    /** 币别名称 */
    private String curName;
    /** 业务类型 */
    private String bussinessType;
    /** 担保单位 */
    private String guaPerson;
    /** 债权人 */
    private String creditorPerson;
    /** 债务人 */
    @Corp(inject = "debtorPersonName")
    private String debtorPerson;
    /** 债务人名称 */
    private String debtorPersonName;
    /** 合同编号 */
    private String contractCode;
    /** 担保金额 */
    private BigDecimal guaMoney;
    /** 占用额度 */
    private BigDecimal occupyMoney;
    /** 可用额度 */
    private BigDecimal availMoney;
    /** 担保开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 担保结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 担保范围 */
    private String warr;
    /** 备注 */
    private String rmk;
    /** 状态 */
    private Integer status;
    /** 是否已变更 */
    private String isChange;
    /** 附件ID */
    private String attachmentId;
    /** 创建日期 */
    private Date createDate;
    /** 创建人登录名称 */
    private String createBy;
    /** 更新日期 */
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
}
