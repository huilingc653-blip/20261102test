package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.util.Date;

/**
 * 付款通知查询数据。
 *
 * @author zhaohc
 * @since 2024/7/25 19:59
 */
@Data
public class NisBillDataVo {

    private String billId;
    private String payId;
    private String dtlId;
    private Date transTime;
    private Date businessDate;
}
