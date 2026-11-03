package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*@Description 账号复核-复核数据页面
*@Author zhukr
*@Date 2026/1/6
*/

@ApiModel("账号复核-复核数据页面所需数据")
@Data
public class FcAccCheckVO implements Serializable {

    @ApiModelProperty("账号相关内容")
    private FcAccVO fcAccPage;
    @ApiModelProperty("网点代码")
    private String netCode;
    @ApiModelProperty("单位代码")
    private String corpCode;
    @ApiModelProperty("联行网点账号")
    private String unionNetCode;
    @ApiModelProperty("顺序号生成模式")
    private Integer seqNumModel;
    @ApiModelProperty("手动输入的限制长度")
    private Integer renlen;
}
