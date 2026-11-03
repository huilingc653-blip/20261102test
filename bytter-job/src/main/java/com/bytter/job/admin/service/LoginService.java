package com.bytter.job.admin.service;

import com.bytter.job.admin.core.model.XxlJobLoginLog;
import com.bytter.job.admin.core.model.XxlJobUser;
import com.bytter.job.admin.core.util.CookieUtil;
import com.bytter.job.admin.core.util.I18nUtil;
import com.bytter.job.admin.core.util.JacksonUtil;
import com.bytter.job.admin.dao.XxlJobLoginLogDao;
import com.bytter.job.admin.dao.XxlJobUserDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.IpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author xuxueli 2019-05-04 22:13:264
 */
@Configuration
public class LoginService {
    public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";
    @Resource
    private XxlJobUserDao xxlJobUserDao;
    @Resource
    private XxlJobLoginLogDao xxlJobLoginLogDao;

    private String makeToken(XxlJobUser xxlJobUser){
        String tokenJson = JacksonUtil.writeValueAsString(xxlJobUser);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }
    private XxlJobUser parseToken(String tokenHex){
        XxlJobUser xxlJobUser = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());      // username_password(md5)
            xxlJobUser = JacksonUtil.readValue(tokenJson, XxlJobUser.class);
        }
        return xxlJobUser;
    }
    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, String username, String password, boolean ifRemember){
        // param
        if (username==null || username.trim().length()==0 || password==null || password.trim().length()==0){
            return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
        }
        // valid passowrd
        XxlJobUser xxlJobUser = xxlJobUserDao.loadByUserName(username);
        if (xxlJobUser == null) {
            // 记录登录日志
            initLoginLog(username, 0, I18nUtil.getString("login_param_unvalid"), 0);
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(xxlJobUser.getPassword())) {
            // 记录登录日志
            initLoginLog(username, 0, I18nUtil.getString("login_param_unvalid"), 0);
            return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
        }
        String loginToken = makeToken(xxlJobUser);
        // 记录登录日志
        initLoginLog(username, 1, null, xxlJobUser.getId());
        // do login
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
        return ReturnT.SUCCESS;
    }

    /**
     * 添加登陆日志。
     *
     * @param username 用户名。
     * @param status 登录状态。0-失败；1-成功。
     */
    private void initLoginLog(String username, int status, String reason, int userId) {
        XxlJobLoginLog loginLog = new XxlJobLoginLog();
        loginLog.setLoginTime(new Date());
        loginLog.setUserId(userId);
        loginLog.setUsername(username);
        loginLog.setIp(IpUtil.getIp());
        loginLog.setStatus(status);
        loginLog.setReason(reason);
        loginLog.setCreateBy("system");
        loginLog.setCreateTime(new Date());
        xxlJobLoginLogDao.save(loginLog);
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return ReturnT.SUCCESS;
    }
    /**
     * logout
     *
     * @param request
     * @return
     */
    public XxlJobUser ifLogin(HttpServletRequest request, HttpServletResponse response){
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (cookieToken != null) {
            XxlJobUser cookieUser = null;
            try {
                cookieUser = parseToken(cookieToken);
            } catch (Exception e) {
                logout(request, response);
            }
            if (cookieUser != null) {
                XxlJobUser dbUser = xxlJobUserDao.loadByUserName(cookieUser.getUsername());
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }
}
