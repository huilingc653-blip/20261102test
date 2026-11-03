package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: erp付款单vo
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author fangcf
 * @date 2026-4-29
 */
@Data
public class PaymentReqVo extends BaseRequest implements Serializable {

    private String serialNoErp;// 主键流水号
    private String voucherNoErp;// ERP凭证号
    private String wishPayday;// 期望支付日期

    private String corpCode;// 单位代码
    private String corpId;
    private String payerAccId;// 付款账号id
    private String payerAccNo;// 付款账号
    private String payerBankName;// 付款方开户行
    private String payerBankCode;// 付款方行号
    private String payerAccName;// 付款方户名
    private String payerProv;// 付款方省
    private String payerCity;// 付款方市
    private String bifCode;

    private BigDecimal amt;// 金额
    private String cur;// 币别 国标代码
    private String curId;

    private String payeeAccNo;// 收款人账号
    private String payeeCorpCode;// 收款方单位代码
    private String payeeAccName;// 收款人户名
    private String payeeBank;// 收款方开户行
    private String payeeCode;// 收款人行号
    private String payeeProv;// 收款人省
    private String payeeCity;// 收款人市

    private String abs;// 用途/摘要
    private String urgencyFlag; // 加急标志
    private String voucherType;// 交易类型
    private String voucherTypeId;
    private String isforindividual;// 对公对私标识
    private String dataSource;// 来源系统标识

    private String tenantId;// 租户ID
    private String areaSign;//同城
    private String difBank;//同行
    private String bankTypeId;
}
