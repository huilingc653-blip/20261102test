package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.core.annotation.BtCompositeSearch;
/**
 * 授信追加VO
 * 
 * @author liyh
 *
 */
public class RatBkrationAddPageVO implements Serializable {
    /** 授信录入主键ID **/
    private String id;
    /** 授信追加主键ID **/
    private String addCode;
    /** 授信编号 */
    private String ratCode;
    /** 单位代码 */
    private String corpId;
    /** 单位代码 */
    private String corpName;
    /** 授信种类 */
    private String ratType;
    /** 银行类型 */
    private String bankType;
    /** 银行类型名称 */
    private String typeName;
    /** 银行编号 */
    private String bankCode;
    /** 银行编号 */
    private String bankName;
    /** 敞口授信 */
    private String openSign;
    /** 授信模式 */
    private String ratMode;
    /** 授信币种 */
    private String curCode;
    /** 授信币种 */
    private String curName;
    /** 授信开始时间 */
    private java.util.Date beginDate;
    /** 授信结束时间 */
    private java.util.Date endDate;
    /** 授信金额 */
    private BigDecimal money;
    /** 敞口额度 */
    private BigDecimal openMoney;
    /** 追加授信金额 */
    private BigDecimal addMoney;
    /** 追加敞口金额 */
    private BigDecimal addOpenMoney;
    /** 状态 */
    private Integer status;
    /** 授信合同号 */
    @BtCompositeSearch(fieldName = "rb.contract_no")
    private String contractNo;
    /** 额度是否包含保证金 */
    private String includeBailmoney;
    /**
     * 单品种条数
     */
    private Integer singItemSize;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    /**
     * 综合品种行数
     */
    private Integer compItemIndex;
    /**
     * 总品种数
     */
    private Integer itemSize;
    /**
     * 授信录入综合品种明细数据
     */
    private List<RatBkRationAddDtlVm> compRationDtls;
    /**
     * 授信录入单品种明细数据
     */
    private List<RatBkRationAddDtlVm> singRationDtls;
    /** 备注 */
    private String remark;
    /** 担保类型 */
    private String guaType;
    /** 担保单位 */
    private String guaCorpId;
    /** 担保合同 */
    private String guaContract;
    /**
     * 二次分配单位
     */
    private String addCorpId;
    /**
     * 二次分配单位名称
     */
    private String addCorpName;
    private String ratAddCode;// 追加code
    /** 附件ID */
    private String attachmentId;
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getAddCode() {
        return addCode;
    }
    public void setAddCode(String addCode) {
        this.addCode = addCode;
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
    public java.util.Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public BigDecimal getAddMoney() {
        if (null == this.addMoney) {
            this.addMoney = BigDecimal.ZERO;
        }
        return addMoney;
    }
    public void setAddMoney(BigDecimal addMoney) {
        this.addMoney = addMoney;
    }
    public BigDecimal getAddOpenMoney() {
        if (null == this.addOpenMoney) {
            this.addOpenMoney = BigDecimal.ZERO;
        }
        return addOpenMoney;
    }
    public void setAddOpenMoney(BigDecimal addOpenMoney) {
        this.addOpenMoney = addOpenMoney;
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
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public List<RatBkRationAddDtlVm> getCompRationDtls() {
        return compRationDtls;
    }
    public void setCompRationDtls(List<RatBkRationAddDtlVm> compRationDtls) {
        this.compRationDtls = compRationDtls;
    }
    public List<RatBkRationAddDtlVm> getSingRationDtls() {
        return singRationDtls;
    }
    public void setSingRationDtls(List<RatBkRationAddDtlVm> singRationDtls) {
        this.singRationDtls = singRationDtls;
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
    public Integer getCompItemIndex() {
        if (null == this.compItemIndex) {
            this.compItemIndex = Integer.valueOf(0);
        }
        return compItemIndex;
    }
    public void setCompItemIndex(Integer compItemIndex) {
        this.compItemIndex = compItemIndex;
    }
    public Integer getItemSize() {
        return itemSize;
    }
    public void setItemSize(Integer itemSize) {
        this.itemSize = itemSize;
    }
    public String getIncludeBailmoney() {
        return includeBailmoney;
    }
    public void setIncludeBailmoney(String includeBailmoney) {
        this.includeBailmoney = includeBailmoney;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getGuaType() {
        return guaType;
    }
    public void setGuaType(String guaType) {
        this.guaType = guaType;
    }
    public String getGuaCorpId() {
        return guaCorpId;
    }
    public void setGuaCorpId(String guaCorpId) {
        this.guaCorpId = guaCorpId;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getAddCorpId() {
        return addCorpId;
    }
    public void setAddCorpId(String addCorpId) {
        this.addCorpId = addCorpId;
    }
    public String getAddCorpName() {
        return addCorpName;
    }
    public void setAddCorpName(String addCorpName) {
        this.addCorpName = addCorpName;
    }
    public String getRatAddCode() {
        return ratAddCode;
    }
    public void setRatAddCode(String ratAddCode) {
        this.ratAddCode = ratAddCode;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
