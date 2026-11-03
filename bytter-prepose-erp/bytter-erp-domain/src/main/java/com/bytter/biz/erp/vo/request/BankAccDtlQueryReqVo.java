package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行明细查询请求参数
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Data
public class BankAccDtlQueryReqVo extends BaseRequest implements Serializable {
    private String bankAcc;
    private String beginDate;
    private String endDate;
    private String cdSign;
}
