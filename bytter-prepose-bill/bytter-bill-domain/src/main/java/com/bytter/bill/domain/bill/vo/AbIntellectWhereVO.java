package com.bytter.bill.domain.bill.vo;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 智能选票查询条件VO
 * @date 2022-10-20
 */
@SuppressWarnings("serial")
public class AbIntellectWhereVO implements java.io.Serializable {
    /**
     * 条件类型：table 关联表  where查询条件 order 排序
     */
    private String whereType;
    private String whereStr;
    public String getWhereType() {
        return whereType;
    }
    public void setWhereType(String whereType) {
        this.whereType = whereType;
    }
    public String getWhereStr() {
        return whereStr;
    }
    public void setWhereStr(String whereStr) {
        this.whereStr = whereStr;
    }
}
