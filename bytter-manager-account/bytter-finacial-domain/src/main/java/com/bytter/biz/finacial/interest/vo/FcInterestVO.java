package com.bytter.biz.finacial.interest.vo;

import lombok.Data;

/**
 * @author luoyj
 * @date 2024/1/24.
 */
@Data
public class FcInterestVO {
    //account,tt.interest,led.ACC_NAME accName,fc.ACC_FOR_DEDUCT accForDeduct,led.check_balance balance
    private String account;
    private String interest;
    private String accName;
    private String accForDeduct;
    private String balance;
}
