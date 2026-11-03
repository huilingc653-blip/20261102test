package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 贴现申请操作参数
 */
@Data
public class AbPoolDiscountOperateDTO implements Serializable {
    private String pdId;
    private String opt;
}
