package com.bytter.system.service;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.IFeishuPropertiesService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
/**
 * @author liyh
 * @Title: H5FeishuService
 * @Description: 飞书登录实现层
 * @date 2022/10/26 17:10
 */
@Service("h5FeishuService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class H5FeishuService {
    @Autowired
    private IFeishuPropertiesService feishuPropertiesService;
    /**
     * 获取accessToken
     *
     * @return
     * @throws Exception
     */
    public String getAccessToken(String code) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        String content = "{\"code\":\"" + code + "\",\"grant_type\":\"authorization_code\"}";
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url("https://open.feishu.cn/open-apis/authen/v1/access_token")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + getAppAccessToken())
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();
        Response response = client.newCall(request).execute();
        Map<String, Object> jo = JsonUtils.json2Map(response.body().string());
        String access_token = null;
        if (jo.get("msg").equals("success")) {
            if (null != jo.get("data")) {
                Map<String, Object> data = (LinkedHashMap) jo.get("data");
                if (null != data.get("access_token")) {
                    access_token = (String) data.get("access_token");
                }
            }
        }
        return access_token;
    }
    /**
     * 获取accessToken
     *
     * @return
     * @throws Exception
     */
    public String getAppAccessToken() throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        String content = "{\"app_id\":\"" + feishuPropertiesService.getAgentId()
                + "\",\"app_secret\":\"" + feishuPropertiesService.getAppSecret() + "\"}";
        RequestBody rbody = RequestBody.create(mediaType, content);
        /*RequestBody rbody = RequestBody.create(mediaType, "{\"app_id\":" + feishuPropertiesService.getAgentId()
                + ",\"app_secret\":\"" + feishuPropertiesService.getAppSecret() + "\"}");*/
        Request request = new Request.Builder()
                .url("https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal")
                .method("POST", rbody)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        Map<String, Object> jo = JsonUtils.json2Map(body.string());
        String access_token = null;
        if (jo.get("msg").equals("ok")) {
            access_token = (String) jo.get("app_access_token");
        }
        return access_token;
    }
    /**
     * 获取飞书用户信息
     *
     * @param requestAuthCode
     * @param accessToken
     * @return
     * @throws Exception
     */
    public String getUserInfo(String requestAuthCode, String accessToken) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://open.feishu.cn/open-apis/authen/v1/user_info")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        return body.string();
    }
    /**
     * 发送飞书用户信息
     *
     * @param content      消息内容
     * @param feishuUserId 飞书用户ID
     * @return
     * @throws Exception
     */
    public String sendMessage(String content, String feishuUserId) throws Exception {
        String accessToken = getAppAccessToken();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        String contentText = "{\"content\":\"{\\\"text\\\":\\\""
                + content + "\\\"}\",\"msg_type\":\"text\",\"receive_id\":\""
                + feishuUserId + "\",\"uuid\":\"" + UUID.randomUUID() + "\"}";
        RequestBody body = RequestBody.create(mediaType, contentText);
        Request request = new Request.Builder()
                .url("https://open.feishu.cn/open-apis/im/v1/messages" + "?receive_id_type=user_id")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody rbody = response.body();
        Map<String, Object> jo = JsonUtils.json2Map(rbody.string());
        String message_id = null;
        if (jo.get("msg").equals("ok")) {
            Map<String, Object> data = (LinkedHashMap) jo.get("data");
            if (null != data.get("message_id")) {
                message_id = (String) data.get("message_id");
            }
        }
        return message_id;
    }
}
