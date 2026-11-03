package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 校验票据池代码或名称参数
 */
@Data
public class AbPoolCheckBpCodeOrBpNameDTO implements Serializable {
    private String bpCode;
    private String bpName;
    private String id;
    private String pifId;
}
