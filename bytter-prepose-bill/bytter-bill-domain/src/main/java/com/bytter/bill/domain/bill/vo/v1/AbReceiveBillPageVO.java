package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 应收票据登记新增/编辑页面数据
 *
 * @author onlineGenerator
 * @date 2026-08-27
 * @version 1.0
 */
@Data
@ApiModel("应收票据登记页面数据")
public class AbReceiveBillPageVO {

    @ApiModelProperty("收票方式系统参数")
    private String recItem;

    @ApiModelProperty("纸票票据金额最大额度")
    private BigDecimal maxBillMoney;

    @ApiModelProperty("人民币币种ID")
    private String curId;

    @ApiModelProperty("应收票据信息")
    private AbReceiveBillVO abReceiveBillPage;

    @ApiModelProperty("是否补录或对内背书业务完成后自动收票登记")
    private Boolean insideSing6101or010;

    @ApiModelProperty("业务名称")
    private String businessName;

    @ApiModelProperty("流程系统名")
    private String processSysname;

    @ApiModelProperty("附件信息")
    private AbBillAttachmentVO attachmentInfo;
}
