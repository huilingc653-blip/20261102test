package com.bytter.olap.dataprovider.result;

import com.bytter.olap.dataprovider.config.DimensionConfig;
import com.bytter.olap.dataprovider.config.ValueConfig;

/**
 * 维度列
 * Created by yfyuan on 2017/1/19.
 */
public class ColumnIndex {

    /**
     * 下标
     */
    private int index;
    /**
     * 类型
     */
    private String aggType;
    /**
     * 维度列名称
     */
    private String name;

    public static ColumnIndex fromDimensionConfig(final DimensionConfig dimensionConfig) {
        ColumnIndex columnIndex = new ColumnIndex();
        columnIndex.setName(dimensionConfig.getColumnName());
        return columnIndex;
    }

    public static ColumnIndex fromValueConfig(final ValueConfig valueConfig) {
        ColumnIndex columnIndex = new ColumnIndex();
        columnIndex.setName(valueConfig.getColumn());
        columnIndex.setAggType(valueConfig.getAggType());
        return columnIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAggType() {
        return aggType;
    }

    public void setAggType(String aggType) {
        this.aggType = aggType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
