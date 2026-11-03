package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.entity.Config;
import com.bytter.system.service.ConfigService;
import com.bytter.system.service.UserConfigService;
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
@Controller
@RequestMapping("/sys/config")
public class ConfigController extends BaseController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private ICorpService corpServiceI;
    @GetMapping("/type/{type}/name/{name}")
    @ResponseBody
    public Object getType(@PathVariable("type") String type, @PathVariable("name") String name) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(name)) {
            throw new ParameterException(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
        }
        return configService.getByTypeAndName(type, name);
    }
    @GetMapping("/system")
    public void system(Model model) {
        model.addAttribute("config", configService.listByType(ConfigConstant.SYS));
    }
    @PostMapping("/save")
    @ResponseBody
    public Object systemPost(HttpServletRequest request) {
        String[] userName = new String[]{getUserLoginName()};
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("UName", userName);
        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            if ("sys.attachmentFormat".equals(e.getKey()) && e.getValue() != null && StringUtils.isNotBlank(Arrays.toString(e.getValue()))) {
                String val = Arrays.toString(e.getValue()).replaceAll("，", ",").replaceAll("[.。、|【】 \\[\\]]", "");
                val = Pattern.compile("[',']+").matcher(val).replaceAll(",");
                val = Pattern.compile("^,").matcher(val).replaceAll("");
                val = Pattern.compile(",$").matcher(val).replaceAll("");
                map.put(e.getKey(), new String[]{val});
                continue;
            }
            map.put(e.getKey(), e.getValue());
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
