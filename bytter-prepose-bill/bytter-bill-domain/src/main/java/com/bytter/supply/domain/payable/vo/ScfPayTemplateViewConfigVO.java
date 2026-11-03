package com.bytter.supply.domain.payable.vo;

import com.bytter.supply.domain.payable.dto.ScfPayProductDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("应付债权模板视图配置VO")
public class ScfPayTemplateViewConfigVO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 模版版本ID
     */
    @ApiModelProperty("模版版本ID")
    private String versionId;

    @ApiModelProperty("模板编码")
    private String templateCode;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板描述")
    private String templateDesc;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("有效标志")
    private String validSign;

    @ApiModelProperty("产品列表")
    private List<ScfPayProductDTO> productList;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("修改时间")
    private Date updateDate;

}
