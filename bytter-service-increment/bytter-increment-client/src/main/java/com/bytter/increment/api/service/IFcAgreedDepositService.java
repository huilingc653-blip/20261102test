package com.bytter.increment.api.service;
import com.bytter.increment.api.dto.FcAgreedDepositDTO;
import java.util.Date;
/**
 * 协定存款接口
 *
 * @author wangzx
 * @since 2019-11-11 18:49:44
 */
public interface IFcAgreedDepositService {
    /**
     * 根据协定存款账号和时间查找登记信息
     *
     * @param blnctrAcc 协定主表账号
     * @param queryDate 查询时间
     * @return 协定存款DTO
     */
    FcAgreedDepositDTO getFcAgreedDepositByBlnctrAcc(String blnctrAcc, Date queryDate);
    /**
     * 根据协定存款协定账号和时间查找登记信息
     *
     * @param agrAcc 协定账号
     * @param queryDate 查询时间
     * @return 协定存款DTO
     */
    FcAgreedDepositDTO getChoAgreeInfoByAgrAcc(String agrAcc,Date queryDate);
}
