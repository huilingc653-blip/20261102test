package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.AbVoucherinfoDTO;
import com.bytter.biz.payment.api.dto.NisBillBankSerialNoDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 付款接口类
 *
 * @author huangx
 * @since 2017-09-02 09:49:44
 */
public interface INisBillheadService {
    /**
     * 更新付款单（票据反写接口）
     * 状态：-1-打回；95-审批通过；99-票据完成
     *
     * @param id     付款单ID
     * @param status 状态
     * @param msg    信息
     */
    void updateNisBillhead(String id, Integer status, String msg, ICurrentUser user);

    /**
     * 更新付款单（退款勾兑）
     *
     * @param id     付款单ID
     * @param status 状态
     * @param msg    信息
     */
    void updateNisBillheadForRefundBlend(String id, Integer status, String msg, String bankSerialNo, ICurrentUser user);

    /**
     * 记账作废反写账务状态（记账作废使用）
     *
     * @param billNo
     * @param status
     * @param msg
     * @param tallySign
     * @param user
     */
    void updateNisBillheadForInvalidFin(String billNo, Integer status, String msg, String tallySign, ICurrentUser user);

    /**
     * 更新下拨账号（记账反写接口）
     *
     * @param billNo    付款单号
     * @param downAccId 下拨账号Id
     */
    void updateNisBillheadDownAccId(String billNo, String downAccId);

    /**
     * 更新接口代码（记账反写接口）
     *
     * @param billNo  付款单号
     * @param bankAcc 银行账号
     */
    void updateNisBillheadForindividual(String billNo, String bankAcc);

    /**
     * 付款单生成（外模块生成付款单）
     *
     * @param entity
     */
    void saveNisBillhead(NisBillheadDTO entity, ICurrentUser user);

    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return 付款单信息集合
     */
    List<NisBillheadDTO> listNisBillheadByErpId(String erpId);

    /**
     * 根据共享结算单号list获取付款信息
     *
     * @param bursarBillNoList
     */
    List<NisBillheadDTO> listNisBillheadByBursarBillNos(List<String> bursarBillNoList);

    /**
     * 根据外部单号获取付款单状态信息。
     *
     * @param erpIdList 外部单号列表。
     * @return 付款单状态列表。
     */
    Map<String,NisBillheadDTO> listNisBillInfoByErpId(List<String> erpIdList);

