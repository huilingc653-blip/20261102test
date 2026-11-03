package com.bytter.system.service.impl;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.OkHttpUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountMappingService;
import com.bytter.system.api.ICorpWeiXinMessageService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.system.api.dto.NotificationExtDTO;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.system.api.dto.GroupChatBotMsgDTO;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.third.dto.GroupChatBotResp;
import com.bytter.third.service.CorpWxClient;
import com.bytter.system.service.NotificationCategoryService;
import com.bytter.third.service.ThirdProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 企业微信-消息推送Service接口
 *
 * @author chenlw
 * @date 2020/03/02
 */
@Service("corpWeiXinServiceI")
@DubboService
public class CorpWeiXinMessageServiceImpl implements ICorpWeiXinMessageService {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * 消息推送URL
     */
    private static final String PUSH_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
    /**
     * 企业应用的id，整型。企业内部开发，可在应用的设置页面查看
     */
    @Value("${weiXin.corp.agentid}")
    private String agentid;
    /**
     * 微信企业id
     */
    @Value("${weiXin.corp.corpid}")
    private String weiXinCorpId;

    @Value("${weiXin.corp.corpsecret}")
    private String weiXinCorpSecret;

    /**
     * 是否推送消息到企业微信
     */
    @Value("${weiXin.pushMessage.switch}")
    private boolean pushSwitch;
    /**
     * 是否推送审批办结消息给审批发起人
     */
    @Value("${weiXin.pushMessage.isPushApprovalCompletedMsgToApplicant}")
    private boolean isPushApprovalCompletedMsgToApplicant;

    @Autowired
    private CorpWxClient corpWxClient;
    @Autowired
    private NotificationCategoryService notificationCategoryService;
    @Autowired
    private IAccountMappingService accountMapService;
    @Autowired
    @Lazy
    private INotificationService notificationService;
    @Autowired
    private ThirdProperties thirdProperties;

