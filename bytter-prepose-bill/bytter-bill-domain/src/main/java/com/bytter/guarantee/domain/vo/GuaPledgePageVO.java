package com.bytter.guarantee.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("抵质押物数据")
public class GuaPledgePageVO {
    private GuaPledgeInfoVO guaPledge;
    private String flag;
    private String operator;
}
