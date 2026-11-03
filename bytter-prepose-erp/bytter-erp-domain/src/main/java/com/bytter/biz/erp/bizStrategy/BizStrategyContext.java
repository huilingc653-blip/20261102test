package com.bytter.biz.erp.bizStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 业务策略上下文
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
@Component
public class BizStrategyContext {
    @Autowired
    private List<BizStrategy> strategies;

    /**
     * 根据服务代码和版本号获取对应的策略
     * @param serviceCode 服务代码
     * @param version 版本号
     * @return 匹配的策略，如果没有找到则返回null
     */
    public BizStrategy getStrategy(String serviceCode, String version) {
        for (BizStrategy strategy : strategies) {
            if (strategy.matches(serviceCode, version)) {
                return strategy;
            }
        }
        return null;
    }
}
