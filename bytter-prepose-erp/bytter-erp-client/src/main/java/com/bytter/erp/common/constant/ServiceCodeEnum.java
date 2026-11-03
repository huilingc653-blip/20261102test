package com.bytter.erp.common.constant;

import lombok.Getter;

/**
 * Description: 系统代码枚举
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
@Getter
public enum ServiceCodeEnum {

    BIS_ACC_BAL_QUERY("bankAccBalQuery","余额查询接口"),
    BIS_ACC_DTL_QUERY("bankAccDtlQuery","明细查询接口"),
    PAYMENT_SINGLE("paymentSingle","付款单单笔接口"),
    PAYMENT_BATCH("paymentBatch","付款单批量接口"),
    PAYMENT_SINGLE_RESULT("paymentSingleResult","付款单单笔状态查询接口"),
    PAYMENT_BATCH_RESULT("paymentBatchResult","付款单批量状态查询接口"),
    BIS_BANK_RECEIPT_QUERY("bankReceiptQuery","电子回单查询接口"),
    BIS_BANK_RECEIPT_DOWN("bankReceiptDown","电子回单文件下载接口"),
    NIS_WAGE("nisWage","代发工资同步接口"),
    NIS_WAGE_RESULT("nisWageResult","代发工资状态查询接口"),
    FILE_UPLOAD("fileUpload","文件上传接口"),
    ERP_USER_QUERY("userQuery","erp登录用户查询"),
    ERP_BANK_ACC_QUERY("bankAccQuery","银行账户查询接口"),
    ERP_BANK_INPUT_QUERY("bankInputQuery","银行联行号查询接口"),
    ERP_CORP_QUERY("corpQuery","单位信息查询接口"),
    ERP_CUR_QUERY("curQuery","币别信息查询接口"),
    ERP_EXTERNAL_CORP_QUERY("externalCorpQuery","客商信息查询接口"),
    ERP_EXTERNAL_CORP_ACC_QUERY("externalCorpAccQuery","客商账号信息查询接口"),
    ERP_ITEM_QUERY("itemQuery","预算科目查询接口"),
    ERP_REGION_QUERY("regionQuery","省市查询接口")
    ;

    private final String serviceCode;
    private final String serviceName;


    ServiceCodeEnum(String serviceCode, String serviceName) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
    }

    public static String getNameByCode(String code) {
        ServiceCodeEnum[] values = values();
        for (ServiceCodeEnum value : values) {
            if(value.serviceCode.equals(code)){
                return value.getServiceName();
            }
        }
        return null;
    }
}
