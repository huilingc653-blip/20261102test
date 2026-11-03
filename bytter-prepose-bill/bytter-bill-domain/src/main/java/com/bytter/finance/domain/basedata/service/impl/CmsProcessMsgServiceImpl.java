package com.bytter.finance.domain.basedata.service.impl;
import com.bytter.finance.api.ICmsProcessMsgService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.core.base.entity.ICurrentUser;

@DubboService
@Service("cmsProcessMsgServiceImpl")
@Transactional
public class CmsProcessMsgServiceImpl implements ICmsProcessMsgService {
    @Autowired
    private CmsProcessMsgService cmsProcessMsgService;
    @Override
    public void insertProcessMsg(String billCode, String operationType, Integer messageType, String msg,
            ICurrentUser user) {
        cmsProcessMsgService.insertProcessMsg(billCode, operationType, messageType, msg, user);
    }
}
