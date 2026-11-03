package com.bytter.system.api.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@ApiModel("增添新子账户传参")
public class BtBankAccRelationAddDTO {


    @ApiModelProperty(value = "子账户IDs")
    private String ids;

    @ApiModelProperty(value = "父账户ID")
    private String prentAccId;

    @ApiModelProperty(value = "币别")
    private String curId;
}
