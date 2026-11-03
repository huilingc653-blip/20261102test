package com.bytter.supply.domain.receivable.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableTransferInfoGroupVO
 * @Description: 债权凭证转让信息分组VO（暂存查询返回：一对多/多对一 单对象）
 * @since 2026/8/20
 */
@ApiModel(value = "ScfReceivableTransferInfoGroupVO", description = "债权凭证转让信息分组VO")
@Data
public class ScfReceivableTransferInfoGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 转让标识：1 一对多，2 多对一 */
    @ApiModelProperty("转让标识：1 一对多，2 多对一")
    private String transferFlag;

    /** 凭证信息列表（一对多：1条凭证；多对一：多条凭证） */
    @ApiModelProperty("凭证信息列表（一对多：1条凭证；多对一：多条凭证）")
    private List<ScfReceivableVoucherInfoVO> voucherInfoList;

    /** 转让信息列表（一对多：多条接收方转让信息；多对一：1条接收方转让信息） */
    @ApiModelProperty("转让信息列表（一对多：多条接收方转让信息；多对一：1条接收方转让信息）")
    private List<ScfReceivableTransferInfoVO> transferInfoList;
}
