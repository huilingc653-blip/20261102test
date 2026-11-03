package com.bytter.biz.allocation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BisGatherPolicyVO {

    private String id;

    /**调拨策略代码*/
    @ApiModelProperty("调拨策略代码")
    private java.lang.String gatherPolicyCode;
    /**调拨策略名称*/
    @ApiModelProperty("调拨策略名称")
    private java.lang.String gatherPolicyName;
    /**策略生效日期（yyyy/MM/dd）*/
    @ApiModelProperty("策略生效日期（yyyy/MM/dd）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date defDate;
    /**策略生效时间*/
    @ApiModelProperty("策略生效时间")
    private java.lang.String launchTime;
    /**
     * 调拨类型0-定时归集，1-实时归集，2-定时下拨
     */
    @ApiModelProperty("调拨类型0-定时归集，1-实时归集，2-定时下拨")
    private String gatherType;

    private String gatherTypeName;

    /**
     * 调拨周期4-按日，8-按周，16-按月，32-按季，64-按年
     */
    @ApiModelProperty(value = "调拨周期4-按日，8-按周，16-按月，32-按季，64-按年",example = "4")
    private Integer frqMode;

    private String frqModeName;

    /**
     * 调拨日 间隔时长以天为单位周策略：选择周一至周日月策略：选择1号到31号
     */
    @ApiModelProperty(value = "调拨日 间隔时长以天为单位周策略：选择周一至周日月策略：选择1号到31号",example = "1")
    private Integer serialDay;

    /**
     * 金额上限
     */
    @ApiModelProperty("金额上限")
    private java.math.BigDecimal maxAmt;

    /**
     * 金额下限（最小执行额度）
     */
    @ApiModelProperty(value = "金额下限（最小执行额度）",example = "0")
    private java.math.BigDecimal minAmt;

    /**
     * 策略模式（归集/下拨方式）
     */
    @ApiModelProperty("策略模式（归集/下拨方式）")
    private String gatherMode;

    /**
     * 调拨金额（留底资金金额/取整单位/留底资金金额/固定上划金额/账户固定余额/下拨固定额度）
     */
    @ApiModelProperty(value = "调拨金额（留底资金金额/取整单位/留底资金金额/固定上划金额/账户固定余额/下拨固定额度）",example = "0")
    private java.math.BigDecimal gatherAmt;

    /**
     * 下拨固定额度（定时下拨，余额不足时固定额度下拨时，保存下拨固定额度）
     */
    @ApiModelProperty(value = "下拨固定额度（定时下拨，余额不足时固定额度下拨时，保存下拨固定额度）",example = "0")
    private java.math.BigDecimal downFixedAmt;

    /**
     * 下一次下拔时间
     */
    @ApiModelProperty("下一次下拔时间")
    private Date appNextTime;

    /**
     * 比例
     */
    @ApiModelProperty(value = "比例",example = "0")
    private java.math.BigDecimal differScale;

    /**
     * 加急标志 1-是 0-否
     */
    @ApiModelProperty("加急标志 1-是 0-否")
    private String isQuick;

    /**
     * 有效标志 1-有效 0-无效
     */
    @ApiModelProperty("有效标志 1-有效 0-无效")
    private String validSign;

    /**
     * 创建人登录名称
     */
    @ApiModelProperty("创建人登录名称")
    private String createBy;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 更新人登录名称
     */
    @ApiModelProperty("更新人登录名称")
    private String updateBy;

    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     * 币别
     */
    @ApiModelProperty("币别")
    private String curId;
    private String curName;

    /**
     * 智能跳过节假日
     */
    @ApiModelProperty("智能跳过节假日")
    private String autoFilterHolidays;

    /**
     * 用途
     */
    @ApiModelProperty("用途")
    private String purpose;

    /**
     * 附言
     */
    @ApiModelProperty("附言")
    private String addWord;

    /**
     * 跳过节假日规则:1,不执行,2,提前执行,3,顺延执行
     */
    @ApiModelProperty("跳过节假日规则:1,不执行,2,提前执行,3,顺延执行")
    private String autoFilterHolidaysRule;

    /**
     * 执行失败处理方式 0,反复执行至成功 1-执行一次
     */
    @ApiModelProperty("执行失败处理方式 0,反复执行至成功 1-执行一次")
    private String failHandleMethod;

    /**
     * 策略结束日期（yyyy/MM/dd）
     */
    @ApiModelProperty("策略结束日期（yyyy/MM/dd）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 策略结束时间
     */
    @ApiModelProperty("策略结束时间")
    private String endTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 关联账户ID列表（JSON格式存储）
     */
    @ApiModelProperty("关联账户ID列表（JSON格式存储）")
    private String accountIds;

    /**
     * 优先级（1-10，数字越小优先级越高）
     */
    @ApiModelProperty(value = "优先级（1-10，数字越小优先级越高）",example = "1")
    private Integer priority;

    /**
     * 版本号（用于乐观锁）
     */
    @ApiModelProperty(value = "版本号（用于乐观锁）",example = "0")
    private Integer version;
}
