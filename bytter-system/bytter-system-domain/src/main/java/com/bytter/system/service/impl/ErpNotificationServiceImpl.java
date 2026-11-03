package com.bytter.system.service.impl;

import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IErpNotificationService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.entity.NotificationCategory;
import com.bytter.system.service.NotificationCategoryService;
import com.bytter.system.service.UserService;
import com.bytter.third.service.ThirdProperties;
import com.bytter.velocity.VelocityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author chenlw
 * @date 2020/03/09
 */
@Service("erpNotificationServiceI")
@DubboService
public class ErpNotificationServiceImpl implements IErpNotificationService {
    /**
     * ERP消息webservice接口的XML请求报文
     */
    private static final String ERP_NOTIFICATION_SERVICE_REQUEST_XML = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ser=\"http://server.webservice.hq.erp.hibernate.byttersoft.com\">\n" +
            "   <soap:Header/>\n" +
            "   <soap:Body>\n" +
            "      <ser:acceptWeixinSendMsg>\n" +
            "         <ser:data>%s</ser:data>\n" +
            "      </ser:acceptWeixinSendMsg>\n" +
            "   </soap:Body>\n" +
            "</soap:Envelope>";
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private NotificationCategoryService notificationCategoryService;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private UserService userService;
    // @Autowired
    // private IFlowCategoryService flowCategoryService;
    // todo 工作流消息推送是否在工作流
    // @Autowired
    // private TaskService taskService;

