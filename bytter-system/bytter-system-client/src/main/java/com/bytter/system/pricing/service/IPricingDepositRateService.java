package com.bytter.system.pricing.service;
import com.bytter.system.pricing.dto.DepositRateDTO;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 存款利率维护接口
 * @author luxy
 */
public interface IPricingDepositRateService {
    /**
     * 根据存款账户类别获取存款利率信息
     * @param depositClass 存款账户类别
     * @param time 开户日期
     * @param curId 币别
     * @return 存款利率DTO
     */
    DepositRateDTO getDepositRateByAccType(String depositClass, Date time, String curId);
    /**
     * 根据id获取存款利率
     * @param id
     * @return 存款利率DTO
     */
    DepositRateDTO getDepositRateById(String id);
    /**
     * 根据条件查询存款利率
     * @param depositRateDTO
     * @return
     */
    DepositRateDTO getDepositRateByParams(DepositRateDTO depositRateDTO);
    /**
     * 根据存款种类和周期获取存款利率
     * @return
     */
    BigDecimal getDepositRateByPeriod(String depositClass, String depositPeriod,String curId);
}
