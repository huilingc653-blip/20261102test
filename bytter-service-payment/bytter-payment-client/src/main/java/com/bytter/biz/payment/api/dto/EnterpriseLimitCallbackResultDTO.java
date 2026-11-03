package com.bytter.biz.payment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 企业端限额审批回调处理结果。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseLimitCallbackResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private boolean duplicate;
    private String code;
    private String message;

    public static EnterpriseLimitCallbackResultDTO success(boolean duplicate) {
        return new EnterpriseLimitCallbackResultDTO(true, duplicate, duplicate ? "DUPLICATE" : "SUCCESS",
                duplicate ? "重复事件已幂等处理" : "处理成功");
    }
}
