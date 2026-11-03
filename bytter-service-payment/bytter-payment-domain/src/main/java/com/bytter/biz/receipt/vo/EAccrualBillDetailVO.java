package com.bytter.biz.receipt.vo;

import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 电子回单查询
 */
@ApiModel("EAccrualBillDetailVO")
@Data
public class EAccrualBillDetailVO implements Serializable {
    private List<FcInterestDtlDTO> dtlDTOList;
    private EVisaTypeVO eVisaTypePage;
    //操作类型：0-打印预览 1-打印
    private String opType;
    private EAccrualBillVO eAccrualBillPage;

}
