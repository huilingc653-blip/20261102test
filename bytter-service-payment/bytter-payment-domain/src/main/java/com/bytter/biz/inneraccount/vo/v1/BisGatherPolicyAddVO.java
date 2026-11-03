package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author zhengzf
 * @Description: 调拨策略表-添加页面所需数据
 * @date 2026-01-04 15:02:30
 */
@ApiModel("调拨策略表-添加页面所需数据")
@Data
public class BisGatherPolicyAddVO implements Serializable {
    @ApiModelProperty("获取树形表格-增加父账号筛选")
    private BisGatherPolicy bisGatherPolicyPage;
    @ApiModelProperty("获取树形表格-增加父账号筛选")
    private String defDate;
}
