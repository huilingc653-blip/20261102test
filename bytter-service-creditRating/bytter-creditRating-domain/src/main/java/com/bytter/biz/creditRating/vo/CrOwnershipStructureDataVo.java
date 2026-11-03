package com.bytter.biz.creditRating.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CrOwnershipStructureDataVo extends ExcelModel implements Serializable {

    /**
     * 伪序号
     */
    private Integer rowNum;

    private String id;
    /**
     * 出资人
     */
    @Excel(name = "出资人")
    private String investor;
    /**
     * 融资方式 1-转让；2-增资
     */
    @Excel(name = "融资方式")
    private String financingWay;

//    @Dict(dictGroup = "cms.equity.financingWay", inject = "financingWayName")
    private String financingWayName;
    /**
     * 出资方式:1-现金；2-实物资产；
     */
    @Excel(name = "出资方式")
//    @Dict(dictGroup = "cms.equity.invWay", inject = "investmentMethodName")
    private String investmentMethod;

    private String investmentMethodName;
    /**
     * 出资金额
     */
    @Excel(name = "出资金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal contributionAmount;
    /**
     * 持股比例
     */
    @Excel(name = "持股比例%", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal shareholdingRatio;
    private String shareholdingRatioStr;
    /**
     * 资产名称
     */
    @Excel(name = "资产名称")
    private String assetName;
    /**
     * 外键（结构原始数据）
     */
    private String institutionalRawDataId;

    @Excel(name = "备注")
    private String rmk;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间:yyyy-mm-dd hh24:mm:ss
     */
    private Date createDate;

    @Override
    public String getBeanName() {
        return "crOwnershipStructureDataService";
    }
}
