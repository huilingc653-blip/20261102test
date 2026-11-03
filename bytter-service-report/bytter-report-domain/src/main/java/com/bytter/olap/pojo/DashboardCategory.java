package com.bytter.olap.pojo;


/**
 * 看板分类实体类
 *
 * @author yfyuan
 * @date 2016/8/26
 */
public class DashboardCategory {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 所属用户ID
     */
    private String userId;

    /**
     * 分类名称
     */
    private String name;

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
}
