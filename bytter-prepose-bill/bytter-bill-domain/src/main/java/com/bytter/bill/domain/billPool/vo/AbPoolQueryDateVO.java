package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 列表页面日期区间查询参数(提示付款/出票申请/融资申请共用)
 */
@Data
public class AbPoolQueryDateVO implements Serializable {
    private String dateStart;
    private String dateEnd;
}
