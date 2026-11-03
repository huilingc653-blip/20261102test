package com.bytter.cloud.auth.authentication.service.apiimpl;

import cn.hutool.core.util.StrUtil;
import com.bytter.auth.api.ApiIAuthenticationService;
import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import com.bytter.cloud.common.core.persistence.vo.Result;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.IpUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.ApiResourceDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.SocketMsg;
import com.bytter.system.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.http.server.PathContainer;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.bytter.core.cache.redis.core.RedisCacheKey.URL_PERMISSION_MAPPING_ALL;

/**
 * 鉴权服务实现类
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@DubboService
@Service
@Slf4j
public class ApiAuthenticationServiceImpl implements ApiIAuthenticationService, DictInjectProvider {

    /**
     * 未在资源库中的URL默认标识
     */
    public static final String NONEXISTENT_URL = "NONEXISTENT_URL";

    private static final String USER_NAME_FORMAT = "%s@BT@%s";


    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    
    @Autowired
    private TokenStore tokenStore;
    
    @DubboReference(check = false)
    private IUserService userService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * url对应资源与用户拥有资源进行匹配
     *
     * @param urlConfigAttribute 树形配置
     * @param userResources      资源
     * @return 是否存在资源
     */
    public boolean isMatch(ConfigAttribute urlConfigAttribute, Set<String> userResources) {
        return userResources.stream()
                .anyMatch(resource -> resource.equals(urlConfigAttribute.getAttribute()));
    }
    
    /**
     * 租户退出
     *
     * @param tenantId 租户ID
     * @param userName 用户账号
     * @return 租户退出结果
     */
    @Override
    public boolean tenantLogout(String tenantId, String userName) {
        Result<List<UserDTO>> result = this.userService.getUsersByTenantId(tenantId, userName);
        if (result.isFail()) {
            return false;
        }
        List<UserDTO> userList = result.getData();
        if (CollectionUtils.isEmpty(userList)) {
            return false;
        }
        String token = ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue();
        OAuth2Authentication accessToken = this.tokenStore.readAuthentication(token);
        String clientId = accessToken.getOAuth2Request().getClientId();
        Collection<OAuth2AccessToken> tokenCollection = this.tokenStore.findTokensByClientId(clientId);
        if (tokenCollection == null || tokenCollection.isEmpty()) {
            return false;
        }
        Map<String, UserDTO> userMap = new HashMap<>(userList.size());
        String userNameFormat = "%s@BT@%s";
        for (UserDTO user : userList) {
            userMap.put(String.format(userNameFormat, /*user.getTenantId()*/"", user.getUsername()), user);
        }
        Iterator<OAuth2AccessToken> tokenIterator = tokenCollection.iterator();
        String tenantUserName;
        OAuth2AccessToken removeToken;
        AuthUser userDetail;
        OAuth2Authentication authentication;
        while (tokenIterator.hasNext()) {
            removeToken = tokenIterator.next();
            authentication = this.tokenStore.readAuthentication(removeToken.getValue());
            //当用户token已经过期 获取用户信息异常处理
            if (Objects.isNull(authentication)) {
                continue;
            }
            userDetail = (AuthUser) authentication.getPrincipal();
            tenantUserName = String.format(userNameFormat, userDetail.getTenantId(), userDetail.getUsername());
            if (userMap.containsKey(tenantUserName)) {
                this.consumerTokenServices.revokeToken(removeToken.getValue());
            }
        }
        return true;
    }
    
    @Override
    public boolean tenantLogouts(List<String> tenantIds) {
        if (CollectionUtils.isEmpty(tenantIds)) {
            return false;
        }
        for (String tenantId: tenantIds) {
            if (!tenantLogout(tenantId, null)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * 租户退出
     *
     * @param tenantId
     * @param clientIds
     * @return
     */
    @Override
    public boolean tenantLogouts(String tenantId, List<String> clientIds) {
        // 标记是否成功吊销至少一个令牌（默认false）
        boolean isLogoutSuccess = false;

        // 校验入参
        if (StringUtils.isEmpty(tenantId)) {
            log.warn("租户ID为空，无需吊销令牌");
            return false;
        }
        // 获取租户用户账号
        Set<String> tenantUserNames = getTenantUserNames(tenantId);
        if (!EmptyUtils.isNotEmpty(tenantUserNames)) {
            log.info("租户{}下无有效用户，无需处理", tenantId);
            return false;
        }
        // 获取客户端的所有有效令牌
        Collection<OAuth2AccessToken> allValidToken = getClientTokens(clientIds);
        if (CollectionUtils.isEmpty(allValidToken)) {
            log.info("客户端下无有效令牌，无需处理");
            return false;
        }

        // 遍历令牌，匹配并吊销目标用户令牌
        int revokedCount = 0; // 统计吊销的令牌数量
        int revokedSuccCount = 0;
        int revokedFailCount = 0;
        for (OAuth2AccessToken accessToken : allValidToken) {
            try {
                // 读取令牌对应的认证信息
                String tokenValue = accessToken.getValue();
                OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
                if (Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal())) {
                    log.warn("令牌{}的认证信息为空，跳过处理", tokenValue);
                    continue;
                }

                // 提取用户信息并拼接匹配字符串
                AuthUser authUser = (AuthUser) authentication.getPrincipal();
                String tokenUserKey = String.format(USER_NAME_FORMAT, authUser.getTenantId(), authUser.getUsername());

                // 匹配租户用户，吊销令牌
                if (tenantUserNames.contains(tokenUserKey)) {
                    revokedCount++;
                    this.consumerTokenServices.revokeToken(tokenValue);
                    isLogoutSuccess = true;
                    log.info("成功吊销租户{}下用户{}的令牌：{}", tenantId, authUser.getUsername(), tokenValue);
                    revokedSuccCount++;
                }
            } catch (ClassCastException e) {
                revokedFailCount++;
                log.error("令牌认证信息转换为AuthUser失败", e);
            } catch (Exception e) {
                revokedFailCount++;
                log.error("处理令牌时发生异常", e);
            }
        }
        log.info("租户{}令牌吊销完成，共吊销{}个令牌，成功{}个令牌，失败{}个令牌", tenantId, revokedCount, revokedSuccCount, revokedFailCount);
        return isLogoutSuccess;
    }

    @Override
    public Map<Object, Object> getUrlPermissionMapping() {
        String key = RedisCacheKey.makeRedisKey("", URL_PERMISSION_MAPPING_ALL);
        Long size = redisTemplate.opsForHash().size(key);
        //不存在资源缓存到redis
        if (size == 0) {
            List<ApiResourceDTO> resources = userService.getResources();
            // 转换为：（k->v）==>（url->code1,code2...），一个URL下有多个资源权限
            // 按URL分组，生成对应资源权限列表
            Map<String, List<String>> groupedByName = resources.stream()
                    .filter(item -> StringUtils.isNotBlank(item.getUrl()))
                    .collect(Collectors.groupingBy(ApiResourceDTO::getUrl,
                            Collectors.mapping(ApiResourceDTO::getCode, Collectors.toList())));
            // 将资源权限列表组装成字符串存储
            Map<String, String> mapping = groupedByName.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));
            redisTemplate.opsForHash().putAll(key, mapping);
        }
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Map<Object, Object> getUrlPermissionMappingV1(String tenantId) {
        String key = RedisCacheKey.makeRedisKey("", URL_PERMISSION_MAPPING_ALL,tenantId);
        Long size = redisTemplate.opsForHash().size(key);
        //不存在资源缓存到redis
        if (size == 0) {
            List<ApiResourceDTO> resources = userService.getResourcesV1(tenantId);
            // 转换为：（k->v）==>（url->code1,code2...），一个URL下有多个资源权限
            // 按URL分组，生成对应资源权限列表
            Map<String, List<String>> groupedByName = resources.stream()
                    .filter(item -> StringUtils.isNotBlank(item.getUrl()))
                    .collect(Collectors.groupingBy(ApiResourceDTO::getUrl,
                            Collectors.mapping(ApiResourceDTO::getCode, Collectors.toList())));
            // 将资源权限列表组装成字符串存储
            Map<String, String> mapping = groupedByName.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));
            redisTemplate.opsForHash().putAll(key, mapping);
        }
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Boolean hasPermission(String uid, String url, String method) {
        Map<Object, Object> urlPermissionMapping = getUrlPermissionMapping();
        String permission = getPermission(url, urlPermissionMapping);
        log.info("请求路径{},对应授权码为{} 用户ID={}", url, permission, uid);
        //如果资源授权码为空认定未认证通过后可访问 否则进行资源鉴权处理
        if (StringUtils.isEmpty(permission)) {
            return Boolean.TRUE;
        }
        return userService.hasPermission(uid, permission);
    }

    @Override
    public Boolean hasPermissionV1(String uid, String url, String method,String tenantId) {
        // 将租户上下文安装到 TTL，确保下游 Dubbo 调用透传租户，system 服务 SQL 走租户改写器自动注入租户条件
        UserContextHelper.helper().setTTLTenant(tenantId);
        try {
            Map<Object, Object> urlPermissionMapping = getUrlPermissionMappingV1(tenantId);
            String permission = getPermission(url, urlPermissionMapping);
            log.info("请求路径{},对应授权码为{} 用户ID={}", url, permission, uid);
            //如果资源授权码为空认定未认证通过后可访问 否则进行资源鉴权处理
            if (StringUtils.isEmpty(permission)) {
                return Boolean.TRUE;
            }
            return userService.hasPermissionV1(uid, permission,tenantId);
        } finally {
            UserContextHelper.helper().clearTTLTenant();
        }
    }

    /**
     * 按精确地址、通配符规则取得接口所需权限。
     *
     * @param url 当前请求路径
     * @param urlMappingCode 接口地址与权限码的现有映射
     * @return 接口所需权限码，多个权限以逗号分隔；其他未配置接口返回空字符串
     */
    private String getPermission(String url,Map<Object, Object> urlMappingCode) {
        url = PathContainer.parsePath(url).elements().stream()
                .map(element -> element instanceof PathContainer.PathSegment
                        ? ((PathContainer.PathSegment) element).valueToMatch() : element.value())
                .collect(Collectors.joining());
        url = org.springframework.util.StringUtils.cleanPath(url).replaceAll("/{2,}", "/");
        if (url.length() > 1 && url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        String code = (String) urlMappingCode.get(url);
        if (StringUtils.isNotBlank(code)) {
            return code;
        }
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (Map.Entry<Object, Object> entry : urlMappingCode.entrySet()) {
            boolean match = antPathMatcher.match((String) entry.getKey(), url);
            if (match && StringUtils.isNotBlank((String) entry.getValue())) {
                return (String) entry.getValue();
            }
        }
        return "";
    }

    @Override
    public String ipWhiteList() {
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.IP_WHITE_ALL);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return StrUtil.toStringOrNull(redisTemplate.opsForValue().get(key));
        }
        String ip = CommonConstant.EMPTY_STR;
        ConfigDTO config = configService.getGlobalConfig("sys", "ipWhiteList");
        if (Objects.nonNull(config) && StringUtils.isNotEmpty(config.getValue())) {
            ip = config.getValue();
        }
        redisTemplate.opsForValue().set(key, ip, 5, TimeUnit.MINUTES);
        return ip;
    }

    private boolean openIPCheck() {
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.IP_WHITE_SWITCH);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            String value = StrUtil.toStringOrNull(redisTemplate.opsForValue().get(key));
            return CommonConstant.YES.equalsIgnoreCase(value);
        }
        ConfigDTO config = configService.getGlobalConfig("sys", "loginOpenIpLimit");
        String configValue = Objects.nonNull(config) ? config.getValue() : null;
        redisTemplate.opsForValue().set(key, configValue, 5, TimeUnit.MINUTES);
        return CommonConstant.YES.equalsIgnoreCase(configValue);
    }

    @Override
    public Boolean checkIp(String remoteIp) {
        if(!openIPCheck()){
            return true;
        }
        String ipWhiteList = ipWhiteList();
        if (StringUtils.isBlank(ipWhiteList)) {
            return true;
        }
        return checkIpList(ipWhiteList, remoteIp);
    }

    @Override
    public Boolean tokenExpiredNotify(String userId) {
        return notificationService.sendSocketMsg(userId, SocketMsg.build(SocketMsg.Type.TOKEN_EX, "用户登陆已过期，请重新登陆！！"));
    }

    @Override
    public boolean checkToken(String tokenValue) {
        // 去除可能的"Bearer "前缀
        String processedToken = tokenValue.replace("Bearer ", "");
        // 读取Token对象
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(processedToken);
        // Token存在且未过期
        return accessToken != null && !accessToken.isExpired();
    }

    private boolean checkIpList(String ipWhiteList, String remoteHost) {
        String[] ipList = ipWhiteList.split("\\n");
        for (String ip : ipList) {
            if (ip.endsWith(".")) {
                if (remoteHost.startsWith(ip)) {
                    return true;
                }
            } else if (ip.indexOf("-") > 0) {
                if (IpUtil.ipExistsInRange(remoteHost, ip)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 
     * 获取客户端的所有有效令牌
     * 
     * @param clientIds
     * @return
     */
    private Collection<OAuth2AccessToken> getClientTokens(List<String> clientIds){
        if (!EmptyUtils.isNotEmpty(clientIds)) {
            clientIds = Arrays.asList("test_client");
        }
        // 查询指定客户端的所有有效令牌
        Collection<OAuth2AccessToken> allValidToken = new ArrayList<>();
        for (String clientId : clientIds) {
            Collection<OAuth2AccessToken> clientTokens = tokenStore.findTokensByClientId(clientId);
            if (EmptyUtils.isNotEmpty(clientTokens)) {
                allValidToken.addAll(clientTokens);
            } else {
                log.info("客户端{}下无有效令牌", clientId);
            }
        }
        return allValidToken;
    }

    /**
     *
     * 获取租户下账户
     *
     * @param tenantId
     * @return
     */
    private Set<String> getTenantUserNames(String tenantId){
        Set<String> tenantUserNames = new HashSet<>();
        // 查询租户下的用户列表
        Result<List<String>> result = this.userService.getUserNamesByTenantId(tenantId);
        if (result.isFail()) {
            log.error("查询租户{}下用户失败：{}", tenantId, result.getMsg());
            return tenantUserNames;
        }
        List<String> userNameList = result.getData();
        if (CollectionUtils.isEmpty(userNameList)) {
            log.info("租户{}下无用户", tenantId);
            return tenantUserNames;
        }

        // 构建「租户ID@BT@用户名」的Set
        tenantUserNames = userNameList.stream()
                .map(userName -> String.format(USER_NAME_FORMAT, tenantId, userName))
                .collect(Collectors.toSet());

        return tenantUserNames;
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }


}
