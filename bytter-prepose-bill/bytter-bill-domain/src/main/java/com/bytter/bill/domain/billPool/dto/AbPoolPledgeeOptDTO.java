package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 质押人启用/禁用操作参数
 */
@Data
public class AbPoolPledgeeOptDTO implements Serializable {
    private String id;
    private Integer opt;
}
