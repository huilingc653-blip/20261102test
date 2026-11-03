package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.FcFinacialAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.core.base.entity.ICurrentUser;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 利息计息对外接口
 *
 * @author caozx
 * @since 2017-8-24
 */
public interface IFcInterestService {
    /**
     * 计算指定金额的利息
     *
     * @param account     账号
     * @param money       金额
     * @param orderNo     流水号(或业务Id)
     * @param endInstDate 止息日期(包含)
     * @return 利息
     */
    BigDecimal calTellerMoneyInterest(String account, BigDecimal money, String orderNo, Date endInstDate, ICurrentUser user);
    /**
     * 根据指定金额获取利息明细
     *
     * @param account     账号
     * @param money       金额
     * @param orderNo     流水号
     * @param endInstDate 止息日期(包含)
     * @return 利息明细
     */
    List<FcInterestDtlDTO> getTellerMoneyInterests(String account, BigDecimal money, String orderNo, Date endInstDate, ICurrentUser user);
    /**
     * 指定金额计息后处理积数
     *
     * @param account         账号
     * @param money           金额
     * @param orderNo         流水号
     * @param user            用户Id
     * @param depositMangerId 取款ID
     * @param endInstDate     止息日期(包含)
     */
    void processTellerMoneyBalance(String account, BigDecimal money, String orderNo, ICurrentUser user, String depositMangerId, Date endInstDate);
    /**
     * 删除账户产生的利息，更新积数
     *
     * @param account         账号
     * @param orderNo         流水号
     * @param depositMangerId 取款ID
     */
    void deleteAccInterest(String account, String orderNo, String depositMangerId);
    /**
     * 获取账户冲预提金额
     *
     * @param account          账号
     * @param tellerMoney      金额
     * @param depositManagerId 本次冲预提暂存的取款ID
     * @return 冲预提金额
     */
    BigDecimal getRushPreDrawMoney(String account, BigDecimal tellerMoney, String depositManagerId);
    /**
     * 处理冲预提
     *
     * @param account
     * @param user
     * @param depositManagerId
     * @param tellerMoney
     */
    void processRushPredraw(String account, ICurrentUser user, String depositManagerId, BigDecimal tellerMoney, Date interestTime);
    /**
     * 根据流水号或者账号查询利息明细
     *
     * @param account    账号
     * @param vourcherNo 流水号
     * @return 利息明细
     */
    List<FcInterestDtlDTO> getFcInterestDtlDTOByVoucherNo(String account, String vourcherNo);
    /**
     * 根据流水号账号回单号查询利息明细
     *
     * @param account    账号
     * @param orderNo    流水号
     * @param eReceiptNo 回单号
     * @return 利息明细
     */
    List<FcInterestDtlDTO> getFcInterestDtlDTOByOrderNo(String account, String orderNo, String eReceiptNo);
    /**
     * 是否存在在途利息
     *
     * @param blnctrAcc 内部账号
     * @return true or false
     */
    boolean existUnOverInterest(String blnctrAcc);
    /**
     * 处理电子回单号
     *
     * @param account    内部账号
     * @param orderNo    流水号
     * @param eReceiptNo 回单号
     */
    void processEReceiptNo(String account, String orderNo, String eReceiptNo);
    /**
     * 保存利息
     *
     * @param fcInterestDtlDTOS
     */
    void saveFcInterestDtl(List<FcInterestDtlDTO> fcInterestDtlDTOS, String userId);
    /**
     * 创建预提凭证
     */
    List<FcFinacialAccbookDTO> createPredrawVoucher(FcInterestDtlDTO fcInterestDtlDTO, Date tallyDate, String userId, Integer certificateWay);
    /**
     * 根据账号查询利息明细
     *
     * @param blnctrAcc 内部账号
     * @param isPool
     * @return 利息明细
     */
    List<FcInterestDtlDTO> getFcInterestDtlByAcc(String blnctrAcc, String isPool);
}
