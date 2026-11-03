package com.bytter.biz.payment.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("post请求Ids接收器")
public class BisIDsDTO {

    private String ids;

    private Integer status;
}
