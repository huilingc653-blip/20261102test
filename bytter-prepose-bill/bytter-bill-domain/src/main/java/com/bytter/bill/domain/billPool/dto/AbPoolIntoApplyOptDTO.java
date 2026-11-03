package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 入池申请指令状态修改请求参数
 */
@Data
public class AbPoolIntoApplyOptDTO implements Serializable {
    private String id;
    private Integer opt;
}
