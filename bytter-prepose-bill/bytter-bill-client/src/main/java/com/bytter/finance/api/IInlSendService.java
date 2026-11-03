package com.bytter.finance.api;

import com.bytter.core.base.entity.ICurrentUser;
public interface IInlSendService {
    /**
     * 付款打回或删除时打回内部拆借发放
     *
     * @param id
     * @param user
     */
    void backInlSendForNisBillHead(String id, ICurrentUser user);
    /**
     * 付款打回或删除时打回内部拆借还款
     *
     * @param id
     * @param user
     */
    void backInlRepayForNisBillHead(String id, ICurrentUser user);
    /**
     * 收款单删除时打回内部拆借发放
     *
     * @param id
     * @param user
     */
    void backInlSendForNisReceivable(String id, ICurrentUser user);
    /**
     * 收款单删除时打回内部拆借还款
     *
     * @param id
     * @param user
     */
    void backInlRepayForNisReceivable(String id, ICurrentUser user);
}
