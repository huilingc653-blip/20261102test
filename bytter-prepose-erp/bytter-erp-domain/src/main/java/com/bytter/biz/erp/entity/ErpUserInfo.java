package com.bytter.biz.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.core.base.entity.BaseEntity;
import com.bytter.core.base.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/2/9
 */
@Entity
@Table(name = "erp_user_info", schema = "")
public class ErpUserInfo extends IdEntity implements Serializable {
    private String loginName;
    private String password;
    private String tenantId;
    private Integer status;

    @Column(name ="LOGIN_NAME")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(name ="PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name ="TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
