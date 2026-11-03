package com.bytter.finance.domain.equity.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 股权融资表
 * @date 2022-06-08
 */
@Entity
@Table(name = "CMS_EQUITY_INFO", schema = "")
public class CmsEquityInfo extends IdEntity implements java.io.Serializable {
    /**
     * 股权融资类型编号
     */
    private String equityTypeCode;
    /**
     * 境内/境外
     */
    private String isDomestic;
    /**
     * 融资单位
     */
    private String corpId;
    /**
     * 币种
     */
    private String curId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 融资金额
     */
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
    private Date financingDate;
    /**
     * 交割日期
     */
    private Date deliveryDate;
    /**
     * 融资用途
     */
    private String financingUse;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 附件ID
     */
    private String uploadId;
    /**
     * 状态
     */
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
    @Column(name = "EQUITY_TYPE_CODE")
    public String getEquityTypeCode() {
        return equityTypeCode;
    }
    public void setEquityTypeCode(String equityTypeCode) {
        this.equityTypeCode = equityTypeCode;
    }
    @Column(name = "ISDOMESTIC")
    public String getIsDomestic() {
        return isDomestic;
    }
    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    @Column(name = "ACC_NAME")
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "FINANCING_DATE")
    public Date getFinancingDate() {
        return financingDate;
    }
    public void setFinancingDate(Date financingDate) {
        this.financingDate = financingDate;
    }
    @Column(name = "DELIVERY_DATE")
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    @Column(name = "FINANCING_USE")
    public String getFinancingUse() {
        return financingUse;
    }
    public void setFinancingUse(String financingUse) {
        this.financingUse = financingUse;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "UPLOAD_ID")
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
