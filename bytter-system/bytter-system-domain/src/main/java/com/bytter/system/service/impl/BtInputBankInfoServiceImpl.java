package com.bytter.system.service.impl;

import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtInputBankInfoService;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("BtInputBankInfoServiceI")
@DubboService
@Transactional
public class BtInputBankInfoServiceImpl implements IBtInputBankInfoService {

    @Autowired
    private BtInputBankInfoService btInputBankInfoService;

    @Override
    public InputBankInfoDTO getByBankCodeOrBankName(String bankCode, String bankName) {
        BtInputBankInfoVO byBankCodeOrBankName = btInputBankInfoService.getByBankCodeOrBankName(bankCode, bankName);
        if (EmptyUtils.isEmpty(byBankCodeOrBankName)) {
            return null;
        }
        InputBankInfoDTO InputBankInfoDTO = new InputBankInfoDTO();
        BeanUtils.copyProperties(byBankCodeOrBankName, InputBankInfoDTO);
        return InputBankInfoDTO;
    }
}
