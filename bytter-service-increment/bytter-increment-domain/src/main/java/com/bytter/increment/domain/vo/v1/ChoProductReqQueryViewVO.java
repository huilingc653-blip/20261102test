package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.entity.ChoProductRenewal;
import com.bytter.increment.domain.vo.ChoProductRedeemDetailVO;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("理财产品查询详情")
public class ChoProductReqQueryViewVO {

    private ChoProductReqVo choProductReqPage;

    private List<ChoProductRedeemDetailVO> list;

    private List<ChoProductRenewal> renewalList;

    private List<ChoProductNetVal> netValList;

    private String busType;
}
