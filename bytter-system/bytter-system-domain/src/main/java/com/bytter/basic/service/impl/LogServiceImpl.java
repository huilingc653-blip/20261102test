package com.bytter.basic.service.impl;

import com.bytter.basic.entity.BtLog;
import com.bytter.basic.service.LogService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.ILogService;
import com.bytter.system.api.dto.LogDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志Service接口实现类
 * @author zhouj
 *
 */
@Service("logServiceImpl")
@DubboService
@Transactional
public class LogServiceImpl implements ILogService {
    @Autowired
    private LogService logService;

    @Override
    public void saveLog(LogDTO logDTO) {
        BtLog btLog = new BtLog();
        BeanUtils.copyProperties(logDTO, btLog);
        logService.saveEntity(btLog);
    }
}
