package com.bytter.guarantee.bond.service.impl;

import com.bytter.guarantee.api.IFcBondBalService;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.bond.service.FcBondBalService;
import com.bytter.guarantee.vo.FcBondBalVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service("fcBondBalServiceImpl")
@DubboService
public class FcBondBalServiceImpl implements IFcBondBalService {
    @Autowired
    private FcBondBalService fcBondBalService;

    /**
     * 根据bondId获取保证金余额数据
     */
    @Transactional(readOnly=true)
    public FcBondBalVO getBondBalByBondId(String bondId) {
        return fcBondBalService.getBondBalByBondId(bondId);
    }

    /**
     * 更新保证金余额表中相应的信息
     * @param fcBondBal
     */
    public void updateFcBondBal(FcBondBalVO fcBondBal){
        FcBondBal en = new FcBondBal();
        BeanUtils.copyProperties(fcBondBal, en);
        fcBondBalService.updateEntity(en);
    }
    public void deleteByBondId(String bondId){
        fcBondBalService.deleteByBondId(bondId);
    }
    public void backBondAddMoney(String bondId, BigDecimal money){
        fcBondBalService.backBondAddMoney(bondId,money);
    }
}
