package com.bytter.system.api.i18n;

import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * 国际化的MessageSource，把从文件读取的资源转换为从数据库中读取
 *
 * @author 其他服务都继承system 都可以vm本地化
 * @since 2017年5月3日 10:07:34
 */
public class DatabaseMessageSource extends AbstractMessageSource {
    /*@DubboReference
    private IMultiLangService multiLangServiceImpl;
    private String getText(String code, Locale locale) {
        return multiLangServiceImpl.getText(code, locale);
    }*/

    @Autowired
    private IMultiLangService multiLangServiceI;

    private String getText(String code, Locale locale) {
        return multiLangServiceI.getText(code, locale);
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getText(code, locale);
        return createMessageFormat(msg, locale);
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        return getText(code, locale);
    }
}
