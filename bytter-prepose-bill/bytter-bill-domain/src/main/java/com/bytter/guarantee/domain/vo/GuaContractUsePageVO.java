package com.bytter.guarantee.domain.vo;

import com.bytter.system.api.dto.BtCurrencyDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("被担保合同使用登记数据获取")
public class GuaContractUsePageVO {

    private GuaContractInfoVO vo;

    private String guaWays;

    private String guaWayNames;

    private BtCurrencyDTO currencyDTO;
}
