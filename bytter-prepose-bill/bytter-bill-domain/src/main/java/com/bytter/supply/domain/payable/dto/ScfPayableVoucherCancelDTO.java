package com.bytter.supply.domain.payable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 应付债权凭证作废入参
 */
@ApiModel("应付债权凭证作废DTO")
public class ScfPayableVoucherCancelDTO implements Serializable {

    @ApiModelProperty("凭证ID")
    private String id;

    @ApiModelProperty("作废原因")
    private String cancelReason;

    @ApiModelProperty("作废时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }
}
