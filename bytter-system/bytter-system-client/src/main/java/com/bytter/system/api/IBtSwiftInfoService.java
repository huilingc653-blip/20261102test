package com.bytter.system.api;
import com.bytter.system.api.dto.BtSwiftInfoDTO;

import java.util.Arrays;
import java.util.List;

/**
 * 境外银行行号信息业务接口
 *
 * @author qinl
 * @date 2019-07-25
 */
public interface IBtSwiftInfoService {
    /**
     * <p>
     * 根据 <b>swiftCode</b> 获取一条境外银行信息
     * </p>
     *
     * @param swiftCode [swiftCode]
     * @return (BtSwiftInfoDTO)
     */
    BtSwiftInfoDTO getByCode(String swiftCode);
    /**
     * <p>
     * 根据 <b>机构名称</b> 获取一条境外银行信息
     * </p>
     *
     * @param bankName [机构名称]
     * @return (BtSwiftInfoDTO)
     */
    BtSwiftInfoDTO getByBankName(String bankName);

    List<BtSwiftInfoDTO> getByCountryCode(String countryId);
}
