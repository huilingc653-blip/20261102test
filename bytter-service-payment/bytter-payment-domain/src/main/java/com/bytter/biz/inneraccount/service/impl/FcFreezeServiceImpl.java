package com.bytter.biz.inneraccount.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.inneraccount.service.FcFreezeService;
import com.bytter.biz.payment.api.dto.FcFreezeDTO;
import com.bytter.biz.payment.api.service.IFcFreezeService;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IFcAccService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author luoyj
 * @date 2023/9/13.
 */
@DubboService
@Service("fcFreezeServiceImpl")
public class FcFreezeServiceImpl implements IFcFreezeService {
    @Autowired
    private FcFreezeService fcFreezeService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;

    @Override
    public List<FcFreezeDTO> findNoCheckFreeze(Date closeDate,List<String> accList) {
        if (!CollectionUtil.isNotEmpty(accList)) {
            return null;
        }
        return fcFreezeService.findNoCheckFreeze(closeDate, accList);
    }
}
