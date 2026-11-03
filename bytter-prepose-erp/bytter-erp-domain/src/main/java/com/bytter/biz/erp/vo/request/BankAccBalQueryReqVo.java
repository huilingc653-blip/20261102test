package com.bytter.biz.erp.vo.request;

import lombok.Data;

/**
 * 银行账户余额查询请求参数
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Data
public class BankAccBalQueryReqVo extends BaseRequest implements java.io.Serializable {
    private String bankAcc;
    private String beginDate;
    private String endDate;
}
