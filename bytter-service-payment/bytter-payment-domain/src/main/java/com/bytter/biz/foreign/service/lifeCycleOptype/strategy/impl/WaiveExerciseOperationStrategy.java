package com.bytter.biz.foreign.service.lifeCycleOptype.strategy.impl;

import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.service.FexTranReqExtService;
import com.bytter.biz.foreign.service.FexProfitLossManageService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.service.lifeCycleOptype.LifeCycleBalanceUtil;
import com.bytter.biz.foreign.service.lifeCycleOptype.strategy.LifeCycleOperationStrategy;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component("waiveExercise")
public class WaiveExerciseOperationStrategy implements LifeCycleOperationStrategy {

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexTranReqExtService fexTranReqExtService;

    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;

    @Override
    public void dealField(FexTranLifeCycleVo vo, ICurrentUser user, FexTranLifeCycle entity) {
        //生命周期编号
        entity.setBillNo(vo.getBillNo());
        //操作类型
        entity.setOpType(vo.getOpType());
        //操作日期
        entity.setOpDate(vo.getOpDate());
        //结算日期
        entity.setSettleDate(vo.getSettleDate());
        //买入金额
        entity.setBuyMoney(vo.getBuyMoney());
        //卖出金额
        entity.setSellMoney(vo.getSellMoney());
        //买入余额
        entity.setBuyBal(vo.getBuyBal());
        //卖出余额
        entity.setSellBal(vo.getSellBal());
        //即期汇率
        entity.setSpotRate(vo.getSpotRate());
        //设置创建人
        entity.setCreateBy(user.getId());
        //设置创建时间
        entity.setCreateDate(new Date());
    }

    @Override
    public void approvePass(FexTranLifeCycle fexTranLifeCycle, ICurrentUser user) {
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexTranLifeCycle.getReqId());
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranLifeCycle.getReqId());
        FexTranReqExt fexTranReqExt = fexTranReqExtService.get(fexTranReqExtVo.getId());
        FexTranReq fexTranReq = new FexTranReq();
        BeanUtils.copyNotNullProperties(fexTranReqVO,fexTranReq);
        //交易的持仓金额反写为余额的第一行
        // 交易.持仓金额反写为余额第一行
        BigDecimal positionBalance = LifeCycleBalanceUtil.resolveOptionPositionBalance(fexTranReq,
                fexTranReq.getUpdownDirection(), fexTranLifeCycle);
        fexTranReqExt.setPositionMoney(positionBalance);
        fexTranReqExtService.updateEntity(fexTranReqExt);
        //入股余额的第一行等于0，交易状态为结束
        if (LifeCycleBalanceUtil.isPositionBalanceZero(positionBalance)){
            fexTranReq.setStatus(ForeignConstant.STATUE_END);
        } else {
            fexTranReq.setStatus(ForeignConstant.ONGOING);
        }
        fexTranReqService.merge(fexTranReq);
        fexProfitLossManageService.refreshFloatingProfitLoss(
                fexTranLifeCycle.getReqId(), positionBalance, user.getLoginName());
    }
}
