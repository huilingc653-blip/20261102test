package com.bytter.biz.payment.api.service;
/**
 * 外汇资金池接口类
 * @author wangzx
 * @since 2020-3-30 16:49:44
 */
public interface IFexCorpPoolService {
    /**
     * 根据资金池id查找外汇资金池维护是否使用
     * @param poolId
     * @return
     */
    boolean isUsePool(String poolId);
}
