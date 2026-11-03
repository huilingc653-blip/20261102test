package com.bytter.biz.inneraccount.dto.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@ApiModel("内部传票凭证 Table的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccSummonsSearchDTO  implements Serializable {

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("付款方式")
    private String voucherId;

    @ApiModelProperty("凭证号")
    private String voucherNo;

    @ApiModelProperty("凭证种类")
    private String voucherType;

    @ApiModelProperty("记账日期 开始范围")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDateStart;

    @ApiModelProperty("记账日期 结束范围")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDateEnd;

    @ApiModelProperty("摘要")
    private String summaryContent;
}
