package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractInfoVO 担保合同信息VO
 * @Description
 * @date 2018年12月15日
 */
public class GuaContractExcelVO extends ExcelModel implements Serializable {
    private String id;
    /** 单位ID */
    @Corp(inject = "corpName")
    private String corpId;
    /** 单位名称 */
    @Excel(name = "*融资主体")
    private String corpName;
    /** 担保编号 */
    @Excel(name = "*担保编号")
    private String guaNo;
    /** 币别 */
    @Currency(inject = "curName")
    private String curId;
    /** 币别名称 */
    @Excel(name = "*币别")
    private String curName;
    /** 担保开始日期 */
    @Excel(name = "*担保开始日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 担保结束日期 */
    @Excel(name = "*担保结束日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 债务人 */
    // @Corp(inject = "debtorPersonName")
    @Excel(name = "*债务人")
    private String debtorPerson;
    /** 债务人名称 */
    // todo 当债务人为单位确定为单位时使用
    // @Excel(name = "*债务人")
    // private java.lang.String debtorPersonName;
    /** 债权人 */
    @Excel(name = "*债权人")
    private String creditorPerson;
    /** 是否集团内担保单位 */
    @Excel(name = "*是否集团内担保单位")
    private String innerGua;
    /** 担保单位 */
    @Excel(name = "*担保单位")
    private String guaPerson;
    /** 合同编号 */
    @Excel(name = "*合同编号")
    private String contractCode;
    /** 担保范围 */
    @Excel(name = "担保范围")
    private String warr;
    /** 备注 */
    @Excel(name = "备注")
    private String rmk;
    @Override
    public String getBeanName() {
        return "guaContractInfoService";
    }
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
    public String getDebtorPerson() {
        return debtorPerson;
    }
    public void setDebtorPerson(String debtorPerson) {
        this.debtorPerson = debtorPerson;
    }
    public String getCreditorPerson() {
        return creditorPerson;
    }
    public void setCreditorPerson(String creditorPerson) {
        this.creditorPerson = creditorPerson;
    }
    public String getInnerGua() {
        return innerGua;
    }
    public void setInnerGua(String innerGua) {
        this.innerGua = innerGua;
    }
    public String getGuaPerson() {
        return guaPerson;
    }
    public void setGuaPerson(String guaPerson) {
        this.guaPerson = guaPerson;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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
}
