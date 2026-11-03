package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 境外银行行号信息维护实体
 *
 * @author qinl
 * @date 2019-07-15
 */
@Entity
@Table(name = "BT_SWIFT_INFO")
public class BtSwiftInfo extends IdEntity implements java.io.Serializable {
    /**
     * swift code
     */
    private String swiftCode;
    /**
     * 机构名称
     */
    private String bankName;
    /**
     * 所在国
     */
    private String countryCode;
    /**
     * 所在城市
     */
    private String cityName;
    /**
     * 地址
     */
    private String bankAdd;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 开户银行代码类型
     */
    private String clsFlag;


    /**
     * 本地清算号
     */
    private String localClearNo;


    /**
     * 银行类别id
     */
    private String bankTypeId;

    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name = "SWIFT_CODE", nullable = false, length = 30)
    public String getSwiftCode() {
        return swiftCode;
    }
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
    @Column(name = "BANK_NAME", nullable = false, length = 200)
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "COUNTRY_CODE", nullable = true, length = 30)
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    @Column(name = "CITY_NAME", nullable = true, length = 100)
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    @Column(name = "BANK_ADD", nullable = true, length = 300)
    public String getBankAdd() {
        return bankAdd;
    }
    public void setBankAdd(String bankAdd) {
        this.bankAdd = bankAdd;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
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
    @Column(name = "CLS_FLAG")
    public String getClsFlag() {
        return clsFlag;
    }
    public void setClsFlag(String clsFlag) {
        this.clsFlag = clsFlag;
    }

    @Column(name = "LOCAL_CLEAR_NO")
    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }
}
