package com.bytter.system.api;





import com.bytter.system.api.dto.BtCurrencyPairDTO;

import java.util.Map;

/**
 * 货币对接口
 * @author chenqin
 * @date 2025/09/03.
 */
public interface IBtCurrencyPairService {

    /**
     * 根据币种id获取币别对
     * @param curId1
     * @param curId2
     * @return
     */
    BtCurrencyPairDTO getBtCurrencyPairByCurId(String curId1, String curId2);

    /**
     *
     * 获取所有货币对MAP
     *
     * @return
     */
    public Map<String, BtCurrencyPairDTO> getCurrencyPairMap();

    /**
     * 根据id获取交易货币对
     * @param id
     * @return
     */
    BtCurrencyPairDTO getById(String id);
}
