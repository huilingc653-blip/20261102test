package com.bytter.system.mobile.controller;
import com.bytter.system.service.CorpService;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
/**
 * @author chenlw
 * @since 2020-04-28
 */
@Controller
@RequestMapping("/api/corp")
public class MobileCorpController extends BaseController {
    @Resource
    private CorpService corpService;
    @GetMapping("/corpList")
    @ResponseBody
    public Object corpList(String resource) {
        if (StringUtils.isNotBlank(resource)) {
            return corpService.getCorpListByUserId(resource, getUserId());
        }
        return corpService.getCorpListByUserId(getUserId());
    }
}
