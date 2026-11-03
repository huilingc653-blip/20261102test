package com.bytter.core.mybatis;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Objects;

/**
 * Mybatis-plus插件中公共字段自动填充拦截器。
 *
 * @author zhaohc
 * @date 2023/3/9 16:11
 */
@Slf4j
public class BytterMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert...]");
        this.setFieldValByName("createDate", DateUtil.date(), metaObject);// 创建时间统一赋值
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            this.setFieldValByName("createBy", currentUser.getUsername(), metaObject);// 创建人统一赋值
        }
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update...]");
        this.setFieldValByName("updateDate", DateUtil.date(), metaObject);// 更新时间统一赋值
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            this.setFieldValByName("updateBy",currentUser.getUsername(), metaObject);// 更新人统一赋值
        }
    }
}
