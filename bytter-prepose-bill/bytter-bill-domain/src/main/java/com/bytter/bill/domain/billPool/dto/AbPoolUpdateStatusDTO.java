package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改票据池状态参数
 */
@Data
public class AbPoolUpdateStatusDTO implements Serializable {
    private String id;
    private String status;
}
