package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * @author mof
 * @version 1.0
 * @Title: Vo
 * @Description: 账号规则配置参数配置
 * @date 2022-03-07 17:35:25
 */
@SuppressWarnings("serial")
public class AbClaimAccountVo extends IdEntity implements Serializable {
    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "corp_id")
    private String corpId;
    /**
     * 单位
     */
    @BtCompositeSearch(fieldName = "corp_name")
    private String corpName;
    /**
     * 银行账户ID
     */
    private String bankId;
    /**
     * 银行账号
     */
    @BtCompositeSearch(fieldName = "bank_acc")
    private String bankAcc;
    /**
     * 银行账号名称
     */
    private String bankName;
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    private String bankTypeName;
    /**
     * 业务类型
     */
    @Dict(dictGroup = "AB_CLAIM_ACCOUNTCONFIG", inject = "busTypeName")
    private String busType;
    /**
     * 业务类型名称
     */
    @BtCompositeSearch(fieldName = "bus_type")
    private String busTypeName;
    /**
     * 有效标记0无效1有效
     */
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    @BtCompositeSearch(fieldName = "valid_sign")
    private String validSign;
    @BtCompositeSearch(fieldName = "valid_sign")
    private String validSignName;
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
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
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
