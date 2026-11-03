package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("查询账号冻结的返回参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccFreezePageVO implements Serializable {

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("网点代码")
    private String netCode;

    @ApiModelProperty("单位代码")
    private String corpCode;

    @ApiModelProperty("操作类型")
    private String opType;

    @ApiModelProperty("账户冻结相关信息")
    private FcFreezeListVO fcFreezePage;
}
