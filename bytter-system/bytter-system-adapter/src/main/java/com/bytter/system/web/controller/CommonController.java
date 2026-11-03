package com.bytter.system.web.controller;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.jdbc.TenantProperties;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.dao.UserDao;
import com.bytter.system.entity.*;
import com.bytter.system.service.*;
import io.netty.handler.codec.http2.Http2Headers;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.persistence.Page;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.FileCacheManagerUtils;
import com.bytter.core.utils.FileUtils;
import com.bytter.core.utils.MD5Util;
import com.bytter.core.utils.RedisUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.system.api.IDisposableTokenService;
import com.bytter.system.api.ILicenseService;
import com.bytter.system.api.config.SM2ManagerService;
import com.bytter.system.api.constant.MenuConstant;
import com.bytter.system.api.constant.MutiLangConstant;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.enums.AttachmentCategoryEnum;
import com.bytter.system.api.enums.LoginMessageEnum;
import com.bytter.system.vo.DictJsonVO;
import com.bytter.system.vo.DictVO;
import com.bytter.system.vo.MenuIndexVO;
import com.bytter.system.vo.MenuSimpleVO;
import com.bytter.system.vo.ResourcesSimpleVO;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
/**
 * 公共页面控制器
 * 周亮
 */
@Controller
@RequestMapping("/")
@Slf4j
public class CommonController extends BaseController {

    @Autowired
    private MultiLangService multiLangService;

    @Autowired
    private DictService dictService;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private CorpUserResourcesService corpUserResourcesService;

    @Autowired
    private IDisposableTokenService disposableTokenService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuCollectService menuCollectService;

    @Autowired
    private ILicenseService licenseServiceImpl;

    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    I18nService i18nService;
    @Autowired
    private SM2ManagerService sm2ManagerService;
    @Resource
    private TenantProperties tenantProperties;
    /**
     * 是否开启了CAS单点登录
     */
    @Value("${cas.switch}")
    private boolean casSwitch;

    /**
     * 单点登录退出登录跳转的页面URL，可配置。
     * 若不配置该项，则默认跳转到我们的登录页面
     */
    @Value("${cas.pc.logoutToPageUrl}")
    private String casLogoutToPageUrl;
    /**
     * 网关地址，用于首页请求
     */
    @Value("${bytter.gateway.url}")
    private String gatewayUrl;
    /**
     * 大参林退出地址
     */
    @Value("${sso.service.dsl.sso-logout}")
    private String ssoLogout;
    /**
     * 大参林SSO鉴权登陆地址
     */
    @Value("${sso.service.dsl.sso-login}")
    private String ssoLogin;
    /**
     * 大参林SSO鉴权回调地址
     */
    @Value("${sso.service.dsl.login-url}")
    private String loginUrl;
    /**
     * 资金系统退出登陆地址
     */
    @Value("${sso.service.dsl.logout-url}")
    private String logoutUrl;

    @Resource
    private UserDao userDao;

    /**
     * 在这里加入“system:common:index”是为了用户登录进来后直接调用shiro的方法获取用户的角色、菜单、资源权限
     *
     * @param model
     * @author 周亮
     */
    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        List<MenuSimpleVO> menuSimpleVOList = menuService.listMenusByUserId(getUserId());
        List<String> menus = new ArrayList<>();
        for (MenuSimpleVO menuSimpleVO : menuSimpleVOList) {
            menus.add(menuSimpleVO.getId());
        }
        Locale locale = LocaleContextHolder.getLocale();
        List<Menu> menuList = menuService.listByMenuIds(locale.toString(), menus);
        Collections.sort(menuList, Comparator.comparingInt(Menu::getSort));

        List<MenuIndexVO> menuIndexVOS = BeanUtils.copyListProperties(menuList, MenuIndexVO.class);

        if (!licenseServiceImpl.isFinalLicense()) {
            boolean expire = licenseServiceImpl.isExpire();
            if (expire) {
            	return "licenseExpire";
            }
            boolean readyExpire = licenseServiceImpl.isReadyExpire();
            if (readyExpire) {
            	  model.addAttribute("isLicenseReadyExpire", true);
            	  Date licenseEndDate = licenseServiceImpl.getLicenseEndDate();
                  String licenseLimitDate = new SimpleDateFormat("yyyy-MM-dd").format(licenseEndDate);
            	  model.addAttribute("licenseLimitDate", licenseLimitDate);
            }
        }


