package com.bytter.system.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.bytter.core.base.AbstractService;
import com.bytter.core.enums.AppPageEnum;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service("pushService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PushService extends AbstractService {
    private static final Logger logger = LoggerFactory.getLogger(PushService.class);
    @Value("${push.switch}")
    private boolean pushSwitch;
    @Value("${push.accessKeyId}")
    private String accessKeyId;
    @Value("${push.accessKeySecret}")
    private String accessKeySecret;
    @Value("${push.customerPrefix}")
    private String customerPrefix;
    @Value("${push.iOSApnsEnv}")
    private String iOSApnsEnv;
    private static String regionId = "cn-hangzhou";
    @Value("${push.androidAppKey}")
    private Long androidAppKey;
    @Value("${push.iosAppKey}")
    private Long iosAppKey;
    @Value("${proxy.switch}")
    private boolean proxySwitch;
    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private String proxyPort;
    protected static DefaultAcsClient client;
    @Autowired
    private ObjectMapper objectMapper;
    /**
     * 缓存发送队列
     */
    public static BlockingQueue<Map<String, String>> queue = new ArrayBlockingQueue<>(5000);
    /**
     * 发送线程状态
     */
    private static boolean isRunning = false;
    /**
     * 超时时间间隔
     */
    private static long TIMEOUT_INTERVAL = 2 * 60 * 1000;
    /**
     * 移动推送消费线程池（TTL 包装，上下文感知）。
     */
    private static final ExecutorService PUSH_EXECUTOR = SmartThreadPoolManager.wrap(Executors.newSingleThreadExecutor());
    public String getCustomerPrefix() {
        return customerPrefix;
    }
    public boolean isPushSwitch() {
        return pushSwitch;
    }
    private DefaultAcsClient getClient() {
        if (client == null) {
            DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            client = new DefaultAcsClient(profile);
        }
        return client;
    }
    private void pushNoticeToAndroid(String account, String title, String body, String extParameters) throws ClientException {
        PushRequest pushRequest = new PushRequest();
        pushRequest.setConnectTimeout(3000);
//        pushRequest.setProtocol(ProtocolType.HTTPS);
//        pushRequest.setMethod(MethodType.POST);
        // 推送目标
        pushRequest.setAppKey(androidAppKey);
        pushRequest.setTarget("ACCOUNT"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALIAS: 按别名推送; ALL: 全推
        pushRequest.setTargetValue(account); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setDeviceType("ANDROID"); // 设备类型deviceType, iOS设备: "iOS"; Android设备: "ANDROID"; 全部: "ALL", 这是默认值.
        // 推送配置
        pushRequest.setPushType("NOTICE"); // MESSAGE:表示消息(默认), NOTICE:表示通知
        pushRequest.setTitle(title); // 消息的标题
        pushRequest.setBody(body); // 消息的内容
        // 推送配置: Android
        pushRequest.setAndroidOpenType("APPLICATION"); // 点击通知后动作 'APPLICATION': 打开应用 'ACTIVITY': 打开应用AndroidActivity 'URL': 打开URL 'NONE': 无跳转
        pushRequest.setAndroidNotifyType("BOTH"); // 通知的提醒方式 ‘VIBRATE': 振动  'SOUND': 声音 'BOTH': 声音和振动 'NONE': 不做处理，用户自定义
        //配置Android8 的信道
        pushRequest.setAndroidNotificationChannel("Bytter_channel");
        if (StringUtils.isNoneBlank(extParameters)) {
            pushRequest.setAndroidExtParameters(extParameters);
        }
        try {
            getClient().getAcsResponse(pushRequest);
        } catch (ClientException e) {
            logger.error("push failed: " + e.getMessage());
        }
    }
    private void pushNoticeToIOS(String account, String title, String body, String extParameters) throws ClientException {
        PushRequest pushRequest = new PushRequest();
//        pushRequest.setProtocol(ProtocolType.HTTPS);
//        pushRequest.setMethod(MethodType.POST);
        pushRequest.setConnectTimeout(3000);
        // 推送目标
        pushRequest.setAppKey(iosAppKey);
        pushRequest.setTarget("ACCOUNT"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALIAS: 按别名推送; ALL: 全推
        pushRequest.setTargetValue(account); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setDeviceType("iOS"); // 设备类型deviceType, iOS设备: "iOS"; Android设备: "ANDROID"; 全部: "ALL", 这是默认值.
        // 推送配置
        pushRequest.setPushType("NOTICE"); // MESSAGE:表示消息(默认), NOTICE:表示通知
        pushRequest.setTitle(title); // 消息的标题
        pushRequest.setBody(body); // 消息的内容
//        pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
        // 推送配置: iOS
        pushRequest.setIOSBadgeAutoIncrement(true);
        pushRequest.setIOSMusic("default"); // iOS通知声音
//        pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
        pushRequest.setIOSApnsEnv(iOSApnsEnv);//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
        pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
        if (StringUtils.isNoneBlank(extParameters)) {
            pushRequest.setIOSExtParameters(extParameters); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
        }
        try {
            getClient().getAcsResponse(pushRequest);
        } catch (ClientException e) {
            logger.error("push failed: " + e.getMessage());
        }
    }
    public void pushNotice(List<String> accounts, String title, String body, AppPageEnum appPageEnum, Object extParameters)
            throws Exception {
        pushNotice(accounts, title, body, appPageEnum, extParameters, false);
    }
    public void pushNotice(List<String> accounts, String title, String body, AppPageEnum appPageEnum, Object extParameters,
                           boolean isThrowException) throws Exception {
        if (!pushSwitch || (accounts == null || accounts.isEmpty())) {
            return;
        }
        String extParametersStr = null;
        Map<String, Object> map = new HashMap<>();
        map.put("action", appPageEnum.getPage());
        if (extParameters != null) {
            map.put("data", extParameters);
        }
        try {
            extParametersStr = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        }
        StringBuffer accountValue = new StringBuffer();
        List<String> batchAccountList = new ArrayList<>();
        //为了避免每个账号调用一次接口，所以组装账号信息，阿里只允许每次推送的账号最大数量为100
        for (int i = 0; i < accounts.size(); i++) {
            accountValue.append(customerPrefix).append("_").append(accounts.get(i)).append(",");
            if ((i != 0 && i % 100 == 0) || i == accounts.size() - 1) {
                batchAccountList.add(accountValue.substring(0, accountValue.length() - 1));
                accountValue = new StringBuffer();
            }
        }
        for (String account : batchAccountList) {
            Map<String, String> messageVO = new HashMap<>();
            messageVO.put("account", account);
            messageVO.put("title", title);
            messageVO.put("body", body);
            messageVO.put("extParametersStr", extParametersStr);
            try {
                sendQueue(messageVO, isThrowException);
            } catch (Exception e) {
                if (isThrowException) {
                    throw e;
                } else {
                    logger.error("添加消息移动队列出错", e);
                }
            }
        }
    }
    private void sendQueue(Map<String, String> message, boolean isThrowException) throws Exception {
        try {
            queue.offer(message);
            if (!isRunning) {
                startup();
                isRunning = true;
            }
        } catch (Exception e) {
            if (isThrowException) {
                throw e;
            } else {
                logger.error("添加消息移动队列出错", e);
            }
        }
    }
    private void startup() {
        if (isRunning) {
            return;
        }
        PUSH_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                logger.debug("移动发送消息线程启动");
                long timestamp = System.currentTimeMillis();
                while (isRunning) {
                    long timeout = timestamp + TIMEOUT_INTERVAL;
                    if (System.currentTimeMillis() > timeout) {
                        logger.debug("消息队列为空，移动推送线程结束");
                        //空跑一段时间(3分钟)后线程退出
                        break;
                    }
                    try {
                        if (queue.size() == 0) {
                            Thread.sleep(1000);
                            continue;
                        }
                        timestamp = System.currentTimeMillis();
                        Map<String, String> messageVO = queue.poll();
                        setProxy();
                        String account = messageVO.get("account");
                        String title = messageVO.get("title");
                        String body = messageVO.get("body");
                        String extParametersStr = messageVO.get("extParametersStr");
                        //批量分别向android和ios发送消息
                        pushNoticeToIOS(account, title, body, extParametersStr);
                        pushNoticeToAndroid(account, title, body, extParametersStr);
                        clearProxy();
                        //发送一个消息休息，防止发送过快
                        Thread.sleep(100);
                    } catch (Exception e) {
                        logger.error("移动推送线程出错", e);
                    }
                }
                isRunning = false;
                logger.debug("移动发送线程停止。");
            }
        });
    }
    /**
     * 设置代理
     */
    private void setProxy() {
        if (proxySwitch) {
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort);
        }
    }
    /**
     * 取消代理
     */
    private void clearProxy() {
        if (proxySwitch) {
            System.clearProperty("https.proxyHost");
            System.clearProperty("https.proxyPort");
        }
    }
}
