package com.bytter.system.api;
import com.bytter.system.api.dto.BtRateTypeDTO;
import java.util.List;
/**
 * 汇率类型接口
 *
 * @author zuoy
 * @date 2017/6/26.
 */
public interface IBtRateTypeService {
    /**
     * 根据汇率类型获取位数
     *
     * @param rateTypeId
     * @return
     */
    Integer getDecimalPlacesById(String rateTypeId);
    /**
     * 获取有效的汇率类型
     *
     * @return
     */
    List<BtRateTypeDTO> getAllRateType();
    /**
     * 根据id查找
     * @param id
     * @return
     */
    BtRateTypeDTO getObjectById(String id);
}
