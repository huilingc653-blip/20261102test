package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 原始数据
 */
@Entity
@Table(name = "RAW_DATA", schema = "")
@SuppressWarnings("serial")
public class CrdRawData extends IdEntity implements Serializable {

    /**
     *数据名称:供应商、子企业、机构或者其他的数据名称
     */
    private String dataName;
    /**
     *数据代码
     */
    private String dataCode;
    /**
     *成立时间(yyyy-MM-dd)
     */
    private Date establishmentDate;
    /**
     *注册资本
     */
    private BigDecimal registeredCapital;
    /**
     *法定代表人
     */
    private String legalRepresentative;
    /**
     *经营范围
     */
    private String businessScope;
    /**
     *数据来源业务表主建(供应商、子企业、机构的主键)
     */
    private String dataId;
    /**
     *数据来源类别名称;数据来源(Data_Source)Data_Source_Category_Name
     */
    private String dataSourceCategoryName;
    /**
     *数据来源类别代码;数据来源(Data_Source)Data_Source_Category_Code。D001 供应商
     */
    private String dataSourceCategoryCode;
    /**
     *外键(数据来源主键);数据来源(Data_Source)主键 id
     */
    private String dataSourceId;
    /**
     *状态：-2 已删除；-1 审批打回; 95 正常；默认为 0; 11 待审核;
     */
    private Integer status;

    /**
     * 附件id
     */
    private String attachmentId;
    /**
     *创建人
     */
    private String creator;
    /**
     *创建日期时间:yyyy-MM-dd hh24:mm:ss
     */
    private Date createDate;


    @Column(name ="DATA_NAME")
    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    @Column(name ="DATA_CODE")
    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }
    @Column(name ="ESTABLISHMENT_DATE")
    public Date getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(Date establishmentDate) {
        this.establishmentDate = establishmentDate;
    }
    @Column(name ="REGISTERED_CAPITAL")
    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }
    @Column(name ="LEGAL_REPRESENTATIVE")
    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }
    @Column(name ="BUSINESS_SCOPE")
    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }
    @Column(name ="DATA_ID")
    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @Column(name ="DATA_SOURCE_CATEGORY_NAME")
    public String getDataSourceCategoryName() {
        return dataSourceCategoryName;
    }

    public void setDataSourceCategoryName(String dataSourceCategoryName) {
        this.dataSourceCategoryName = dataSourceCategoryName;
    }
    @Column(name ="DATA_SOURCE_CATEGORY_CODE")
    public String getDataSourceCategoryCode() {
        return dataSourceCategoryCode;
    }

    public void setDataSourceCategoryCode(String dataSourceCategoryCode) {
        this.dataSourceCategoryCode = dataSourceCategoryCode;
    }

    @Column(name ="DATA_SOURCE_ID")
    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name ="CREATOR")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "RawData{" +
                "dataName='" + dataName + '\'' +
                ", dataCode='" + dataCode + '\'' +
                ", establishmentDate=" + establishmentDate +
                ", registeredCapital=" + registeredCapital +
                ", legalRepresentative='" + legalRepresentative + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", dataId='" + dataId + '\'' +
                ", dataSourceCategoryName='" + dataSourceCategoryName + '\'' +
                ", dataSourceCategoryCode='" + dataSourceCategoryCode + '\'' +
                ", dataSourceId='" + dataSourceId + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
