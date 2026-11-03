package com.bytter.biz.foreign.service.impl;

import com.bytter.biz.foreign.service.FexMessageService;
import com.bytter.biz.payment.api.service.IFexMessageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author huangx
 * @Description 外汇管理日志接口实现类
 * @date 2019.7.19
 */
@Service("fexMessageServiceImpl")
@Transactional
@DubboService
public class FexMessageServiceImpl implements IFexMessageService {

    @Autowired
    private FexMessageService fexMessageService;

    /**
     * 写入日志信息表
     *
     * @param billId      外部引用ID
     * @param messageType 日志类型
     * @param message     日志信息
     * @param busType     业务类型
     * @param userCode    操作人代码
     * @param userName    操作人名称
     * @return
     */
    @Override
    public boolean saveMessage(String billId, Integer messageType, String message, String busType, String userCode, String userName) {
        return this.fexMessageService.saveMessage(billId, messageType, message, busType, userCode, userName);
    }

}
