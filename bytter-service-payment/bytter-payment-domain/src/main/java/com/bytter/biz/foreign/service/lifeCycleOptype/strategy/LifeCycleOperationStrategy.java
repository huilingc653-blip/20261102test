package com.bytter.biz.foreign.service.lifeCycleOptype.strategy;

import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.core.base.entity.ICurrentUser;

public interface LifeCycleOperationStrategy {
    /**
     * 处理字段
     * @param vo
     * @param user
     * @return
     */
    void dealField(FexTranLifeCycleVo vo, ICurrentUser user, FexTranLifeCycle entity);

    /**
     * 审批通过的逻辑
     * @param fexTranLifeCycle
     * @param user
     */
    void approvePass(FexTranLifeCycle fexTranLifeCycle,ICurrentUser user);
}
