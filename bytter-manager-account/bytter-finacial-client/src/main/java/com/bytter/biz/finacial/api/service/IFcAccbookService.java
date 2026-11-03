package com.bytter.biz.finacial.api.service;

import com.bytter.biz.finacial.api.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 柜台账务对外接口
 *
 * @author liyh
 * @since 2017-8-13
 */
public interface IFcAccbookService {
    /**
     * 功能介绍 : 记账凭证暂存
     * voucher需设置凭证对象target中FcAccbookVoucherDTO主表和明细表数据
     * 如果是修改暂存，则还需要设置busGlobalId，因此记账完成后要把busGlobalId回写到业务表中
     *
     * @param voucher 凭证对象
     */
    VoucherWrapperDTO saveVoucher(VoucherWrapperDTO voucher);
    /**
     * 功能介绍 : 记账凭证提交
     * voucher需设置凭证对象target中FcAccbookVoucherDTO主表和明细表数据
     * 如果是暂存后提交，则还需要设置busGlobalId，因此记账完成后要把busGlobalId回写到业务表中
     *
     * @param voucher 凭证对象
     */
    VoucherWrapperDTO submitVoucher(VoucherWrapperDTO voucher);
    
    /**
     * 功能介绍 : 记账凭证提交
     * voucher需设置凭证对象target中FcAccbookVoucherDTO主表和明细表数据
     * 如果是暂存后提交，则还需要设置busGlobalId，因此记账完成后要把busGlobalId回写到业务表中
     *
     * @param voucher 凭证对象
     */
    VoucherWrapperDTO checkSubmitVoucher(VoucherWrapperDTO voucher);
    /**
     * 功能介绍 : 记账凭证复核
     * 凭证复核只需要设置busGlobalId即可
     *
     * @param voucher 凭证对象
     */
    VoucherWrapperDTO checkVoucher(VoucherWrapperDTO voucher);
    /**
     * 功能介绍 : 记账凭证复核打回。
     * 凭证打回只需要设置busGlobalId即可
     *
     * @param voucher 凭证对象
     */
    VoucherWrapperDTO backVoucher(VoucherWrapperDTO voucher);
    /**
     * 功能介绍 : 记账凭证删除
     * 凭证删除只需要设置busGlobalId即可
     *
     * @param voucher 凭证对象
     */
    VoucherWrapperDTO deleteVoucher(VoucherWrapperDTO voucher);
    /**
     * 功能介绍 : 记账凭证作废。
     * 作废只需要设置原凭证的 busGlobalId
     *
     * @param voucher 凭证对象
     */
    void invalidVoucher(VoucherWrapperDTO voucher);
    /**
     * 功能介绍 : 记账凭证冲账。
     * 冲账需要设置原凭证的 busGlobalId和ID
     *
     * @param voucher 凭证对象
     */
    void rushVoucher(VoucherWrapperDTO voucher);
    /**
     * 功能介绍 : 获取记账凭证对象。<br>
     *
     * @param oriType     业务来源类型
     * @param busGlobalId 全局业务id
     */
    VoucherWrapperDTO getVoucher(Integer oriType, String busGlobalId);
    /**
     * 功能介绍 : 获取记账凭证对象。<br>
     *
     * @param voucherNo 凭证号
     */
    VoucherWrapperDTO getVoucherByVoucherNo(String voucherNo);
    /**
     * 根据账号获取未结转手续费
     *
     * @param account 内部账号
     * @return 账号-手续费
     */
    Map<String, BigDecimal> getNotOverChargeByAccount(String account);
    /**
     * 是否存在未复核的账务
     *
     * @param blnctrAcc   内部账号
     * @param busGlobalId 全局业务
     * @return true or false
     */
    boolean existsUnCheckAccbook(String blnctrAcc, String busGlobalId);
    /**
     * 功能介绍 : 根据业务单号和业务来源获取凭证记账和日结状态。<br>
     *
     * @param billId     业务单据Id
     * @param busOriType 业务来源类型
     */
    FcAccbookCloseDTO getAccbookCloseDTO(String billId, Integer busOriType);
    /**
     * 更新资金计划挂接状态（内部流水挂计划专用）
     *
     * @param id           单据id
     * @param status       状态
     * @param dayCloseSign 日结标志 true 已日结 false 未日结
     */
    void updateFcAccFbsStatus(String id, String status, boolean dayCloseSign);
    /**
     * 根据凭证号获取账务信息
     *
     * @param voucherNo
     * @return
     */
    FcAccbookDTO getBookVOByVoucherNo(String voucherNo);
    /**
     * 根据流水号获取账务
     *
     * @param orderNo 流水号
     * @param isPool  是否资金池
     * @return FcAccbookDTO 账务信息
     */
    FcAccbookDTO getBookVOByOrderNo(String orderNo, String isPool);
    /**
     * 暂存账务凭证信息
     *
     * @param voucher 凭证对象
     */
    FcAccbookDTO saveFcAccbook(VoucherWrapperDTO voucher);
    /**
     * 保存账务凭证信息
     *
     * @param voucher 凭证对象
     * @return 更新后的参数
     */
    FcAccbookDTO submitFcAccbook(VoucherWrapperDTO voucher);
    /**
     * 复核账务凭证信息
     *
     * @param voucher 凭证对象
     */
    VoucherWrapperDTO checkFcAccbook(VoucherWrapperDTO voucher);
    /**
     * 删除账务凭证信息
     *
     * @param voucher 凭证对象
     */
    void deleteFcAccbook(VoucherWrapperDTO voucher);
    /**
     * 打回账务凭证信息
     *
     * @param voucher 凭证对象
     */
    void backFcAccbook(VoucherWrapperDTO voucher);
    /**
     * 根据账务I的获取账务扩展表数据
     *
     * @param bookId 账务id
     * @return FcAccbookExtDTO 账务扩展信息
     */
    FcAccbookExtDTO getBookExtVOByBookId(String bookId);
    /**
     * 根据账务id获取资金池账务明细
     *
     * @param bookId 账务id
     * @return List&lt;FcAccbookDtlDTO&gt; 账务明细
     */
    List<FcAccbookDtlDTO> getPoolPageDtlsByBookId(String bookId);
    /**
     * 按照计息参数获取相关利息账号
     */
    List<FcAccDTO> getInterestAcc(String curCode, String netId, String corpId, String subjectCode, String busType, String bankCode, String user, String msg);
    FcAccbookDTO getAccbookByBusGlobalId(String busGlobalId);
    /**
     * 功能介绍 : 获取当前登陆用户的网点ID。<br>
     */
//    CorpDTO getUserNet(ICurrentUser user);
    /**
     * 获取已有记账的bill_id
     *
     * @param billId
     * @return
     */
    List<String> getTallyRecordListId(List<String> billId);
    /**
     * 根据全局业务ID获取流水号以及凭证号、凭证状态。
     *
     * @param busGlobalId 全局业务ID。
     * @return 流水号以及凭证号、凭证状态。
     */
    List<Map<String, Object>> getOrderVoucherByGlobalId(List<String> busGlobalId, String voucherState, String orderNo, String voucherNo);
    /**
     * 获取核算数据
     *
     * @param cbsBillId 业务单据id
     * @return 单据核算数据
     */
    FcAccbookDTO getAccBookVoById(String cbsBillId);
    /**
     * 根据凭证流水号获取记账流水
     *
     * @param billCode 凭证号
     * @return 流水信息
     */
    FcAccbookDTO getAccBookVOByOrigVoucherNo(String billCode);

    /**
     * 查询单位下某业务状态下的记账主数据
     */
    List<FcAccbookDTO> listByInCorpIdAndBusOriTypeAndInState(List<String> corpIds,String busOriType,List<String> voucherStates);
}
