package com.bytter.biz.creditRating.vo;

import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CrdRawDataVo implements Serializable, IBusinessKey {
    private String id;
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
     *创建人
     */
    private String creator;
    /**
     *创建日期时间:yyyy-MM-dd hh24:mm:ss
     */
    private Date createDate;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getDataName() {
//        return dataName;
//    }
//
//    public void setDataName(String dataName) {
//        this.dataName = dataName;
//    }
//
//    public String getDataCode() {
//        return dataCode;
//    }
//
//    public void setDataCode(String dataCode) {
//        this.dataCode = dataCode;
//    }
//
//    public Date getEstablishmentDate() {
//        return establishmentDate;
//    }
//
//    public void setEstablishmentDate(Date establishmentDate) {
//        this.establishmentDate = establishmentDate;
//    }
//
//    public BigDecimal getRegisteredCapital() {
//        return registeredCapital;
//    }
//
//    public void setRegisteredCapital(BigDecimal registeredCapital) {
//        this.registeredCapital = registeredCapital;
//    }
//
//    public String getLegalRepresentative() {
//        return legalRepresentative;
//    }
//
//    public void setLegalRepresentative(String legalRepresentative) {
//        this.legalRepresentative = legalRepresentative;
//    }
//
//    public String getBusinessScope() {
//        return businessScope;
//    }
//
//    public void setBusinessScope(String businessScope) {
//        this.businessScope = businessScope;
//    }
//
//    public String getDataId() {
//        return dataId;
//    }
//
//    public void setDataId(String dataId) {
//        this.dataId = dataId;
//    }
//
//    public String getDataSourceCategoryName() {
//        return dataSourceCategoryName;
//    }
//
//    public void setDataSourceCategoryName(String dataSourceCategoryName) {
//        this.dataSourceCategoryName = dataSourceCategoryName;
//    }
//
//    public String getDataSourceCategoryCode() {
//        return dataSourceCategoryCode;
//    }
//
//    public void setDataSourceCategoryCode(String dataSourceCategoryCode) {
//        this.dataSourceCategoryCode = dataSourceCategoryCode;
//    }
//
//    public String getDataSourceId() {
//        return dataSourceId;
//    }
//
//    public void setDataSourceId(String dataSourceId) {
//        this.dataSourceId = dataSourceId;
//    }
//
//    public String getCreator() {
//        return creator;
//    }
//
//    public void setCreator(String creator) {
//        this.creator = creator;
//    }
//
//    public Date getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
