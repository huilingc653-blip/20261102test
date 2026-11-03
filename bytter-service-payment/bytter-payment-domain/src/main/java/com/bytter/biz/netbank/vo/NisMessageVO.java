package com.bytter.biz.netbank.vo;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: NIS_MESSAGE
 * @date 2017-06-12 14:53:20
 *
 */
@Data
public class NisMessageVO  implements java.io.Serializable {

    private String id;
    /**
     * 录入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date enrolDate;
    /**
     * 单据号
     */
    private String billNo;
    /**
     * 操作人员
     */
    private String op;
    /**
     * 操作人员姓名
     */
    private String opName;
    /**
     * messageType
     */
    private Integer messageType;
    /**
     * message
     */
    private String message;
    /**
     * busType
     */
    private String busType;

    private String messageTypeName;
}
