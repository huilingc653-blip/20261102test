package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 保证金登记簿列表页面日期区间参数
 */
@Data
public class AbPoolBailBookQueryVO implements Serializable {
    private String startDate;
    private String endDate;
}
