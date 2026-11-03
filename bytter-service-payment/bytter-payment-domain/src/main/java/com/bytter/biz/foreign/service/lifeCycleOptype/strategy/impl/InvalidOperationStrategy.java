package com.bytter.biz.foreign.service.lifeCycleOptype.strategy.impl;

import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.service.FexTranLifeCycleService;
import com.bytter.biz.foreign.service.FexProfitLossManageService;
import com.bytter.biz.foreign.service.FexTranReqExtService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.service.lifeCycleOptype.strategy.LifeCycleOperationStrategy;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生命周期失效操作策略
 */
@Component("invalid")
public class InvalidOperationStrategy implements LifeCycleOperationStrategy {

    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;

    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private FexTranReqExtService fexTranReqExtService;

    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;

    /**
     * 处理字段
     * @param lifeCycleVo
     * @param user
     * @return
     */
    @Override
    public void dealField(FexTranLifeCycleVo lifeCycleVo, ICurrentUser user,FexTranLifeCycle fexTranLifeCycle) {
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
        entity.setConvertRate(lifeCycleVo.getConvertRate());
    }

    @Override
    public void approvePass(FexTranLifeCycle fexTranLifeCycle, ICurrentUser user) {
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexTranLifeCycle.getReqId());
        FexTranReq fexTranReq = fexTranReqService.get(fexTranReqVO.getId());
        //持仓金额反写为0
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranLifeCycle.getReqId());
        FexTranReqExt fexTranReqExt = fexTranReqExtService.get(fexTranReqExtVo.getId());
        fexTranReqExt.setPositionMoney(BigDecimal.ZERO);
        fexTranReqExtService.updateEntity(fexTranReqExt);
        // 更新为失效
        fexTranReq.setStatus(ForeignConstant.STATUE_INVALID);
        fexTranReqService.updateEntity(fexTranReq);
        fexProfitLossManageService.refreshFloatingProfitLoss(
                fexTranLifeCycle.getReqId(), BigDecimal.ZERO, user.getLoginName());
    }
}
