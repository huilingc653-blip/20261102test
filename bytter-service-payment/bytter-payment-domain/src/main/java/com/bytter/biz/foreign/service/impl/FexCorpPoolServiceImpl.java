package com.bytter.biz.foreign.service.impl;

import com.bytter.biz.foreign.service.FexCorpPoolService;
import com.bytter.biz.payment.api.service.IFexCorpPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 外汇资金池接口实现类
 * @author wangzx
 * @since 2020-3-30 16:49:44
 */
@Service("fexCorpPoolServiceImpl")
@Transactional
public class FexCorpPoolServiceImpl implements IFexCorpPoolService {

    @Autowired
    private FexCorpPoolService fexCorpPoolService;

    /**
     * 根据资金池id查找外汇资金池维护是否使用
     * @param poolId
     * @return
     */
    @Override
    public boolean isUsePool(String poolId) {
        return fexCorpPoolService.isUsePool(poolId);
    }
}
