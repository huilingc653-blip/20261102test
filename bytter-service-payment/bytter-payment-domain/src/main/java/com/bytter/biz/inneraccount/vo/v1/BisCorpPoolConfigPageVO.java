package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.entity.BisCorpPoolConfig;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigRuleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("单位归集数据相关配置查询数据")
@Data
public class BisCorpPoolConfigPageVO {

    @ApiModelProperty(value = "单位归集数据相关配置")
    private BisCorpPoolConfig bisCorpPoolConfigPage;

    @ApiModelProperty(value = "根据业务id对应的所有业务规则")
    private List<BisCorpPoolConfigRuleVO> bisCorpPoolConfigRuleList;

    @ApiModelProperty(value = "所有业务规则长度")
    private Integer length;

    @ApiModelProperty(value = "产生规则编码")
    private String configCode;


}
