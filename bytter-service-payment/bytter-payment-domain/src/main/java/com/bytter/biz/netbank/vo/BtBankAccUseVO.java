package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Description:账号用途维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/10/23
 */
@Data
public class BtBankAccUseVO extends ExcelModel implements Serializable {
    private String id;

    @Excel(name = "*用途编码")
    @NotNull(message = "用途编码不能为空")
    @Length(max = 32, message = "用途编码长度不能超过32")
    @BtCompositeSearch(fieldName = "t.use_code")
    private String useCode;

    @Excel(name = "*用途名称")
    @NotNull(message = "用途名称不能为空")
    @Length(max = 100, message = "用途名称长度不能超过100")
    @BtCompositeSearch(fieldName = "t.use_name")
    private String useName;
    @Excel(name = "备注")
    @BtCompositeSearch(fieldName = "t.use_rmk")
    @Length(max = 100, message = "备注长度不能超过100")
    private String useRmk;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    public String getBeanName() {
        return "btBankAccUseService";
    }
}
