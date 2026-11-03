package com.bytter.core.base;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.IMessageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
/**
 * 基础控制器 todo 放置位置不合适 auth服务提供对应服务
 * 
 * @author zhoul
 * @since 2018/1/15.
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 返回用户包装类
     *
     * @return ShiroUser
     * @deprecated by zhoul 2018-01-03 17:22 <br>
     *             此方法已过时，推荐使用{@link #getCurrentUser()}方法
     */
    @Deprecated
    protected ShiroUser getUserVo() {
        return (ShiroUser) UserContextHelper.helper().getCurrentUser();
    }
    protected ICurrentUser getCurrentUser() {
        return UserContextHelper.helper().getCurrentUser();
    }
    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    protected String getUserId() {
        ICurrentUser currentUser = getCurrentUser();
        return currentUser.getId();
    }
    /**
     * 获取当前用户登录名
     *
     * @return 当前用户登录名
     */
    protected String getUserLoginName() {
        ICurrentUser currentUser = getCurrentUser();
        return currentUser.getLoginName();
    }
    private AjaxJson getSuccess() {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
    /**
     * 返回系统成功信息，默认提示国际化Key“common.message.success”
     *
     * @return AjaxJson 成功标识
     */
    protected AjaxJson success(Object data) {
        return AjaxJson.success(data);
    }
    protected AjaxJson success() {
        return AjaxJson.success();
    }
    /**
     * 返回用户自定义的消息枚举
     *
     * @param messageEnum
     *            业务消息枚举
     * @return AjaxJson
     */
    protected AjaxJson success(IMessageEnum messageEnum) {
        AjaxJson ajaxJson = getSuccess();
        ajaxJson.setMessageEnum(messageEnum);
        return ajaxJson;
    }
    /**
     * 返回用户自定义的消息枚举
     *
     * @param messageEnum
     *            消息枚举
     * @param params
     *            数组形式，填充国际化占位符参数
     * @return AjaxJson
     * @author 周亮
     */
    protected AjaxJson success(IMessageEnum messageEnum, String... params) {
        AjaxJson ajaxJson = success(messageEnum);
        ajaxJson.setParams(params);
        return ajaxJson;
    }
    /**
     * 创建TOKEN
     *
     * @param tokenName
     *            the token name used in view
     * @param secondsOfTimeOut
     *            the seconds of time out, secondsOfTimeOut &gt;= Globals.MIN_SECONDS_OF_TOKEN_TIME_OUT
     */
    protected void createToken(HttpServletRequest request, String tokenName, int secondsOfTimeOut) {
    }
    /**
     * Create a token with default token name and with default seconds of time out.
     */
    protected void createToken(HttpServletRequest request) {
        createToken(request, Globals.DEFAULT_TOKEN_NAME, Globals.DEFAULT_SECONDS_OF_TOKEN_TIME_OUT);
    }
    /**
     * Create a token with default seconds of time out.
     *
     * @param tokenName
     *            the token name used in view
     */
    protected void createToken(HttpServletRequest request, String tokenName) {
        createToken(request, tokenName, Globals.DEFAULT_SECONDS_OF_TOKEN_TIME_OUT);
    }
    /**
     * Check token to prevent resubmit.
     *
     * @param tokenName
     *            the token name used in view's form
     * @return true if token is correct
     */
    protected boolean validateToken(HttpServletRequest request, String tokenName) {
        return true;
    }
    /**
     * Check token to prevent resubmit with default token key ---&gt; "BYTTER_TOKEN_KEY"
     *
     * @return true if token is correct
     */
    protected boolean validateToken(HttpServletRequest request) {
        return validateToken(request, Globals.DEFAULT_TOKEN_NAME);
    }
}
