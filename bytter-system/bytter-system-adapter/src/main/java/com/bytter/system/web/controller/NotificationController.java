package com.bytter.system.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.rest.RestInput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ILicenseService;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.constants.NotifyObjectTypeConstants;
import com.bytter.system.dao.NotificationRoleDao;
import com.bytter.system.dao.NotificationUserDao;
import com.bytter.system.entity.NotificationCategory;
import com.bytter.system.entity.NotificationUser;
import com.bytter.system.entity.User;
import com.bytter.system.service.NotificationCategoryService;
import com.bytter.system.service.NotificationService;
import com.bytter.system.service.RoleService;
import com.bytter.system.service.UserService;
import com.bytter.system.vo.NotificationUserVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/10/25.
 */
@Controller
@RequestMapping("/sys/notification")
public class NotificationController extends BaseController {
    @Resource
    private NotificationService notificationService;
    @Resource
    private NotificationCategoryService notificationCategoryService;
    @Resource
    private RoleService roleService;
    @Resource
    private NotificationRoleDao notificationRoleDao;
    @Resource
    private NotificationUserDao notificationUserDao;
    @Autowired
    private ILicenseService licenseService;
    @Autowired
    private UserService userService;

    @RequiresPermissions(SysResourceConstant.SYS_NOTIFY_CATEGORY_VIEW)
    @GetMapping("/index")
    public void index() {
    }
    @PostMapping("/list")
    @ResponseBody
    public Object list(@RequestBody RestInput restInput) {
        Locale locale = LocaleContextHolder.getLocale();
        return notificationService.restResult(restInput, locale.toString());
    }
    @GetMapping("/count")
    @ResponseBody
    public Object count() {
        return notificationService.getUserAllNotificationCount(getUserId());
    }
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam("ids[]") List<String> ids) {
        notificationService.delete(ids);
        return success();
    }
    @GetMapping("/category")
    public void category() {
    }
    @GetMapping("/todoPage")
    public void todoPage(Model model) {
        Map<String, Object> userTodoData = notificationService.getUserNotification(getUserId());
        model.addAttribute("categoryList", userTodoData.get("categoryList"));
        model.addAttribute("todoCounts", userTodoData.get("counts"));
    }
    @GetMapping("/categoryTree")
    @ResponseBody
    public Object tree() throws Exception {
        List<NotificationCategory> notificationCategoryList = notificationCategoryService.getTreeTable();
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (NotificationCategory notificationCategory : notificationCategoryList) {
//            if (StringUtils.isNotEmpty(notificationCategory.getMenuId()) && !licenseService.isMenusAuth(notificationCategory.getMenuId())) {
//                continue;
//            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", notificationCategory.getId());
            map.put("pId", notificationCategory.getParentId());
            map.put("name", notificationCategory.getName());
            tempList.add(map);
        }
        return tempList;
    }
    @RequiresPermissions(SysResourceConstant.SYS_NOTIFY_CATEGORY_VIEW)
    @PostMapping("/category.json")
    @ResponseBody
    public TreeGridResponse<NotificationCategory> categoryPost(@RequestBody TableGridInput tableGridInput) {
        return notificationCategoryService.getTreeGrid(tableGridInput);
    }
    @RequiresPermissions(SysResourceConstant.SYS_NOTIFY_CATEGORY_EDIT)
    @GetMapping("/categoryView")
    public void categoryView(String id, String parentId, Model model) {
        if (StringUtils.isNotBlank(id)) {
            NotificationCategory notificationCategory = notificationCategoryService.get(id);
            model.addAttribute("category", notificationCategory);
            model.addAttribute("notificationRoles", notificationRoleDao.listBy("categoryId", id));
            List<NotificationUserVO> notificationUserList = notificationUserDao.listNotifyUserByCategoryId(id);

            String userIds = "";
            String userNames = "";
            if(CollectionUtils.isNotEmpty(notificationUserList)){
                List<String> listUserId = notificationUserList.stream().map(NotificationUserVO::getUserId).collect(Collectors.toList());
                List<String> listUserName = notificationUserList.stream().map(NotificationUserVO::getUserName).collect(Collectors.toList());
                userIds = CollUtil.join(listUserId, ",");
                userNames = CollUtil.join(listUserName, ",");
            }
            model.addAttribute("userIds", userIds);
            model.addAttribute("userNames", userNames);
        }
        if (StringUtils.isNotEmpty(parentId)) {
            NotificationCategory notificationCategory = new NotificationCategory();
            notificationCategory.setParentId(parentId);
            model.addAttribute("category", notificationCategory);
        }
        model.addAttribute("roles", roleService.listAll());
    }

    @RequiresPermissions(SysResourceConstant.SYS_NOTIFY_CATEGORY_EDIT)
    @PostMapping("/categoryView")
    @ResponseBody
    public Object categoryViewPost(NotificationCategory notificationCategory, @RequestParam(value = "roleIds[]", required = false) List<String> roleIds
            // , @RequestParam(value = "userIds[]", required = false) List<String> userIds
            , @RequestParam(value = "userNames[]", required = false) List<String> userNames) {
        // 校验用户名
        List<String> userIds = null;
        if (NotifyObjectTypeConstants.BY_USER.equals(notificationCategory.getNotifyObjectType())){
            if (ObjectUtil.isNotEmpty(userNames)) {
                List<User> userList = userService.listByLoginName(userNames);
                Map<String, User> userMap = userList.stream().collect(Collectors.toMap(User::getLoginName, user -> user));
                for (String userName : userNames) {
                    if (ObjectUtil.isEmpty(userMap.get(userName))) {
                        return AjaxJson.failed("登陆名【" + userName + "】不存在，请重新输入！");
                    }
                }
                userIds = userList.stream().map(User::getId).collect(Collectors.toList());
            } else {
                return AjaxJson.failed("消息提醒对象为用户时，用户名不能为空，请重新输入！");
            }
        }

        notificationCategory.setContent(HtmlUtils.htmlUnescape(notificationCategory.getContent()));
        if (StringUtils.isNotBlank(notificationCategory.getId())) {
            notificationCategoryService.updateEntity(notificationCategory, roleIds, userIds);
        } else {
            notificationCategoryService.saveEntity(notificationCategory, roleIds, userIds);
        }
        return success();
    }
    @RequiresPermissions(SysResourceConstant.SYS_NOTIFY_CATEGORY_DELETE)
    @PostMapping("/deleteCategory")
    @ResponseBody
    public Object deleteCategory(@RequestParam("ids[]") List<String> ids) {
        notificationCategoryService.deleteCategory(ids);
        return success();
    }
    /**
     * 获取重点关注的信息
     *
     * @param restInput
     * @return
     */
    @PostMapping("/listStarTarget")
    @ResponseBody
    public Object listStarTarget(@RequestBody RestInput restInput) {
        return notificationService.listStarTarget(this.getCurrentUser().getId());
    }
    /**
     * 根据消息分类查询消息
     *
     * @param categoryId
     * @param model
     * @return
     */
    @GetMapping("/query/{categoryId}")
    public String query(@PathVariable("categoryId") String categoryId, Model model) {
        if (!CommonConstant.NO.equals(categoryId)) {
            model.addAttribute("categoryId", categoryId);
        }
        return "sys/notification/listCategoryMsg";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        Locale locale = LocaleContextHolder.getLocale();
        return notificationService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(), locale.toString());
    }
    /**
     * 获取重点关注的信息
     *
     * @param restInput
     * @return
     */
    @PostMapping("/listExpire")
    @ResponseBody
    public Object listExpire(@RequestBody RestInput restInput) {
        return notificationService.listExpire(restInput, this.getCurrentUser().getId());
    }
    /**
     * 获取重点关注的信息
     *
     * @param restInput
     * @return
     */
    @PostMapping("/listExpireByCreateDate")
    @ResponseBody
    public Object listExpireByCreateDate(@RequestBody RestInput restInput) {
        return notificationService.listExpireByCreateDate(this.getCurrentUser().getId(), restInput);
    }
}
