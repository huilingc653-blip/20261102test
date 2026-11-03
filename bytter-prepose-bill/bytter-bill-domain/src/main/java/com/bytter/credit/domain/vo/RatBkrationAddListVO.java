package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 授信追加VO
 * 
 * @author liyh
 *
 */
public class RatBkrationAddListVO implements Serializable, IBusinessKey {
    @BtCompositeSearch(fieldName = "rb.id")
    private String id;
    /** 授信追加编号 **/
    private String addCode;
    /** 授信编号 */
    @BtCompositeSearch(fieldName = "rd.rat_Code")
    private String ratCode;
    /** 单位代码 */
    @BtCompositeSearch(fieldName = "rd.corp_id")
    private String corpId;
    /** 单位代码 */
    private String corpName;
    /** 授信种类 */
    private String ratType;
    /** 银行类型 */
    private String bankType;
    /** 银行编号 */
    private String bankCode;
    /** 银行编号 */
    private String bankName;
    /** 敞口授信 */
    private String openSign;
    /** 授信模式 */
    @BtCompositeSearch(fieldName = "rb.rat_mode")
    private String ratMode;
    /** 授信币种 */
//    @Currency(inject = "curName")
    private String curCode;
    /** 授信币种 */
    private String curName;
    /** 授信开始时间 */
    private Date beginDate;
    /** 授信结束时间 */
    private Date endDate;
    /** 授信金额 */
    private java.math.BigDecimal money;
    /** 敞口额度 */
    private java.math.BigDecimal openMoney;
    /** 追加类型 */
    @BtCompositeSearch(fieldName = "rd.flag")
    private String flag;
    /** 状态 */
    private Integer status;
    /** 授信合同号 */
    @BtCompositeSearch(fieldName = "rb.contract_no")
    private String contractNo;
    /**
     * 单品种条数
     */
    private Integer singItemSize;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    /**
     * 总品种数
     */
    private Integer itemSize;
    /**
     * 授信录入单位
     */
    private String ratCorpId;
    /**
     * 授信录入单位
     */
    private String ratCorpName;
    /**
     * 授信录入明细数据
     */
    private List<RatBkRationAddDtlVm> rationDtls;
    @BtCompositeSearch(fieldName = "rb.begindate")
    private Date sDate;
    @BtCompositeSearch(fieldName = "rb.enddate")
    private Date eDate;
    // 创建日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    @BtCompositeSearch(fieldName = "rd.create_Date")
    private Date startDate1;
    @BtCompositeSearch(fieldName = "rd.create_Date")
    private Date startDate2;
    public String getAddCode() {
        return addCode;
    }
    public void setAddCode(String addCode) {
        this.addCode = addCode;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
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
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getOpenSign() {
        return openSign;
    }
    public void setOpenSign(String openSign) {
        this.openSign = openSign;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    public java.math.BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(java.math.BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public List<RatBkRationAddDtlVm> getRationDtls() {
        return rationDtls;
    }
    public void setRationDtls(List<RatBkRationAddDtlVm> rationDtls) {
        this.rationDtls = rationDtls;
    }
    public Integer getSingItemSize() {
        return singItemSize;
    }
    public void setSingItemSize(Integer singItemSize) {
        this.singItemSize = singItemSize;
    }
    public Integer getCompItemSize() {
        return compItemSize;
    }
    public void setCompItemSize(Integer compItemSize) {
        this.compItemSize = compItemSize;
    }
    public Integer getItemSize() {
        return itemSize;
    }
    public void setItemSize(Integer itemSize) {
        this.itemSize = itemSize;
    }
    public Date getSDate() {
        return sDate;
    }
    public void setSDate(Date sDate) {
        this.sDate = sDate;
    }
    public Date getEDate() {
        return eDate;
    }
    public void setEDate(Date eDate) {
        this.eDate = eDate;
    }
    public String getRatCorpId() {
        return ratCorpId;
    }
    public void setRatCorpId(String ratCorpId) {
        this.ratCorpId = ratCorpId;
    }
    public String getRatCorpName() {
        return ratCorpName;
    }
    public void setRatCorpName(String ratCorpName) {
        this.ratCorpName = ratCorpName;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getStartDate1() {
        return startDate1;
    }
    public void setStartDate1(Date startDate1) {
        this.startDate1 = startDate1;
    }
    public Date getStartDate2() {
        return startDate2;
    }
    public void setStartDate2(Date startDate2) {
        this.startDate2 = startDate2;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