    /**
     * 手工记账待记账的付款单
     *
     * @return 付款单信息
     */
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);

    /**
     * 根据单号获取付款信息
     *
     * @param billNo
     */
    NisBillheadDTO getNisBillheadByBillNo(String billNo);

    /**
     * 根据id获取付款信息
     *
     * @param id
     */
    NisBillheadDTO getNisBillheadById(String id);

    /**
     * 修改单据-记账打回使用 zhengxn
     *
     * @param nisBillheadDTO
     * @param user
     */
    void updateNisBillheadByDto(NisBillheadDTO nisBillheadDTO, ICurrentUser user);

    /**
     * <br>
     * <b>功能：</b>查询付款计划（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param corpflag
     * @param corpCode
     * @return 付款金额
     */
    BigDecimal queryNisBillhead(String corpflag, String corpCode, Date wishPayday);

    /**
     * 外系统根据时间获取付款单
     *
     * @param beginDate
     * @param endDate
     * @return 付款单集合
     */
    List<NisBillheadDTO> listNisBillheadByTime(String beginDate, String endDate);

    /**
     * Erp同步接口-批量保存
     *
     * @param list
     */
    void syncBatchSave(List<NisBillheadDTO> list);

    /**
     * Erp同步接口-批量删除
     *
     * @param list
     */
    void syncBatchDelete(List<NisBillheadDTO> list);

    /**
     * Erp接口-根据erpId获取付款单交易状态信息
     *
     * @param erpId
     * @return 交易状态
     */
    String getVoucherStatByErpId(String erpId, String billFrom);

    /**
     * 判断账号付款是否使用中
     *
     * @param blnctrAcc
     * @return 是否使用中
     */
    boolean isExistBlnctrAcc(String blnctrAcc);

    /**
     * 根据银行流水获取付款单
     *
     * @param dataTablesInput
     * @return 付款单信息
     */
    DataTablesOutput listNisBillheadByDtl(DataTablesInput dataTablesInput,String joinSql);

    /**
     * 更新付款单银行流水号（付款明细勾兑返写银行流水号）
     *
     * @param id
     * @param bankSerialNo
     */
    void updateNisBillheadForBankSerialNo(String id, String bankSerialNo);

    /**
     * 根据银行流水获取付款单（退款勾兑）
     *
     * @param dataTablesInput
     * @return
     */
    DataTablesOutput listNisBillheadByRefundblending(DataTablesInput dataTablesInput);

    List<NisBillheadDTO> listNisBillheadByRefundblending(Date beginDate,Date endDate);

    List<NisBillheadDTO> listNisBillheadByRefundblendingNew(Date beginDate,Date endDate,int size);

    int getCountDataTablesUnionForRefundblending(Date beginDate,Date endDate);

    /**
     * 退款勾兑打回
     *
     * @param id
     * @param user
     * @param backType
     */
    void backNisBillhead(String id, ICurrentUser user, String backType);

    /**
     * 根据银行流水号得到付款单
     *
     * @param bankSerialNo
     * @return
     */
    List<NisBillheadDTO> listNisBillheadByBankSerialNo(String bankSerialNo);

    /**
     * Erp接口（WebService）获取付款单交易状态信息
     *
     * @param serialNoErp 外部系统id
     * @return
     */
    List<Map<Object, Object>> getVoucherStatByWs(String serialNoErp);

    /**
     * erp标准接口保存
     *
     * @param dto
     */
    void saveErpNisBillhead(NisBillheadDTO dto);
    /*限额begin*/

    /**
     * 根据付款账号和币别获取付款单的付款金额总和
     *
     * @param bankAcc 付款账号
     * @param curId   币别
     * @return
     */
    BigDecimal getSum(String bankAcc, String curId, String startTime, String endTime);

    /**
     * 根据付款账号和币别获取付款单的付款金额总和
     *
     * @param corpId 单位
     * @param curId  币别
     * @return
     */
    BigDecimal getSumForCorp(String corpId, String curId, String startTime, String endTime);

    /**
     * 根据id查询数据
     *
     * @param id 数据项Id
     * @return
     */
    NisBillheadDTO getMoneyById(String id);
    /*限额end*/

    /**
     * 待记账查询
     */
    List<NisBillheadDTO> queryTallyUnions();

    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return 付款单信息集合
     */
    NisBillheadDTO getNisBillheadByErpId(String erpId);

    /**
     * 根据单号删除付款信息
     *
     * @param id
     * @param user
     */
    void deteleNisbillHeadById(String id, ICurrentUser user);

    /**
     * 票据支付失败处理
     *
     * @param id          付款单ID
     * @param failureList
     * @param successList
     */
    void backNisBillheadForBill(String id, List<AbVoucherinfoDTO> failureList, List<AbVoucherinfoDTO> successList);

    /**
     * 票据支付成功处理
     *
     * @param ids 付款单IDS
     */
    void successNisBillheadForBill(String ids,Integer status);

    /**
     * 获取ERP待处理单据
     *
     * @return
     */
    List<NisBillheadDTO> listErpTodoBill();

    /**
     * 根据票据流水ID过滤相关票据流水ID。
     *
     * @param listId 票据流水ID。
     * @return　票据流水ID。
     */
    List<String> listBillId(List<String> listId);

    /**
     * 获取付款开票未用退票成功付款单号ID。
     *
     * @param listId 付款单号ID。
     * @return 付款单号ID。
     */
    List<String> listPayIdByBill(List<String> listId);

    /**
     * 获取资金拆借关联id
     *
     * @param ids 资金拆借id
     * @return 符合条件的资金拆借id
     */
    List<NisBillheadDTO> getCmsInlSendId(List<String> ids);

    /**
     * 更新付款单记账信息
     *
     * @param businessId 业务id
     * @param tallySign  记账标志
     * @return 更新条数
     */
    int updateEditPassSign(String businessId, String tallySign);

    /**
     * 更新付款单复核信息
     *
     * @param id                   单据id
     * @param orderNo              流水号
     * @param userId               用户id
     * @param voucherNo            凭证号
     * @param statueTallyCheckPass 处理状态
     * @return
     */
    int updateAutoCheckSign(String id, String orderNo, String userId, String voucherNo, Integer statueTallyCheckPass);

    /**
     * 发送消息
     *
     * @param id      单据id
     * @param orderNo 流水号
     * @param userId  用户id
     * @return 更新条数
     */
    int updateTallySign(String id, String orderNo, String userId);

    /**
     * 更新付款单记账信息
     *
     * @param id                   单据主键
     * @param voucherNo            流水号、凭证号
     * @param statueTallyCheckPass 更新状态
     * @return 更新条数
     */
    int updateCheckSign(String id, String voucherNo, Integer statueTallyCheckPass);

    /**
     * 更新付款单记账信息
     *
     * @param businessId 业务单据id
     * @param tallySign  记账标识
     * @return 更新条数
     */
    int updateEditBackSign(String businessId, String tallySign);

    /**
     * 更新付款单记账信息
     *
     * @param id        业务单据id
     * @param voucherNo 凭证号
     * @return 更新条数
     */
    int updateCheckBackSign(String id, String voucherNo);

    /**
     * 数据回写
     *
     * @param toString sql
     * @param params   参数
     */
    void createSQLQueryFinacialColumn(String toString, Map<String, Object> params);

    List<NisBillBankSerialNoDTO> getBlendingList(List<String> dtlIdS);

    Map<String, NisBillBankSerialNoDTO> getBlendingMap(List<String> dtlIdS);

    public List<NisBillBankSerialNoDTO> getBlendingDate(NisBillBankSerialNoDTO dto);

    /**
     * 插入中间关联表
     *
     * @param billId
     * @param isFrom
     * @param bankSerialNo
     */
    void insetBillBankSerialNo(String billId, String isFrom, String bankSerialNo, String dtlId);
    /**
     * 删除中间关联表
     *
     * @param dtlId
     */
    public void deleteBillBankSerialNoByDtlId(String dtlId);
    /**
     * 删除中间关联表
     *
     * @param billId
     */
    public void deleteBillBankSerialNoByBillId(String billId);
    /**
     * 删除退款勾兑中间关联表
     *
     * @param billId
     */
    public void deleteBillBankSerialNoRefundByBillId(String billId);
    /**
     * 根据业务单据号得到流水id
     *
     * @param billId
     * @return
     */
    public List<String> getDtlId(String billId) ;
    /**
     * 根据流水id获取业务单号
     *
     * @param dtlId
     * @return
     */
    public List<String> getBillId(String dtlId);


    List<NisBillheadDTO> listByErpIds(List<String> ids);

    /**
     * 获取挂接付款余额
     *
     * @param billIds 票据流水id
     * @return
     */
    String getBillBalances(String billIds, String payId);

    /**
     * 根据旧billId修改新的billId
     *
     * @param billIdOld 旧票据流水id
     * @param billIdNew 新票据流水id
     * @return
     */
    void updateNisBillByBillId(String billIdOld, String billIdNew);

    List<NisBillheadDTO> listNisBillheadByPayIds(String payIds);

    void updateNisBillheadPyaerInfo(String id, String payerAcc,String payerAccName, String payerBank, ICurrentUser user);

    /**
     * 根据退款原单获取所有新单
     *
     * @param refundOrgId
     * @return
     */
    List<NisBillheadDTO> listByRefundOrgId(String refundOrgId);

    /**
     * 应付债权凭证开立审批通过后，同步付款单绑定的平台/融资产品（仅当不一致时回写中间表 nis_scf_info）
     * <p>由 prepose-bill 模块在凭证开立审批通过时调用：按凭证ID反查绑定的付款单，
     * 将付款单在 nis_scf_info 中的 platform_id/product_id 更新为凭证当前的平台/产品。
     *
     * @param voucherId  应付债权凭证ID
     * @param platformId 凭证当前平台ID
     * @param productId  凭证当前融资产品ID
     */
    void syncScfPlatformProduct(String voucherId, String platformId, String productId);
}
