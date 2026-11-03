package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 租赁物新增/修改/查看页返回 VO
 */
@Data
public class LeaseObjectInfoPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String leaseId;

    private LeaseObjectInfoVO leaseObjectInfoVO;
}
