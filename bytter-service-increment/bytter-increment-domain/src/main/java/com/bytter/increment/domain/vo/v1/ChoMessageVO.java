package com.bytter.increment.domain.vo.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ChoMessageVO {

    private String id;
    /**录入时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date enrolDate;
    /**单据号*/
    private String billNo;
    /**操作人员*/
    private String op;
    /**操作人员姓名*/
    private String opName;
    /**messageType*/
    private Integer messageType;
    private String messageTypeName;
    /**message*/
    private String message;
    /**busType*/
    private String busType;
}
