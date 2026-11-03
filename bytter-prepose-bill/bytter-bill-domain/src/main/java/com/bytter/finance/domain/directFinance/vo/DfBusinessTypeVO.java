package com.bytter.finance.domain.directFinance.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:31
 * @Version 1.0
 */
public class DfBusinessTypeVO implements Serializable {
    /**
     * 业务主键
     */
    private String id;
    /**
     * 业务类别编码
     */
    @BtCompositeSearch(fieldName = "t.business_code")
    private String businessCode;
    /**
     * 业务类别名称
     */
    @BtCompositeSearch(fieldName = "t.business_name")
    private String businessName;
    /**
     * 是否有效
     */
    private String isValid;
    public String getBusinessCode() {
        return businessCode;
    }
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
