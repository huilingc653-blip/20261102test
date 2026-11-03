package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 预计利息明细页面数据
 */
@Data
public class LeaseCapitalPlanPayWayVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String showSign;
    private String interestID;
    private List<LeaseCapitalPlanDelVO> list;
}
