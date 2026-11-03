package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
/**
 * @ClassName BiRemainTermDetail
 * @Description 剩余期限维护详细表
 * @Author fusb
 * @Date 2020/1/9 9:22
 * @Version 1.0
 **/
public class BiRemainTermDetailVO implements Serializable {
    /**主键id*/
    private String id;
    /**CMS_REMAIN_TERM表id*/
    private String remainTermId;
    /**字段1:"到期日期"2*/
    @Dict(dictGroup="remainTerm.field",inject="field1Name")
    private String field1;
    /**字段1名称*/
    private String field1Name;
    /**字段2*/
    @Dict(dictGroup="remainTerm.field",inject="field2Name")
    private String field2;
    /**字段2:"系统日期"1*/
    private String field2Name;
    /**运算符:"+"1,"-"2,"*"3,"/"4*/
    @Dict(dictGroup="remainTerm.symbol",inject="symbolName")
    private String symbol;
    /**运算符名称*/
    private String symbolName;
    /**条件:">"1,"<"2,"="3,"<="4,">="4,">="5,"≠"6*/
    @Dict(dictGroup="remainTerm.condition",inject="conditionName")
    private String conditions;
    /**条件名称*/
    private String conditionName;
    /**天数*/
    private Integer days;
    /**单位:"天"D,"月"M,"年"Y*/
    @Dict(dictGroup="remainTerm.unit",inject="unitName")
    private String unit;
    /**单位名称*/
    private String unitName;
    /**逻辑关系:"或者"1,"并且"2*/
    @Dict(dictGroup="remainTerm.logicalRelationship",inject="logicalRelationshipName")
    private String logicalRelationship;
    /**逻辑关系名称*/
    private String logicalRelationshipName;
    /**公式*/
    private String formula;
    /**排序*/
    private Integer sort;
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 主表id
     */
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getField1Name() {
        return field1Name;
    }
    public void setField1Name(String field1Name) {
        this.field1Name = field1Name;
    }
    public String getField2Name() {
        return field2Name;
    }
    public void setField2Name(String field2Name) {
        this.field2Name = field2Name;
    }
    public String getSymbolName() {
        return symbolName;
    }
    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }
    public String getConditionName() {
        return conditionName;
    }
    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }
    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    public String getLogicalRelationshipName() {
        return logicalRelationshipName;
    }
    public void setLogicalRelationshipName(String logicalRelationshipName) {
        this.logicalRelationshipName = logicalRelationshipName;
    }
}
