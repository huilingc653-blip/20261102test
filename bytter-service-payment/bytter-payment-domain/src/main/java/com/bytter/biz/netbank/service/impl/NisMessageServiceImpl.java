package com.bytter.biz.netbank.service.impl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.payment.api.service.INisMessageService;
/**
 * @author huangx
 * @Description 结算日志接口实现类
 * @date 2017.9.14
 */
@Service("nisMessageServiceImpl")
@Transactional
@DubboService
public class NisMessageServiceImpl implements INisMessageService {
    @Autowired
    private NisMessageService nisMessageService;
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
        return this.nisMessageService.saveMessage(billId, messageType, message, busType, userCode, userName);
    }
}
