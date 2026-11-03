package com.bytter.system.service;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.constants.LoginoutConstant;
import com.bytter.system.dao.LoginLogDao;
import com.bytter.system.entity.LoginLog;
import com.bytter.system.vo.LoginLogVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Service("loginLogService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class LoginLogService extends BaseService<LoginLog, String> {
    @Autowired
    private LoginLogDao loginLogDao;
    @Autowired
    private ICorpService corpServiceI;
    @Override
    public BaseDao<LoginLog, String> getEntityDao() {
        return loginLogDao;
    }
    public DataTablesOutput listLoginLog(DataTablesInput input, ICurrentUser user) {
        List<String> userCorpIds = corpServiceI.getUserCorpIds(SysResourceConstant.SYS_LOGIN_LOG_MANAGE_VIEW, user.getId());
        DataTablesOutput dataTablesOutput = loginLogDao.listLoginLog(input, userCorpIds, user.getId());
        if(!CollectionUtils.isEmpty(dataTablesOutput.getData())) 
        {
        	List<LoginLogVO> list = dataTablesOutput.getData();
        	for(LoginLogVO loginLogVO : list) {
        		 if(loginLogVO.getType()==null) {
        			 loginLogVO.setTypeLang(LoginoutConstant.USER_PASS_GRANT);
        		 }
        		 if(loginLogVO.getType()==0) {
        			 loginLogVO.setTypeLang(LoginoutConstant.USER_PASS_GRANT);
        		 }else if(loginLogVO.getType()==1) {
        			 loginLogVO.setTypeLang(LoginoutConstant.CREDIT_PASS_GRANT);
        		 }else if(loginLogVO.getType()==2) {
        			 loginLogVO.setTypeLang(LoginoutConstant.CREDIT_GRANT);
        		 }else if(loginLogVO.getType()==3) {
        			 loginLogVO.setTypeLang(LoginoutConstant.SS0_GRANT);
        		 }else {
        			 loginLogVO.setTypeLang(LoginoutConstant.USER_PASS_GRANT);
        		 }
        	}
        	dataTablesOutput.setData(list);
        }
        return dataTablesOutput;
    }
    @Transactional
    @Override
    public void saveEntity(LoginLog entity) {
        handleHtmlEscapeDecimal(entity);
        super.saveEntity(entity);
    }
    /**
     * Html转义及防止javascript注入攻击
     *
     * @param entity 日志实体
     */
    private void handleHtmlEscapeDecimal(LoginLog entity) {
        // Html转义及防止javascript注入攻击
        String userAgent = entity.getUserAgent();
        if (StringUtils.isNotBlank(userAgent)) {
            entity.setUserAgent(HtmlUtils.htmlEscapeDecimal(userAgent, StandardCharsets.UTF_8.name()));
        }
        String host = entity.getHost();
        if (StringUtils.isNotBlank(host)) {
            entity.setHost(HtmlUtils.htmlEscapeDecimal(host, StandardCharsets.UTF_8.name()));
        }
    }
    public void saveAddUsername(LoginLog loginLog, String username) {
        try {
            String userAgent = loginLog.getUserAgent();
            if (userAgent.length() > 300) {
                loginLog.setUserAgent(userAgent.substring(0, 300));
            }
//            User user = userService.getUserByLoginName(username);
            loginLog.setLoginName(username);
        } catch (Exception ex) {
            logger.info("user login log error:", ex);
        }
        this.saveEntity(loginLog);
    }
    public LoginLog getLoginLogBySessionId(String sessionId) {
        List<LoginLog> result = loginLogDao.listBy("sessionId", sessionId);
        if (EmptyUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }
}
