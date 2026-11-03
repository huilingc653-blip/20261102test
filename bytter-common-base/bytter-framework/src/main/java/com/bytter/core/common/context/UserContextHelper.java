package com.bytter.core.common.context;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;

/**
 * 请求上下文中用户信息获取工具类 通过拦截器统一设置与移除，当前线程独享用户信息和租户ID。
 *
 * @author zhaohc
 * @since 2023/05/09
 */
@Slf4j
public class UserContextHelper {

    /**
     * 当前线程用户信息
     */
    private final TransmittableThreadLocal<ICurrentUser> threadLocal;
    /**
     * 当前线程租户ID
     */
    private final TransmittableThreadLocal<String> tenantThreadLocal;

    private UserContextHelper() {
        this.threadLocal = new TransmittableThreadLocal<>();
        this.tenantThreadLocal = new TransmittableThreadLocal<>();
    }

    /**
     * 获取线程工具类单例。
     *
     * @return 线程工具类单例。
     */
    public static UserContextHelper helper() {
        return Singleton.INSTANCE.helper();
    }

    /**
     * 设置当前线程用户信息和租户ID。
     *
     * @param user 用户信息
     */
    public void setUser(ShiroUser user) {
        threadLocal.set(user);
        // 设置租户ID
        if (user != null) {
            setTTLTenant(user.getTenantId());
        }
    }

    /**
     * 设置当前线程租户ID。
     *
     * @param tenantId 租户ID
     */
    public void setTTLTenant(String tenantId) {
        tenantThreadLocal.set(tenantId);
    }

    /**
     * 获取当前线程租户ID。
     *
     * @return 当前线程租户ID
     */
    public String getTTLTenant() {
        return tenantThreadLocal.get();
    }

    /**
     * 清除当前线程租户ID。
     */
    public void clearTTLTenant() {
        tenantThreadLocal.remove();
    }

    /**
     * 判断是否有租户上下文
     */
    public boolean hasTTLTenant() {
        return tenantThreadLocal.get() != null;
    }

    /**
     * 获取当前线程中用户信息
     *
     * @return ICurrentUser
     */
    public ICurrentUser getCurrentUser() {
        // 获取当前线程中用户信息,先从当前线程中获取，如果为空则从Dubbo线程中获取
        ICurrentUser currentUser = threadLocal.get();
        if (currentUser == null) {
            String userToken = getAttachmentFromDubbo("shiroUser");
            if (userToken != null) {
                try {
                    // log.info("获取dubbo用户信息：{}", userToken);
                    currentUser = JsonUtils.parseObject(userToken, ShiroUser.class);
                } catch (Exception e) {
                    log.error("获取dubbo用户信息失败", e);
                }
            }
        }
        return currentUser;
    }

    /**
     * 获取当前线程中用户信息
     *
     * @return ICurrentUser
     */
    public ICurrentUser getLocalUser() {
        // 获取当前线程中用户信息，从当前线程中获取，如果为空则从Dubbo线程中获取
        return threadLocal.get();
    }

    /**
     * 从Dubbo线程中获取上下文信息。
     *
     * @param key 信息key。
     * @return 上下文信息。
     */
    private static String getAttachmentFromDubbo(String key) {
        // 尝试从ServerAttachment获取
        String attachment = RpcContext.getServerAttachment().getAttachment(key);
        if (attachment == null) {
            // 尝试从ServiceContext获取
            attachment = RpcContext.getServiceContext().getAttachment(key);
        }
        return attachment;
    }

    /**
     * 返回用户包装类
     *
     * @return ShiroUser
     * @deprecated by zhoul 2018-01-03 17:22 <br>
     * 此方法已过时，推荐使用{@link #getCurrentUser()}方法
     */
    @Deprecated
    public ShiroUser getUserVo() {
        return (ShiroUser) UserContextHelper.helper().getCurrentUser();
    }

    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    public String getUserId() {
        ICurrentUser currentUser = getCurrentUser();
        return currentUser.getId();
    }

    /**
     * 获取当前用户租户ID。
     *
     * <p>TTL 租户为唯一权威源，优先读取；Dubbo attachment 与 shiroUser 仅为 TTL 未正确安装时的
     * 异常降级兜底。正常链路经 Dubbo Provider 一致性校验安装后，第一分支即可命中。</p>
     *
     * @return 当前用户租户ID。
     */
    public String getTenantId() {
        String tenantId = tenantThreadLocal.get();
        if (ObjectUtil.isEmpty(tenantId)) {
            tenantId = getAttachmentFromDubbo("ttlTenant");
            if (ObjectUtil.isEmpty(tenantId)) {
                ShiroUser shiroUser = (ShiroUser) UserContextHelper.helper().getCurrentUser();
                if (shiroUser != null) {
                    tenantId = shiroUser.getTenantId();
                }
            }
            if (ObjectUtil.isNotEmpty(tenantId)) {
                log.warn("租户上下文从降级源获取，TTL 未正确安装，请检查上下文传递链路：tenantId={}", tenantId);
            }
        }
        return tenantId;
    }

    /**
     * 设置当前用户皮肤
     *
     * @param skin 皮肤颜色
     */
    public void setSkin(String skin) {
        ShiroUser shiroUser = (ShiroUser) UserContextHelper.helper().getCurrentUser();
        shiroUser.setSysSkin(skin);
    }

    /**
     * 获取当前用户登录名
     *
     * @return 当前用户登录名
     */
    public String getUserLoginName() {
        ICurrentUser currentUser = getCurrentUser();
        return currentUser.getLoginName();
    }

    /**
     * 清除当前线程用户信息
     */
    public void removeUser() {
        threadLocal.remove();
        tenantThreadLocal.remove();
    }

    private enum Singleton {
        INSTANCE;

        private final UserContextHelper helper;

        Singleton() {
            this.helper = new UserContextHelper();
        }

        public UserContextHelper helper() {
            return helper;
        }
    }



}
