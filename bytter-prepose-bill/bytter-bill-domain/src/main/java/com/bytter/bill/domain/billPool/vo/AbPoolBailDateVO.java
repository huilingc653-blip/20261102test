package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 保证金存取列表页面数据(存入/提取共用)
 */
@Data
public class AbPoolBailDateVO implements Serializable {
    private String appDateStart;
    private String appDateEnd;
}
