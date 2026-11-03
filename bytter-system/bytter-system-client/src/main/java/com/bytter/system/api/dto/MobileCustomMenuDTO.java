package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.Map;
/**
 * 移动端 自定义菜单实体类
 *
 * @author chenlw
 * @since 2021-01-28
 */
public class MobileCustomMenuDTO implements Serializable {
    /**
     * 菜单名称，名称不要过长。要控制在10个字以内
     */
    private String name;
    /**
     * 菜单的跳转链接。
     * - 内部链接，就配置T2控制器层的访问URL，无需配置T2的基础访问URL。
     */
    private String url;
    /**
     * 菜单图标URL
     * - 配置T2静态资源文件路径，无需配置T2的基础访问URL。
     */
    private String iconUrl;
    /**
     * 参数对象
     */
    private Map<String, Object> params;
    /**
     * 是否需要校验登录密码才能进入菜单
     * - 默认不需要。值为空或0
     * - 值为1时表示需要校验登录密码
     */
    private String needCheckPassword;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public Map<String, Object> getParams() {
        return params;
    }
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    public String getNeedCheckPassword() {
        return needCheckPassword;
    }
    public void setNeedCheckPassword(String needCheckPassword) {
        this.needCheckPassword = needCheckPassword;
    }
}
