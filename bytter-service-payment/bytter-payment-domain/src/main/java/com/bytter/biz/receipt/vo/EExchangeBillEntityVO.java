package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.ECompareBill;
import com.bytter.biz.receipt.entity.EExchangeBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("内部结售汇电子回单")
public class EExchangeBillEntityVO {

    private EExchangeBill eExchangeBillPage;

}
