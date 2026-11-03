package com.bytter.olap.pojo;

/**
 * 看板-角色权限表
 *
 * @author chenlw
 * @date 2019/11/05.
 */
public class DashboardRolePermission {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 看板ID
     */
    private String boardId;


    /**
     * 角色ID
     */
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}

