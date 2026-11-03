package com.bytter.increment.api.service;
import com.bytter.increment.api.dto.FcAgreeDepositDetailDTO;
import java.util.Date;
import java.util.List;
/**
 * 协定利率接口
 *
 * @author wangzx
 * @since 2019-08-26 18:49:44
 */
public interface IFcAgreedDetailService {
    /**
     * 根据协定存款id查找协定明细
     *
     * @param agreedId 协定存款id
     * @return 协定明细DTO集合
     */
    List<FcAgreeDepositDetailDTO> getFcAgreedDetailById(String agreedId);
    /**
     * 根据协定存款id和行号查找协定明细
     *
     * @param agreedId 协定主表id
     * @param lineNo   行号
     * @return 协定明细DTO集合
     */
    FcAgreeDepositDetailDTO getDetailByAgreedIdLineNo(String agreedId, Integer lineNo);
    /**
     * 获取账户协定明细
     *
     * @param account     账号
     * @param balanceDate 协定日期
     * @param isAdjust 是否调整 true 是 false 否
     * @return 协定明细DTO集合
     */
    List<FcAgreeDepositDetailDTO> getFcAgreeDepositDetail(String account, Date balanceDate, boolean isAdjust);
}
