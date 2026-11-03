package com.bytter.system.service.impl;
import java.net.URLEncoder;
import java.util.*;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.bytter.system.api.dto.GroupChatBotMsgDTO;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.third.dto.GroupChatBotResp;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountMappingService;
import com.bytter.system.api.IDingTalkMessageService;
import com.bytter.system.api.IDingTalkPropertiesService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.system.service.H5DingTalkService;
import com.bytter.system.service.NotificationCategoryService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 钉钉平台-消息推送Service接口
 *
 * @author chenlw
 * @date 2020/12/01
 * todo 消息通知造成循环依赖
 */
@Service("dingtalkNotificationServiceI")
@DubboService
public class DingtalkNotificationServiceImpl implements IDingTalkMessageService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 消息推送URL
     */
    private static final String PUSH_MESSAGE_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
    /**
     * 机器人群发推送URL
     */
    private static final String GROUP_CHAT_ROBoT_MESSAGE_URL = "https://oapi.dingtalk.com/robot/send";
    @Autowired
    private IDingTalkPropertiesService dingTalkPropertiesService;
    @Autowired
    //@Qualifier("h5DingTalkServiceBaseBase")
    private H5DingTalkService dingService;
    @Autowired
    private IAccountMappingService accountMapService;
    @Autowired
    //@Qualifier("notificationCategoryServiceBase")
    private NotificationCategoryService notificationCategoryService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Override
    public boolean isAllowedPushMessage() {
        return dingTalkPropertiesService.getPushMessageSwitch();
    }
    @Override
    public boolean isPushApprovalCompletedMsgToApplicant() {
        return dingTalkPropertiesService.isPushApprovalCompletedMsgToApplicant();
    }
    @Override
    public void pushMarkdownMessage(String userId, String title, String content) {
        try {
            // 查询钉钉账户映射关系
            AccountMappingDTO accountMappingDTO = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.DING_TALK);
            if (accountMappingDTO == null) {
                logger.error("推送Markdown消息到钉钉失败。原因：用户未绑定钉钉账号。用户ID为：" + userId);
                return;
            }
            String accessToken = dingService.getAccessToken();
            DingTalkClient client = new DefaultDingTalkClient(PUSH_MESSAGE_URL);
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(Long.parseLong(dingTalkPropertiesService.getAgentId()));
            req.setUseridList(accountMappingDTO.getRelationUserId());
            req.setToAllUser(false);
            OapiMessageCorpconversationAsyncsendV2Request.Msg msgObj = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msgObj.setMsgtype("markdown");
            OapiMessageCorpconversationAsyncsendV2Request.Markdown markdownMsg = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
            markdownMsg.setTitle(title);
            markdownMsg.setText(content);
            msgObj.setMarkdown(markdownMsg);
            req.setMsg(msgObj);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, accessToken);
            logger.info("推送Markdown消息到钉钉====接口响应内容：" + rsp.getBody());
        } catch (Exception e) {
            logger.error("推送Markdown消息到钉钉发生错误！错误信息：{}", e.getMessage(), e);
        }
    }
