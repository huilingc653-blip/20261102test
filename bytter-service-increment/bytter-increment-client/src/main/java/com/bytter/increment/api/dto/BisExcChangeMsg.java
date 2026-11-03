package com.bytter.increment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 指令变更消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BisExcChangeMsg implements Serializable {

    /**
     * 付款单号
     */
    private String cbsBillId;

    /**交易状态*/
    private String voucherStat;
    /**
     * 收/付款指令来源
     */
    private String voucherFrom;


}
