package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("账户查询Table组件的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccTableSelectSearchDTO implements Serializable {

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("科目")
    private String subjectId;

    @ApiModelProperty("账号")
    private String account;

}
