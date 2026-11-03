package com.bytter.system.web.controller;

import com.bytter.system.service.MultiLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 登录页公开语言资源，仅返回登录页面实际使用的文本。
 *
 * @author laozhao
 * @since 2026-10-15
 */
@Controller
public class LoginLanguageController {

    private static final List<String> LOGIN_LANGUAGE_KEYS = Collections.unmodifiableList(Arrays.asList(
            "common.layer.titleInformation",
            "common.button.sure",
            "common.button.cancel",
            "sys.form.verify.noIDName",
            "sys.form.verify.noPassWord",
            "sys.login.TianweiCertificate",
            "sys.login.caLoginFail",
            "sys.login.insertCertificate",
            "sys.login.message.login.success",
            "sys.login.selectEncryptionType"
    ));

    @Autowired
    private MultiLangService multiLangService;

    /**
     * 获取固定范围的登录页语言文本，不接受客户端指定语言键或扩大查询范围。
     *
     * @param locale 当前请求的语言，沿用系统现有语言解析和中文回退方式
     * @return 仅包含 langKey、langContext 的登录文本列表，不包含内部业务文本或维护字段
     */
    @GetMapping("/lang/login")
    @ResponseBody
    public List<Map<String, String>> getLoginLang(Locale locale) {
        List<Map<String, String>> result = new ArrayList<>();
        for (String key : LOGIN_LANGUAGE_KEYS) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("langKey", key);
            item.put("langContext", multiLangService.getLang(key, locale));
            result.add(item);
        }
        return result;
    }
}
