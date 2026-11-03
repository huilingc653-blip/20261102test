package com.bytter.olap.pojo;

/**
 * 看板-用户权限表
 *
 * @author chenlw
 * @date 2019/11/05.
 */
public class DashboardUserPermission {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 看板ID
     */
    private String boardId;


    /**
     * 用户ID
     */
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

