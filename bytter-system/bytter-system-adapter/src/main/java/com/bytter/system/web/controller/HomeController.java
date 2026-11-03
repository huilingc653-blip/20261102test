package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.system.api.enums.UserMessageEnum;
import com.bytter.system.entity.Role;
import com.bytter.system.entity.User;
import com.bytter.system.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/10/12.
 */
@Controller
@RequestMapping("/sys/home")
public class HomeController extends BaseController {
    private static final String WIDGET_TYPE_CHART = "chart";
    private static final String WIDGET_TYPE_WIDGET_CONTENT = "widgetContent";
    private static final String WIDGET_TYPE_USER_INFO = "userInfo";
    private static final String WIDGET_TYPE_TODO_INFO = "todoInfo";
    @Resource
    private HomeService homeService;
    //@Resource
    //private IWorkFlowService workFlowService;
    @Resource
    private NotificationService notificationService;
    @Resource
    private HomeWidgetService homeWidgetService;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @PostMapping("/save")
    @ResponseBody
    public Object save(@RequestBody List<Map<String, Object>> content) {
        if (homeService.judgeRepeat(content)) {
            throw new BusinessException(UserMessageEnum.SYS_HOME_WIDGET_NOT_REPEAT);
        }
        homeService.save(content, getUserId());
        return success();
    }
    @GetMapping("/resetDefault")
    @ResponseBody
    public Object resetDefault() {
        homeService.resetDefault(getUserId());
        return success();
    }
    /**
     * 获取首页最新的布局
     *
     * @return
     */
    @GetMapping("/newest")
    @ResponseBody
    public Object newest() {
        return homeService.newest(getUserId());
    }
    /**
     * 获取组件内容
     */
    @PostMapping("/widgetContent")
    public String widgetContent(@RequestBody Map<String, Object> widget) {
        String content = (String) widget.get("content");
        ICurrentUser currentUser = getCurrentUser();
        if (!homeWidgetService.judgeWidgetPermission(content, currentUser)) {
            return "common/noPermission";
        }
        return "forward:" + content;
    }
    @PostMapping("/userInfo")
    public void userInfo(Model model) {
        ShiroUser principal = (ShiroUser) getCurrentUser();
        User user = userService.get(getUserId());
        List<Role> roleList = roleService.getRoleListByUser(user);
        StringBuilder roles = new StringBuilder();
        for (Role role : roleList) {
            roles.append(role.getName()).append("  ");
        }
        model.addAttribute("roles", roles.toString());
        model.addAttribute("corp", user.getCorp());
    }
    @PostMapping("/usuallyMenu")
    public void usuallyMenu() {
    }
    /**
     * 待办数量 组件
     * 数量记录
     * todo 接口放置在这不适合
     */
    @PostMapping("/todoInfo")
    public void todoInfo(Model model) {
        /*Integer userNotificationCount = notificationService.getUserNotificationCount(getUserId());
        Map<String, Object> flowNumber = workFlowService.getFlowNumber(getUserId());
        int finishedCount = workFlowService.getFinishedTaskNumber(getUserId(), new Date(), new Date());
        flowNumber.put("notificationCount", userNotificationCount);
        flowNumber.put("finishedCount", finishedCount);
        model.addAttribute("data", flowNumber);*/
        HashMap<String, Object> flowNumber = new HashMap<String, Object>() {{
            put("todoCount", 10);
            put("startCount", 10);
            put("notificationCount", 10);
            put("finishedCount", 10);
        }};
        model.addAttribute("data", flowNumber);
    }
    @PostMapping("/chartA")
    public void chartA() {
    }
    @PostMapping("/chartB")
    public void chartB() {
    }
    @PostMapping("/chartC")
    public void chartC() {
    }
}
