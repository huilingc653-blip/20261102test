package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.ChoProductRedeemDetailVO;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("理财相关产品")
public class ChoProductRedeemPageVO {

    private ChoProductRedeemVo choProductRedeemPage;

    private List<ChoProductRedeemDetailVO> list;

    private String busType;

}
