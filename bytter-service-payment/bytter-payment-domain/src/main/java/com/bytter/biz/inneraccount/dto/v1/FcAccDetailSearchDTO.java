package com.bytter.biz.inneraccount.dto.v1;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("内部账户流水查询参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccDetailSearchDTO implements Serializable {

    @ApiModelProperty("账户类别")
    private String accType;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("凭证种类代码(付款方式)")
    private String voucherType;

    @ApiModelProperty("记账日期 开始范围")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDateStart;

    @ApiModelProperty("记账日期 结束范围")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDateEnd;

    @ApiModelProperty("金额 最小值范围")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal minMoney;

    @ApiModelProperty("金额 最大值范围")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal maxMoney;

    @ApiModelProperty("外单位名称")
    private String externalName;

    @ApiModelProperty("摘要")
    private String summaryContent;





}
