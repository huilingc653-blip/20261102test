package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class EditContractInfoVO implements Serializable {
    private  String guaWays;
    private String conType;
    private String conId;
    private  String conTypeId;
    private  String relateCon;
    private  GuaContractInfoVO vo;

}
