package com.bytter.system.saas.controller.web;

import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.entity.Config;
import com.bytter.system.service.ConfigService;
import com.bytter.system.service.UserConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zhoul on 2017/4/11.
 */
@Api(value = "/v1-system/sys/config",tags = "系统配置项Controller")
@RestController("v1ConfigController")
@RequestMapping("/v1-system/sys/config")
public class ConfigController extends BaseController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private ICorpService corpServiceI;
    @GetMapping("/type/{type}/name/{name}")
    @ResponseBody
    public AjaxJson<Object> getType(@PathVariable("type") String type, @PathVariable("name") String name) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(name)) {
            throw new ParameterException(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
        }
        return success(configService.getByTypeAndName(type, name));
    }
    @ResponseBody
    @GetMapping("/system")
    public AjaxJson<Map<String, Object>> system() {
        return success(configService.listByType(ConfigConstant.SYS));
    }
    @ApiOperation(value = "保存系统配置项")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson systemPost(@RequestBody Map<String, Object> configMap) {
        String[] userName = new String[]{getUserLoginName()};
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("UName", userName);

        for (Map.Entry<String, Object> e : configMap.entrySet()) {
            if ("sys.attachmentFormat".equals(e.getKey()) && e.getValue() != null) {
                String val = e.getValue().toString();
                // 对数组类型的值进行特殊处理
                if (e.getValue() instanceof java.util.List) {
                    val = ((java.util.List<?>) e.getValue()).stream()
                            .map(Object::toString)
                            .reduce((a, b) -> a + "," + b)
                            .orElse("");
                }
                val = val.replaceAll("，", ",").replaceAll("[.。、|【】 \\[\\]]", "");
                val = Pattern.compile("[',']+").matcher(val).replaceAll(",");
                val = Pattern.compile("^,").matcher(val).replaceAll("");
                val = Pattern.compile(",$").matcher(val).replaceAll("");
                map.put(e.getKey(), new String[]{val});
                continue;
            }

            // 处理其他类型的值
            String valueStr;
            if (e.getValue() instanceof java.util.List) {
                // 将List转换为逗号分隔的字符串
                valueStr = ((java.util.List<?>) e.getValue()).stream()
                        .map(Object::toString)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");
                map.put(e.getKey(), new String[]{valueStr});
            } else if (e.getValue() instanceof String[]) {
                map.put(e.getKey(), (String[]) e.getValue());
            } else {
                map.put(e.getKey(), new String[]{e.getValue() != null ? e.getValue().toString() : ""});
            }
        }
        configService.saveOrUpdate(map, getUserId());
        return success();
    }
    @PostMapping("/saveSkin")
    @ResponseBody
    public Object saveSkip(HttpServletRequest request) {
        userConfigService.saveOrUpdateIndexSkin(getUserId(), request.getParameter("content"));
        return success();
    }
    @GetMapping("/advert")
    @ResponseBody
    public String advert() {
        ICurrentUser user = getCurrentUser();
        Config configCode = configService.getByTypeAndName("sys", "scfCode");
        Config configUrl = configService.getByTypeAndName("sys", "scfUrl");
        String custIdentifition = "", url = "", corpCode = "";
        if (configCode != null && configUrl != null) {
            custIdentifition = configCode.getValue();
            url = configUrl.getValue();
        }
        CorpDTO corp = corpServiceI.get(user.getCorpId());
        if (corp != null) {
            corpCode = corp.getCode();
        }
        if (StringUtils.isNotBlank(custIdentifition) && StringUtils.isNotBlank(url) && StringUtils.isNotBlank(corpCode)) {
            String sb = Base64.encodeBase64String(user.getLoginName().getBytes()) + "." +
                    Base64.encodeBase64String(corpCode.getBytes()) + "." +
                    Base64.encodeBase64String(custIdentifition.getBytes()) + "." +
                    Base64.encodeBase64String(String.valueOf(System.currentTimeMillis()).getBytes());
            String extendParamsJson = Base64.encodeBase64String(sb.getBytes());
            return url + extendParamsJson;
        }
        return "";
    }
}
