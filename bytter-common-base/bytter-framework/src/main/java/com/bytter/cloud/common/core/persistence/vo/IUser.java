package com.bytter.cloud.common.core.persistence.vo;
import java.io.Serializable;
/**
 * 当前用户对象接口
 *
 * @author zhoul 2021-1-21
 * @since 1.0.0
 */
public interface IUser extends Serializable {
    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    String getCurrentUserId();
    /**
     * 获取当前用户所在单位ID
     *
     * @return 单位ID
     */
    String getCorpId();
    /**
     * 获取当前用户所在的租户ID
     *
     * @return 租户ID
     */
    String getTenantId();
    /**
     * 获取当前用户名称
     * @return 用户名称
     */
    String getUsername();
    /**
     * 获取当前用户数据范围
     *
     * @return 数据范围
     */
    String getDataScope();
}
