package com.bytter.biz.corpCashier.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Description: 单位出纳表用户表
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/11
 */
@Entity
@Table(name = "NIS_CORP_CASHIER_USER")
public class NisCorpCashierUser extends IdEntity {
    private String corpCashierId;    // 主表ID
    private String corpId;           // 单位ID
    private String userId;           // 用户ID
    private String userCode;         // 用户登录名
    private String userName;         // 用户名称

    @Column(name = "CORP_CASHIER_ID")
    public String getCorpCashierId() {
        return corpCashierId;
    }

    public void setCorpCashierId(String corpCashierId) {
        this.corpCashierId = corpCashierId;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "USER_CODE")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "USER_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
