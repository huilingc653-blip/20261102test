package com.bytter.erp.service;

import javax.jws.WebService;

/**
 * @author yuwj
 */
@WebService
public interface BillWsService {

    /**
     * 应付票据开票暂存
     *
     * @param params 报文
     * @return 结果
     */
    String emitSave(String params);

    /**
     * 应付票据线下兑付确认
     *
     * @param params 报文
     * @return 结果
     */
    String emitCashingConfirmation(String params);

    /**
     * 应收票据登记暂存
     * @param params 报文
     * @return 结果
     */
    String receiveBillSave(String params);

    /**
     * 应收票据背书
     * @param params 报文
     * @return 结果
     */
    String endorse(String params);

    /**
     * 应收票据质押申请
     * @param params 报文
     * @return 结果
     */
    String pledgeApply(String params);

    /**
     * 应收票据贴现申请
     * @param params 报文
     * @return 结果
     */
    String discountApply(String params);

    /**
     * 应收票据托收申请
     * @param params 报文
     * @return 结果
     */
    String collectionApply(String params);

}
