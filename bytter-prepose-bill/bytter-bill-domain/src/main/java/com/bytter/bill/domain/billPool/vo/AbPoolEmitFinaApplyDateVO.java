package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资申请列表页面日期区间参数
 */
@Data
public class AbPoolEmitFinaApplyDateVO implements Serializable {
    private String dateStart;
    private String dateEnd;
}
