package com.bytter.system.service;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.IDingTalkPropertiesService;
import com.bytter.system.constants.AppH5Constants;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import java.util.Map;
/**
 * @author luxy
 * @Title: H5DingTalkService
 * @ProjectName T3.4
 * @Description: 钉钉登录实现层
 * @date 2020/8/28 16:22
 */
@Service("h5DingTalkService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class H5DingTalkService {
    @Autowired
    private IDingTalkPropertiesService dingTalkPropertiesService;
    /**
     * 获取accessToken
     *
     * @return
     * @throws ApiException
     */
    public String getAccessToken() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(AppH5Constants.AccessURL.DINGTALK_URL + "/gettoken");
        OapiGettokenRequest req = new OapiGettokenRequest();
        req.setAppkey(dingTalkPropertiesService.getAppKey());
        req.setAppsecret(dingTalkPropertiesService.getAppSecret());
        req.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(req);
        String body = response.getBody();
        Map<String, Object> jo = JsonUtils.json2Map(body);
        String errcode = jo.get("errcode").toString();
        String access_token = null;
        if ("0".equals(errcode)) {
            access_token = jo.get("access_token").toString();
        }
        return access_token;
    }
    /**
     * 获取钉钉用户信息
     *
     * @param requestAuthCode
     * @param accessToken
     * @return
     * @throws ApiException
     */
    public String getUserInfo(String requestAuthCode, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(AppH5Constants.AccessURL.DINGTALK_URL + "/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setHttpMethod("GET");
        request.setCode(requestAuthCode);
        OapiUserGetuserinfoResponse response = client.execute(request, accessToken);
        return response.getBody();
    }
}
