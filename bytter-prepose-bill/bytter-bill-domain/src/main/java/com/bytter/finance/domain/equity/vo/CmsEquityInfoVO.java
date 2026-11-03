package com.bytter.finance.domain.equity.vo;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 股权融资表vo
 * @date 2022-06-08
 */
public class CmsEquityInfoVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 股权融资类型编号
     */
    @BtCompositeSearch(fieldName = "einfo.EQUITY_TYPE_CODE")
    private String equityTypeCode;
    /**
     * 股权融资类型名称
     */
    @BtCompositeSearch(fieldName = "einfo.EQUITY_TYPE_CODE")
    private String equityTypeName;
    /**
     * 境内/境外
     */
    private String isDomestic;
    /**
     * 融资单位
     */
    @BtCompositeSearch(fieldName = "einfo.CORP_ID")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 币种
     */
    @BtCompositeSearch(fieldName = "einfo.CUR_ID")
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 融资金额
     */
    @BtCompositeSearch(fieldName = "einfo.MONEY")
    private BigDecimal money;
    /**
     * 验资账号
     */
    private String account;
    /**
     * 账户名称
     */
    private String accName;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 融资日期
     */
    @BtCompositeSearch(fieldName = "einfo.FINANCING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date financingDate;
    /**
     * 交割日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;
    /**
     * 融资用途
     */
    @BtCompositeSearch(fieldName = "einfo.FINANCING_USE")
    private String financingUse;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "einfo.RMK")
    private String rmk;
    /**
     * 附件ID
     */
    private String uploadId;
    /**
     * 第三方费率信息
     */
    private List<DfIntermediateCost> costTypeDtl;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "einfo.STATUS")
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEquityTypeCode() {
        return equityTypeCode;
    }
    public void setEquityTypeCode(String equityTypeCode) {
        this.equityTypeCode = equityTypeCode;
    }
    public String getEquityTypeName() {
        return equityTypeName;
    }
    public void setEquityTypeName(String equityTypeName) {
        this.equityTypeName = equityTypeName;
    }
    public String getIsDomestic() {
        return isDomestic;
    }
    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public Date getFinancingDate() {
        return financingDate;
    }
    public void setFinancingDate(Date financingDate) {
        this.financingDate = financingDate;
    }
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    public String getFinancingUse() {
        return financingUse;
    }
    public void setFinancingUse(String financingUse) {
        this.financingUse = financingUse;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    public List<DfIntermediateCost> getCostTypeDtl() {
        return costTypeDtl;
    }
    public void setCostTypeDtl(List<DfIntermediateCost> costTypeDtl) {
        this.costTypeDtl = costTypeDtl;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
