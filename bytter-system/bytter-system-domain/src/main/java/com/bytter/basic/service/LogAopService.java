package com.bytter.basic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.basic.entity.BtLog;
import com.bytter.core.base.entity.BtLogDTO;
import com.bytter.core.utils.BeanUtils;

/**
 * 保存操作记录
 * Created by wade on 2025/6/3.
 */
@Component
public class LogAopService {
    @Autowired
    private LogService logService;


    @Async("logPoolExecutor")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void asynSaveLog(BtLogDTO btLogDTO) {
        BtLog log = new BtLog();
        BeanUtils.copyNotNullProperties(btLogDTO, log);
        logService.saveEntity(log);
    }
}
