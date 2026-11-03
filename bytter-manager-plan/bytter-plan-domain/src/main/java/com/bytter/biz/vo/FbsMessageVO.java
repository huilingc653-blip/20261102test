package com.bytter.biz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FbsMessageVO {
    private String id;
    /**录入时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrolDate;
    /**单据号*/
    private String billId;
    /**操作人员*/
    private String op;
    /**操作人员姓名*/
    private String opName;
    /**操作类型*/
    private Integer messageType;

    private String messageTypeName;
    /**消息*/
    private String message;
    /**业务名称 如计划上报 计划追加 计划执行*/
    private String busName;
}
