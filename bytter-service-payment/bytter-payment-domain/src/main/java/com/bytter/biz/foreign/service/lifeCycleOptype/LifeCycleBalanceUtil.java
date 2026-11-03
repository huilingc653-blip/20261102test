package com.bytter.biz.foreign.service.lifeCycleOptype;

import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.payment.api.constant.ForeignConstant;

import java.math.BigDecimal;

/**
 * 生命周期持仓余额处理工具。
 */
public final class LifeCycleBalanceUtil {

    private LifeCycleBalanceUtil() {
    }

    /**
     * 根据外汇交易买卖方向取得货币对第一行余额。
     *
     * @param fexTranReq 交易单
     * @param lifeCycle 生命周期单
     * @return 持仓余额，买卖方向无效时返回null
     */
    public static BigDecimal resolveForeignPositionBalance(FexTranReq fexTranReq,
                                                           FexTranLifeCycle lifeCycle) {
        if (ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReq.getBusDirection())) {
            return lifeCycle.getBuyBal();
        }
        if (ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReq.getBusDirection())) {
            return lifeCycle.getSellBal();
        }
        return null;
    }

    /**
     * 根据期权买卖方向和涨跌方向取得货币对第一行余额。
     *
     * @param fexTranReq 交易单
     * @param updownDirection 涨跌方向
     * @param lifeCycle 生命周期单
     * @return 持仓余额
     */
    public static BigDecimal resolveOptionPositionBalance(FexTranReq fexTranReq, String updownDirection,
                                                          FexTranLifeCycle lifeCycle) {
        if ((ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReq.getBusDirection())
                && ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(updownDirection))
                || (ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReq.getBusDirection())
                && ForeignConstant.FEX_TRAN_DIRECTION_DOWN.equals(updownDirection))) {
            return lifeCycle.getBuyBal();
        }
        return lifeCycle.getSellBal();
    }

    /**
     * 判断货币对第一行余额是否已经全部处理完毕。
     *
     * @param positionBalance 持仓余额
     * @return 余额不大于0时返回true
     */
    public static boolean isPositionBalanceZero(BigDecimal positionBalance) {
        return positionBalance != null && BigDecimal.ZERO.compareTo(positionBalance) >= 0;
    }
}
