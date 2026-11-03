package com.bytter.olap.dataprovider.config;

import java.util.List;

/**
 * 图表的聚合配置类
 * Created by zyong on 2017/1/9.
 */
public class AggConfig {

    /**
     * 行维，对应图表设计中的行维栏
     */
    private List<DimensionConfig> rows;

    /**
     * 列维，对应图表设计中的列维栏
     */
    private List<DimensionConfig> columns;

    /**
     * 过滤条件，对应图表设计中的过滤栏
     */
    private List<ConfigComponent> filters;

    /**
     * 指标，对应图表设计中的指标栏
     */
    private List<ValueConfig> values;

    public List<DimensionConfig> getRows() {
        return rows;
    }

    public void setRows(List<DimensionConfig> rows) {
        this.rows = rows;
    }

    public List<DimensionConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<DimensionConfig> columns) {
        this.columns = columns;
    }

    public List<ConfigComponent> getFilters() {
        return filters;
    }

    public void setFilters(List<ConfigComponent> filters) {
        this.filters = filters;
    }

    public List<ValueConfig> getValues() {
        return values;
    }

    public void setValues(List<ValueConfig> values) {
        this.values = values;
    }
}
