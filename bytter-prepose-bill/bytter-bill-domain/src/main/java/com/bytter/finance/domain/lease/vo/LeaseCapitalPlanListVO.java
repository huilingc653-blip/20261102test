package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 租金计划登记列表页面数据
 */
@Data
public class LeaseCapitalPlanListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String isSaveSign;
    private String resource;
}
