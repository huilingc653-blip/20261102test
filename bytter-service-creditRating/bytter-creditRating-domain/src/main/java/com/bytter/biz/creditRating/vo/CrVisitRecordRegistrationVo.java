package com.bytter.biz.creditRating.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.Serializable;
import java.util.Date;

@Data
public class CrVisitRecordRegistrationVo extends ExcelModel implements Serializable {

    private Integer rowNum;

    private String id;
    /**
     * 拜访事项
     */
    @Excel(name = "拜访事项")
    private String visitingMatters;
    /**
     * 拜访人员
     */
    @Excel(name = "拜访人员")
    private String visitingPersonnel;
    /**
     * 拜访对象
     */
    @Excel(name = "拜访对象")
    private String visitingObj;
    /**
     * 拜访对象职务
     */
    @Excel(name = "拜访对象职务")
    private String visitingObjJob;
    /**
     * 拜访日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel(name = "拜访日期", align = HorizontalAlignment.CENTER,databaseFormat = "yyyy-MM-dd")
    private Date visitDate;

    private String visitDateStr;
    /**
     * 外键（结构原始数据）id
     */
    private String institutionalRawDataId;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期
     */
    private Date createDate;

    @Override
    public String getBeanName() {

        return "crVisitRecordRegistrationService";
    }
}
