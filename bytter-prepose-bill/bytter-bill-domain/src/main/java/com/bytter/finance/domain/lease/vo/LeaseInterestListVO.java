package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 预计利息明细返回 VO
 * 旧接口：/cms/lease/leaseinterest/list
 */
@Data
public class LeaseInterestListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 显示标识 0查看.1带选择
     */
    private String showSign;

    private List<LeaseInterestVo> list;
}
