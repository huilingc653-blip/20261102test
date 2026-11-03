package com.bytter.bill.domain.bill.service.impl;

import com.bytter.bill.api.bill.dto.AbBillCreditInfoMaintainDTO;
import com.bytter.bill.api.bill.service.IAbBillCreditMaintainService;
import com.bytter.bill.domain.bill.dao.AbBillCreditMaintainDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 银行开票授信资料维护对外接口实现
 *
 * @author fangcf
 * @date 2025-07-02
 */
@Service("abBillCreditMaintainServiceImpl")
@Transactional
@DubboService
public class AbBillCreditMaintainServiceImpl implements IAbBillCreditMaintainService {

    @Autowired
    private AbBillCreditMaintainDao abBillCreditMaintainDao;

    /**
     * 根据单位，银行类型，授信类型，获取唯一的一条银行开票授信资料
     *
     * @param corpId     单位id
     * @param bankTypeId 银行类型
     * @param creditType 授信类型
     */
    @Override
    public AbBillCreditInfoMaintainDTO getOneBy(String corpId, String bankTypeId, String creditType) {
        return abBillCreditMaintainDao.getOneBy(corpId, bankTypeId, creditType);
    }
}