//    @Override
//    public void pushTodoApprovalLinkNotificationToApprover(String userId, String taskId, String processDefinitionKey, Object object) {
//        // 查询钉钉账户映射关系
//        AccountMappingDTO accountMappingDTO = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.DING_TALK);
//        if (accountMappingDTO == null) {
//            logger.error("推送消息到钉钉失败。原因：审批人未绑定钉钉账号。");
//            return;
//        }
//        FlowCategoryDTO flowCategoryDTO = flowCategoryService.findDtoByKey(processDefinitionKey);
//        if (flowCategoryDTO == null) {
//            logger.error("推送消息到钉钉失败。原因：根据processDefinitionKey[{}]找不到对应的流程分类。", processDefinitionKey);
//            return;
//        }
//        // 非移动端审批不推送
//        if (flowCategoryDTO.getMobileApprovalFlag() != null &&
//                FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
//            logger.error("推送消息到钉钉失败。原因：流程分类[{}]不是移动端审批。", flowCategoryDTO.getName());
//            return;
//        }
//        if (StringUtils.isEmpty(flowCategoryDTO.getNotificationId())) {
//            logger.error("推送消息到钉钉失败。原因：流程分类[{}]未挂接消息通知。", flowCategoryDTO.getName());
//            return;
//        }
//        NotificationCategory notificationCategory = notificationCategoryService.get(flowCategoryDTO.getNotificationId(), true);
//        if (notificationCategory == null) {
//            logger.error("推送消息到钉钉失败。原因：根据ID[{}]找不到对应的流程分类通知（未挂接消息通知）。", flowCategoryDTO.getNotificationId());
//            return;
//        }
//        // 推送待审批OA消息
//        pushTodoApprovalLinkedOaMessage(userId, taskId, flowCategoryDTO, notificationCategory, object);
//    }
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
            // 查询钉钉账户映射关系
            AccountMappingDTO accountMappingDTO = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.DING_TALK);
            if (accountMappingDTO == null) {
                logger.error("推送消息到钉钉失败。原因：审批人未绑定钉钉账号。");
                return;
            }
            /*FlowCategoryDTO flowCategoryDTO = flowCategoryService.findDtoByKey(processDefinitionKey);
            if (flowCategoryDTO == null) {
                logger.error("推送消息到钉钉失败。原因：根据processDefinitionKey[{}]找不到对应的流程分类。", processDefinitionKey);
                return;
            }
            // 非移动端审批不推送
            if (flowCategoryDTO.getMobileApprovalFlag() != null &&
                    FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
                logger.error("推送消息到钉钉失败。原因：流程分类[{}]不是移动端审批。", flowCategoryDTO.getName());
                return;
            }
            if (StringUtils.isEmpty(flowCategoryDTO.getNotificationId())) {
                logger.error("推送消息到钉钉失败。原因：流程分类[{}]未挂接消息通知。", flowCategoryDTO.getName());
                return;
            }*/
            List<String> dingTalkUserIdList = new ArrayList<>(1);
            dingTalkUserIdList.add(accountMappingDTO.getRelationUserId());
            StringBuilder content = new StringBuilder();
            // 给同一员工发送内容相同的消息，一天只能发一次，因此加时间
            content.append(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL)).append("：");
            //content.append("【").append(flowCategoryDTO.getName()).append("】");
            // 获取该流程分类的消息通知模板并解析
