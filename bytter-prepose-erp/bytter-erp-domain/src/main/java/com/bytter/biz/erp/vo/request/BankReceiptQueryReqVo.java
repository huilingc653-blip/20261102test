package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 电子回单查询请求参数
 *
 * @author fangcf
 * @date 2026-05-12
 */
@Data
public class BankReceiptQueryReqVo extends BaseRequest implements Serializable {
    private String bankAcc;
    private String beginDate;
    private String endDate;
    private String cdSign;
}
