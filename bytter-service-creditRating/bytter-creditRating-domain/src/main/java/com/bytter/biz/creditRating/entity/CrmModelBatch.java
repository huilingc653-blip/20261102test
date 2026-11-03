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
@Table(name = "MODEL_BATCH", schema = "")
public class CrmModelBatch extends IdEntity implements java.io.Serializable{
    /** 批次号*/
    private String modelBatchNumber;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date createDate;

    @Column(name ="MODEL_BATCH_NUMBER")
    public String getModelBatchNumber() {
        return modelBatchNumber;
    }

    public void setModelBatchNumber(String modelBatchNumber) {
        this.modelBatchNumber = modelBatchNumber;
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
