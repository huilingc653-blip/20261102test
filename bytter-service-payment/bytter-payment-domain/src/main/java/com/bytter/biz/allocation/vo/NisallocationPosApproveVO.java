package com.bytter.biz.allocation.vo;

import java.io.Serializable;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/12/25
 */
public class NisallocationPosApproveVO implements Serializable {
    private String categoryKey;
    private String userId;
    private Long num;

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}