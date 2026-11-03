package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 电票签收继续/中止操作参数
 */
@Data
public class AbPoolSignOperateDTO implements Serializable {
    private String pdId;
    private String opt;
}
