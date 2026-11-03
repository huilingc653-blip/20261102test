package com.bytter.system.mobile.controller;
import com.bytter.system.dao.NotificationDao;
import com.bytter.system.entity.Notification;
import com.bytter.system.service.NotificationService;
import com.bytter.system.vo.NotificationResponseVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.persistence.BasePersistence;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.rest.RestInput;
import com.bytter.core.common.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * 用户管理控制器
 * Created by zhoul on 2017/4/11.
 */
@Controller
@RequestMapping("/api/notification")
public class NotificationApiController extends BaseController {
    @Resource
    private NotificationService notificationService;
    @Autowired
    private NotificationDao notificationDao;
    @GetMapping("/count")
    @ResponseBody
    public Object count() {
        Integer userAllNotificationCount = notificationService.getUserAllNotificationCount(getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("count", userAllNotificationCount);
        return map;
    }
    @GetMapping("/unreadCount")
    @ResponseBody
    public Object unreadNotificationCount() {
        Locale locale = LocaleContextHolder.getLocale();
        Integer unreadCount = notificationService.getUserUnreadNotificationCount(String.valueOf(locale), getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("count", unreadCount);
        return map;
    }
    @PostMapping("/list")
    @ResponseBody
    public Object list(@RequestBody RestInput restInput) {
        Locale locale = LocaleContextHolder.getLocale();
        Integer type = (Integer) restInput.get("type");
        RestResponse<NotificationResponseVO> result = null;
        switch (type) {
            case 0:
                // 未读消息
                result = notificationService.getUnreadUserNotificationNote(String.valueOf(locale), restInput);
                break;
            case 1:
                // 已读消息
                result = notificationService.getHaveReadUserNotificationNote(String.valueOf(locale), restInput);
                break;
            case 2:
                // 全部消息，包括未读和已读
                result = notificationService.apiRestResult(String.valueOf(locale), restInput);
                break;
            default:
                result = notificationService.apiRestResult(String.valueOf(locale), restInput);
        }
        return result;
    }
    /**
     * 将所有未读消息置为已读状态
     *
     * @return
     */
    @PostMapping("/haveReadAll")
    @ResponseBody
    public Object heaveReadAll() {
        String userId = getUserId();
        List<Notification> unreadNotificationList = notificationDao.listByUserId(userId);
        notificationService.createNotificationHistoricByNotificationList(unreadNotificationList, userId);
        return success();
    }
    @PostMapping("/haveRead")
    @ResponseBody
    public Object haveRead(@RequestBody BasePersistence basePersistence) {
        if (basePersistence.get("ids") != null) {
            List<String> notificationIdList = (List<String>) basePersistence.get("ids");
            String userId = basePersistence.getCurrentUserId();
            notificationService.createNotificationHistoric(notificationIdList, userId);
        }
        return success();
    }
    /**
     * 删除所有消息
     *
     * @return
     */
    @PostMapping("/deleteAll")
    @ResponseBody
    public Object deleteAll(@RequestBody RestInput restInput) {
        Integer type = (Integer) restInput.get("type");
        String userId = getUserId();
        switch (type) {
            case 0:
                // 删除未读消息
                notificationService.deleteUnreadUserNotificationByUserId(userId);
                break;
            case 1:
                // 删除已读消息
                notificationService.deleteHeadReadUserNotificationByUserId(userId);
                break;
            case 2:
                // 删除全部消息，包括未读和已读
                notificationService.deleteUnreadUserNotificationByUserId(userId);
                notificationService.deleteHeadReadUserNotificationByUserId(userId);
                break;
        }
        return success();
    }
    @PostMapping("/deleteBatch")
    @ResponseBody
    public Object deleteBatch(@RequestBody List<NotificationResponseVO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            notificationService.deleteBatch(list);
        }
        return success();
    }
    @GetMapping("/read")
    @ResponseBody
    public Object read(String notificationId) {
        Notification notification = notificationService.get(notificationId);
        if (notification == null) {
            throw new ParameterException("参数错误");
        }
        notificationService.createNotificationHistoric(notification, getUserId());
        return success();
    }
    @GetMapping("/delete")
    @ResponseBody
    public Object delete(String notificationId, Integer status) {
        notificationService.delete(notificationId, status);
        return success();
    }
}
