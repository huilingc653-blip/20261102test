package com.bytter.guarantee.domain.vo;
import java.io.Serializable;
/**
 * @version V1.0
 * @Title: 合同字段明细表对应VO
 * @Description: 合同字段明细表对应VO
 * @author: zhenghj
 * @date: 2019-11-4
 */
@SuppressWarnings("serial")
public class ConInfoBusdtlVO implements Serializable {
    public ConInfoBusdtlVO() {
    }
    public ConInfoBusdtlVO(String mid) {
        this.mid = mid;
    }
    /**
     * 关联ConInfo主信息ID
     */
    private String mid;
    /**
     * 要素名称
     */
    private String name;
    /**
     * 要素值
     */
    private String val;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 排序序号
     */
    private Integer sortNum;
    /**
     * 是否必输：0否1是
     */
    private String requiredSign;
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
    public String getRequiredSign() {
        return requiredSign;
    }
    public void setRequiredSign(String requiredSign) {
        this.requiredSign = requiredSign;
    }
    @Override
    public String toString() {
        return "ConInfoBusdtlVO{" + "mid='" + mid + '\'' + ", name='" + name + '\'' + ", val='" + val + '\''
                + ", fieldType='" + fieldType + '\'' + ", sortNum=" + sortNum + ", requiredSign='" + requiredSign + '\''
                + '}';
    }
}
