package com.bytter.guarantee.domain.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("担保合同数据获取")
public class GuaContractInfoPageVO {

    private GuaContractInfoVO info;

    private String guaWays;

    private String conType;

    private String conId;

    private String conTypeId;

    private String relateCon;

    private String guaWayNames;

    private List<GuaContractWayVO> guaWayVOList;

    private String taskId;
}
