package com.bytter.biz.bdlinks.vo.v1;

import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.dto.NisExpenseDetailDTO;
import com.bytter.biz.payment.api.dto.NisWageDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("付款指令数据查询")
public class BisExcViewPayResultPageVO implements Serializable {

    @ApiModelProperty("付款指令页面信息")
    private BisExcVO bisExcPage;

    @ApiModelProperty("交易状态")
    private String voucherStat;

    @ApiModelProperty("指令数据来源：0-bis_Exc 1-bis_Exc_Proc")
    private String excFrom;

    @ApiModelProperty("是否拥有费用报销明细")
    private String hasExpenseDetail;
    @ApiModelProperty("费用报销明细")
    private List<NisExpenseDetailDTO> listNisExpenseDetail;

    @ApiModelProperty("付款单信息")
    private NisBillheadDTO nisBillheadPage;

    @ApiModelProperty("代发工资等待结果的明细")
    private List<NisWageDetailDTO> listNisWageDetail;
}
