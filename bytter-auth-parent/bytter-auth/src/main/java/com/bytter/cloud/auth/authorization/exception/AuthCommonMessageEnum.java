package com.bytter.cloud.auth.authorization.exception;
import com.bytter.cloud.common.core.exception.ErrorType;
import lombok.Getter;
/**
 * 公共消息
 *
 * @author pzc 2021-07-18
 * @since 1.0.0
 */
@Getter
public enum AuthCommonMessageEnum implements ErrorType {
    /**
     * System服务请求参数校验不通过
     */
    ARGUMENT_NOT_VALID("AU4000", "请求参数校验不通过"),
    /**
     * System服务操作失败
     */
    COMMON_TIPS_FAIL("AU5000", "操作失败!"),
    /**
     * 工作流服务正在维护或暂停服务，请稍后再试
     */
    WORKFLOW_CONNECT_TIME_OUT("WF5004", "工作流服务正在维护或暂停服务，请稍后再试"),
    /**
     * 工作流服务正在维护或暂停服务，请稍后再试
     */
    ACCOUNT_CONNECT_TIME_OUT("AC5004", "账户管理服务正在维护或暂停服务，请稍后再试"),
    /**
     * 基础服务正在维护或暂停服务
     */
    MANAGER_CONNECT_TIME_OUT("MG5004", "平台管理服务正在维护或暂停服务，请稍后再试"),
    /**
     * 基础正在维护或暂停服务
     */
    BASIC_CONNECT_TIME_OUT("BS5004", "基础服务正在维护或暂停服务，请稍后再试"),
    /**
     * 分布式ID服务正在维护或暂停服务
     */
    DISTRIBUTE_ID_CONNECT_TIME_OUT("DI5004", "分布式ID服务正在维护或暂停服务，请稍后再试"),
    /**
     * 系统管理服务正在维护或暂停服务
     */
    SYSTEM_CONNECT_TIME_OUT("ST5004", "系统管理服务正在维护或暂停服务，请稍后再试"),
    /**
     * 支付结算服务正在维护或暂停服务
     */
    PAY_CONNECT_TIME_OUT("PA5004", "支付结算服务正在维护或暂停服务，请稍后再试"),
    /**
     * 资金归集服务正在维护或暂停服务
     */
    GATEWAY_CONNECT_TIME_OUT("AL5004", "资金归集服务正在维护或暂停服务，请稍后再试");
    /**
     * 错误码
     */
    private String code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 国际化键
     */
    private String name;
    AuthCommonMessageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.name = msg;
    }
}
