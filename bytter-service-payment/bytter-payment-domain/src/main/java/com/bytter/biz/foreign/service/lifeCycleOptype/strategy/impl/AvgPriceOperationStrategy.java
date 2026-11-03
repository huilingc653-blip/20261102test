package com.bytter.biz.foreign.service.lifeCycleOptype.strategy.impl;

import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.service.FexTranLifeCycleService;
import com.bytter.biz.foreign.service.lifeCycleOptype.strategy.LifeCycleOperationStrategy;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("avgPrice")
public class AvgPriceOperationStrategy implements LifeCycleOperationStrategy {

    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;
    /**
     * 处理字段
     * @param lifeCycleVo
     * @param user
     * @return
     */
    @Override
    public void dealField(FexTranLifeCycleVo lifeCycleVo, ICurrentUser user, FexTranLifeCycle fexTranLifeCycle) {
        FexTranLifeCycle entity;
        if(StringUtils.isBlank(lifeCycleVo.getId())){
            entity = new FexTranLifeCycle();
            BeanUtils.copyProperties(lifeCycleVo,entity);
            entity.setId(null);
            if (StringUtils.isBlank(entity.getBillNo())) {
                entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_LIFE_CYCLE));
            }
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(new Date());
        }else{
            entity = fexTranLifeCycleService.get(lifeCycleVo.getId());
            entity.setLifeStatus(lifeCycleVo.getLifeStatus());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(new Date());
        }
        entity.setOpDate(lifeCycleVo.getOpDate());
        entity.setAvgRate(lifeCycleVo.getAvgRate());
    }

    @Override
    public void approvePass(FexTranLifeCycle fexTranLifeCycle, ICurrentUser user) {

    }
}
