package com.bytter.system.mobile.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IMobileCustomMenuService;
import com.bytter.system.api.dto.MobileCustomMenuDTO;
import com.bytter.system.service.AccountMappingService;
import com.bytter.system.service.SysAttachmentService;
import com.bytter.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
/**
 * @author chenlw
 * @since 2020-06-08
 */
@Controller
@RequestMapping("/api")
public class MobileCommonController extends BaseController {
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    //todo 没有实现？
    @Autowired(required = false)
    private IMobileCustomMenuService mobileCustomMenuService;
    @Resource
    private UserService userService;
    @Autowired
    private AccountMappingService accountMappingService;
    /**
     * 获取附件
     *
     * @param groupId 附件组ID
     * @return 附件
     */
    @GetMapping("/findAttachmentByGroupId")
    @ResponseBody
    public Object findAttachmentByGroupId(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return new ArrayList<>();
        }
        return sysAttachmentService.findAttachmentByGroupId(groupId);
    }
    /**
     * 下载文件
     *
     * @param method     方法
     * @param attachuuid 附件ID
     * @param request    请求对象
     * @param response   响应对象
     */
    @RequestMapping("/download")
    public void downloadAttachment(String method, String attachuuid, HttpServletRequest request, HttpServletResponse response) {
        if ("downFile".equals(method)) {
            if (StringUtils.isNotBlank(attachuuid)) {
                sysAttachmentService.downloadAttachment(attachuuid, request, response);
            }
        }
    }
    /**
     * 获取移动端 【我的】个性化菜单
     *
     * @return 菜单列表
     */
    @GetMapping("/getMineCustomMenu")
    @ResponseBody
    public Object getMineCustomMenu() {
        List<MobileCustomMenuDTO> mobileCustomMenuList = new ArrayList<>();
        // 添加【修改密码】菜单
        MobileCustomMenuDTO modifyPasswordMenu = new MobileCustomMenuDTO();
        modifyPasswordMenu.setName("修改密码");
        modifyPasswordMenu.setUrl("/api/modifyPasswordPage");
        modifyPasswordMenu.setIconUrl("/static/mobile/icon/common/icon_modify_password.png" + "?_btv=" + System.currentTimeMillis());
        // modifyPasswordMenu.setNeedCheckPassword("1");
        mobileCustomMenuList.add(modifyPasswordMenu);
        if (mobileCustomMenuService != null) {
            // 把扩展的自定义菜单添加进来
            mobileCustomMenuList.addAll(mobileCustomMenuService.getMineCustomMenuList(getCurrentUser()));
        }
        return mobileCustomMenuList;
    }
    /**
     * 移动端修改密码页面
     *
     * @return 移动端修改密码页面
     */
    @RequestMapping("/modifyPasswordPage")
    public String modifyPassword(HttpServletRequest request, Model model) {
        /**
         * 密码组成模式
         * - 0：数字。
         * - 1：字母。
         * - 2：数字+字母。
         * - 3：字母+数字+特殊字符。
         */
        model.addAttribute("passwordModel", configServiceI.getConfig("sys", "passwordModel"));
        model.addAttribute("passwordLength", configServiceI.getConfig("sys", "passwordLength"));
        return "sys/mobile/mine/modifyPassword";
    }
}
