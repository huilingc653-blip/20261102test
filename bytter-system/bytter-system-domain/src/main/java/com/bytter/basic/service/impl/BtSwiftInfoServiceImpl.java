package com.bytter.basic.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.bytter.basic.service.BtSwiftInfoService;
import com.bytter.basic.vo.BtSwiftInfoVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 境外银行行号信息业务层接口类
 *
 * @author qinl
 * @date 2019-07-25
 */
@Service("btSwiftInfoServiceI")
@DubboService
@Transactional
public class BtSwiftInfoServiceImpl implements IBtSwiftInfoService {
    /**
     * 境外银行信息业务层
     */
    @Autowired
    private BtSwiftInfoService btSwiftInfoService;
    /**
     * <p>
     * 根据 <b>swiftCode</b> 获取一条境外银行信息
     * </p>
     *
     * @param swiftCode [swiftCode]
     * @return (BtSwiftInfoDTO)
     */
    @Override
    public BtSwiftInfoDTO getByCode(String swiftCode) {
        BtSwiftInfoDTO btSwiftInfoDTO = new BtSwiftInfoDTO();
        BtSwiftInfoVO btSwiftInfoVO = btSwiftInfoService.getByCode(swiftCode);
        if (btSwiftInfoVO != null) {
            BeanUtils.copyNotNullProperties(btSwiftInfoVO, btSwiftInfoDTO);
        }
        return btSwiftInfoDTO;
    }
    /**
     * <p>
     * 根据 <b>机构名称</b> 获取一条境外银行信息
     * </p>
     *
     * @param bankName [机构名称]
     * @return (BtSwiftInfoDTO)
     */
    @Override
    public BtSwiftInfoDTO getByBankName(String bankName) {
        BtSwiftInfoDTO btSwiftInfoDTO = new BtSwiftInfoDTO();
        BtSwiftInfoVO btSwiftInfoVO = btSwiftInfoService.getByBankName(bankName);
        if (btSwiftInfoVO != null) {
            BeanUtils.copyNotNullProperties(btSwiftInfoVO, btSwiftInfoDTO);
        }
        return btSwiftInfoDTO;
    }


    /**
     * 根据国家ID查询
     * @param countryCode
     * @return
     */
    public List<BtSwiftInfoDTO> getByCountryCode(String countryCode) {
        List<BtSwiftInfoVO> data = btSwiftInfoService.getByCountryCode(countryCode);
        if (EmptyUtils.isEmpty(data)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(data, BtSwiftInfoDTO.class);
    }
}
