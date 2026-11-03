package com.bytter.biz.receipt.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author pzc  2025/09/09
 * @since 1.0.0
 */
@Data
@ToString
public class BisRunBlendingVO {
    private String matchingRule;
    private String transTimeRule;
    private String transDays;
    private String ged_tradeTime;
    private String led_tradeTime;
}
