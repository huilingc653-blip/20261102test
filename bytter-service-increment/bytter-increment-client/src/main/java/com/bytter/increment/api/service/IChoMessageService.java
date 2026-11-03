package com.bytter.increment.api.service;
/**
 * 理财日志接口类
 * @author wangzx
 * @since 2019-10-32
 */
public interface IChoMessageService {
    /**
     * 写入日志信息表
     *
     * @param billId      外部引用ID
     * @param messageType 日志类型（参考NisMessageConstant类）
     * @param message     日志信息
     * @param busType     业务类型（参考NisMessageConstant类）
     * @param userCode    操作人代码
     * @param userName    操作人名称
     * @return true/false
     */
    boolean saveMessage(String billId, Integer messageType, String message, String busType, String userCode, String userName);
}
