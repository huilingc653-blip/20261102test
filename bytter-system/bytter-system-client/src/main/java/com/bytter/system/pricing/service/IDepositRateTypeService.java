package com.bytter.system.pricing.service;
import com.bytter.system.pricing.dto.DepositRateTypeDTO;
import java.util.List;
/**
 * @author luxy
 * @Title: IDepositRateTypeService
 * @ProjectName T3(3.2)
 * @Description: 存款利率类型接口
 * @date 2019/11/8 14:16
 */
public interface IDepositRateTypeService {
    /**
     * 获取所有存款利率类型
     * @return
     */
    List<DepositRateTypeDTO> getDepositRateType();

    DepositRateTypeDTO getDepositRateTypeById(String id);

}
