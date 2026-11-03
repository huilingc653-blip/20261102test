package com.bytter.olap.dto;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.pojo.DashboardBoard;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ViewDashboardBoard {

    private String id;
    private String categoryId;
    private String name;
    private String createTime;
    private String updateTime;
    private Map<String, Object> layout;
    private String categoryName;

    /**
     * 拥有权限的用户ID列表
     */
    private List<String> userIdList;

    /**
     * 拥有权限的角色ID列表
     */
    private List<String> roleIdList;


    public static final Function TO = new Function<DashboardBoard, ViewDashboardBoard>() {
        @Nullable
        @Override
        public ViewDashboardBoard apply(@Nullable DashboardBoard input) {
            return new ViewDashboardBoard(input);
        }
    };

    public ViewDashboardBoard(DashboardBoard board) {
        this.id = board.getId();
        this.categoryId = board.getCategoryId();
        this.name = board.getName();
        if(board.getCreateTime() != null) {
            this.createTime = board.getCreateTime().toString();
            this.updateTime = board.getUpdateTime().toString();
        }
        // this.layout = JSONObject.parseObject(board.getLayout());
        this.layout = JsonUtils.json2Map(board.getLayout());
        this.categoryName = board.getCategoryName();
    }


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


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Map<String, Object> getLayout() {
        return layout;
    }

    public void setLayout(Map<String, Object> layout) {
        this.layout = layout;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
