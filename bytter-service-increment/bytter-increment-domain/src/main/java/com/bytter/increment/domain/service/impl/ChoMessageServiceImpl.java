package com.bytter.increment.domain.service.impl;
import com.bytter.increment.domain.service.ChoMessageService;
import com.bytter.increment.api.service.IChoMessageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 理财日志接口实现类
 * @author wangzx
 * @since 2019-10-32
 */
@Service("choMessageServiceImpl")
@Transactional
@DubboService
public class ChoMessageServiceImpl implements IChoMessageService {
    @Autowired
    private ChoMessageService choMessageService;
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
        return this.choMessageService.saveMessage(billId, messageType, message, busType, userCode, userName);
    }
}
