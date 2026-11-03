package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 智能选票-选票要素VO
 * @date 2022-10-10
 */
@SuppressWarnings("serial")
@Data
public class AbIntellectElementVO implements java.io.Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 选票要素编码
     */
    @BtCompositeSearch(fieldName = "t.element_code")
    private String elementCode;
    /**
     * 选票要素名称
     */
    @BtCompositeSearch(fieldName = "t.element_name")
    private String elementName;
    /**
     * 有效标识 0无效 1有效
     */
    @BtCompositeSearch(fieldName = "t.valid_sign")
    private String validSign;
    /**
     * 来源类型：1数据字典 2数据字段 3计算公式 4系统常量
     */
    private String sourceType;
    /**
     * 系统常量值 sysDate：系统当前日期
     */
    private String sysConstant;
    /**
     * 来源名称
     */
    private String sourceName;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "t.rmk")
    private String rmk;
    /**
     * 数据字典
     */
    private String dictGroup;
    /**
     * 字典名
     */
    private String dictGroupName;
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
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
}
