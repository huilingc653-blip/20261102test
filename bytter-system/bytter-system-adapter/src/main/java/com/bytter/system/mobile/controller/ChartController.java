package com.bytter.system.mobile.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.dto.MobileChartMenuDTO;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.User;
import com.bytter.system.entity.UserMobileChartModule;
import com.bytter.system.enums.MobileChartTopMenuEnum;
import com.bytter.system.service.MobileChartModuleService;
import com.bytter.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户管理控制器
 * Created by zhoul on 2017/4/11.
 */

@Controller
@RequestMapping("/api/chart")
public class ChartController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private MobileChartModuleService mobileChartModuleService;

    @Value("${bytter.app.url}")
    private String appUrl;

    @RequestMapping("/mainPage")
    public String mainPage(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        return "sys/mobile/appMain";
    }

    @RequestMapping("/toPage")
    public String toPage(HttpServletRequest request) {
        String url = request.getParameter("forwardURL");
        if (url != null) {
            Map<String, String[]> queryParams = request.getParameterMap();
            String newUriPath = url + "?" + queryParams.entrySet().stream()
                    .filter(entry -> (ObjectUtil.notEqual(entry.getKey(), "forwardURL")))
                    .map(entry -> entry.getKey() + "=" + URLUtil.encode(entry.getValue()[0]))
                    .collect(Collectors.joining("&"));
            String requestUrl = appUrl + newUriPath + "&customMethod=POST";
            logger.info("requestUrl==>【{}】", requestUrl);
            return "redirect:" + requestUrl;
        }
        return "sys/mobile/toPage";
    }

    /**
     * APP报表首页
     *
     * @param model
     * @return
     */
    @RequestMapping("/appChartIndex")
    public String appChartIndex(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        model.addAttribute("mobileChartModuleList", mobileChartModuleService.getShowMobileChartTopMenuEnumList(userId));
        // 获取月份
        String[] months = new String[6];
        Date curDate = DateUtils.getDate();
        for (int i = months.length; i > 0; i--) {
            int m = i - 1;
            int month = DateUtils.getMonth(DateUtils.addMonth(curDate, -m));
            months[months.length - i] = month + "月";
        }
        model.addAttribute("months", StringUtils.join(months, CommonConstant.COMMA_SPLIT));
        return "sys/mobile/appChartIndex";
    }

    /**
     * APP报表二级菜单页面
     *
     * @param chartModuleKey 报表模块KEY
     * @param model
     * @return
     */
    @RequestMapping("/appSecondChartMenuPage")
    public String secondChartMenuPage(String chartModuleKey, Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        model.addAttribute("pageTitle", mobileChartModuleService.getMobileChartModuleName(chartModuleKey));
        model.addAttribute("chartModuleKey", chartModuleKey);
        model.addAttribute("menuList", mobileChartModuleService.getMobileChartMenuList(getUserId(), chartModuleKey));
        return "sys/mobile/chartModules/appSecondChartMenuPage";
    }

    /**
     * 统计报表-综合查询
     * 【报表-信贷融资-查询】
     * @param request
     * @param menuId
     * @param chartModuleKey
     * @param netId
     * @param corpId
     * @return
     */
    @RequestMapping("/getMobileChartTabMenuList")
    @ResponseBody
    public List<MobileChartMenuDTO> getMobileChartTabMenuList(HttpServletRequest request, String menuId, String chartModuleKey,
                                                              @RequestParam(required = false) String netId, @RequestParam(required = false) String corpId) {
        String accessToken = request.getParameter("access_token");
        // System.out.println("accessToken = " + accessToken);
        List<MobileChartMenuDTO> mobileChartMenuDTOS = mobileChartModuleService.getMobileChartTabMenuList(getUserId(), menuId, chartModuleKey);
        for (MobileChartMenuDTO mobileChartMenuDTO : mobileChartMenuDTOS) {
            mobileChartMenuDTO.setCorpId(corpId);
            mobileChartMenuDTO.setNetId(netId);
            mobileChartMenuDTO.setAccessToken(accessToken);
        }
        return mobileChartMenuDTOS;
    }


    /**
     * APP 统计报表页面
     *
     * @param model model
     * @return
     */
    @RequestMapping("/appStatisticalReportIndex")
    public String appStatisticalReportIndex(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        model.addAttribute("menuList", mobileChartModuleService.getMobileChartMenuList(getUserId(),
                MobileChartTopMenuEnum.statisticalReport.name()));
        return "sys/mobile/chartModules/appStatisticalReportIndex";
    }


    /**
     * APP报表-结算管理
     *
     * @param model model
     * @return
     */
    @RequestMapping("/appNetbankReportIndex")
    public String appNetbankReportIndex(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        model.addAttribute("menuList", mobileChartModuleService.getMobileChartMenuList(getUserId(),
                MobileChartTopMenuEnum.netbank.name()));
        return "sys/mobile/chartModules/appNetbankReportIndex";
    }

    /**
     * APP报表-承兑汇票
     *
     * @param model model
     * @return
     */
    @RequestMapping("/appAccebillReportIndex")
    public String appAccebillReportIndex(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        return "sys/mobile/chartModules/appAccebillReportIndex";
    }


    /**
     * APP报表-资金计划
     *
     * @param model model
     * @return
     */
    @RequestMapping("/appFbsBudgetReportIndex")
    public String appFbsBudgetReportIndex(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        model.addAttribute("chartModuleKey", MobileChartTopMenuEnum.fbsBudget.name());
        model.addAttribute("menuList", mobileChartModuleService.getMobileChartMenuList(getUserId(),
                MobileChartTopMenuEnum.fbsBudget.name()));
        return "sys/mobile/chartModules/appFbsBudgetReportIndex";
    }

    /**
     * APP报表-理财管理
     *
     * @param model model
     * @return
     */
    @RequestMapping("/appChowmatisticReportIndex")
    public String appChowmatisticReportIndex(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        model.addAttribute("menuList", mobileChartModuleService.getMobileChartMenuList(getUserId(),
                MobileChartTopMenuEnum.chowmatistic.name()));
        return "sys/mobile/chartModules/appChowmatisticReportIndex";
    }

    /**
     * APP报表-信贷融资
     *
     * @param model model
     * @return
     */
    @RequestMapping("/appCmsReportIndex")
    public String appCmsReportIndex(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        model.addAttribute("menuList", mobileChartModuleService.getMobileChartMenuList(getUserId(),
                MobileChartTopMenuEnum.cms.name()));
        return "sys/mobile/chartModules/appCmsReportIndex";
    }


    /**
     * 获取用户的报表模块资源数据
     *
     * @return 用户的报表模块资源数据
     */
    @GetMapping("/getUserMobileChartModuleResource")
    @ResponseBody
    public Object getUserMobileChartModuleResource() {
        return mobileChartModuleService.getUserMobileChartModuleResource(getUserId());
    }

    /**
     * 保存 用户-报表模块的关联状态信息
     *
     * @param userMobileChartModuleList 用户-报表模块的关联状态信息
     * @return 保存结果
     */
    @PostMapping("/saveUserMobileChartModuleResource")
    @ResponseBody
    public Object saveUserMobileChartModuleResource(@RequestBody List<UserMobileChartModule> userMobileChartModuleList) {
        String userId = getUserId();
        for (UserMobileChartModule userMobileChartModule : userMobileChartModuleList) {
            userMobileChartModule.setUserId(userId);
        }
        mobileChartModuleService.saveUserMobileChartModuleResource(userMobileChartModuleList);
        return success();
    }


    /**********************************  测试使用 start  *************************************/

    /**
     * 查询条件Demo页面
     *
     * @return
     */
    @RequestMapping("/appConditionExample")
    public String appConditionExample(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        return "sys/mobile/appConditionExample";
    }

    /**
     * tab切换demo
     *
     * @return
     */
    @RequestMapping("/appChartTabPageExample")
    public String appChartTablePageExample(Model model) {
        String userId = getUserId();
        User user = userService.get(userId);
        Corp corp = user.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        return "sys/mobile/appChartTabPageExample";
    }


    @PostMapping("/testPage1")
    public String testPage1(HttpServletRequest request) {
        System.out.println(request.getParameter("aaa"));
        System.out.println(request.getParameter("bbb"));
        return "sys/mobile/testPage1";
    }

    @GetMapping("/testPage2")
    public String testPage2(HttpServletRequest request) {
        System.out.println(request.getParameter("aaa"));
        System.out.println(request.getParameter("bbb"));
        return "sys/mobile/testPage1";
    }

    /**********************************  测试使用 end *************************************/

}
