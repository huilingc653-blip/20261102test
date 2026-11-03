package com.bytter.biz.inneraccount.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangxb on 2017/6/6.
 */
@Data
@ApiModel("账户冻结信息")
public class FcFreezeVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @BtCompositeSearch(fieldName = "fcAcc.blnctr_Acc")
    @ApiModelProperty(value = "余额账户")
    private String blnctrAcc;
    @ApiModelProperty(value = "账户名称")
    private String accName;
    @BtCompositeSearch(fieldName = "fcAcc.net_id")
    @ApiModelProperty(value = "网点ID")
    private String netId;
    @ApiModelProperty(value = "网点名称")
    private String netName;
    @BtCompositeSearch(fieldName = "fcAcc.corp_id")
    @ApiModelProperty(value = "单位ID")
    private String corpId;
    @ApiModelProperty(value = "单位名称")
    private String corpName;
    @BtCompositeSearch(fieldName = "fcAcc.cur_id")
    @ApiModelProperty(value = "币种ID")
    private String curId;
    @ApiModelProperty(value = "币种名称")
    private String curName;
    @BtCompositeSearch(fieldName = "fcAcc.freeze_Type")
    @ApiModelProperty(value = "冻结类型")
    private String freezeType;
    @ApiModelProperty(value = "冻结类型名称")
    private String freezeTypeName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private Date beginDate;
    @ApiModelProperty(value = "开始日期字符串")
    private String beginDateStr;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private Date endDate;
    @ApiModelProperty(value = "结束日期字符串")
    private String endDateStr;
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeMoney;
    @ApiModelProperty(value = "累计冻结金额")
    private BigDecimal accuFreezeMoney;
    @ApiModelProperty(value = "可用金额")
    private BigDecimal activeMoney;
    @ApiModelProperty(value = "解冻金额")
    private BigDecimal unFreezeMoney;
    @ApiModelProperty(value = "冻结编号")
    private String freezeNo;
    @ApiModelProperty(value = "冻结原因")
    private String freezeReason;
    @ApiModelProperty(value = "执行人员公务证")
    private String execOfficialNo;
    @ApiModelProperty(value = "执行人身份证")
    private String execCardNo;
    @ApiModelProperty(value = "冻结机构")
    private String freezeOrg;
    @ApiModelProperty(value = "解冻ID")
    private String unFreezeId;
    /**打回原因*/
    @ApiModelProperty(value = "打回原因")
    private String refuseWhy;
    /**冻结解除标志*/
    @ApiModelProperty(value = "冻结解除标志")
    private String freezeEndSign;
    /**冻结已提醒标志  1：已提醒；0空未提醒*/
    @ApiModelProperty(value = "冻结已提醒标志  1：已提醒；0空未提醒")
    private String deductSign;
    /**账户状态*/
    @ApiModelProperty(value = "账户状态")
    private String curAccState;
    @Dict(dictGroup = "fcfreeze.status", inject = "stateName")
    @BtCompositeSearch(fieldName = "Status")
    @ApiModelProperty(value = "状态")
    private Integer status;
    @BtCompositeSearch(fieldName = "Status")
    @ApiModelProperty(value = "状态名称")
    private String stateName;
    /**
     * 冻结(解冻)标志
     */
    @ApiModelProperty(value = "冻结(解冻)标志")
    private String freezeSign;
    @ApiModelProperty(value = "操作类型")
    private String opType;
    /**更新日期*/
    @ApiModelProperty(value = "更新日期")
    private Date updateDate;
    /**付款人账号*/
    @ApiModelProperty(value = "付款人账号")
    private String payerAccNo;
    /**操作员*/
    @ApiModelProperty(value = "操作员")
    private String freezeOperator;
    /**操作日期*/
    @ApiModelProperty(value = "操作日期")
    private Date operateTime;
    /**复核员代码*/
    @ApiModelProperty(value = "复核员代码")
    private String checkerCode;
    /**复核日期*/
    @ApiModelProperty(value = "复核日期")
    private Date checkTime;
    /**创建人登录名称*/
    @ApiModelProperty(value = "创建人登录名称")
    private String createBy;
    /**创建日期*/
    @ApiModelProperty(value = "创建日期")
    private Date createDate;
    /**更新人登录名称*/
    @ApiModelProperty(value = "更新人登录名称")
    private String updateBy;
}
