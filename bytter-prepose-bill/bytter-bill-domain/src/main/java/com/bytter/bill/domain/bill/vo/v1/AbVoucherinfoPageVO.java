package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("票据信息展示")
public class AbVoucherinfoPageVO {

    @ApiModelProperty("isPost")
    private String isPost;
    @ApiModelProperty("票据主信息Vo")
    private AbVoucherinfoVO voucherinfoVO;

    @ApiModelProperty("票据显示类型")
    private String billShowType;

    @ApiModelProperty("今日日期")
    private  Date today;

    @ApiModelProperty("票据列表")
    private List<AbVoucherinfoVO> list;

    @ApiModelProperty("账号ID")
    private String accIds;

    @ApiModelProperty("是否显示银行同步按钮 1为是0为否")
    private String isShowSyncro;

    @ApiModelProperty("是否添加附加信息")
    private String isShowAdditional;
}
