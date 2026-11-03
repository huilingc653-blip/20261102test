package com.bytter.credit.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class RatYearFinPlanRegisterVO extends ExcelModel implements Serializable, IBusinessKey {

    private String id;
    /**
     * 单位id
     */
    @BtCompositeSearch(fieldName = "a.CORP_ID")
    private String corpId;
    /**
     * 单位名称
     */
    @Excel(name = "*单位名称")
    private String corpName;
    /**
     * 开户行名称
     */
    @Excel(name = "*年度")
    @BtCompositeSearch(fieldName = "a.YEARS")
    private String years;
    /**
     * 年采购量
     */
    @Excel(name = "*年采购量")
    private BigDecimal yearPurchaseAmount;
    /**
     * 并购投资
     */
    @Excel(name = "*并购投资")
    private BigDecimal mergeInvestmentAmount;
    /**
     * 解决措施
     */
    @Excel(name = "解决措施")
    private String solveMeasure;
    /**
     * 存在的困难
     */
    @Excel(name = "存在的困难")
    private String existDifficulty;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createDate;

    @Override
    public String getBusinessKey() {
        return getId();
    }

    @Override
    public String getBeanName() {
        return "ratYearFinPlanRegisterService";
    }
}
