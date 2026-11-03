package com.bytter.biz.corpCashier.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/11
 */
@ApiModel("NisCorpCashierVO")
@ExcelTarget("NisCorpCashierVO")
@Data
public class NisCorpCashierVO extends ExcelModel implements Serializable {
    @ApiModelProperty("ID")
    private String id;

    @BtCompositeSearch(fieldName = "c.corp_id")
    @ApiModelProperty("单位ID")
    private String corpId;        // 单位ID

    @BtCompositeSearch(fieldName = "c.corp_code")
    @ApiModelProperty("单位CODE")
    private String corpCode;      // 单位CODE

    @Excel(name = "*单位名称")
    @ApiModelProperty("单位名称")
    private String corpName;      // 单位名称

    @ApiModelProperty("状态：1-正常，0-禁用")
    private String status;        // 状态：1-正常，0-禁用

    @ApiModelProperty("创建人")
    private String createBy;      // 创建人

    @ApiModelProperty("创建时间")
    private Date createDate;      // 创建时间

    @ApiModelProperty("修改人")
    private String updateBy;      // 修改人

    @ApiModelProperty("修改时间")
    private Date updateDate;      // 修改时间

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户ID集合")
    private String userIds;

    @Excel(name = "*出纳员工编码")
    @ApiModelProperty("出纳员工编码")
    private String userCodes;

    @Override
    public String getBeanName() {
        return "nisCorpCashierService";
    }
}
