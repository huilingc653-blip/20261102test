package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("抵质押物页面数据")
public class PledgeOccupyPageVO {

    private List<GuaPledgeOccupyDTO> pledgeList;

    private String viewFlag;
}
