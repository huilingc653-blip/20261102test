package com.bytter.bill.api.scf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @description TODO
 * @since 2026/8/21
 */
@Data
public class ScfReceivableTransferReceiveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private String id;

    /** 接收单位 */
    @ApiModelProperty("接收单位")
    private String receiveCompany;

    /** 接收单位id */
    @ApiModelProperty("接收单位id")
    private String receiveCompanyId;

    /** 接收方统一社会信用代码 */
    @ApiModelProperty("接收方统一社会信用代码")
    private String receiverCreditCode;

    /** 转让日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("转让日期")
    private Date transferDate;

    /** 转让金额 */
    @DecimalMin(value = "0.00", inclusive = false, message = "转让金额必须大于0")
    @ApiModelProperty("转让金额")
    private BigDecimal transferAmt;

    /** 用途 */
    @ApiModelProperty("用途")
    private String purpose;

    /** 备注 */
    @ApiModelProperty("备注")
    private String remark;
}
