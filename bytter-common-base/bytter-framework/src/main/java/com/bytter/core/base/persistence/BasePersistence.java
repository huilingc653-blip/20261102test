package com.bytter.core.base.persistence;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.context.UserContextHelper;

import java.util.HashMap;
/**
 * 与用户相关的持久类
 *
 * @author zhoul
 * @since 2017/8/1.
 */
public class BasePersistence extends HashMap<String, Object> {
    private String currentUserId;
    public BasePersistence() {
        ShiroUser principal = (ShiroUser) UserContextHelper.helper().getCurrentUser();
        if (principal != null) {
            this.currentUserId = principal.getId();
        }
    }
    public String getCurrentUserId() {
        return currentUserId;
    }
}
