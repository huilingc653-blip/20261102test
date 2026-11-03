package com.bytter.core.common.datatable;
import java.io.Serializable;
public class Order implements Serializable {
    private Integer column;
    private String dir;
    public Order() {
    }
    public Order(Integer column, String dir) {
        this.column = column;
        this.dir = dir;
    }
    public Integer getColumn() {
        return column;
    }
    public void setColumn(Integer column) {
        this.column = column;
    }
    public String getDir() {
        return dir;
    }
    public void setDir(String dir) {
        this.dir = dir;
    }
    @Override
    public String toString() {
        return "Order{" + "column=" + column + ", dir='" + dir + '\'' + '}';
    }
    /**
     * 验证排序参数是否合法
     * 
     * @param param
     * @return
     */
    public static boolean checkOrderParam(String param) {
        for (OrderType value : OrderType.values()) {
            if (value.name().equalsIgnoreCase(param)) {
                return true;
            }
        }
        return false;
    }
    public enum OrderType {
        DESC, ASC,;
    }
}
