package com.bytter.system.vo;
import com.bytter.core.annotation.Resource;
/**
 * Created by zhoul on 2017/6/8.
 */
public class ResourcesSimpleVO {
	@Resource(inject = "name")
    private String id;
    private String name;
    private String menuId;
    private String menuName;
    private String corpId;
    private String permission;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getMenuName() {
        return menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    @Override
    public String toString() {
        return "ResourcesSimpleVO [id=" + id + ", name=" + name + ", menuId=" + menuId + ", menuName=" + menuName
                + ", corpId=" + corpId + ", permission=" + permission + "]";
    }
}
