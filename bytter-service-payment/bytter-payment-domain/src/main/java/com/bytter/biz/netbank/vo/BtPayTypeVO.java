package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Title: vo
 * @description: 系统结算方式
 * @author: qijh
 * @create: 2019-11-28 17:45
 **/
@ApiModel("系统结算方式")
@Data
public class BtPayTypeVO implements  java.io.Serializable{
    @ApiModelProperty("id")
    @Dict(tableName="BT_PAY_TYPE",valueFieldName="id",showFiledName="SYS_VOUCHER_NAME",inject="sysVoucherName")
    private String id;
    /**结算方式编码*/
    @ApiModelProperty("结算方式编码")
    private java.lang.String sysVoucherCode;
    /**结算方式名称*/
    @ApiModelProperty("结算方式名称")
    private java.lang.String sysVoucherName;
    /**备注*/
    @ApiModelProperty("备注")
    private java.lang.String rmk;
    /**有效标志*/
    @ApiModelProperty("有效标志")
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    private java.lang.String validSign;

    /**有效标志*/
    @ApiModelProperty("有效标志")
    private java.lang.String validSignName;



}
