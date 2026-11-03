package com.bytter.biz.vo;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.vo.FbsBudgetProcessCommonVO;
import com.bytter.biz.budget.vo.FbsBudgetProjectDetailVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FbsBudgetProcessPageVO {
    @ApiModelProperty(value = "返回信息")
    private String msg;

    @ApiModelProperty(value = "预算执行列表")
    private List<FbsBudgetProcessCommonVO> listProcess;

    @ApiModelProperty(value = "是否为RMB")
    private Boolean isRMB;

    @ApiModelProperty(value = "预算信息")
    private FbsBudget fbsBudget;

    private FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO;

    @ApiModelProperty(value = "明细项控制模式")
    private String dtlColSel;

    @ApiModelProperty(value = "预算状态")
    private Integer status;

    @ApiModelProperty(value = "原系统页面跳转")
    private String  forwardUrl;
}
