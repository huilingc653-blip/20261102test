package com.bytter.guarantee.domain.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPledgeOccupyInfoVO
 * @Description 抵质押物被担保合同占用信息VO
 * @date 2018年12月12日
 */
@ExcelTarget("GuaPledgeOccupyInfoVO")
public class GuaPledgeOccupyInfoVO extends ExcelModel implements Serializable {
    private static final long serialVersionUID = 8518844566933081163L;
    private String id;
    /** 融资主体ID */
//    @Corp(inject = "corpName")
    private String corpId;
    /** 融资主体 */
    private String corpName;
    /** 抵质押物编号 */
    private String pledgeNo;
    /** 抵质押物性质 */
    private String pledgeAttr;
    /** 抵质押物类型 */
    private String pledgeType;
    @BtCompositeSearch(fieldName = "e.type_name")
    private String typeName;
    /** 抵质押物名称 */
    private String pledgeName;
    /** 担保开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startDate;
    /** 担保结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate;
    /** 币别 */
//    @Currency(inject = "curName")
    private String curId;
    /** 币别名称 */
    private String curName;
    /** 被担保合同登记时挂接的金额 */
    private java.math.BigDecimal guaMoney;
    /** 担保合同id */
    private String guaId;
    /** 担保方式 */
    private String guaTypeName;
    /** 担保编号 */
    private String guaNo;
    /** 担保单位 */
    private String guaPerson;
    /** 债务人 */
    @Corp(inject = "debtorPersonName")
    private String debtorPerson;
    /** 债务人名称 */
    private String debtorPersonName;
    /** 债权人 */
    private String creditorPerson;
    /** 合同编号 */
    private String contractCode;
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
    public String getPledgeNo() {
        return pledgeNo;
    }
    public void setPledgeNo(String pledgeNo) {
        this.pledgeNo = pledgeNo;
    }
    public String getPledgeAttr() {
        return pledgeAttr;
    }
    public void setPledgeAttr(String pledgeAttr) {
        this.pledgeAttr = pledgeAttr;
    }
    public String getPledgeType() {
        return pledgeType;
    }
    public void setPledgeType(String pledgeType) {
        this.pledgeType = pledgeType;
    }
    public String getPledgeName() {
        return pledgeName;
    }
    public void setPledgeName(String pledgeName) {
        this.pledgeName = pledgeName;
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
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getGuaId() {
        return guaId;
    }
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    public String getGuaTypeName() {
        return guaTypeName;
    }
    public void setGuaTypeName(String guaTypeName) {
        this.guaTypeName = guaTypeName;
    }
    public String getGuaNo() {
        return guaNo;
    }
    public void setGuaNo(String guaNo) {
        this.guaNo = guaNo;
    }
    public String getGuaPerson() {
        return guaPerson;
    }
    public void setGuaPerson(String guaPerson) {
        this.guaPerson = guaPerson;
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
    public String getCreditorPerson() {
        return creditorPerson;
    }
    public void setCreditorPerson(String creditorPerson) {
        this.creditorPerson = creditorPerson;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public java.math.BigDecimal getGuaMoney() {
        return guaMoney;
    }
    public void setGuaMoney(java.math.BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
}
