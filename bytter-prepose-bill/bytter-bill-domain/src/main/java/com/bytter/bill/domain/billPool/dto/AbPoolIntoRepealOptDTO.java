package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 入池撤销指令状态修改请求参数
 */
@Data
public class AbPoolIntoRepealOptDTO implements Serializable {
    private String id;
    private Integer opt;
}
