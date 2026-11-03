package com.bytter.bill.domain.bill.service.impl;

import com.bytter.bill.api.bill.service.IAbVoucherinfoViceService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("abVoucherinfoViceServiceImpl")
@Transactional
@DubboService
public class AbVoucherinfoViceServiceImpl implements IAbVoucherinfoViceService {
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 是否挂接预算系统
     * @param briefId 业务流水 001开票，6001库存登记，010背书
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isBudget(String briefId){
        return abVoucherinfoViceService.isBudget(briefId);
    }

    /**
     * 根据业务获取预算科目
     * @param briefId 业务流水 001开票，6001库存登记，010背书
     * @return
     */
    @Transactional(readOnly = true)
    public String getBudgetItem(String briefId){
        return abVoucherinfoViceService.getBudgetItem(briefId);
    }

    /**
    * @Description 根据从表id获取票据号
    * @author zhous
    * @date 2024/8/20 16:06
    */
    @Override
    public String getOrigNoByVid(String vId) {
        return abVoucherinfoViceService.getOrigNoByVid(vId);
    }
}
