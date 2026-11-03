package com.bytter.third.service;

import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.OkHttpUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.third.dto.CorpWeiXinAccessToken;
import com.bytter.third.enums.ThirdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 企业微信Service
 *
 * @author chenlw
 * @date 2020/03/02
 */
@Service("corpWxClient")
public class CorpWxClient extends ThirdAuthClient{
    private static final Logger logger = LoggerFactory.getLogger(CorpWxClient.class);

    /**
     * 获取access_token的接口地址（GET）
     */
    private final static String ACCESS_TOKEN= "/cgi-bin/gettoken";

    private final static String USER_INFO = "/cgi-bin/user/getuserinfo";

    private final static int QW_TOKEN_RESULT_SUCCESS = 0;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ThirdProperties thirdProperties;

    /**
     * 微信企业id
     */
    @Value("${weiXin.corp.corpid}")
    private String weiXinCorpId;

    @Value("${weiXin.corp.corpsecret}")
    private String weiXinCorpSecret;


    @Override
    public ThirdProperties getThirdProperties() {
        return thirdProperties;
    }

    /**
     * 获取企业微信的accessToken
     * <p>
     * 参考文档：https://work.weixin.qq.com/api/doc/90000/90135/91039
     * <p>
     * access_token的有效期通过返回的expires_in来传达，正常情况下为7200秒（2小时），有效期内重复获取返回相同结果，过期后获取会返回新的access_token。
     *
     * @return accessToken
     */
    @Override
    public String getAppAccessToken() {
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.THIRD_LOGIN_TOKEN, getType().name());
        CorpWeiXinAccessToken accessToken = (CorpWeiXinAccessToken) redisTemplate.opsForValue().get(key);
        if (accessToken != null) {
            return accessToken.getAccessToken();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("corpid", weiXinCorpId);
        params.put("corpsecret", weiXinCorpSecret);
        String resp = OkHttpUtils.get(formatURI(ACCESS_TOKEN), params);
        logger.info("获取token结果：：：{}", resp);
        AssertHelper.isTrueBusEx(StringUtils.isNoneBlank(resp), "获取企业微信AccessToken失败!");
        CorpWeiXinAccessToken corpWeiXinAccessToken = JsonUtils.parseObject(resp, CorpWeiXinAccessToken.class);
        if(QW_TOKEN_RESULT_SUCCESS==corpWeiXinAccessToken.getErrCode()){
            redisTemplate.opsForValue().set(key, corpWeiXinAccessToken, corpWeiXinAccessToken.getExpiresIn() - TOKEN_OFFSET, TimeUnit.SECONDS);
            return corpWeiXinAccessToken.getAccessToken();
        }
        return null;
    }

    @Override
    public String getUserInfo(String code) {
        String accessToken = this.getAppAccessToken();
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("access_token", accessToken);
        requestParams.put("code", code);
        String response = OkHttpUtils.get(formatURI(USER_INFO), requestParams);
        logger.info("获取访问用户身份-响应内容:::{}", response);
        AssertHelper.isTrueBusEx(StringUtils.isNoneBlank(response), "获取访问用户身份失败！");
        return response;
    }

    @Override
    public ThirdType getType() {
        return ThirdType.CP_WX;
    }

    public String getUserId(String code) throws Exception {
        String userInfo = getUserInfo(code);
        if (StringUtils.isEmpty(userInfo)) {
            logger.error("获取访问用户身份失败！");
            throw new Exception("获取访问用户身份失败！");
        }
        Map<String, Object> userMap = JsonUtils.json2Map(userInfo);
        String errcode = userMap.get("errcode").toString();
        if (!errcode.equals("0")) {
            String msg = "获取访问用户身份失败！错误代码：" + errcode;
            logger.error(msg);
            throw new Exception(msg);
        }
        return userMap.get("UserId").toString();
    }

}
