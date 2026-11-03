package com.bytter.supply.domain.receivable.dto;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.supply.enums.VoucherSignEnum;
import lombok.Data;

import java.util.List;
import java.util.Date;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherBatchSignDTO
 * @Description: 批量签收状态更新DTO
 * @since 2026/7/3
 */
@Data
public class ScfReceivableVoucherBatchSignDTO {

    /**
     * 待签收凭证ID列表
     */
    private String voucherIds;

    /**
     * 目标签收状态（对应VoucherSignEnum的code值）
     */
    private VoucherSignEnum signStatus;

    /** 作废原因，仅作废操作使用 */
    private String cancelReason;
}
