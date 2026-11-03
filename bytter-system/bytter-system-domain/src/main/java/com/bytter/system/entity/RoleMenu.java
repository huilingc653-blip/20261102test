package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
/**
 * 角色和菜单的关系维护类
 * Created by zhoul on 2017/11/2.
 */
@Entity
@Table(name = "SYS_ROLE_MENU")
public class RoleMenu extends IdEntity {
    private Role role;
    private Menu menu;
//    private String roleId;
//
//    private String menuId;
    public RoleMenu() {
    }
    public RoleMenu(Role role, Menu menu) {
        this.role = role;
        this.menu = menu;
    }
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    public Menu getMenu() {
        return menu;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
//    @Column(name = "ROLE_ID")
//    public String getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(String roleId) {
//        this.roleId = roleId;
//    }
//
//    @Column(name = "MENU_ID")
//    public String getMenuId() {
//        return menuId;
//    }
//
//    public void setMenuId(String menuId) {
//        this.menuId = menuId;
//    }
}
