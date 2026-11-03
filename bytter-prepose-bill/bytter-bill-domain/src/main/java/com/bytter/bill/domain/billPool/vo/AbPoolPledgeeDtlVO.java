package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 质押人详情页面数据(permitDtl/view共用)
 */
@Data
public class AbPoolPledgeeDtlVO implements Serializable {
    private AbPoolPledgeeVO abPoolPledgeeVO;
    private Boolean flag;
}
