package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 用途地区中间表
 * @date 2022-11-23 09:57:15
 */
@Entity
@Table(name = "BT_PURPOSE_COUNTRY", schema = "")
@SuppressWarnings("serial")
public class BtPurposeCountry extends IdEntity {
    private String purposeId;
    /**
     * 国家地区ID
     */
    private String countryId;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  用途ID
     */
    @Column(name = "PURPOSE_ID", nullable = false, length = 32)
    public String getPurposeId() {
        return purposeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  用途ID
     */
    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  国家地区ID
     */
    @Column(name = "COUNTRY_ID", nullable = false, length = 32)
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
