package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.inneraccount.vo.BisAccDtlImportConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("离线流水智能导入配置参数返回")
@Data
public class BisAccDtlImportConfigPageVO {

    @ApiModelProperty("填输的表头信息")
    private List<Map<String, String>> headList;

    @ApiModelProperty("相关配置信息")
    private BisAccDtlImportConfigVO bisAccDtlImportConfigPage;
}
