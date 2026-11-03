package com.bytter.biz.payment.api.dto;

import com.bytter.core.annotation.Dict;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统结算方式。
 *
 * @author zhaohc
 * @since 2026/4/10 10:51
 */
@Data
public class BtPayTypeDTO implements Serializable {

    @Dict(tableName="BT_PAY_TYPE",valueFieldName="id",showFiledName="SYS_VOUCHER_NAME",inject="sysVoucherName")
    private String id;
    /**结算方式编码*/
    private String sysVoucherCode;
    /**结算方式名称*/
    private String sysVoucherName;
    /**备注*/
    private String rmk;
    /**有效标志*/
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    private String validSign;
    /**有效标志*/
    private String validSignName;

}
