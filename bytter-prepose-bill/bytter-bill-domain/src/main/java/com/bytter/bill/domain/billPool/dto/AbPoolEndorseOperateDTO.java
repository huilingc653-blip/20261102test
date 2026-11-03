package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 背书申请操作参数
 */
@Data
public class AbPoolEndorseOperateDTO implements Serializable {
    private String pdId;
    private String opt;
}
