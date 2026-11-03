package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author fangcf
 * @date 2026-4-29
 */
@Data
public class PaymentBatchResultReqVo extends BaseRequest implements Serializable {
    private String serialNoErp;// 流水号
    private String voucherNoErp;// 凭证号
}
