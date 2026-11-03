package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 提示付款列表页面日期区间参数
 */
@Data
public class AbPoolCollectionApplyDateVO implements Serializable {
    private String dateStart;
    private String dateEnd;
}
