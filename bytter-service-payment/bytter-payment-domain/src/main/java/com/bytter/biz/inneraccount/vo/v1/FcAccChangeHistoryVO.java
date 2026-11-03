package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.system.api.vo.FcAccHistoryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel("内部账户修改历史信息")
@Data
public class FcAccChangeHistoryVO implements Serializable {

    @ApiModelProperty("网点代码")
    private String netCode;
    @ApiModelProperty("单位代码")
    private String corpCode;
    @ApiModelProperty("联行网点账号")
    private String unionNetCode;
    @ApiModelProperty("查看标识")
    private String isView;
    @ApiModelProperty("cancle标识")
    private String isFl;
    @ApiModelProperty("日志查询类型")
    private String logType;
    @ApiModelProperty("账号修改相关内容")
    private FcAccHistoryVO fcAccPage;

}
