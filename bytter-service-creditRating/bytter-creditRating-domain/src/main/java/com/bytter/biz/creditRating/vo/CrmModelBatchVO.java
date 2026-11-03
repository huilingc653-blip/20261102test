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
public class CrmModelBatchVO implements Serializable {
    private String id;
    /** 批次号*/
    private String modelBatchNumber;
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

    public String getModelBatchNumber() {
        return modelBatchNumber;
    }

    public void setModelBatchNumber(String modelBatchNumber) {
        this.modelBatchNumber = modelBatchNumber;
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
