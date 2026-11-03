package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;

import java.util.Date;

/**
 * @Title: VO
 * @Description: 评估操作日志
 * @author chenqin
 * @date 2024-08-28
 * @version 1.0
 *
 */

@Data
public class EvaluationLogVO implements java.io.Serializable {
    /**id*/
    private String id;

    /**外部引用ID(流水表ID)*/
    @BtCompositeSearch(fieldName = "el.quote_id")
    private String quoteId;

    /**日志类型*/
    private String logType;

    private String logTypeName;

    /**日志消息*/
    private String msg;

    /**创建人名称*/
    private String createName;

    /**创建人登录名称*/
    private String createBy;

    /**创建日期*/
    @BtCompositeSearch(fieldName = "el.create_date")
    private Date createDate;
}
