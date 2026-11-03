package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行联行号查询请求参数。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
public class BankInputQueryRequestVO extends BaseRequest implements Serializable {

    /** 银行联行号。 */
    private String bankCode;

    /** 银行名称（支持模糊匹配）。 */
    private String bankName;

    /** 更新日期（格式：yyyy-MM-dd）。 */
    private String updateDate;

}
