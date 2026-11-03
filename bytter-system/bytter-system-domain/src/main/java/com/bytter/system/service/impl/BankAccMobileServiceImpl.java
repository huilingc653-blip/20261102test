package com.bytter.system.service.impl;

import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IBankAccMobileService;
import com.bytter.system.api.dto.BtBankAccMobileDTO;
import com.bytter.system.service.BankAccMobieService;
import com.bytter.system.vo.BtBankAccMobileVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luoyj
 * @date 2024/4/18.
 */
@Service("bankAccMobileServiceImpl")
@DubboService
@Transactional
public class BankAccMobileServiceImpl implements IBankAccMobileService {
    @Autowired
    private BankAccMobieService bankAccMobieService;

    @Override
    public String getBal(String corpId, String userId, String beginDate, String endDate, String bankAccId, String curId) {
        return bankAccMobieService.getBal(corpId,userId,beginDate,endDate,bankAccId,curId);
    }

    @Override
    public String getDtl(String corpId, String userId, String beginDate, String endDate, String bankAcc, String beginMoney, String endMoney, String oppBankAcc, String curId) {
        return bankAccMobieService.getDtl(corpId,userId,beginDate,endDate,bankAcc,beginMoney,endMoney,oppBankAcc,curId);
    }

    @Override
    public List<BtBankAccMobileDTO> getGrantBankAcc(String userId, String corpId, String grantType) {
        List<BtBankAccMobileVO> list = bankAccMobieService.getGrantBankAcc(userId, corpId, grantType);
        return BeanUtils.copyListProperties(list, BtBankAccMobileDTO.class);
    }
}
