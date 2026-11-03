package com.bytter.olap.pojo;

import java.sql.Timestamp;


/**
 * 数据源实体类
 *
 * @author yfyuan
 * @date 2016/8/18.
 */
public class DashboardDatasource {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 创建者ID
     */
    private String userId;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型，如JDBC等
     */
    private String type;

    /**
     * 配置值，其数据结构如下：
     * {
     * 	"aggregateProvider":true,
     * 	"password":"btv10_ynby",
     * 	"pooled":true,
     * 	"driver":"oracle.jdbc.driver.OracleDriver",
     * 	"jdbcurl":"jdbc:oracle:thin:@192.168.0.38:1521/orcl",
     * 	"username":"btv10_ynby"
     * }
     */
    private String config;

    /**
     * 权限值
     */
    private String permission;


    private String userName;
    private String loginName;


    private Timestamp createTime;
    private Timestamp updateTime;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
