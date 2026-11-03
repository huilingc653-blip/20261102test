package com.bytter.system.vo;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
/**
 * @author qinl
 * @version V1.0
 * @Title: VO
 * @Description: 账户属性表
 * @date 2019-11-15
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BisAccAttributeVO implements Serializable {
    @Dict(tableName="BIS_ACC_ATTRIBUTE",valueFieldName="id",showFiledName="ATTRIBUTE_NAME",inject="attributeName")
    @ApiModelProperty("ID")
    private String id;

    /**
     * 收支属性编码
     */
    @ApiModelProperty("收支属性编码")
    private String attributeCode;

    /**
     * 收支属性名称
     */
    @ApiModelProperty("收支属性名称")
    private String attributeName;

    @ApiModelProperty("收支属性显示名称")
    private String attributeNameShow;

    /**
     * 对外属性
     */
    @ApiModelProperty("对外属性")
    @Dict(dictGroup = "attributeDes",inject = "foreignDesName")
    private String foreignDes;
    private String foreignDesName;

    /**
     * 对同级属性
     */
    @ApiModelProperty("对同级属性")
    @Dict(dictGroup = "attributeDes",inject = "sameDesName")
    private String sameDes;
    private String sameDesName;

    /**
     * 收支属性描述
     */
    @ApiModelProperty("收支属性描述")
    private String attributeDes;

    /**
     * 对上级属性
     */
    @ApiModelProperty("对上级属性")
    @Dict(dictGroup = "attributeDes",inject = "superDesName")
    private String superDes;
    private String superDesName;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String rmk;

    /**
     * 有效标志
     */
    @ApiModelProperty("有效标志")
    @Dict(dictGroup = "valid_sign",inject = "validSignName")
    private String validSign;

    private String validSignName;

    /**
     * 创建人登录名称
     */
    @ApiModelProperty("创建人登录名称")
    private String createBy;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private java.util.Date createDate;

    /**
     * 更新人登录名称
     */
    @ApiModelProperty("更新人登录名称")
    private String updateBy;

    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")
    private java.util.Date updateDate;

}
