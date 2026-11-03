package com.bytter.biz.finacial.api.service;

import com.bytter.biz.finacial.api.vo.EAccbookBlnedVo;
import com.bytter.biz.finacial.api.vo.ELoanBillInfoVo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IReceiptQueryService {
    /**
     * 根据参数查询记账信息
     *
     * @param params
     * @return
     */
    List<EAccbookBlnedVo> listAccbookBlneInfo(Map<String, Object> params);

    /**
     * 根据参数查询记账信息
     *
     * @param orderNo
     * @return
     */
    EAccbookBlnedVo getEAccbookBlnedInfo(String orderNo);

    /**
     * 获取结售汇非冲账数据数据
     *
     * @param params
     * @return
     */
    List<EAccbookBlnedVo> listAccbookBlneExchangeInfo(Map<String, Object> params);

    /**
     * 获取结售汇冲账数据数据
     *
     * @param params
     * @return
     */
    List<EAccbookBlnedVo> listAccbookBlneRushExchangeInfo(Map<String, Object> params);

    /**
     * 委托贷款利息清单数据数据
     *
     * @param params
     * @return
     */
    List<EAccbookBlnedVo> lisInterestInfo(Map<String, Object> params);

    /**
     * 已结流水信息
     *
     * @param params
     * @return
     */
    List<EAccbookBlnedVo> listInterestOthersInfo(Map<String, Object> params);

    /**
     * 根据凭证流水和账号获取记账信息
     *
     * @param orderNo  凭证流水
     * @param accounts 内部账号
     * @return
     */
    List<ELoanBillInfoVo> listAccbookBlnedAcc(String orderNo, String[] accounts);

    /**
     * 根据凭证流水和账号获取记账信息
     *
     * @param orderNo  凭证流水
     * @param accounts 内部账号
     * @return
     */
    List<ELoanBillInfoVo> listAccbookBlnedAcc(String orderNo, String[] accounts, boolean debitMoneyNot0, boolean creditMoneyNot0);

    /**
     * 根据凭证流水和账号获取记账信息
     *
     * @param orderNo  凭证流水
     * @param accounts 内部账号
     * @return hashmap
     */
    HashMap<String, List<ELoanBillInfoVo>> listAccbookBlnedAccToHashMap(List<String> orderNo, String[] accounts,
                                                                        boolean debitMoneyNot0, boolean creditMoneyNot0);

    BigDecimal getCountCompareBalanceInvalid(Map<String,Object> params);

    List<ELoanBillInfoVo> getCompareAccBalance(Map<String,Object> params);

    List<ELoanBillInfoVo> listFcAccbookBlnedSubjectByOrderNo(String orderNo);
    List<ELoanBillInfoVo> listFcAccbookBlnedSubjectBybusGlobalId(String busGlobalId);
}