    /**
     * ERP消息通知接口地址
     */
    @Value("${erp.notification.acceptNotificationUrl}")
    private String erpAcceptNotificationUrl;
    /**
     * 是否推送消息到ERP接口
     */
    @Value("${erp.notification.pushSwitch}")
    private boolean pushSwitch;
    @Autowired
    private ThirdProperties thirdProperties;
    /**
     * 是否推送消息到ERP接口
     *
     * @return true表示允许推送；false表示不允许推送
     */
    @Override
    public boolean isAllowedPushNotification() {
        return pushSwitch;
    }
    /***************************  通用通知接口（无需处理消息） start *******************************/
    @Override
    public void sendUniversalNotificationToUserByUserId(String userId, String notificationContent) {
        List<String> userIdList = new ArrayList<>(1);
        userIdList.add(userId);
        sendUniversalNotificationToUserByUserIdList(userIdList, notificationContent);
    }
    @Override
    public void sendUniversalNotificationToUserByUserId(String userId, String notificationCategoryId, String notificationContent) {
        List<String> userIdList = new ArrayList<>(1);
        userIdList.add(userId);
        sendUniversalNotificationToUserByUserIdList(userIdList, notificationCategoryId, notificationContent);
    }
    @Override
    public void sendUniversalNotificationToUserByUserIdList(List<String> userIds, String notificationContent) {
        List<String> loginNameList = userService.listLoginNameByUserIds(userIds);
        if (loginNameList == null || loginNameList.size() == 0) {
            return;
        }
        // 推送消息
        pushUniversalNotification(loginNameList, notificationContent);
    }
    @Override
    public void sendUniversalNotificationToUserByUserIdList(List<String> userIds, String notificationCategoryId, String notificationContent) {
        List<String> loginNameList = userService.listLoginNameByUserIds(userIds);
        if (loginNameList == null || loginNameList.size() == 0) {
            return;
        }
        if (StringUtils.isEmpty(notificationCategoryId)) {
            return;
        }
     /*   FlowCategoryDTO flowCategoryDTO = flowCategoryService.findByNotificationId(notificationCategoryId);
        if (flowCategoryDTO == null) {
            logger.info("通过通知分类ID[{}]无法找到对应的流程分类，无法推送通知到ERP消息接口。", notificationCategoryId);
            return;
        }*/
        // 非移动端审批不推送
      /*  if (flowCategoryDTO.getMobileApprovalFlag() != null &&
                FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
            logger.info("流程分类[{}]不是移动端审批，因此不推送通用通知（无需处理消息）到ERP消息接口", flowCategoryDTO.getName());
            return;
        }*/
        // 推送消息
        pushUniversalNotification(loginNameList, notificationContent);
    }
//    @Override
//    public void sendUniversalNotificationToUserByUserId(String userId, String tag, Object object) {
//        if (StringUtils.isEmpty(userId)) {
//            return;
//        }
//        List<String> userIdList = new ArrayList<>(1);
//        userIdList.add(userId);
//        sendUniversalNotificationToUserByUserIdList(userIdList, tag, object);
//    }
//    @Override
//    public void sendUniversalNotificationToUserByUserIdList(List<String> userIdList, String tag, Object object) {
//        List<String> loginNameList = userService.listLoginNameByUserIds(userIdList);
//        sendUniversalNotificationToUserByLoginNameList(loginNameList, tag, object);
//    }
//
//    @Override
//    public void sendUniversalNotificationToUserByLoginName(String loginName, String tag, Object object) {
//        List<String> loginNameList = new ArrayList<>(1);
//        loginNameList.add(loginName);
//        sendUniversalNotificationToUserByLoginNameList(loginNameList, tag, object);
//    }
//
//    /**
//     * 发送通用通知接口（无需处理消息）到ERP消息接口
//     *
//     * @param loginNameList 登录名列表
//     * @param tag           标签 @see com.bytter.facade.sys.constant.NotificationConstant
//     * @param object        模版解析对象
//     */
//    @Override
//    public void sendUniversalNotificationToUserByLoginNameList(List<String> loginNameList, String tag, Object object) {
//        if (loginNameList == null || loginNameList.size() == 0) {
//            return;
//        }
//        NotificationCategory notificationCategory = notificationCategoryService.findByTag(tag);
//        if (notificationCategory == null) {
//            return;
//        }
//        // 根据流程分类的是否是移动端审批来决定是否推送消息
//        if (!whetherPushMessage(notificationCategory)) {
//            return;
//        }
//        // 获取并解析流程的模板消息
//        String messageContent = getNotificationMessageTemplateContent(notificationCategory, object);
//        // 推送消息
//        pushUniversalNotification(loginNameList, messageContent);
//    }
    /**
     * 推送通用通知接口
     *
     * @param loginNameList 消息接收者登录名列表
     * @param content       通知内容
     */
    private void pushUniversalNotification(List<String> loginNameList, String content) {
        if (loginNameList == null || loginNameList.size() == 0) {
            logger.error("消息接收者集合为空，无法发送消息到ERP消息接口！");
            return;
        }
        if (StringUtils.isEmpty(content)) {
            logger.error("消息内容为空，无法发送消息到ERP消息接口！");
            return;
        }
        try {
            String notificationJsonString = getNotificationRequestJsonString(loginNameList, content, UNIVERSAL_NOTIFICATION, null);
            // 组装Webservice接口请求报文
            String requestData = String.format(ERP_NOTIFICATION_SERVICE_REQUEST_XML, notificationJsonString);
            logger.info("推送通用通知（无需处理消息）到ERP消息接口。消息内容:{}", content);
            // 发送请求到Webservice接口
            String soapResponse = callWebserviceUsingHttpClient(erpAcceptNotificationUrl, requestData);
            // 解析处理结果
            handleErpResponseData(soapResponse);
        } catch (Exception e) {
            logger.error(String.format("推送通用通知（无需处理消息）到ERP消息接口发生错误！错误信息：%s", e.getMessage()), e);
        }
    }
    /***************************  通用通知接口（无需处理消息） end *******************************/
    /***************************  通用链接通知接口（带跳转链接，需处理业务) start *******************************/
    /**
     * 通用链接通知（带跳转链接，需处理业务)
     *
     * @param userId              通知接收者ID
     * @param notificationContent 通知内容
     * @param url                 跳转链接
     */
    @Override
    public void sendUniversalLinkNotificationToUserByUserId(String userId, String notificationContent, String url) {
        List<String> userIdList = new ArrayList<>(1);
        userIdList.add(userId);
        sendUniversalLinkNotificationToUserByUserIdList(userIdList, notificationContent, url);
    }
    public void sendUniversalLinkNotificationToUserByUserIdList(List<String> userIdList, String notificationContent, String url) {
        List<String> loginNameList = userService.listLoginNameByUserIds(userIdList);
        if (loginNameList == null || loginNameList.size() == 0) {
            return;
        }
        pushUniversalLinkNotification(loginNameList, notificationContent, url);
    }
    /**
     * @param processDefinitionKey 流程分类KEY
     * @param businessKey          业务ID
     * @param object               模版解析对象
     */
    @Override
    public void sendTodoApprovalLinkNotificationToApprover(String processDefinitionKey, String businessKey, Object object) {
        try {
 /*           FlowCategoryDTO flowCategoryDTO = flowCategoryService.findDtoByKey(processDefinitionKey);
            if (flowCategoryDTO == null) {
                logger.info("根据processDefinitionKey[{}]找不到对应的流程分类，无法发送待审批工作流的通知到ERP消息接口", processDefinitionKey);
                return;
            }
            // 非移动端审批不推送
            if (flowCategoryDTO.getMobileApprovalFlag() != null &&
                    FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
                logger.info("流程分类[{}]不是移动端审批，因此不推送通知（带跳转链接，需处理业务）", flowCategoryDTO.getName());
                return;
            }
            if (StringUtils.isEmpty(flowCategoryDTO.getNotificationId())) {
                logger.info("流程分类[{}]未挂接消息通知，无法发送待审批工作流的通知到ERP消息接口", flowCategoryDTO.getName());
                return;
            }*/
            NotificationCategory notificationCategory = notificationCategoryService.get(null, true);
            if (notificationCategory == null) {
                logger.info("根据ID[{}]找不到对应的流程分类通知（未挂接消息通知），无法发送待审批工作流的通知到ERP消息接口", " flowCategoryDTO.getNotificationId()");
                return;
            }
            //sendTodoApprovalLinkNotificationToApprover(processDefinitionKey, businessKey, notificationCategory, object);
        } catch (Exception e) {
            logger.error(String.format("推送消息到ERP消息接口（带跳转链接，需处理业务）发生错误！错误信息：%s", e.getMessage()), e);
        }
    }
    /**
     * 推送通知（带跳转链接，需处理业务），不能用异步。这过程需要查询审批数据，得同步执行
     *
     * @param processDefinitionKey 流程分类KEY
     * @param businessKey          业务ID
     * @param notificationCategory 通知分类
     * @param object               模板参数
     * @throws Exception 异常
    private void sendTodoApprovalLinkNotificationToApprover(String processDefinitionKey,
    String businessKey,
    NotificationCategory notificationCategory,
    Object object) throws Exception {
    // 查询该审批的任务列表，给对应审批人发送待审批通知
    List<Task> taskList = getTaskList(processDefinitionKey, businessKey);
    if (taskList == null || taskList.size() == 0) {
    logger.info("根据processDefinitionKey[{}]、businessKey[{}]找不到对应的审批流程数据，无法发送待审批工作流的通知到ERP消息接口", processDefinitionKey, businessKey);
    return;
    }
    for (Task task : taskList) {
    // 获取审批人ID
    String userId = task.getAssignee();
    User user = userService.get(userId, true);
    if (user == null) {
    continue;
    }
    // 生成H5待审批页面的URL
    String h5TodoApprovalUrl = getH5TodoApprovalUrl(task.getId());
    // 获取并解析流程的模板消息
    String messageContent = getNotificationMessageTemplateContent(notificationCategory, object);
    List<String> loginNameList = new ArrayList<>(1);
    loginNameList.add(user.getLoginName());
    pushUniversalLinkNotification(loginNameList, messageContent, h5TodoApprovalUrl);
    }
    }
    /**
     * 根据业务ID-businessKey，流程分类KEY-processDefinitionKey来查询Task，获取task id，生成进入H5审批页面的URL
     *
     * @param processDefinitionKey 流程分类KEY-processDefinitionKey
     * @param businessKey          业务ID-businessKey
     * @return 审批task
    private List<Task> getTaskList(String processDefinitionKey, String businessKey) {
    TaskQuery taskQuery = taskService.createTaskQuery();
    return taskQuery.active()
    .processInstanceBusinessKey(businessKey)
    .processDefinitionKey(processDefinitionKey)
    .list();
    }
     */
    /**
     * 通用链接通知接口（带跳转链接，需处理业务)
     *
     * @param loginNameList 消息接收者登录名列表
     * @param content       通知内容
     * @param respUrl       响应地址
     */
    private void pushUniversalLinkNotification(List<String> loginNameList, String content, String respUrl) {
        if (loginNameList == null || loginNameList.size() == 0) {
            logger.error("消息接收者集合为空，无法通用通知到ERP消息接口！");
            return;
        }
        if (StringUtils.isEmpty(content)) {
            logger.error("消息内容为空，无法发送通知到ERP消息接口！");
            return;
        }
        try {
            String notificationJsonString = getNotificationRequestJsonString(loginNameList, content, UNIVERSAL_LINK_NOTIFICATION, respUrl);
            // 组装Webservice接口请求报文
            String requestData = String.format(ERP_NOTIFICATION_SERVICE_REQUEST_XML, notificationJsonString);
            logger.info("推送通用链接通知（带跳转链接，需处理业务)到ERP消息接口。消息内容：{}，跳转地址：{}", content, respUrl);
            // 发送请求到Webservice接口
            String soapResponse = callWebserviceUsingHttpClient(erpAcceptNotificationUrl, requestData);
            // 解析处理结果
            handleErpResponseData(soapResponse);
        } catch (Exception e) {
            logger.error(String.format("推送通用链接通知（带跳转链接，需处理业务）到ERP消息接口发生错误！错误信息：%s", e.getMessage()), e);
        }
    }
    /**
     * 解析处理结果
     *
     * @param soapResponse Webservice接口响应内容
     * @throws Exception 异常
     */
    private void handleErpResponseData(String soapResponse) throws Exception {
        // 将SOAP报文转化为map
        Map<String, String> map = parseSoap(soapResponse);
        String jsonResponseData = map.get("return");
        if (StringUtils.isEmpty(jsonResponseData)) {
            logger.error("ERP消息接口无响应！无法发送消息到ERP消息接口！");
            return;
        }
        JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonResponseData);
        if (jsonNode.get("STATUS") != null && "1".equals(jsonNode.get("STATUS").asText())) {
            logger.info("推送消息到ERP消息接口成功！接口返回数据：{}", jsonNode.toString());
        } else {
            String msg = jsonNode.get("MESSAGE") != null ? jsonNode.get("MESSAGE").asText() : "";
            logger.error(String.format("推送通知到ERP消息接口失败！描述消息：%s", msg));
        }
    }
    /***************************  通用链接通知接口（带跳转链接，需处理业务) end *******************************/
    /**
     * 获取并解析流程的模板消息内容。
     *
     * @param notificationCategory 消息分类
     * @param object               参数
     * @return 消息内容
     */
    private String getNotificationMessageTemplateContent(NotificationCategory notificationCategory, Object object) {
        String messageContent = null;
        if (notificationCategory == null) {
            return null;
        }
        String logTag = "notification.messageTemplate." + notificationCategory.getId();
        List<I18nDTO> i18nDTOList = i18nServiceI.listBusinessBaseByKeyId(notificationCategory.getId() + "CategoryTpl");
        if (!CollectionUtils.isEmpty(i18nDTOList)) {
            for (I18nDTO i18nDTO : i18nDTOList) {
                messageContent = getTemplateParams(object, logTag, i18nDTO.getContext());
            }
        } else {
            messageContent = getTemplateParams(object, logTag, notificationCategory.getContent());
        }
        return messageContent;
    }
    /**
     * 解析流程的模板消息。
     *
     * @param params
     * @param logTag
     * @param message
     * @return
     */
    private String getTemplateParams(Object params, String logTag, String message) {
        if (params instanceof Map) {
            return VelocityUtils.evaluate((Map<String, Object>) params, logTag, message);
        }
        if (params != null) {
            Map<String, Object> map = BeanUtils.beanToMap(params);
            return VelocityUtils.evaluate(map, logTag, message);
        } else {
            Map<String, Object> map = new HashMap<>();
            return VelocityUtils.evaluate(map, logTag, message);
        }
    }
    /**
     * 根据流程分类的是否是移动端审批来决定是否推送消息
     *
     * @param notificationCategory 消息通知分类
     * @return 返回true表示可以推送消息；返回false表示不可以推送消息
     */
    private boolean whetherPushMessage(NotificationCategory notificationCategory) {
        boolean result = true;
 /*       FlowCategoryDTO flowCategoryDTO = flowCategoryService.findByNotificationId(notificationCategory.getId());
        if (flowCategoryDTO != null) {
            if (flowCategoryDTO.getMobileApprovalFlag() != null
                    && FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL == flowCategoryDTO.getMobileApprovalFlag()) {
                result = false;
            }
        } else {
            result = false;
        }*/
        return result;
    }
    /**
     * 获取提交到ERP消息接口的JSON字符串
     *
     * @param loginNameList 接收者登录名列表
     * @param content       消息内容
     * @param type          消息类型：0，普通文本消息；1，带链接消息；
     * @param url           业务处理链接。当type为1时，url才会有数据。
     * @return JSON字符串
     */
    private String getNotificationRequestJsonString(List<String> loginNameList, String content, String type, String url) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userList", loginNameList);
        jsonObject.put("content", content);
        jsonObject.put("type", type);
        if (UNIVERSAL_LINK_NOTIFICATION.equals(type) && StringUtils.isNotBlank(url)) {
            jsonObject.put("url", url);
        }
        return jsonObject.toString();
    }
    /**
     * 生成进入H5审批页面的URL
     *
     * @param approveId 审批ID
     * @return 进入H5审批详情页面的控制器层URL
     * @throws Exception
     */
    private String getH5TodoApprovalUrl(String approveId) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(thirdProperties.getTrustedHost() + "/mobile/h5TodoApproveDetailPage");
        url.append("?taskId=" + approveId);
        return url.toString();
    }
    /**
     * Webservice连接超时时间
     */
    private static final int CLIENT_CONNECT_TIMEOUT = 10 * 1000;
    /**
     * Http Client 调用Webservice接口
     *
     * @param wsdl        Webservice接口
     * @param requestData 请求数据
     * @return 结果
     */
    private String callWebserviceUsingHttpClient(String wsdl, String requestData) throws Exception {
        String soapResponse = null;
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        try {
            HttpPost httpPost = new HttpPost(wsdl);
            // 添加HTTP请求头
            // httpPost.setHeader("ticket", ticket);
            // 设置连接参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CLIENT_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CLIENT_CONNECT_TIMEOUT)
                    .setSocketTimeout(CLIENT_CONNECT_TIMEOUT)
                    .build();
            httpPost.setConfig(requestConfig);
            // 请求正文
            StringEntity stringEntity = new StringEntity(requestData, StandardCharsets.UTF_8.displayName());
            // stringEntity.setContentType("text/xml; charset=utf-8");
            // 解决SOAP Message namespace URI错误
            stringEntity.setContentType("application/soap+xml;charset=utf-8");
            httpPost.setEntity(stringEntity);
            // 发送请求
            CloseableHttpResponse response = client.execute(httpPost);
            int responseStatusCode = response.getStatusLine().getStatusCode();
            if (responseStatusCode == HttpStatus.SC_OK) {
                logger.info("调用ERP接收消息Webservice接口成功！");
            } else if (responseStatusCode == HttpStatus.SC_NOT_FOUND) {
                logger.error("ERP消息Webservice接口未能正确处理请求，HTTP响应状态码为：" + responseStatusCode);
            } else {
                logger.error("ERP消息Webservice接口未能正确处理请求，HTTP响应状态码为：" + responseStatusCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                soapResponse = EntityUtils.toString(entity, StandardCharsets.UTF_8.displayName());
            }
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
            logger.error("调用ERP消息Webservice接口出错：" + e.getMessage());
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return soapResponse;
    }
    /**
     * 将SOAP响应报文转化成Map
     *
     * @param soap SOAP响应报文
     * @return Map
     * @throws DocumentException
     */
    private Map<String, String> parseSoap(String soap) throws DocumentException {
        //报文转成doc对象
        Document doc = DocumentHelper.parseText(soap);
        //获取根元素，准备递归解析这个XML树
        Element root = doc.getRootElement();
        Map<String, String> resultMap = new HashMap<>();
        getCode(root, resultMap);
        return resultMap;
    }
    private void getCode(Element root, Map<String, String> resultMap) {
        if (root.elements() != null) {
            //如果当前跟节点有子节点，找到子节点
            List<Element> list = root.elements();
            for (Element e : list) {
                //遍历每个节点
                if (e.elements().size() > 0) {
                    //当前节点不为空的话，递归遍历子节点；
                    getCode(e, resultMap);
                }
                if (e.elements().size() == 0) {
                    //如果为叶子节点，那么直接把名字和值放入map
                    resultMap.put(e.getName(), e.getTextTrim());
                }
            }
        }
    }
}
