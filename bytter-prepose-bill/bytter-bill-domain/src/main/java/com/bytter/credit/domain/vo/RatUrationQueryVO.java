package com.bytter.credit.domain.vo;
import java.io.Serializable;
/**
 * 授信查询条件
 * 
 * @author liyh
 *
 */
public class RatUrationQueryVO implements Serializable {
    private String ratCode;
    private String itemFlag;
    private String itemCode;
    private String corpId;
    private String curCode;
    private Integer useType;
    private String businessName;
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public Integer getUseType() {
        return useType;
    }
    public void setUseType(Integer useType) {
        this.useType = useType;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
