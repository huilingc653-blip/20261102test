package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EExchangeBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("内部结售汇电子回单")
public class EExchangeBillPageVO {

    private String opType;

    private EExchangeBill eExchangeBillPage;

}
