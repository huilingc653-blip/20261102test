package com.bytter.system.api;
import com.bytter.system.api.dto.BtCurrencyInitDTO;
/**
 * 系统币别维护接口
 *
 * @author jw
 * @date 2023/5/9.
 */
public interface IBtCurrencyInitService {
    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    BtCurrencyInitDTO getObjectById(String id);
}
