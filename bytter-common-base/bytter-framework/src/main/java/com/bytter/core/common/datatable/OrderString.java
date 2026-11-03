package com.bytter.core.common.datatable;
/**
 * Created by zhoul on 2017/8/14.
 */
public class OrderString {
    private String column;
    private String sort;

    public OrderString(String column, String sort) {
        this.column = column;
        this.sort = sort;
    }

    public String getColumn() {
        return column;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
}
