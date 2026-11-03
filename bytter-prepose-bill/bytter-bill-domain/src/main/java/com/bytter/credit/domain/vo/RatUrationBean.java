package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * 授信结转Bean
 * 
 * @author liyh
 *
 */
public class RatUrationBean implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 占用ID
     */
    private String urationId;
    /**
     * 占用CODE
     */
    private String urationCode;
    /**
     * 授信ID
     */
    private String ratId;
    /**
     * 授信CODE
     */
    private String ratCode;
    /**
     * 占用金额
     */
    private BigDecimal money;
    /**
     * 结转授信的结果状态
     */
    private String resultCode;
    /**
     * 可结转授信
     */
    private List<RatUrationDisBean> rationDtls;
    /**
     * 可结转的授信明细
     */
    private List<String> ratCodes;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getUrationId() {
        return urationId;
    }
    public void setUrationId(String urationId) {
        this.urationId = urationId;
    }
    public String getUrationCode() {
        return urationCode;
    }
    public void setUrationCode(String urationCode) {
        this.urationCode = urationCode;
    }
    public String getRatId() {
        return ratId;
    }
    public void setRatId(String ratId) {
        this.ratId = ratId;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getResultCode() {
        return resultCode;
    }
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    public List<RatUrationDisBean> getRationDtls() {
        return rationDtls;
    }
    public void setRationDtls(List<RatUrationDisBean> rationDtls) {
        this.rationDtls = rationDtls;
    }
    public List<String> getRatCodes() {
        return ratCodes;
    }
    public void setRatCodes(List<String> ratCodes) {
        this.ratCodes = ratCodes;
    }
}
