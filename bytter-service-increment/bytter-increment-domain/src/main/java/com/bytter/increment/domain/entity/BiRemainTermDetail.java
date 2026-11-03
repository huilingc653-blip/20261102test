package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @ClassName BiRemainTermDetail
 * @Description 剩余期限维护详细表
 * @Author fusb
 * @Date 2020/1/9 9:22
 * @Version 1.0
 **/
@Entity
@Table(name = "BI_REMAIN_TERM_DETAIL",schema = "")
public class BiRemainTermDetail extends IdEntity implements Serializable {
    /**CMS_REMAIN_TERM表id*/
    private String remainTermId;
    /**字段1:"到期日期"2*/
    private String field1;
    /**字段2:"系统日期"1*/
    private String field2;
    /**运算符:"+"1,"-"2,"*"3,"/"4*/
    private String symbol;
    /**条件:">"1,"<"2,"="3,"<="4,">="4,">="5,"≠"6*/
    private String conditions;
    /**天数*/
    private Integer days;
    /**单位:"天"D,"月"M,"年"Y*/
    private String unit;
    /**逻辑关系:"或者"1,"并且"2*/
    private String logicalRelationship;
    /**公式*/
    private String formula;
    /**排序*/
    private Integer sort;
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 主表id
     */
    @Column(name = "REMAIN_TERM_ID")
    public String getRemainTermId() {
        return remainTermId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  主表id
     */
    public void setRemainTermId(String remainTermId) {
        this.remainTermId = remainTermId;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 字段1
     */
    @Column(name = "FIELD1")
    public String getField1() {
        return field1;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  字段1
     */
    public void setField1(String field1) {
        this.field1 = field1;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 字段2
     */
    @Column(name = "FIELD2")
    public String getField2() {
        return field2;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  字段2
     */
    public void setField2(String field2) {
        this.field2 = field2;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 运算符
     */
    @Column(name = "SYMBOL")
    public String getSymbol() {
        return symbol;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  运算符
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 条件
     */
    @Column(name = "CONDITIONS")
    public String getConditions() {
        return conditions;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  条件
     */
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    /**
     * 方法: 取得java.lang.Integer
     * @return java.lang.Integer 天数
     */
    @Column(name = "DAYS")
    public Integer getDays() {
        return days;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  天数
     */
    public void setDays(Integer days) {
        this.days = days;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 单位
     */
    @Column(name = "UNIT")
    public String getUnit() {
        return unit;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 逻辑关系
     */
    @Column(name = "LOGICAL_RELATIONSHIP")
    public String getLogicalRelationship() {
        return logicalRelationship;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  逻辑关系
     */
    public void setLogicalRelationship(String logicalRelationship) {
        this.logicalRelationship = logicalRelationship;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 公式
     */
    @Column(name = "FORMULA")
    public String getFormula() {
        return formula;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  公式
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }
    /**
     * 方法: 取得java.lang.Integer
     * @return java.lang.Integer 排序
     */
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
