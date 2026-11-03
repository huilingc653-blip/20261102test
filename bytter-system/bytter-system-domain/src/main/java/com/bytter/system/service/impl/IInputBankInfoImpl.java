package com.bytter.system.service.impl;
import com.bytter.basic.dao.BtInputBankInfoDao;
import com.bytter.system.api.IInputBankInfo;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
@Service("iInputBankInfoI")
@DubboService
@Transactional
public class IInputBankInfoImpl implements IInputBankInfo {
    @Autowired
    BtInputBankInfoDao btInputBankInfoDao;
    @Override
    public InputBankInfoDTO getByBankCode(String bankCode) {
        return null;
    }
    @Override
    public InputBankInfoDTO getByBankName(String bankName) {
        return null;
    }
    @Override
    public List<Map<String, Object>> queryBankNameByPaysyBbankCode(String paysysbankcode) {
        return btInputBankInfoDao.queryBankNameByPaysyBbankCode(paysysbankcode);
    }
}
