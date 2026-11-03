package com.bytter.bill.domain.bill.vo;
import lombok.Data;
import java.io.Serializable;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 智能选票VO
 * @date 2022-10-12
 */
@SuppressWarnings("serial")
@Data
public class AbIntellectVO implements Serializable {
    /**
     * 规则ID
     */
    private String ruleId;
    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 有效标识 0无效 1有效
     */
    private String validSign;
    /**
     * 筛选数量
     */
    private String screenNum;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 选票要素id
     */
    private String elementId;
    /**
     * 选票要素编码
     */
    private String elementCode;
    /**
     * 选票要素名称
     */
    private String elementName;
    /**
     * 上级选票要素id
     */
    private String parentId;
    /**
     * 上级选票要素名称
     */
    private String parentName;
    /**
     * 筛选类型：sort排序 scope范围 val值
     */
    private String screenType;
    /**
     * 排序方式：asc升序 desc降序
     */
    private String sortOrder;
    /**
     * 范围方式：series系列 valSco介于值之间
     */
    private String scopeMode;
    /**
     * 值
     */
    private String value;
    /**
     * 起始值
     */
    private String startValue;
    /**
     * 结束值
     */
    private String endValue;
    /**
     * 起始值关系
     */
    private String startRelation;
    /**
     * 结束值关系
     */
    private String endRelation;
    /**
     * 关系
     */
    private String relation;
    /**
     * 顺序
     */
    private Integer serialNo;
    /**
     * 值是否为选票要素 0否 1是
     */
    private String valueIsElement;
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式
     */
    private String sourceType;
    /**
     * 来源名称
     */
    private String sourceName;
    /**
     * 数据字典
     */
    private String dictGroup;
    /**
     * 系统常量值 sysDate：系统当前日期
     */
    private String sysConstant;
    /**
     * 数据类型:money金额 date日期 text文本 num数值
     */
    private String dataType;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 计算方式：sum求和 count笔数 add相加 sub相减
     */
    private String computeMode;
    /**
     * 来源选票栈要素名称（加数/减数）
     */
    private String sourceElementName1;
    /**
     * 来源选票栈要素名称（加数/被减数）
     */
    private String sourceElementName2;
}
