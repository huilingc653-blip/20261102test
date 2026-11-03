package com.bytter.system.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountMappingService;
import com.bytter.system.api.IFeishuMessageService;
import com.bytter.system.api.IFeishuPropertiesService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.system.service.H5FeishuService;
import com.bytter.system.service.NotificationCategoryService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * 飞书平台-消息推送Service接口
 *
 * @author chenlw
 * @date 2020/12/01
 */
@Service("feishuNotificationServiceI")
@DubboService
public class FeishuNotificationServiceImpl implements IFeishuMessageService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 消息推送URL
     */
    private static final String PUSH_MESSAGE_URL = "https://open.feishu.cn/open-apis/im/v1/messages";
    @Autowired
    private IFeishuPropertiesService feishuPropertiesService;
    @Autowired
    //@Qualifier("h5FeishuServiceBase")
    private H5FeishuService feishuService;
    @Autowired
    private IAccountMappingService accountMapService;
    @Autowired
    //@Qualifier("notificationCategoryServiceBase")
    private NotificationCategoryService notificationCategoryService;
    // @Autowired
    // private IFlowCategoryService flowCategoryService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Override
    public boolean isAllowedPushMessage() {
        return feishuPropertiesService.getPushMessageSwitch();
    }
    @Override
    public boolean isPushApprovalCompletedMsgToApplicant() {
        return feishuPropertiesService.isPushApprovalCompletedMsgToApplicant();
    }
    @Override
    public void pushMarkdownMessage(String userId, String title, String content) {
        try {
            // 查询飞书账户映射关系
            AccountMappingDTO accountMappingDTO = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.FEISHU);
            if (accountMappingDTO == null) {
                logger.error("推送Markdown消息到飞书失败。原因：用户未绑定飞书账号。用户ID为：" + userId);
                return;
            }
            if (EmptyUtils.isEmpty(accountMappingDTO.getRelationUserId())) {
                logger.error("推送Markdown消息到飞书失败。原因：用户未绑定飞书账号。用户ID为：" + userId);
                return;
            }
            feishuService.sendMessage(content, accountMappingDTO.getRelationUserId());
            /*String accessToken = feishuService.getAppAccessToken();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            String contentText = "{\"content\":\" {\\\"text\\\":\\\""
                    + content + "\\\"}\",\"msg_type\":\"text\",\"receive_id\":\""
                    + accountMappingDTO.getRelationUserId() + "\",\"uuid\":\"" + UUID.randomUUID() + "\"}";
            RequestBody body = RequestBody.create(mediaType, contentText);
            Request request = new Request.Builder()
                    .url(PUSH_MESSAGE_URL + "?receive_id_type=user_id")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            logger.error("推送Markdown消息到飞书====接口响应内容：" + response.body());*/
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("推送Markdown消息到飞书发生错误！错误信息：{}", e.getMessage(), e);
        }
    }
    /**
     * 给同一员工发送内容相同的消息，一天只能发一次
     *
     * @param userId               用户ID
     * @param processDefinitionKey 流程分类KEY
     * @param businessKey          业务ID
     * @param object               模版解析对象
     */
    @Override
    public void pushTodoNotificationToApprover(String userId, String processDefinitionKey, String businessKey, Object object) {
        try {
            // 查询飞书账户映射关系
            AccountMappingDTO accountMappingDTO = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.FEISHU);
            if (accountMappingDTO == null) {
                logger.error("推送消息到飞书失败。原因：审批人未绑定飞书账号。");
                return;
            }
           /* FlowCategoryDTO flowCategoryDTO = flowCategoryService.findDtoByKey(processDefinitionKey);
            if (flowCategoryDTO == null) {
                logger.error("推送消息到飞书失败。原因：根据processDefinitionKey[{}]找不到对应的流程分类。", processDefinitionKey);
                return;
            }*/
            // 非移动端审批不推送
           /* if (flowCategoryDTO.getMobileApprovalFlag() != null &&
                    FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
                logger.error("推送消息到飞书失败。原因：流程分类[{}]不是移动端审批。", flowCategoryDTO.getName());
                return;
            }*/
           /* if (StringUtils.isEmpty(flowCategoryDTO.getNotificationId())) {
                logger.error("推送消息到飞书失败。原因：流程分类[{}]未挂接消息通知。", flowCategoryDTO.getName());
                return;
            }*/
            List<String> dingTalkUserIdList = new ArrayList<>(1);
            dingTalkUserIdList.add(accountMappingDTO.getRelationUserId());
            StringBuilder content = new StringBuilder();
            // 给同一员工发送内容相同的消息，一天只能发一次，因此加时间
            content.append(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL)).append("：");
            content.append("【").append("flowCategoryDTO.getName()").append("】");
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
            content.append(notificationTemplateContent);
            pushTextMessage(accountMappingDTO.getRelationUserId(), "", content.toString());
        } catch (Exception e) {
            logger.error(String.format("推送消息到飞书发生错误！错误信息：%s", e.getMessage()), e);
        }
    }
    /**
     * 推送 文本消息 到飞书
     *
     * @param dingTalkUserId
     * @param title
     * @param description
     */
    private void pushTextMessage(String dingTalkUserId, String title, String description) {
        if (EmptyUtils.isEmpty(dingTalkUserId)) {
            logger.error("消息接收者集合为空，无法推送文本消息到飞书！");
            return;
        }
        try {
            logger.info("推送文本消息到飞书。用户userid列表：{}，title:{},description:{}", dingTalkUserId, title, description);
            String accessToken = feishuService.getAppAccessToken();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            String contentText = "{\"content\":\" {\\\"text\\\":\\\" "
                    + description + "\\\"}\",\"msg_type\":\"text\",\"receive_id\":\""
                    + dingTalkUserId + "\",\"uuid\":\"" + UUID.randomUUID() + "\"}";
            RequestBody body = RequestBody.create(mediaType, contentText);
            Request request = new Request.Builder()
                    .url(PUSH_MESSAGE_URL + "?receive_id_type=user_id")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            logger.info("推送文本消息到飞书====接口响应内容：" + response.body());
        } catch (Exception e) {
            logger.error("推送文本消息到飞书发生错误！错误信息：{}", e.getMessage(), e);
        }
    }
    private String getTouserString(List<String> dingTalkUserId) {
        StringBuilder sb = new StringBuilder();
        int len = dingTalkUserId.size();
        if (len > 100) {
            len = 100;
        }
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                sb.append(dingTalkUserId.get(i));
            } else {
                sb.append(dingTalkUserId.get(i));
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
