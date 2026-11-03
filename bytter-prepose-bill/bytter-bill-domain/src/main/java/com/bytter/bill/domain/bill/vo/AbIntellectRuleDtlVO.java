package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;
/**
 * 智能选票规则明细vo
 *
 * @author ouyj
 */
@Data
public class AbIntellectRuleDtlVO implements java.io.Serializable {
    private String id;
    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 选票要素id
     */
    private String elementId;
    /**
     * 选票要素名称
     */
    @BtCompositeSearch(fieldName = "r.element_name")
    private String elementName;
    /**
     * 上级选票要素id
     */
    private String parentId;
    /**
     * 上级选票要素名称
     */
    @BtCompositeSearch(fieldName = "r.parent_name")
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
    @BtCompositeSearch(fieldName = "r.serialNo")
    private Integer serialNo;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "r.rmk")
    private String rmk;
    /**
     * 申请单号
     */
    @BtCompositeSearch(fieldName = "r.apply_no")
    private String applyNo;
    /**
     * 值是否为选票要素 0否 1是
     */
    private String valueIsElement;
    /**
     * 数据类型:money金额  date日期 text文本 num数值
     */
    private String dataType;
    /**
     * true上移,false下移
     */
    private Boolean moveFlag;
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式 4系统常量
     */
    private String sourceType;
    /**
     * 来源类型为数据字典时，保存的字典组代码
     */
    private String dictGroup;
}
