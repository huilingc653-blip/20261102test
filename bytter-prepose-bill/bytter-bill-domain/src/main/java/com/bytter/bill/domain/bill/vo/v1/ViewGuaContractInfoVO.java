package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class ViewGuaContractInfoVO implements Serializable {
    private GuaContractInfoVO vo;
    private String guaWayNames;
    private String guaWays;
    private String  taskId;

}
