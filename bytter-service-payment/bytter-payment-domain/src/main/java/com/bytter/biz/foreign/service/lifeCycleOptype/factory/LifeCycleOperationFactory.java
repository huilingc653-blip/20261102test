package com.bytter.biz.foreign.service.lifeCycleOptype.factory;


import com.bytter.biz.foreign.enums.LifeCycleOperationEnum;
import com.bytter.biz.foreign.service.lifeCycleOptype.strategy.LifeCycleOperationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LifeCycleOperationFactory {
    @Autowired
    private Map<String, LifeCycleOperationStrategy> strategyMap;


    public LifeCycleOperationStrategy getStrategy(String operationType) {
        LifeCycleOperationEnum lifeCycleOperationEnum = LifeCycleOperationEnum.fromCode(operationType);
        return strategyMap.get(lifeCycleOperationEnum.getOpTypeName());
    }
}
