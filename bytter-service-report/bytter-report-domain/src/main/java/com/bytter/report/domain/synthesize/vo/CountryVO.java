package com.bytter.report.domain.synthesize.vo;
import com.bytter.core.annotation.Currency;
import java.math.BigDecimal;
/**
 * 国家余额
 *
 * @author zuoy
 * @since 2020-01-16
 */
public class CountryVO  {
    /**
     * 国家所属大区
     */
    private String continent;
    /**
     * 所属国家
     * 取com.bytter.biz.basic.business.entity
     */
    private String countryCode;
    /**
     * 所属省份
     */
    private String prov;
    /**
     * 所属城市
     */
    private String city;
    /**
     * 国家名称
     */
    private String countryName;
    /**
     * 币别ID
     */
    @Currency(inject="curName")
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 余额
     */
    private BigDecimal balance;
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
