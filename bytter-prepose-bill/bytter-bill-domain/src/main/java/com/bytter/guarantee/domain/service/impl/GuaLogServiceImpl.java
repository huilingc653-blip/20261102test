package com.bytter.guarantee.domain.service.impl;

import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.guarantee.api.IGuaLogService;
import com.bytter.guarantee.domain.entity.GuaLog;
import com.bytter.guarantee.domain.service.GuaLogService;
import com.bytter.guarantee.dto.GuaLogDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 担保日志保存 保函日志也在此处上报
 */
@DubboService(async = true)
@Service
public class GuaLogServiceImpl implements IGuaLogService {

    @Autowired
    private GuaLogService guaLogService;

    @Override
    public void saveGuaLog(GuaLogDTO guaLogDTO) {
        GuaLog guaLog = new GuaLog();
        BeanUtils.copyProperties(guaLogDTO,guaLog);
        guaLog.setCreateDate(DateUtils.getDate());
        guaLogService.save(guaLog);
    }
}
