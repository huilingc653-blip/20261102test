package com.bytter.biz.creditRating.vo;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;

/**
 * 评分指标
 */

@SuppressWarnings("serial")
public class CrDataSourceVo extends IdEntity implements java.io.Serializable{
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
