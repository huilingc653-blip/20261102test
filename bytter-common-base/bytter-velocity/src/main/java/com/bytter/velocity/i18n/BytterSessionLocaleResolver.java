package com.bytter.velocity.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by zhoul on 2017/5/3.
 * <p>
 * 扩展 SessionLocaleResolver，将 locale 持久化到 HTTP Session 中，
 * 确保语言切换后重定向请求能正确读取用户选择的语言。
 */
public class BytterSessionLocaleResolver extends SessionLocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return super.resolveLocale(request);
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response,
                                 LocaleContext localeContext) {
        super.setLocaleContext(request, response, localeContext);
    }
}
