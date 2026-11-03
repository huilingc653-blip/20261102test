package com.bytter.system.service.impl;
import com.bytter.core.enums.AppPageEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IPushMessageService;
import com.bytter.system.api.dto.AppNotificationPushDTO;
import com.bytter.system.entity.User;
import com.bytter.system.service.PushService;
import com.bytter.system.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b> 消息随同  <br>
 * <b>作者：</b>chenlw<br>
 * <b>日期：</b> 2020-12-21 <br>
 */
@Service("pushMessageServiceI")
@DubboService
public class PushMessageServiceImpl implements IPushMessageService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PushService pushService;
    @Autowired
    private UserService userService;
    @Override
    public boolean isPushSwitch() {
        return pushService.isPushSwitch();
    }
    @Override
    public void pushTodoApprovalNotificationToApprover(String userId, String taskId, String processDefinitionKey, Object object) {
        if (!isPushSwitch()) {
            return;
        }
    /*    FlowCategoryDTO flowCategoryDTO = flowCategoryService.findDtoByKey(processDefinitionKey);
        if (flowCategoryDTO == null) {
            logger.error("推送消息到APP失败。原因：根据processDefinitionKey[{}]找不到对应的流程分类。", processDefinitionKey);
            return;
        }*/
        // 非移动端审批不推送 （V22020098需求都非移动端也推送消息）
        /*if (flowCategoryDTO.getMobileApprovalFlag() != null &&
                FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
            logger.error("推送消息到APP失败。原因：流程分类[{}]不是移动端审批", flowCategoryDTO.getName());
            return;
        }*/
        /*if (StringUtils.isEmpty(flowCategoryDTO.getNotificationId())) {
            logger.error("推送消息到APP失败。原因：流程分类[{}]未挂接消息通知。", flowCategoryDTO.getName());
            return;
        }*/
        User user = userService.get(userId);
        if (user == null) {
            return;
        }
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("id", taskId);
        // notificationMap.put("categoryId", flowCategoryDTO.getId());
        //INotificationService notificationService = (INotificationService") ApplicationContextUtil.getContext().getBean("notificationServiceImpl");
        //String notificationTemplateContent = notificationService.getNotificationMessageTemplateContent(flowCategoryDTO.getNotificationId(), object);
        String notificationTemplateContent = "";
        Map<String, Object> variables = (HashMap) object;
        notificationTemplateContent = (String) variables.get("messageTemplate");//移动端配置的消息内容
        if (StringUtils.isEmpty(notificationTemplateContent)) {
           /* IOptionService optionService = (IOptionService") ApplicationContextUtil.getContext().getBean("optionServiceImpl");
            OptionDTO messageTemplateByOption = optionService.getMessageTemplateByCategory(flowCategoryDTO.getId());
            if (messageTemplateByOption != null && StringUtils.isNotBlank(messageTemplateByOption.getMsgTpl())) {
                notificationTemplateContent = VelocityUtils.processTemplate(messageTemplateByOption.getMsgTpl(), variables);
            }*/
        }
        if (StringUtils.isEmpty(notificationTemplateContent)) {
            notificationTemplateContent = String.format("您有一笔【%s】流程待审批。", "flowCategoryDTO.getName()");
        }
//        String notificationTemplateContent = String.format("您有一笔【%s】流程待审批。",flowCategoryDTO.getName());
        try {
            pushService.pushNotice(Collections.singletonList(user.getLoginName()),
                    "flowCategoryDTO.getName()", notificationTemplateContent,
                    AppPageEnum.APPROVE_DETAIL, notificationMap, true);
            logger.info("=====推送审批消息到APP成功=====");
        } catch (Exception e) {
            logger.error("推送消息到APP-异常：" + e.getMessage(), e);
        }
    }
    @Override
    public void pushNotification(AppNotificationPushDTO appNotificationPushDTO) {
        if (!isPushSwitch()) {
            return;
        }
        String userId = appNotificationPushDTO.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        User user = userService.get(userId);
        if (user == null) {
            return;
        }
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("id", appNotificationPushDTO.getNotificationId());
        notificationMap.put("categoryName", appNotificationPushDTO.getCategoryName());
        Date createdDate = appNotificationPushDTO.getCreateDate();
        if (createdDate != null) {
            notificationMap.put("createDate", createdDate.getTime());
        } else {
            notificationMap.put("createDate", DateUtils.getDate().getTime());
        }
        notificationMap.put("name", appNotificationPushDTO.getName());
        try {
            pushService.pushNotice(Collections.singletonList(user.getLoginName()),
                    appNotificationPushDTO.getCategoryName(), appNotificationPushDTO.getName(),
                    AppPageEnum.NOTIFICATION, notificationMap, true);
            logger.info("=====推送消息到APP成功=====");
        } catch (Exception e) {
            logger.error("推送消息到APP-异常：" + e.getMessage(), e);
        }
    }
}
