package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("银行账户变更查询数据")
@Data
public class BisCorpPoolBalPageVO {

    @ApiModelProperty(value = "单位归集数据")
    private BisCorpPoolBalVO bisCorpPoolBalPage;
}
