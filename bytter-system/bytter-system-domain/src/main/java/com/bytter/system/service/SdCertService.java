package com.bytter.system.service;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.OkHttpUtils;
@Transactional
@Service("sdCertService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SdCertService {
    @Value("${sd.url}")
    private String baseUrl;
    /**
     * 山地证书验签
     *
     * @return
     * @throws Exception
     */
    public Boolean check(String serialNumber, String source, String signUserData, String userCode) {
        Boolean msg = false;
        String url = baseUrl + "/usb/check";
        Map<String, Object> param = new HashMap<>();
        param.put("serialNumber", serialNumber);
        param.put("userPlain", source);
        param.put("signUserData", signUserData);
        param.put("userCode", userCode);
        try {
            String result = OkHttpUtils.postJson(url, param);
            Map<String, String> map = JsonUtils.json2Map(result);
            String state = map.get("state");
            if ("success".equals(state)) {
                msg = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = false;
        }
        return msg;
    }
}
