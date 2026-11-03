package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.ParamsVO;
import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("抵质押物占用添加页面数据")
public class PledgeOccupyAddPageVO {

    private List<GuaPledgeInfoVO> list;

    private boolean relateFlag;

    private ConfigDTO config;

    private ParamsVO vo;
}
