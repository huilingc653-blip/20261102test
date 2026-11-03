package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 出池申请指令处理修改请求参数
 */
@Data
public class AbPoolOutApplyOptDTO implements Serializable {
    private String id;
    private Integer opt;
}
