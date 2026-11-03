package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("外部定价限额VO")
public class GuaExguaranteeAmountPageVO implements Serializable {

    private GuaExguaranteeAmountVO amountVO;
}