    @Override
    public boolean isAllowedPushMessage() {
        return this.pushSwitch;
    }
    @Override
    public boolean isPushApprovalCompletedMsgToApplicant() {
        return this.isPushApprovalCompletedMsgToApplicant;
    }
    @Override
    public NotificationExtDTO pushMarkdownMessage(String userId, String title, String content) {

        // 查询企业微信账户映射关系
        AccountMappingDTO wxAccountMap = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.CORP_WEI_XIN);
        if (wxAccountMap == null) {
            logger.error("推送消息到企业微信失败。原因：审批人未绑定企业微信账号。");
            return new NotificationExtDTO("推送消息到企业微信失败。原因：审批人未绑定企业微信账号", null, null);
        }
        logger.info("推送Markdown消息到企业微信。weixinUserId：{}，消息内容：{}", wxAccountMap.getRelationUserId(), content);
        String accessToken = corpWxClient.getAppAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            logger.error("获取企业token失败！");
            return new NotificationExtDTO("获取企业token失败", null, null);
        }

        ObjectNode submitDataBody = (ObjectNode) JsonUtils.getJsonNode("{}");
        submitDataBody.put("touser", wxAccountMap.getRelationUserId());
        submitDataBody.put("msgtype", "markdown");
        submitDataBody.put("agentid", this.agentid);
        ObjectNode markdownJson = (ObjectNode) JsonUtils.getJsonNode("{}");
        markdownJson.put("content", content);
        submitDataBody.put("markdown", markdownJson);
        String sendBody = null;
        String response = null;
        String result = null;
        try {
            sendBody = StringEscapeUtils.unescapeJava(JsonUtils.toJSONString(submitDataBody));
            response = OkHttpUtils.postJson(PUSH_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken), sendBody);
            result = handlePushMessageResponseData(response);
        } catch (Exception e) {
            logger.error("推送Markdown消息到企业微信发生错误！错误信息：{}", e.getMessage(), e);
        }
        return new NotificationExtDTO(result, sendBody, response);
    }

    /**
     *
     * @param userId
     * @param content
     * @return
     */
    public NotificationExtDTO pushTextMessage(String userId, String content) {
        // 查询企业微信账户映射关系
        AccountMappingDTO wxAccountMap = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.CORP_WEI_XIN);
        if (wxAccountMap == null) {
            logger.error("推送消息到企业微信失败。原因：审批人未绑定企业微信账号。");
            return new NotificationExtDTO("推送消息到企业微信失败。原因：审批人未绑定企业微信账号。", null, null);
        }
        logger.info("推送文本消息到企业微信。weixinUserId：{}，消息内容：{}", wxAccountMap.getRelationUserId(), content);
        String accessToken = corpWxClient.getAppAccessToken();
        ObjectNode submitDataBody = (ObjectNode) JsonUtils.getJsonNode("{}");
        submitDataBody.put("touser", wxAccountMap.getRelationUserId());
        submitDataBody.put("msgtype", "text");
        submitDataBody.put("agentid", this.agentid);
        ObjectNode msgJson = (ObjectNode) JsonUtils.getJsonNode("{}");
        msgJson.put("content", content);
        submitDataBody.put("text", msgJson);
        String sendBody = null;
        String response = null;
        String result = null;
        try {
            sendBody = StringEscapeUtils.unescapeJava(JsonUtils.toJSONString(submitDataBody));
            response = OkHttpUtils.postJson(PUSH_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken), sendBody);
            result = handlePushMessageResponseData(response);
        } catch (Exception e) {
            logger.error("推送文本消息到企业微信发生错误！错误信息：{}", e.getMessage(), e);
        }
        return new NotificationExtDTO(result, sendBody, response);
    }
    /**
     * 推送文本卡片消息
     *
     * @param weixinUserId 微信用户ID
     * @param title        消息标题，不超过128个字节，超过会自动截断
     * @param description  消息描述，不超过512个字节，超过会自动截断
     * @param url          点击后跳转的链接。
     */
    private void pushTextCardMessage(String weixinUserId, String title, String description, String url) {
        try {
            logger.info("推送文本卡片消息到企业微信。weixinUserId：{}，title:{},description:{},url:{}", weixinUserId, title, description, url);
            String accessToken = corpWxClient.getAppAccessToken();
            ObjectNode submitDataBody = (ObjectNode) JsonUtils.getJsonNode("{}");
            // 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）
            submitDataBody.put("touser", weixinUserId);
            // params.put("toparty", "");
            // params.put("totag", "");
            // 消息类型，此时固定为：textcard
            submitDataBody.put("msgtype", "textcard");
            submitDataBody.put("agentid", this.agentid);
            ObjectNode textcardObj = (ObjectNode) JsonUtils.getJsonNode("{}");
            textcardObj.put("title", title);
            textcardObj.put("description", description);
            textcardObj.put("url", url);
            submitDataBody.put("textcard", textcardObj);
            submitDataBody.put("enable_id_trans", "0");
            //todo
            String response = OkHttpUtils.postJson(PUSH_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken), JsonUtils.json2Map(submitDataBody.textValue()));
            handlePushMessageResponseData(response);
        } catch (Exception e) {
            logger.error("推送文本卡片消息到企业微信发生错误！错误信息：{}", e.getMessage(), e);
        }
    }
    @Override
    public void pushTodoApprovalLinkNotificationToApprover(String userId, String taskId, String processDefinitionKey, Object object) {
        try {
            // 查询企业微信账户映射关系
            AccountMappingDTO wxAccountMap = accountMapService.queryAccountMapping(null, userId, AppH5Constants.ClientType.CORP_WEI_XIN);
            if (wxAccountMap == null) {
                logger.error("推送消息到企业微信失败。原因：审批人未绑定企业微信账号。");
                return;
            }
         /*   FlowCategoryDTO flowCategoryDTO = flowCategoryService.findDtoByKey(processDefinitionKey);
            if (flowCategoryDTO == null) {
                logger.error("根据processDefinitionKey[{}]找不到对应的流程分类，无法发送待审批工作流的通知到企业微信", processDefinitionKey);
                return;
            }*/
            // 非移动端审批不推送 （V22020098需求都非移动端也推送消息）
           /* if (flowCategoryDTO.getMobileApprovalFlag() != null &&
                    FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
                logger.error("流程分类[{}]不是移动端审批，因此不推送通知（带跳转链接）", flowCategoryDTO.getName());
                return;
            }*/
          /*  if (StringUtils.isEmpty(flowCategoryDTO.getNotificationId())) {
                logger.error("流程分类[{}]未挂接消息通知，无法发送待审批工作流的通知到企业微信", flowCategoryDTO.getName());
                return;
            }*/
            // 生成H5待审批页面的URL
            String h5TodoApprovalUrl = getH5TodoApprovalUrl(taskId);
//            String notificationTemplateContent = notificationService.getNotificationMessageTemplateContent(flowCategoryDTO.getNotificationId(), object);
//            if (StringUtils.isEmpty(notificationTemplateContent)) {
//                notificationTemplateContent = String.format("您有一笔【%s】流程待审批。",flowCategoryDTO.getName());
//            }
            String notificationTemplateContent = "";
            Map<String, Object> variables = (HashMap) object;
            notificationTemplateContent = (String) variables.get("messageTemplate");////移动端配置的消息内容
            if (StringUtils.isEmpty(notificationTemplateContent)) {
                /*IOptionService optionService = (IOptionService") ApplicationContextUtil.getContext().getBean("optionServiceImpl");
                OptionDTO messageTemplateByOption = optionService.getMessageTemplateByCategory(flowCategoryDTO.getId());
                if (messageTemplateByOption != null && StringUtils.isNotBlank(messageTemplateByOption.getMsgTpl())) {
                    notificationTemplateContent = VelocityUtils.processTemplate(messageTemplateByOption.getMsgTpl(), variables);
                }*/
            }
            if (StringUtils.isEmpty(notificationTemplateContent)) {
                //notificationTemplateContent = String.format("您有一笔【%s】流程待审批。", flowCategoryDTO.getName());
            }
            // 获取并解析流程的模板消息
            StringBuilder messageContent = new StringBuilder();
            messageContent.append("<div class='gray'>").append(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL)).append("</div>");
            messageContent.append("<div class='normal'>")
                    .append("【").append("flowCategoryDTO.getName()").append("】")
                    .append(notificationTemplateContent)
                    .append("</div>");
            pushTextCardMessage(wxAccountMap.getRelationUserId(), "待审批", messageContent.toString(), h5TodoApprovalUrl);
        } catch (Exception e) {
            logger.error(String.format("推送消息到企业微信（带跳转链接）发生错误！错误信息：%s", e.getMessage()), e);
        }
    }

    /**
     * 群聊机器人群发消息
     *
     * @param webhook webhook地址
     * @param content 文本内容
     * @param isAllPush 是否发送所有人
     * @param userIds 要@发送的企业微信用户名称列表
     * @param phoneNumbers 要@发送的企业微信手机号列表
     */
    @Override
    public GroupChatBotMsgDTO groupChatRobotPushText(String webhook, String content, boolean isAllPush, List<String> userIds, List<String> phoneNumbers) {
        GroupChatBotMsgDTO dto= new GroupChatBotMsgDTO();
        Map<String,Object> map = new HashMap();
        map.put("msgtype","text");
        Map<String,Object> textMap = new HashMap();
        textMap.put("content",content);
        if(isAllPush){
            textMap.put("mentioned_list","@all");
        }
        if(CollectionUtil.isNotEmpty(userIds)){
            textMap.put("mentioned_list",userIds);
        }
        if(CollectionUtil.isNotEmpty(phoneNumbers)){
            textMap.put("mentioned_mobile_list",phoneNumbers);
        }
        map.put("text",textMap);
        String reqBody = JSON.toJSONString(map);
        dto.setReqMsg(reqBody);
        HttpRequest post = HttpUtil.createPost(webhook);
        try {
            HttpResponse httpResponse = post.body(reqBody).execute();
            GroupChatBotResp resp = JSON.parseObject(httpResponse.body(), GroupChatBotResp.class);
            if(BaseDataConstant.EXCEPTION_WARN_STATUS_0.equals(resp.getErrcode())){
                dto.setStatus(BaseDataConstant.EXCEPTION_WARN_STATUS_0);
                dto.setRespMsg(httpResponse.body());
                logger.info("企业微信机器人群发消息成功！");
            }else{
                dto.setStatus(BaseDataConstant.EXCEPTION_WARN_STATUS_1);
                dto.setRespMsg(httpResponse.body());
                logger.error("企业微信机器人群发消息发生异常，错误信息：{}", resp.getErrmsg());
            }
        }catch (Exception e){
            logger.error(String.format("企业微信机器人群发消息请求失败！错误信息：%s", e.getMessage()), e);
            e.printStackTrace();
            dto.setStatus(BaseDataConstant.EXCEPTION_WARN_STATUS_1);
            dto.setRespMsg(e.getMessage());

        }
        return dto;
    }

    /**
     * 生成进入H5审批页面的URL
     *
     * @param approveId 审批ID
     * @return 进入H5审批详情页面的控制器层URL
     */
    private String getH5TodoApprovalUrl(String approveId) {
        StringBuilder url = new StringBuilder();
        url.append(thirdProperties.getTrustedHost() + "/mobile/h5/login/weixin/h5TodoApproveDetailPage");
        url.append("?taskId=" + approveId);
        // 进入H5审批页面的URL：http://wechart.t2.bytter.com/t2/mobile/h5/login/weixin/h5TodoApproveDetailPage?taskId=57a960c5-ecc2-11ea-9ad3-4437e6de2bfb
        // String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORP_ID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        // 构造网页授权链接
        return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect"
                , weiXinCorpId, url.toString());
    }
    /**
     * 处理推送消息返回的结果
     *
     * @param responseData 推送消息返回的结果
     * @throws Exception 异常
     */
    private String handlePushMessageResponseData(String responseData) throws Exception {

        logger.info("微信响应结果：{}",responseData);
        if (StringUtils.isEmpty(responseData)) {
            logger.error("微信响应结果为空");
            return "微信响应结果为空";
        }
        JsonNode jsonNode = OBJECT_MAPPER.readTree(responseData);
        int errcode = jsonNode.get("errcode").asInt();
        if (0 == errcode) {
            logger.info("推送消息到企业微信成功！");
            return "推送消息到企业微信成功";
        }
//        else if (81013 == errcode) {
//            logger.error("推送消息到企业微信失败！错误代码：{}，错误信息：{}，invaliduser：{}，invalidparty：{}，invalidtag:{}",
//                    errcode, jsonNode.get("errmsg").asText(), jsonNode.get("invaliduser").asText(), jsonNode.get("invalidparty").asText(),
//                    jsonNode.get("invalidtag").asText());
//        }
        else {
            logger.error("推送消息到企业微信发生异常，接口返回信息：{}", jsonNode.toString());
            return "推送消息到企业微信发生异常";
        }
    }
    private String getTouserString(List<String> loginNameList) {
        StringBuilder sb = new StringBuilder();
        int len = loginNameList.size();
        if (len > 1000) {
            // 消息接收者，多个接收者用‘|’分隔，最多支持1000个
            len = 1000;
        }
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                sb.append(loginNameList.get(i));
            } else {
                sb.append(loginNameList.get(i));
                sb.append("|");
            }
        }
        return sb.toString();
    }

    public String getWeiXinCorpSecret() {
        return weiXinCorpSecret;
    }

    public String getWeiXinCorpId() {
        return weiXinCorpId;
    }

    public String getAgentid() {
        return agentid;
    }
}
