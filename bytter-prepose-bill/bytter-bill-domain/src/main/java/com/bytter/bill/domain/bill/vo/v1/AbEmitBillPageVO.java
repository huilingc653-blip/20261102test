package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.credit.domain.vm.RatBkrationCommonDTO;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("查询开票登记详情")
public class AbEmitBillPageVO {


    @ApiModelProperty("票据信息")
    private AbEmitBillVo abEmitBillVo;

    @ApiModelProperty("保证金利率")
    private BigDecimal fcBondRate;

    @ApiModelProperty("日志列表")
    private List<AbLog> abLogList;

    @ApiModelProperty("票据订单id")
    private String billId;

    @ApiModelProperty("预算科目")
    private String payItem;

    @ApiModelProperty("执行业务名称：emit-开票")
    private String businessName;

    @ApiModelProperty("执行系统名称：ab-票据管理")
    private String processSysname;

    @ApiModelProperty("显示预算")
    private Boolean budgetDisplay;

    @ApiModelProperty("挂授信 显示")
    private Boolean isEnabled;

    @ApiModelProperty(" 挂接保证金")
    private String bailmodel;

    @ApiModelProperty(" 是否启用担保参数")
    private String isHangGuarantee;

    @ApiModelProperty(" 开票登记单号")
    private String appNo;

    @ApiModelProperty(" 挂内部授信是否显示")
    private Boolean cpIsEnabled;

    @ApiModelProperty(" 挂内部授信")
    private RatUrationDTO cpURationDTO;

    @ApiModelProperty("银行授信相关信息")
    private RatBkrationCommonDTO ratBkrationPage;

    @ApiModelProperty("保证金信息")
    private FcBondApplyDTO fcBondApplyDTO;

    @ApiModelProperty("设置挂接担保")
    private GuaContractUseDTO guaContractUseDTO;

    @ApiModelProperty(" 币别ID")
    private String curId;

    private AbBillAttachmentVO attachmentInfo;

    private AbBillAttachmentVO attachmentInfoDiscount;

    private Boolean isAttachmentShow;

    private String bankTypeName;

    private AbVoucherinfoVO busInfo;


}
