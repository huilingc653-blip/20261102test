package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Table
 * @Description: 理财产品类型表
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Entity
@Table(name = "CHO_PRODUCT_TYPE",schema = "")
public class ChoProduct extends IdEntity implements java.io.Serializable{
    /**银行类型*/
    private String bankTypeId;
    /**银行类别名称*/
    private String bankTypeName;
    /**产品类型*/
    private String productType;
    /**产品代码*/
    private String productCode;
    /**产品名称*/
    private String productName;
    /**币别*/
    private String curId;
    /**币种名称*/
    private String curName;
    /**备注*/
    private String rmk;
    /**有效标志 1-有效 0-无效*/
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**
     * 理财类型
     */
    private String financialType;
    /**
     * 是否已理财登记审批通过
     */
    private String isApprove;
    /**续存模式 1-本金续存 2-本息续存*/
    private Integer certificateWay;

    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "IS_APPROVE")
    public String getIsApprove() {
        return isApprove;
    }
    public void setIsApprove(String isApprove) {
        this.isApprove = isApprove;
    }
    @Column(name = "FINANCIAL_TYPE")
    public String getFinancialType() {
        return financialType;
    }
    public void setFinancialType(String financialType) {
        this.financialType = financialType;
    }
    @Column(name = "BANK_TYPE_ID", nullable = false, length = 32)
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name = "PRODUCT_TYPE", nullable = false, length = 10)
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    @Column(name = "PRODUCT_CODE", nullable = false, length = 60)
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Column(name = "CUR_ID", nullable = true, length = 32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "RMK", nullable = true, length = 100)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true, length = 6)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true, length = 6)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "CERTIFICATE_WAY")
    public Integer getCertificateWay() {
        return certificateWay;
    }

    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
}
