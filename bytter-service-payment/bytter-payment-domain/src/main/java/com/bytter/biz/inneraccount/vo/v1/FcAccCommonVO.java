package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("账号数据信息公共所需数据")
@Data
public class FcAccCommonVO implements Serializable {
    @ApiModelProperty("网点代码")
    private String netCode;
    @ApiModelProperty("单位代码")
    private String corpCode;
    @ApiModelProperty("联行网点")
    private String unionNetCode;
    @ApiModelProperty("联行网点名称")
    private String unionNetName;
    @ApiModelProperty("顺序号生成模式")
    private Integer seqNumModel;
    @ApiModelProperty("生成顺序号")
    private Integer renlen;
    @ApiModelProperty("查看标识")
    private String isView;
    @ApiModelProperty("cancle标识")
    private String isFl;
    @ApiModelProperty("日志查询类型")
    private String logType;
    @ApiModelProperty("人民币币种信息")
    private BtCurrencyDTO rmbCur;
    @ApiModelProperty("全局流水号ID")
    private String busGlobalId;
    @ApiModelProperty("账号相关内容")
    private FcAccVO fcAccPage;
}
