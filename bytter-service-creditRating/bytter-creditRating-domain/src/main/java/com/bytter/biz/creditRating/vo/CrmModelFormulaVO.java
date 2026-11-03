package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能介绍 : 模型公式<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmModelFormulaVO implements Serializable {
    private String id;
    /** 模型公式编号*/
    private String modelFormulaNumber;
    /** 模型公式名称*/
    private String modelFormulaName;
    /** 模型公式说明*/
    private String modelFormulaDescription;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelFormulaNumber() {
        return modelFormulaNumber;
    }

    public void setModelFormulaNumber(String modelFormulaNumber) {
        this.modelFormulaNumber = modelFormulaNumber;
    }

    public String getModelFormulaName() {
        return modelFormulaName;
    }

    public void setModelFormulaName(String modelFormulaName) {
        this.modelFormulaName = modelFormulaName;
    }

    public String getModelFormulaDescription() {
        return modelFormulaDescription;
    }

    public void setModelFormulaDescription(String modelFormulaDescription) {
        this.modelFormulaDescription = modelFormulaDescription;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
