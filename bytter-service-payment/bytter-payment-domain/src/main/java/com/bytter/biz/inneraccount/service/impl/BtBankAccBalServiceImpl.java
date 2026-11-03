package com.bytter.biz.inneraccount.service.impl;

import com.bytter.biz.inneraccount.service.BtBankAccBalService;
import com.bytter.biz.payment.api.dto.BtBankAccBalDTO;
import com.bytter.biz.payment.api.service.IBtBankAccBalService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC_BAL
 * @date 2022-05-25 17:50:16
 */
@Service
@Transactional
@DubboService
public class BtBankAccBalServiceImpl implements IBtBankAccBalService {
    @Autowired
    private BtBankAccBalService btBankAccBalService;

    @Override
    public void initBankAccBal() {
        btBankAccBalService.initBankAccBal();
    }
	@Override
	public void batchSaveByDTO(List<BtBankAccBalDTO> balList) {
        btBankAccBalService.batchSaveByDTO(balList);
	}

    @Override
    public Map<String, BigDecimal> getBalByAccAndCur(String bankAcc, String cur) {

        return btBankAccBalService.getBalByAccAndCur(bankAcc, cur) ;
    }

}
