package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author mof
 * @version 1.0
 * @Title: Entity
 * @Description: 账号规则配置参数配置
 * @date 2022-03-04 15:43:17
 */
@Entity
@Table(name = "AB_CLAIM_ACCOUNT", schema = "")
@SuppressWarnings("serial")
public class AbClaimAccount extends IdEntity implements java.io.Serializable {
    /**
     * 单位
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 银行账户ID
     */
    private String bankId;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 银行账号名称
     */
    private String bankName;
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 有效标记0无效1有效
     */
    private String validSign;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    @Column(name = "CORP_ID", nullable = true)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CORP_NAME", nullable = true)
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "BANK_ID", nullable = true)
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    @Column(name = "BANK_ACC", nullable = true)
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    @Column(name = "BANK_NAME", nullable = true)
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "BANK_TYPE_ID", nullable = true)
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name = "BANK_TYPE_NAME", nullable = true)
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Column(name = "BUS_TYPE", nullable = true)
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    @Column(name = "VALID_SIGN", nullable = true)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     * 创建人ID
     */
    @Column(name = "CREATE_BY", nullable = true)
    public String getCreateBy() {
        return createBy;
    }
    /**
     * 创建人ID
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Override
    public String toString() {
        return "AbClaimAccount{" +
                "corpId='" + corpId + '\'' +
                ", bankId='" + bankId + '\'' +
                ", validSign='" + validSign + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
