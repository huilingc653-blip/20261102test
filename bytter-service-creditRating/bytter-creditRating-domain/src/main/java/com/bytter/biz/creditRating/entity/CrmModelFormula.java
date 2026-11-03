package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 功能介绍 : 模型公式<br>
 * <b>日期：</b>2024/8/8<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "MODEL_FORMULA", schema = "")
public class CrmModelFormula extends IdEntity implements java.io.Serializable{
    /** 模型公式编号*/
    private String modelFormulaNumber;
    /** 模型公式名称*/
    private String modelFormulaName;
    /** 模型公式说明*/
    private String modelFormulaDescription;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date  createDate;

    @Column(name ="MODEL_FORMULA_NUMBER")
    public String getModelFormulaNumber() {
        return modelFormulaNumber;
    }

    public void setModelFormulaNumber(String modelFormulaNumber) {
        this.modelFormulaNumber = modelFormulaNumber;
    }

    @Column(name ="MODEL_FORMULA_NAME")
    public String getModelFormulaName() {
        return modelFormulaName;
    }

    public void setModelFormulaName(String modelFormulaName) {
        this.modelFormulaName = modelFormulaName;
    }

    @Column(name ="MODEL_FORMULA_DESCRIPTION")
    public String getModelFormulaDescription() {
        return modelFormulaDescription;
    }

    public void setModelFormulaDescription(String modelFormulaDescription) {
        this.modelFormulaDescription = modelFormulaDescription;
    }

    @Column(name ="CREATOR")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
