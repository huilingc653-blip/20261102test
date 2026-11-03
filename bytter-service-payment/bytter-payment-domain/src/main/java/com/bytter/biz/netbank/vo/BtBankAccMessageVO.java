package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BtBankAccMessageVO implements Serializable {
    private String id;
    /**录入时间*/
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
