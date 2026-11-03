package com.bytter.guarantee.dto;
import java.io.Serializable;
/**
 * @version V1.0
 * @Title: 合同字段明细表对应VO
 * @author: zhengg
 * @date: 2019-11-6
 */
@SuppressWarnings("serial")
public class ConInfoBusdtlDTO implements Serializable {
    /**
     * 关联ConInfo主信息ID
     */
    private String mid;
    /**
     * 要素名称
     */
    private String name;
    /**
     * 字段值
     */
    private String val;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 排序字段
     */
    private Integer sortNum;
    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVal() {
        return val;
    }
    public void setVal(String val) {
        this.val = val;
    }
    public String getFieldType() {
        return fieldType;
    }
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    public Integer getSortNum() {
        return sortNum;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    @Override
    public String toString() {
        return "ConInfoBusdtlVO{" + "mid='" + mid + '\'' + ", name='" + name + '\'' + ", val='" + val + '\''
                + ", fieldType='" + fieldType + '\'' + ", sortNum=" + sortNum + '}';
    }
}
