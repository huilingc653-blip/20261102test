package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.entity.ChoProduct;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import lombok.Data;

@Data
public class ChoProductReqPageVO {

    private String busType;

    private ChoProductReqVo choProductReqPage;
}
