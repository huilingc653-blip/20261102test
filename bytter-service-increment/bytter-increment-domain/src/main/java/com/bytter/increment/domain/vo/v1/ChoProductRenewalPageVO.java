package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("理财续期")
public class ChoProductRenewalPageVO {

    private ChoProductRedeemVo choProductRedeemPage;

    private String busType;
}
