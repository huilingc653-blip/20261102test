package com.bytter.biz.netbank.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 关联方
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/17
 */
@ApiModel("NisRelatedPartyVO")
@Data
public class NisRelatedPartyVO extends ExcelModel implements Serializable {
    private String id;
    @Excel(name = "*关联方编码")
    private String relatedCode;
    @Excel(name = "*关联方名称")
    private String relatedName;
    private Integer status;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;

    @Override
    public String getBeanName() {
        return "nisRelatedPartyService";
    }
}
