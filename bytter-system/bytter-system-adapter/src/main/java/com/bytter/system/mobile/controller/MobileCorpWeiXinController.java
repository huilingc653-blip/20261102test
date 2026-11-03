package com.bytter.system.mobile.controller;
import com.bytter.system.api.IAppH5Service;
import com.bytter.core.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
/**
 *
 */
@Controller
@RequestMapping("/mobile/corpWx")
public class MobileCorpWeiXinController extends BaseController {
    @Resource
    private IAppH5Service appH5Service;
    @GetMapping("/appH5Index")
    public String qywxAppH5Index(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Cookie[] cookies = request.getCookies();
//        try {
//            // 判断是否存在Cookie，不存在跳转到首页
//            if (cookies == null) {
//                response.sendRedirect("./index.jsp");
//            }
//        } catch (Exception e) {
//
//        }
//        // 企业微信端跳转到H5页面入口地址
//        // 接口判断当前请求是否有标识成员身份的cookie信息
//        // 如果有，则校验合法性，校验通过则跳转到H5首页；若校验不通过则跳转到登录页面。
//        return "";
        try {
        } catch (Exception e) {
            // 跳转到身份验证失败页面
            String msg = "Error：" + e.getMessage();
            log.error(msg, e);
            return appH5Service.getAuthenticationFailPageUrl(msg);
        }
        // 返回APP-H5首页的URL：
        return appH5Service.getAppH5IndexUrlByLoginName("admin");
    }
}