        //todo 去掉license菜单分配权限
   /*     if (!ILicenseService.LICENSE_TYPE_DEVELOP.equalsIgnoreCase(licenseServiceImpl.getLicenseType())) {
            List<String> licenseMenus = licenseServiceImpl.getMenus();
            for (MenuIndexVO menuIndexVO : menuIndexVOS) {
                if (licenseMenus.contains(menuIndexVO.getId()) || MenuConstant.IS_EXTEND_MENU.equals(menuIndexVO.getIsExtend())) {
                    menuIndexVOTemp.add(menuIndexVO);
                }
            }
        } else {
            menuIndexVOTemp = menuIndexVOS;
        }*/
        List<MenuIndexVO> menuIndexVOTemp = menuIndexVOS;

        //用户权限范围内的菜单集合
        Map<String, MenuIndexVO> menuMap = new HashMap<>();
        //顶级菜单集合
        Map<String, MenuIndexVO> topMenuMap = new HashMap<>();
        for (MenuIndexVO menu : menuIndexVOTemp) {
            menuMap.put(menu.getId(), menu);
            if (StringUtils.isBlank(menu.getParentId())) {
                topMenuMap.put(menu.getId(), menu);
            }
        }
        for (MenuIndexVO menu : menuIndexVOTemp) {
            menu.setParentIds(this.getMenuParentIds(menuMap, menu.getId(), null));
            String[] parentIds = menu.getParentIds().split(CommonConstant.COMMA_SPLIT);
            for (String parentId : parentIds) {
                if (topMenuMap.containsKey(parentId)) {
                    //设置顶级菜单
                    menu.setTopParentId(parentId);
                    menu.setTopParentName(topMenuMap.get(parentId).getName());
                    break;
                }
            }
        }

