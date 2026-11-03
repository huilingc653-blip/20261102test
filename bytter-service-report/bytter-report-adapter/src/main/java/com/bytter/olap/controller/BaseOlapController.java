package com.bytter.olap.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.olap.dto.OlapActionLog;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zyong on 2017/9/28.
 */
public class BaseOlapController extends BaseController {

    protected Logger LOG = LoggerFactory.getLogger(this.getClass());

//    @Value("${log.negativeFilter}")
//    protected String negativeFilter;
//
//    @Value("${log.positveFilter}")
//    protected String positveFilter;

    protected Map<String,Object> sqlParams = new HashMap<>();

    @ModelAttribute
    public void initalAuthUser(HttpServletRequest request) {
//        String url = request.getRequestURL().toString();
//        String log = new OlapActionLog(url).toString();
//
//        boolean isNegtiveMatch = false, isPositveMatch = true;
//
//        if (StringUtils.isNotBlank(positveFilter)) {
//            isPositveMatch = Pattern.compile(positveFilter).matcher(log).find();
//        }
//
//        if (StringUtils.isNotBlank(negativeFilter)) {
//            isNegtiveMatch = Pattern.compile(negativeFilter).matcher(log).find();
//        }
//
//        if (!isNegtiveMatch && isPositveMatch) {
//            LOG.info(log);
//        }
//        ICurrentUser currentUser = getCurrentUser();
//        if (currentUser != null) {
//            sqlParams.put("corpId", currentUser.getCorpId());
//            sqlParams.put("userId", currentUser.getId());
//        }

    }


}
