package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 校验票据池接口代码或名称参数
 */
@Data
public class AbPoolCheckPifCodeOrPifNameDTO implements Serializable {
    private String pifCode;
    private String pifName;
    private String id;
    private String bifCode;
}
