package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 黑白名单实体
 *
 * @author caojy
 */
@Entity
@Table(name = "SYS_BLACK_WHITE_LIST", schema = "")
@SuppressWarnings("serial")
public class BlackWhiteList extends IdEntity implements java.io.Serializable {
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 黑白类型:01黑名单02白名单
     */
    private String bwType;
    /**
     * 名单类别：01客商、02承兑行
     */
    private String listType;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 是否共享：1是、0否
     */
    private String isCommon;
    /***数据来源：01BFS、02手工、03ERP*/
    private String dataSources;
    /**
     * 国家ID
     */
    private String countryId;
    /**
     * 状态，0禁用，1启用, -2删除
     */
    private String validSign;
    /***/
    private String externalId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人ID
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人ID
     */
    private String updateId;
    /**
     * 创建日期
     */
    private Date updateDate;
    @Column(name = "code")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "bw_Type")
    public String getBwType() {
        return bwType;
    }
    public void setBwType(String bwType) {
        this.bwType = bwType;
    }
    @Column(name = "list_type")
    public String getListType() {
        return listType;
    }
    public void setListType(String listType) {
        this.listType = listType;
    }
    @Column(name = "corp_id")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "is_common")
    public String getIsCommon() {
        return isCommon;
    }
    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }
    @Column(name = "data_sources")
    public String getDataSources() {
        return dataSources;
    }
    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }
    @Column(name = "country_id")
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    @Column(name = "valid_sign")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "external_id")
    public String getExternalId() {
        return externalId;
    }
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    @Column(name = "rmk")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_ID")
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_ID")
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name = "UPDATE_TIME")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
