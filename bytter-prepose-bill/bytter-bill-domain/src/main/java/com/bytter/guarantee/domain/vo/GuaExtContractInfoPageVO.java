package com.bytter.guarantee.domain.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("对外担保合同数据获取")
public class GuaExtContractInfoPageVO {

    private GuaExtContractInfoVO vo;

    private String guaWays;

    private String conType;

    private String conId;

    private String conTypeId;

    private String relateCon;

    private String guaWayNames;

    private List<GuaContractWayVO> guaWayVOList;

    private String taskId;
}
