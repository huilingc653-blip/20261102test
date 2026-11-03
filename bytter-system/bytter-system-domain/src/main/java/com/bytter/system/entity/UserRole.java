package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;
/**
 * 用户和角色的关系维护类
 * Created by zhoul on 2017/11/2.
 */
@Entity
@Table(name = "SYS_USER_ROLE")
public class UserRole extends IdEntity {
    private Role role;
    private User user;
    public UserRole() {
    }
    public UserRole(User user, Role role) {
        this.role = role;
        this.user = user;
    }
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "ROLE_ID")
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "USER_ID")
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public int hashCode() {
        return Objects.hash(role.getId(), user.getId());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserRole userRole = (UserRole) obj;
        return (this.getRole().getId().equals(userRole.getRole().getId()) && this.getUser().getId().equals(userRole.getUser().getId()));
    }
}
