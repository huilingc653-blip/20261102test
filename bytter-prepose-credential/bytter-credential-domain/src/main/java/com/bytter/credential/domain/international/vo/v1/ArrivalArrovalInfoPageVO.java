package com.bytter.credential.domain.international.vo.v1;

import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("加载收到信用证的INFO信息")
public class ArrivalArrovalInfoPageVO {

    private LcBillArrivalVO arrival;

    private String creditNo;

    private String lcInter;

    private String flag;

    private LcEnrol lcEnrol;
}
