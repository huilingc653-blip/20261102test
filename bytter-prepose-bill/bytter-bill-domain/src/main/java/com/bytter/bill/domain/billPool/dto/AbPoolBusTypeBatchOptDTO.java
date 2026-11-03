package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务类型批量启用/禁用参数
 */
@Data
public class AbPoolBusTypeBatchOptDTO implements Serializable {
    private String ids;
    private Integer opt;
}
