package com.bytter.system.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FcInstrateAdjustSearchDTO implements Serializable {
    private Integer status;
    private String curId;
    private String instrtType;
    private Integer netId;
    private String subjectId;
    private String isCheck;
    private String poolSign;
    private String account;
}
