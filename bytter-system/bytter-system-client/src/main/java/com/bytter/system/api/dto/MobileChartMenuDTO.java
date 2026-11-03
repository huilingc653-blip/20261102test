package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.List;
/**
 * 移动端报表菜单VO
 *
 * @author chenlw
 * @since 2020-06-22
 */
public class MobileChartMenuDTO implements Serializable {
    private String menuId;
    private String name;
    private String description;
    private String url;
    private String iconUrl;
    private String pageTitle;
    private String corpId;
    private String netId;
    private String accessToken;
    /**
     * 子菜单列表
     */
    private List<MobileChartMenuDTO> menuList;
    /**
     * 菜单跳转类型
     * - jump，默认
     * - tab
     */
    private String jumpType;
    public MobileChartMenuDTO() {
    }
    public MobileChartMenuDTO(String menuId, String name, String description, String url, String iconUrl) {
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.url = url;
        this.iconUrl = iconUrl;
    }
    public MobileChartMenuDTO(String menuId, String name, String url) {
        this.menuId = menuId;
        this.name = name;
        this.url = url;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public List<MobileChartMenuDTO> getMenuList() {
        return menuList;
    }
    public void setMenuList(List<MobileChartMenuDTO> menuList) {
        this.menuList = menuList;
    }
    public String getJumpType() {
        return jumpType;
    }
    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }
    public String getPageTitle() {
        return pageTitle;
    }
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
