package com.bytter.finance.domain.directFinance.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:31
 * @Version 1.0
 */
@Entity
@Table(name = "DF_BUSINESS_TYPE", schema = "")
@SuppressWarnings("serial")
public class DfBusinessType extends IdEntity {
    /**
     * 业务类别编码
     */
    private String businessCode;
    /**
     * 业务类别名称
     */
    private String businessName;
    /**
     * 是否有效
     */
    private String isValid;
    @Column(name = "BUSINESS_CODE")
    public String getBusinessCode() {
        return businessCode;
    }
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
    @Column(name = "BUSINESS_NAME")
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    @Column(name = "IS_VALID")
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
