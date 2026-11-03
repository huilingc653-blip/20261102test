package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.ChoProductReqUncollectedVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("理财产品报表数据")
public class ChoProductReqMoblieReportVO {

    private List<ChoProductReqUncollectedVo> dataList;

    private BigDecimal buyTotal;

    private BigDecimal referIncomeTotal;
}
