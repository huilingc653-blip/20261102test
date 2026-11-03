package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 评分指标
 */
@Entity
@Table(name = "CR_DATA_SOURCE", schema = "")
@SuppressWarnings("serial")
public class CrDataSource extends IdEntity implements java.io.Serializable{
    /**
     * 数据源code
     */
    private String dataSourceCode;
    /**
     * 数据源名称
     */
    private String dataSourceName;
    /**
     * 数据源取值来源描述
     */
    private String dataSourceOrigin;

    @Column(name ="DATA_SOURCE_CODE",nullable=false,length=32)
    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    @Column(name ="DATA_SOURCE_NAME",nullable=false,length=100)
    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    @Column(name ="DATA_SOURCE_ORIGIN",nullable=false,length=1000)
    public String getDataSourceOrigin() {
        return dataSourceOrigin;
    }

    public void setDataSourceOrigin(String dataSourceOrigin) {
        this.dataSourceOrigin = dataSourceOrigin;
    }

}
