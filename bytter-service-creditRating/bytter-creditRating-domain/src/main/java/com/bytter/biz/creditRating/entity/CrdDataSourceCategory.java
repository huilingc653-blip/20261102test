package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据来源类别 实体
 */
@Entity
@Table(name = "DATA_SOURCE_CATEGORY", schema = "")
@SuppressWarnings("serial")
public class CrdDataSourceCategory extends IdEntity implements Serializable {

    /**
     *数据来源类别名称
     */
    private String dataSourceCategoryName;
    /**
     *数据来源类别代码:D001-供应商;D002-子企业;D003-机构
     */
    private String dataSourceCategoryCode;
    /**
     *创建人
     */
    private String creator;
    /**
     *创建日期时间:yyyy-MM-dd hh24:mm:ss
     */
    private Date createDate;

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
        return "DataSourceCategory{" +
                "dataSourceCategoryName='" + dataSourceCategoryName + '\'' +
                ", dataSourceCategoryCode='" + dataSourceCategoryCode + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
