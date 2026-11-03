package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("协定存款新增账号数据")
@Data
public class FcAgreedDepositAddAgreedAccVO implements Serializable {

    @ApiModelProperty("开始日期")
    private String beginDate;

    @ApiModelProperty("网点信息")
    private CorpDTO netInfo;

    @ApiModelProperty("币别信息")
    private BtCurrencyDTO curInfo;

    @ApiModelProperty("单位信息")
    private CorpDTO corpInfo;

    @ApiModelProperty("账号信息")
    private FcAccDTO accInfo;

    @ApiModelProperty("协定账号信息")
    private FcAccDTO agrAccInfo;

    @ApiModelProperty("协定存款数据")
    private FcAgreedDepositVO fcAgreedDepositPage;

    @ApiModelProperty("顺序号生成模式")
    private Integer seqNumPage;

    @ApiModelProperty("账号生成规则长度")
    private Integer relen;

    @ApiModelProperty("资金池标志")
    private String poolSign;
}