//            String notificationTemplateContent = notificationService.getNotificationMessageTemplateContent(flowCategoryDTO.getNotificationId(), object);
//            if (StringUtils.isEmpty(notificationTemplateContent)) {
//                notificationTemplateContent = String.format("您有一笔【%s】流程待审批。",flowCategoryDTO.getName());
//            }
            String notificationTemplateContent = "";
            Map<String, Object> variables = (HashMap) object;
            notificationTemplateContent = (String) variables.get("messageTemplate");//移动端配置的消息内容
            if (StringUtils.isEmpty(notificationTemplateContent)) {
            /*    IOptionService optionService = (IOptionService") ApplicationContextUtil.getContext().getBean("optionServiceImpl");
                OptionDTO messageTemplateByOption = optionService.getMessageTemplateByCategory(flowCategoryDTO.getId());
                if (messageTemplateByOption != null && StringUtils.isNotBlank(messageTemplateByOption.getMsgTpl())) {
                    notificationTemplateContent = VelocityUtils.processTemplate(messageTemplateByOption.getMsgTpl(), variables);
                }*/
            }
            if (StringUtils.isEmpty(notificationTemplateContent)) {
                //notificationTemplateContent = String.format("您有一笔【%s】流程待审批。", flowCategoryDTO.getName());
            }
            content.append(notificationTemplateContent);
            pushTextMessage(dingTalkUserIdList, "", content.toString());
        } catch (Exception e) {
            logger.error(String.format("推送消息到钉钉发生错误！错误信息：%s", e.getMessage()), e);
        }
    }

    /**
     * 群聊机器人群发消息
     *
     * @param token 机器人token
     * @param secret 机器人安全验证secrest
     * @param content 文本内容
     * @param isAllPush 是否发送所有人
     * @param userIds 要@发送的钉钉用户id列表
     * @param phoneNumbers 要@发送的手机号列表
     */
    @Override
    public GroupChatBotMsgDTO groupChatRobotPushText(String token,String secret,String content, boolean isAllPush, List<String> userIds, List<String> phoneNumbers) {
        OapiRobotSendResponse rsp = null;
        GroupChatBotMsgDTO dto = new GroupChatBotMsgDTO();
        try {
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
            //sign字段和timestamp字段必须拼接到请求URL上，否则会出现 310000 的错误信息
            DingTalkClient client = new DefaultDingTalkClient(GROUP_CHAT_ROBoT_MESSAGE_URL+"?sign="+sign+"&timestamp="+timestamp);
            OapiRobotSendRequest req = new OapiRobotSendRequest();
            /**
             * 发送文本消息
             */
            //定义文本内容
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(content);
            //定义 @ 对象
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            if(isAllPush){
                at.setIsAtAll(true);
            }
//            if(CollectionUtil.isNotEmpty(userIds)){
//                at.setAtUserIds(userIds);
//            }
            if(CollectionUtil.isNotEmpty(phoneNumbers)){
                at.setAtMobiles(phoneNumbers);
            }
            //设置消息类型
            req.setMsgtype("text");
            req.setText(text);
            req.setAt(at);
            dto.setReqMsg(JSON.toJSONString(req));
            rsp = client.execute(req, token);
            GroupChatBotResp resp = JSON.parseObject(rsp.getBody(), GroupChatBotResp.class);
            if(BaseDataConstant.EXCEPTION_WARN_STATUS_0.equals(resp.getErrcode())){
                dto.setStatus(BaseDataConstant.EXCEPTION_WARN_STATUS_0);
                dto.setRespMsg(rsp.getBody());
                logger.info("钉钉机器人群发消息成功！");
            }else{
                dto.setStatus(BaseDataConstant.EXCEPTION_WARN_STATUS_1);
                dto.setRespMsg(rsp.getBody());
                logger.error("钉钉机器人群发消息发生异常，错误信息：{}", resp.getErrmsg());
            }
        } catch (Exception e) {
            logger.error(String.format("钉钉机器人群发消息请求失败！错误信息：%s", e.getMessage()), e);
            e.printStackTrace();
            dto.setStatus(BaseDataConstant.EXCEPTION_WARN_STATUS_1);
            dto.setRespMsg(e.getMessage());
        }
        return dto;
    }

    /**
     * 推送 文本消息 到钉钉
     *
     * @param dingTalkUserIdList
     * @param title
     * @param description
     */
    private void pushTextMessage(List<String> dingTalkUserIdList, String title, String description) {
        if (CollectionUtils.isEmpty(dingTalkUserIdList)) {
            logger.error("消息接收者集合为空，无法推送文本消息到钉钉！");
            return;
        }
        try {
            logger.info("推送文本消息到钉钉。用户userid列表：{}，title:{},description:{}", dingTalkUserIdList, title, description);
            String accessToken = dingService.getAccessToken();
            DingTalkClient client = new DefaultDingTalkClient(PUSH_MESSAGE_URL);
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(Long.parseLong(dingTalkPropertiesService.getAgentId()));
            req.setUseridList(getTouserString(dingTalkUserIdList));
            req.setToAllUser(false);
            OapiMessageCorpconversationAsyncsendV2Request.Msg msgObj = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msgObj.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text textObj = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            textObj.setContent(description);
            msgObj.setText(textObj);
            req.setMsg(msgObj);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, accessToken);
            logger.info("推送文本消息到钉钉====接口响应内容：" + rsp.getBody());
        } catch (Exception e) {
            logger.error("推送文本消息到钉钉发生错误！错误信息：{}", e.getMessage(), e);
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