        List<MenuIndexVO> treeNodeList = TreeUtil.coverTreeList(menuIndexVOTemp);
        List<String> menuCollectIds = menuCollectService.listMenuIdByUserId(getUserId());
        String companyName = licenseServiceImpl.getCompanyNameByLicenseType(locale.toString());
        UserConfig userConfig = userConfigService.getUserIndexSkin(getUserId());
        String indexSkin = userConfig == null ? "" : userConfig.getContent();
        Config advert = configService.getByTypeAndName("sys", "scfState");
        model.addAttribute("menuCollectIds", menuCollectIds);
        model.addAttribute("menuList", treeNodeList);
        model.addAttribute("companyName", companyName);
        model.addAttribute("tempLicense", licenseServiceImpl.isTempLicense());
        model.addAttribute("locale", locale.toString());
        model.addAttribute("indexSkin", indexSkin);
        model.addAttribute("scfAdvert", advert == null ? "0" : advert.getValue());
        //CAS开关，根据CAS开关判断是否启动锁屏功能
        model.addAttribute("casSwitch", casSwitch);
        //系统风格
        Config uiStyleConfig = configService.getByTypeAndName("sys", "uiStyle");
        String uiStyle = CommonConstant.NO;
        if (uiStyleConfig != null) {
            uiStyle = uiStyleConfig.getValue();
        }
        model.addAttribute("uiStyle", uiStyle);
        // 添加SSO退出地址，退出后跳转到首页
        model.addAttribute("ssoUrl", ssoLogout + "?service=" + gatewayUrl);
        //todo: 添加SSO授权地址
        // SSO授权登陆地址
        String ssoLoginUrl = StrUtil.format(ssoLogin + "?service={}&realService={}", loginUrl, logoutUrl);
        model.addAttribute("ssoLoginUrl", ssoLoginUrl);
        if (CommonConstant.YES.equals(uiStyle)) {
            //新版UI
            return "index";
        } else {
            //旧版UI
            return "oldIndex";
        }
    }

    /**
     * 设置末级菜单ID全路径
     *
     * @param menuMap
     */
    private String getMenuParentIds(Map<String, MenuIndexVO> menuMap, String menuId, StringBuffer parentIds) {
        if (parentIds == null) {
            parentIds = new StringBuffer();
        }
        MenuIndexVO menu = menuMap.get(menuId);
        if (menu != null) {
            parentIds.insert(0, CommonConstant.COMMA_SPLIT);
            parentIds.insert(0, menu.getId());
            if (StringUtils.isNotBlank(menu.getParentId())) {
                getMenuParentIds(menuMap, menu.getParentId(), parentIds);
            }
        }
        return parentIds.toString();
    }

    /**
     * 在这里加入“system:common:index”是为了用户登录进来后直接调用shiro的方法获取用户的角色、菜单、资源权限
     *
     * @param model
     * @author 周亮
     */
    @RequiresPermissions("system:common:index")
    @GetMapping("/menu/firstlayer")
    @ResponseBody
    public List<MenuIndexVO> firstLayerMenu(Model model) {
        List<String> menuIds = menuService.listFirstLayerMenuIdsByUserId(getUserId());
        Locale locale = LocaleContextHolder.getLocale();
        List<Menu> menuList = menuService.listByMenuIds(locale.toString(), menuIds);
        Collections.sort(menuList, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getSort() - o2.getSort();
            }
        });

        List<MenuIndexVO> menuIndexVOS = BeanUtils.copyListProperties(menuList, MenuIndexVO.class);

        List<MenuIndexVO> menuIndexVOTemp = new ArrayList<>();
        if (!ILicenseService.LICENSE_TYPE_DEVELOP.equalsIgnoreCase(licenseServiceImpl.getLicenseType())) {
            List<String> licenseMenus = licenseServiceImpl.getMenus();
            for (MenuIndexVO menuIndexVO : menuIndexVOS) {
                if (licenseMenus.contains(menuIndexVO.getId()) || MenuConstant.IS_EXTEND_MENU.equals(menuIndexVO.getIsExtend())) {
                    menuIndexVOTemp.add(menuIndexVO);
                }
            }
        } else {
            menuIndexVOTemp = menuIndexVOS;
        }

        List<MenuIndexVO> treeNodeList = TreeUtil.coverTreeList(menuIndexVOTemp);
        return treeNodeList;
    }

    /**
     * 在这里加入“system:common:index”是为了用户登录进来后直接调用shiro的方法获取用户的角色、菜单、资源权限
     *
     * @param model
     * @author 周亮
     */
    @RequiresPermissions("system:common:index")
    @GetMapping("/menu/nextlayers")
    @ResponseBody
    public List<MenuIndexVO> nextLayersMenus(Model model, String parentMenuId) {
        List<MenuIndexVO> result = new ArrayList<>();
        Set<String> parentIds = new HashSet();
        parentIds.add(parentMenuId);
        List<String> menuIds = menuService.listMenuIdsByUserIdAndParentIds(getUserId(), parentIds);
        if (EmptyUtils.isEmpty(menuIds)) {
            return result;
        }
        List<String> nextLayerMenuIds = menuService.listMenuIdsByUserIdAndParentIds(getUserId(), Sets.newHashSet(menuIds));
        if (EmptyUtils.isNotEmpty(nextLayerMenuIds)) {
            menuIds.addAll(nextLayerMenuIds);
        }
        menuIds.add(parentMenuId);
        Locale locale = LocaleContextHolder.getLocale();
        List<Menu> menuList = menuService.listByMenuIds(locale.toString(), menuIds);
        Collections.sort(menuList, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getSort() - o2.getSort();
            }
        });

        List<MenuIndexVO> menuIndexVOS = BeanUtils.copyListProperties(menuList, MenuIndexVO.class);
        MenuIndexVO parentMenu = null;
        List<MenuIndexVO> menuIndexVOTemp = new ArrayList<>();
        if (!ILicenseService.LICENSE_TYPE_DEVELOP.equalsIgnoreCase(licenseServiceImpl.getLicenseType())) {
            List<String> licenseMenus = licenseServiceImpl.getMenus();
            for (MenuIndexVO menuIndexVO : menuIndexVOS) {
                if (menuIndexVO.getId().equals(parentMenuId)) {
                    parentMenu = menuIndexVO;
                } else if (licenseMenus.contains(menuIndexVO.getId()) || MenuConstant.IS_EXTEND_MENU.equals(menuIndexVO.getIsExtend())) {
                    menuIndexVOTemp.add(menuIndexVO);
                }
            }
        } else {
            menuIndexVOTemp = menuIndexVOS;
        }

        result = TreeUtil.coverTree(menuIndexVOTemp, parentMenu, false);
        return result;
    }

    @GetMapping("/main")
    public void main() {
    }

    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 生成随机数
     *
     * @param length
     * @return
     */
    private static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 密码加密
     * @return
     */
    @GetMapping("/randomString")
    @ResponseBody
    public AjaxJson getRandomString() {
        return AjaxJson.success(sm2ManagerService.genSM2PukKey());
    }

    public static String decrypt(String content, String key, String iv) {
        // stringToBase64() 将 Base64编码的字符串转换成 byte[] !!!与base64ToString(）配套使用
        byte[] bytes = Base64.decodeBase64(content);
        return new String(aesCbcDecrypt(bytes, key.getBytes(), iv.getBytes()));
    }

    private static byte[] aesCbcDecrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {

      /*  if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index";
        }*/

        if (casSwitch && StringUtils.isNotBlank(casLogoutToPageUrl)) {
            return String.format("redirect:%s", casLogoutToPageUrl);
        }

        //检查是否切换到国际化
        if (redirectI18nLogin(request, response)) {
            return "redirect:/login";
        }
        //log.info(applicationContext.getBean(ViewResolver.class).toString());

        // 从 LocaleResolver 读取 Session 中存储的 locale，而非从 LocaleContextHolder
        // （后者由 FrameworkServlet 设为了请求 Accept-Language 头，而非用户选择的语言）
        String locale = RequestContextUtils.getLocale(request).toString();
        //是否启用国际化，update by zhoul 2019-11-18
        Config i18nConfig = configService.getGlobalConfig("sys", "i18nEnable");
        model.addAttribute("i18nEnable", i18nConfig == null ? "0" : i18nConfig.getValue());
        model.addAttribute("i18n", locale);
        //加密方式
        Config encrypt = configService.getGlobalConfig("sys", "encrypt");
        //得到系统默认登录方式
        String defaultLoginType = configService.getDefaultLoginType();
        String companyName = licenseServiceImpl.getCompanyNameByLicenseType(locale);
        model.addAttribute("companyName", companyName);
        model.addAttribute("encrypt", encrypt);
        model.addAttribute("licenceApp", "N");
        model.addAttribute("defaultLoginType", defaultLoginType);

        model.addAttribute("closeMultiTenantMode", tenantProperties.getEnabled() ? "0" : "1");
        model.addAttribute("defaultId", tenantProperties.getDefaultId());
        //系统风格
        Config uiStyleConfig = configService.getGlobalConfig("sys", "uiStyle");
        String uiStyle = CommonConstant.NO;
        if (uiStyleConfig != null) {
            uiStyle = uiStyleConfig.getValue();
        }
        model.addAttribute("uiStyle", uiStyle);
        // SSO授权登陆地址
        String ssoUrl = StrUtil.format(ssoLogin + "?service={}&realService={}", loginUrl, logoutUrl);
        model.addAttribute("ssoUrl", ssoUrl);
        // 添加SSO退出地址，退出后跳转到首页
        model.addAttribute("logoutUrl", ssoLogout + "?service=" + gatewayUrl);
        String errorMsg = request.getParameter("errorMsg");
        model.addAttribute("errorMsg", errorMsg);
        // 如果设置为SSO登陆，直接跳转到第三方登陆页面
        if (ObjectUtil.equal(defaultLoginType, CommonConstant.LOGIN_TYPE_SSO)) {
            return "redirect:" + ssoUrl;
        }
        if (CommonConstant.YES.equals(uiStyle)) {
            //新版UI
            return "login";
        } else {
            //旧版UI
            return "oldLogin";
        }
    }

    private boolean redirectI18nLogin(HttpServletRequest request, HttpServletResponse response) {
        String newLocale = request.getParameter("locale");
        if (newLocale != null) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (localeResolver == null) {
                throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
            }
            try {
                localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString(newLocale));
            } catch (IllegalArgumentException ex) {
                throw ex;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取用户登入方式
     *
     * @param loginName 用户登录名称
     * @return -1用户不存在，0用户密码，1银行密码+证书，2证书
     */
    @PostMapping("/userLoginType")
    @ResponseBody
    public AjaxJson getUserLoginType(String loginName, String tenantId) {
        String userLoginType = userService.getUserLoginType(loginName, tenantId);
        AjaxJson ajaxJson = success();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("type", userLoginType);
        ajaxJson.setAttributes(attributes);
        return ajaxJson;
    }

    /**
     * 根据证书得到绑定用户
     *
     * @param userCadn
     * @return
     */
    @PostMapping("/getUserByCadn")
    @ResponseBody
    public AjaxJson getUserByCadn(String userCadn) {
        User user = userService.getUserByLoginCadn(userCadn);
        AjaxJson ajaxJson = success();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("type", user.getLoginName());
        ajaxJson.setAttributes(attributes);
        return ajaxJson;
    }

    @GetMapping("/caBinding")
    public String caBinding(Model model, User user) throws Exception {
        //加密方式
        Config encrypt = configService.getGlobalConfig("sys", "encrypt");
        model.addAttribute("encrypt", encrypt);
        model.addAttribute("user", user);
        return "caBinding";
    }

    @PostMapping("/saveCa")
    @ResponseBody
    public AjaxJson saveCa(User user) {
        User userCadn = userService.getUserByLoginCadn(user.getUserCadn());
        //判断证书是否已经被绑定
        if (!EmptyUtils.isEmpty(userCadn)) {
            throw new BusinessException(LoginMessageEnum.SYS_CA_CABINDING);
        }
        User t = userService.getUserByLoginName(user.getLoginName());
        if (EmptyUtils.isEmpty(t)) {
            //todo
            throw new RuntimeException();
        } else {
            t.setUserCadn(user.getUserCadn());
            userService.saveOrUpdate(t);
        }
        return success();
    }

    @GetMapping("/404")
    public String page404() {
        return "common/404";
    }

    @GetMapping("403")
    public String page403() {
        return "common/403";
    }

    @GetMapping("/503")
    public String page503(String serviceName,Model model) {
        model.addAttribute("serviceName", serviceName);
        return "common/503";
    }

    @GetMapping("/h5ConfigError")
    public String h5ConfigError() {
        // H5配置错误提示页面
        return "common/h5ConfigError";
    }

    /**
     * 获取一次性访问token
     *
     * @param size 次数
     * @return 一次性token集合
     */
    @GetMapping("/allowOnce")
    @ResponseBody
    public List<String> allowOnce(Integer size) {
        return disposableTokenService.createToken(size);
    }

    @GetMapping("/lang")
    @ResponseBody
    public Object getLang(Locale locale) {
        List<MultiLang> list = multiLangService.buildMultiLangJs(locale.toString());
        // 获取不到，如果是zh开头，在用zh_CN中文去查询一遍
        if (EmptyUtils.isEmptyList(list)) {
            if (locale.toString().indexOf(MutiLangConstant.ZH_PREFIX) > -1) {
                list = multiLangService.buildMultiLangJs(MutiLangConstant.ZH_CN);
            }
        }
        return list;
    }


    @GetMapping("/lang/internation")
    @ResponseBody
    public Object getLangInternation(Locale locale) {
        List<I18nDTO> list = i18nService.listI18n(locale.toString());
        return list;
    }

    @GetMapping("/lang/internationBase")
    @ResponseBody
    public Object getLangInternationBase(Locale locale) {
        List<I18nDTO> list = i18nService.listI18nBase(locale.toString());
        return list;
    }


    @GetMapping("/lang/locale")
    @ResponseBody
    public AjaxJson getLocale() {
        String locale = i18nService.getLocale();
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(locale);
        return ajaxJson;
    }

    /**
     * 用户权限资源JS，资源JS不能加上缓存
     *
     * @param model
     * @param response
     * @return
     */
    @GetMapping("/resource.js")
    public String resourceJs(Model model, HttpServletResponse response) {
//        response.setHeader("Cache-Control", "max-age=31536000");
        response.setContentType("text/javascript;charset=utf-8");
        model.addAttribute("resources", resourcesService.listResourceByUserId(getUserId()));
        model.addAttribute("corpResources", corpUserResourcesService.listCorpResourcesByActiveUser(getUserId()));
        return "common/resource";
    }

    @GetMapping("/resource")
    @ResponseBody
    public Object resource() {
        List<ResourcesSimpleVO> corpResources = corpUserResourcesService.listCorpResourcesByActiveUser(getUserId());
        Map<String, StringBuilder> oldMap = Maps.newHashMap();
        String permission;
        StringBuilder corpIds;
        for (ResourcesSimpleVO resource : corpResources) {
            if (StringUtils.isBlank(resource.getPermission())) {
                continue;
            }
            permission = resource.getPermission();
            corpIds = oldMap.get(permission);
            if (corpIds == null) {
                corpIds = new StringBuilder();
                oldMap.put(permission, corpIds);
            }
            if (StringUtils.isNotBlank(resource.getCorpId())) {
                corpIds.append(resource.getCorpId()).append(",");
            }
            oldMap.put(permission, corpIds);
        }

        Map<String, List<String>> map = new HashMap<>();
        List<String> corpIdList;
        HashSet<String> objects = new HashSet<>();
        for (Map.Entry<String, StringBuilder> entry : oldMap.entrySet()) {
            corpIdList = Arrays.asList(entry.getValue().substring(0, entry.getValue().length() - 1).split(","));
            map.put(entry.getKey(), corpIdList);
            objects.addAll(corpIdList);
        }

        // 根据算法设置单位对应的key值
        List<String> sorKeys = generateSorKeys(objects.size());
        ArrayList<String> list = new ArrayList<>(objects);
        Map<String, String> corpMap = new HashMap<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            corpMap.put(sorKeys.get(i), list.get(i));
        }

        // 设置资源对应的单位key值
        Map<String, String> resourceMap = new HashMap<>(list.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            List<String> values = entry.getValue();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                int corpIndex = getCorpIndex(list, values.get(i));
                sb.append(sorKeys.get(corpIndex));
                if (i != values.size() - 1) {
                    sb.append(",");
                }
            }
            resourceMap.put(entry.getKey(), sb.toString());
        }

        Map<String, Map<String, String>> finalMap = Maps.newHashMap();
        finalMap.put("corp", corpMap);
        finalMap.put("resources", resourceMap);
        return finalMap;
    }

    @GetMapping("/resource/{menuId}")
    @ResponseBody
    public Object resource(@PathVariable("menuId") String menuId) {
        Map<String, String> corpCode2CorpId = new HashMap<>();
        Map<String, String> permission2CorpCode = new HashMap<>();
        //所有有序单位id的签名
        String signForAllCorpIds = "";
        Map<String, String> sign2SignVal = new HashMap<>();
        sign2SignVal.put("allCorpIdsSign", signForAllCorpIds);
        Map<String, Map<String, String>> result = Maps.newHashMap();
        result.put("corp", corpCode2CorpId);
        result.put("resources", permission2CorpCode);
        result.put("sign", sign2SignVal);
        List<ResourcesSimpleVO> corpResources = null;
        if (StringUtils.isNotEmpty(menuId)) {
            corpResources = corpUserResourcesService.listCorpResourcesByActiveUser(getUserId(), menuId);
        } else {
            corpResources = corpUserResourcesService.listCorpResourcesByActiveUser(getUserId());
        }
        if (EmptyUtils.isEmpty(corpResources)) {
            return result;
        }
        //安装权限进行分组
        Map<String, Set<String>> permission2CorpIds = Maps.newHashMap();
        for (ResourcesSimpleVO resource : corpResources) {
            if (StringUtils.isBlank(resource.getPermission()) || StringUtils.isBlank(resource.getCorpId())) {
                continue;
            }
            Set<String> corpIds = permission2CorpIds.get(resource.getPermission());
            if (corpIds == null) {
                corpIds = new HashSet<>();
                permission2CorpIds.put(resource.getPermission(), corpIds);
            }
            corpIds.add(resource.getCorpId());
        }
        // 获取所有的单位 根据 时间 和 单位编码，来保证单位的顺序
        List<String> allCorpIds = corpUserResourcesService.listAllCorps();
        if (EmptyUtils.isEmpty(allCorpIds)) {
            return result;
        }
        // 根据算法设置单位对应的key值,即编码
        List<String> sorKeys = generateSorKeys(allCorpIds.size());
        if (EmptyUtils.isEmpty(sorKeys) || allCorpIds.size() != sorKeys.size()) {
            log.error("generate sorKeys error,corpIds size not equal sorKeys size.");
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
        }
        Map<String, String> corpId2CorpCode = new HashMap<>(allCorpIds.size());
        for (int i = 0; i < allCorpIds.size(); i++) {
            corpCode2CorpId.put(sorKeys.get(i), allCorpIds.get(i));
            corpId2CorpCode.put(allCorpIds.get(i), sorKeys.get(i));
        }
        // 设置资源对应的单位key值
        for (String permission : permission2CorpIds.keySet()) {
            if (EmptyUtils.isEmpty(permission2CorpIds.get(permission))) {
                continue;
            }
            List<String> corpCodes = Lists.newArrayList();
            for (String corpIdForPermission : permission2CorpIds.get(permission)) {
                if (EmptyUtils.isEmpty(corpId2CorpCode.get(corpIdForPermission))) {
                    // 因为目前的现状是：删除单位，并不会取删除该单位的所有有该单位的资源权限。为了1.不修改现状，2.不将权限与单位进行关联，因为这样会带来性能消耗，所以在这里过滤掉。这个日志为了确认是否单位不存在。
                    log.warn(String.format("corpId :%s,can not find corpCode.so the corpId not exists in sys_corp table .", corpIdForPermission));
                    continue;
                }
                corpCodes.add(corpId2CorpCode.get(corpIdForPermission));
            }
            if (EmptyUtils.isNotEmpty(corpCodes)) {
                permission2CorpCode.put(permission, Joiner.on(CommonConstant.COMMA_SPLIT).join(corpCodes));
            }
        }
        // allCorpIds是有序的，若是单位有增加或删除，allCorpIds会不同。
        signForAllCorpIds = MD5Util.MD5Encode(Joiner.on(CommonConstant.COMMA_SPLIT).join(allCorpIds));
        sign2SignVal.put("allCorpIdsSign", signForAllCorpIds);
        return result;
    }


    /**
     * 根据sor算法生成单位的key值
     *
     * @param number 单位数量
     * @return 算法集合
     */
    private static List<String> generateSorKeys(int number) {
        String tables = "0123456789abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ";
        int[] doubleNumber = {0, 0};
        int[] threadNumber = {0, 0, 0};
        List<String> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            StringBuilder sb = new StringBuilder();
            if (i < 62) {
                sb.append(tables.charAt(i));
            } else if (i < 3906) {
                sb.append(tables.charAt(doubleNumber[1]));
                sb.append(tables.charAt(doubleNumber[0]++ % tables.length()));
                if (doubleNumber[0] % tables.length() == 0) {
                    doubleNumber[1]++;
                }
            } else {
                sb.append(tables.charAt(threadNumber[2]));
                sb.append(tables.charAt(threadNumber[1]));
                sb.append(tables.charAt(threadNumber[0]++ % tables.length()));
                if (threadNumber[0] % tables.length() == 0) {
                    threadNumber[1]++;
                    if (threadNumber[1] % tables.length() == 0) {
                        threadNumber[2]++;
                        threadNumber[1] = 0;
                    }
                }
            }
            list.add(sb.toString());
        }
        return list;
    }

    private static int getCorpIndex(List<String> corps, String corp) {
        for (int i = 0; i < corps.size(); i++) {
            if (corp.equals(corps.get(i))) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 获取用户资源权限
     * 避免用户单位资源加载太慢导致页面报错
     *
     * @return
     * @author zuoy 2019-06-25
     */
    @GetMapping("/userResource")
    @ResponseBody
    public Object userResource() {
        ShiroUser user = (ShiroUser) getCurrentUser();
        if (CollectionUtils.isEmpty(user.getPermission())) {
            //打印个错误日志，以便跟踪问题
            log.error("网络不稳定，登陆时获取的权限为空，这里再次获取.................................");
            List<ResourcesSimpleVO> resourcesSimpleVOS = resourcesService.listResourceByUserId(user.getId());
            Set<String> res = new HashSet<>();
            for (ResourcesSimpleVO resourcesSimpleVO : resourcesSimpleVOS) {
                res.add(resourcesSimpleVO.getPermission());
            }
            user.setPermission(res);
        }
        return CollectionUtils.isEmpty(user.getPermission()) ? Sets.newHashSet() : user.getPermission();
    }

    /**
     * 根据资源权限获取成员单位ID列表
     * Created by chenlw on 2019/04/29.
     *
     * @param permission
     * @return
     */
    @GetMapping("/getResourceCorpIdsByPermission")
    @ResponseBody
    public Object getResourceCorpIdsByPermission(String permission) {
        List<ResourcesSimpleVO> corpResources = corpUserResourcesService.listCorpResourcesByUserIdAndPermission(getUserId(), permission);
        Map<String, List<String>> map = new HashMap<>();
        for (ResourcesSimpleVO resource : corpResources) {
            if (StringUtils.isBlank(resource.getPermission())) {
                continue;
            }
            String resourcePermission = resource.getPermission();
            List<String> corpIds = map.get(resourcePermission);
            if (corpIds == null) {
                corpIds = new ArrayList<>();
                map.put(resourcePermission, corpIds);
            }
            if (StringUtils.isNotBlank(resource.getCorpId())) {
                corpIds.add(resource.getCorpId());
            }
            map.put(resourcePermission, corpIds);
        }
        return map;
    }


    @GetMapping("/dict")
    @ResponseBody
    public Object dict() {
        Locale locale = LocaleContextHolder.getLocale();
        List<DictVO> dictVOS = dictService.buildDictJs(locale.toString());
        return BeanUtils.copyListProperties(dictVOS, DictJsonVO.class);
    }


    @PostMapping("/upload")
    @ResponseBody
    public AjaxJson upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {

        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        File targetFile = new File(path, System.currentTimeMillis() + fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        targetFile.delete();
        return success();
    }

    //上传附件
    @PostMapping("/upload/attachment")
    @ResponseBody
    public List<AttachmentDTO> uploadAttachment(@RequestParam("code") String code, HttpServletRequest request) throws Exception {
        List<AttachmentDTO> list = new ArrayList<AttachmentDTO>();
        if (StringUtils.isNotBlank(code)) {
            AttachmentCategoryEnum attachmentCategoryEnum = AttachmentCategoryEnum.getAttachmentEnum(code.trim());
            if (attachmentCategoryEnum != null) {
                list = sysAttachmentService.uploadAttachment(request, attachmentCategoryEnum);
            }
        }
        return list;
    }

    //下载附件
    @RequestMapping("/download/attachment")
    public void downloadAttachment(String id, HttpServletRequest request, HttpServletResponse response) {
        //优化代码
        if (StringUtils.isNotBlank(id)) {
            sysAttachmentService.downloadAttachment(id, request, response);
        }
    }

    //下载资源文件
    @RequestMapping("/download/resource")
    public void downloadResource(String code, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(code)) {
            return;
        }
        //获取相对路径
        String path = ResourceTemplateEnum.getResource(code);
        if (StringUtils.isBlank(path)) {
            return;
        }

        String fName = path.trim();
        String fileName = fName.substring(fName.lastIndexOf("/") + 1);
        String codedFileName = ResourceTemplateEnum.getChiniseNameByResource(code);
        // 未定义中文名的则取文件名
        if(StringUtils.isBlank(codedFileName)){
            codedFileName = FileUtils.getFilePrefix(fileName);
        }
        String suffix = FileUtils.getExtend(fileName);
        try {
            // 1. 对整个文件名进行URL编码
            String fullFileName = codedFileName + "." + suffix;
            String encodedFileName = URLEncoder.encode(fullFileName, "UTF-8")
                    .replaceAll("\\+", "%20");  // 将+替换为%20

            // 2. 使用filename*参数，这是解决中文乱码的关键
            response.setHeader("Content-Disposition",
                    "attachment; filename*=UTF-8''" + encodedFileName);

        } catch (Exception e) {
            // 如果出错，使用简单文件名
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + codedFileName + "." + suffix + "\"");
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download");
        InputStream is = FileCacheManagerUtils.getFile(path);
        if (is == null) {
            return;
        }
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单点登入用户传递ssToken返回用户具体信息
     *
     * @param ssoToken 认证Token值
     * @return {"success":"false"} 失败 {"success":"true","user":{"usercode":"admin","username":"管理员","corpId":"1"}} 成功
     */
    @RequestMapping("/sso")
    @ResponseBody
    public Map ssoTokenCheck(String ssoToken) {
        Map<String, Object> reInfo = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(ssoToken)) {
            reInfo.put("success", "true");
            Map<String, Object> userInfo = new LinkedHashMap<>();
            userInfo.put("usercode", "admin");
            userInfo.put("username", "管理员");
            userInfo.put("corpId", "1");
            reInfo.put("user", userInfo);
        } else {
            reInfo.put("success", "false");
        }
        return reInfo;
    }

    /**
     * 访问此接口返回空数据  zhou
     */
    @RequestMapping("/prestrain")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        Page page = new Page(input.getStart(), input.getLength());
        page.setTotalCount(0);
        return new DataTablesOutput(input.getDraw(), new ArrayList(), page.getTotalCount(), page.getTotalCount());
    }


    @GetMapping("/error/casFailure")
    public void casFailure(HttpServletRequest request, Model model) {
        model.addAttribute("errorMessage", request.getParameter("errorMessage"));
        model.addAttribute("loginUrl", request.getParameter("loginUrl"));
    }

    @GetMapping("/error/casFailure1")
    public void enCasFailure(HttpServletRequest request, Model model) {
        Object errorMessage = request.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage.toString());
        } else {
            model.addAttribute("errorMessage", "登录失败，请联系管理员！");
        }

    }




    @GetMapping("/getMenuList")
    @ResponseBody
    public AjaxJson<List<MenuIndexVO>> getMenuList() {
        List<MenuSimpleVO> menuSimpleVOList = menuService.listMenusByUserId(getUserId());
        List<String> menus = new ArrayList<>();
        for (MenuSimpleVO menuSimpleVO : menuSimpleVOList) {
            menus.add(menuSimpleVO.getId());
        }
        Locale locale = LocaleContextHolder.getLocale();
        List<Menu> menuList = menuService.listByMenuIds(locale.toString(), menus);
        Collections.sort(menuList, Comparator.comparingInt(Menu::getSort));
        List<MenuIndexVO> menuIndexVOS = BeanUtils.copyListProperties(menuList, MenuIndexVO.class);
        List<MenuIndexVO> menuIndexVOTemp = menuIndexVOS;
        //用户权限范围内的菜单集合
        Map<String, MenuIndexVO> menuMap = new HashMap<>();
        //顶级菜单集合
        Map<String, MenuIndexVO> topMenuMap = new HashMap<>();
        for (MenuIndexVO menu : menuIndexVOTemp) {
            menuMap.put(menu.getId(), menu);
            if (StringUtils.isBlank(menu.getParentId())) {
                topMenuMap.put(menu.getId(), menu);
            }
        }
        for (MenuIndexVO menu : menuIndexVOTemp) {
            menu.setParentIds(this.getMenuParentIds(menuMap, menu.getId(), null));
            String[] parentIds = menu.getParentIds().split(CommonConstant.COMMA_SPLIT);
            for (String parentId : parentIds) {
                if (topMenuMap.containsKey(parentId)) {
                    //设置顶级菜单
                    menu.setTopParentId(parentId);
                    menu.setTopParentName(topMenuMap.get(parentId).getName());
                    break;
                }
            }
        }
        List<MenuIndexVO> treeNodeList = TreeUtil.coverTreeList(menuIndexVOTemp);
        return AjaxJson.success(treeNodeList);
    }
}

