package com.bytter.finance.api;
import com.bytter.core.base.entity.ICurrentUser;
/**
 * 插入日志接口
 */
public interface ICmsProcessMsgService {
    /**
     * 插入日志
     * 
     * @param billCode
     *            业务编号
     * @param operationType
     *            操作信息
     * @param messageType
     *            消息类型
     * @param msg
     *            操作信息
     * @param user
     *            当前用户
     */
    public void insertProcessMsg(String billCode, String operationType, Integer messageType, String msg,
            ICurrentUser user);
}
