package com.bytter.core.base;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.PageResult;
import com.bytter.core.utils.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 简化Hibernate的CURD处理
 *
 * @param <T>
 *            实体
 * @param <PK>
 *            主键类型
 * @author zhoul
 * @since 2018/1/15.
 */
public abstract class BaseDao<T, PK extends Serializable> extends BaseCommonDao<T, PK> {
    private Integer getFirst(IPagePara pagePara) {
        Integer pageNumber = pagePara.getPageNumber();
        if (pageNumber == 0) {
            return 0;
        } else {
            return (pageNumber - 1) * pagePara.getPageSize();
        }
    }
    /**
     * 根据SQL查询出PageResult对象
     *
     * @param pagePara
     *            页面参数
     * @param sql
     *            SQL语句
     * @param params
     *            查询参数
     * @return PageResult 结果集
     */
    protected IPageResult<T> getPageResultBySQL(IPagePara pagePara, final String sql, final Map<String, ?> params) {
        String countSQL = prepareCountSQL(sql);
        BigDecimal count = (BigDecimal) createSQLQuery(countSQL, params).uniqueResult();
        List<T> list = new ArrayList<>();
        // 若统计出来的信息大于0，则发送查询语句，否则返回空的ArrayList
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            list = listBySQL(sql, getFirst(pagePara), pagePara.getPageSize(), params);
        }
        return new PageResult<>(pagePara, count.intValue(), list);
    }
    /**
     * 根据SQL查询出PageResult对象
     *
     * @param pagePara
     *            页面参数
     * @param sql
     *            SQL语句
     * @param params
     *            查询参数
     * @param clazz
     *            转换为指定Bean
     * @param <U>
     *            Bean
     * @return PageResult 结果集
     */
    protected <U> IPageResult<U> getPageResultBySQL(IPagePara pagePara, final String sql, final Map<String, ?> params,
            final Class<U> clazz) {
        String countSQL = prepareCountSQL(sql);
        BigDecimal count = (BigDecimal) createSQLQuery(countSQL, params).uniqueResult();
        List<U> list = new ArrayList<>();
        // 若统计出来的信息大于0，则发送查询语句，否则返回空的ArrayList
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            list = listBySQLAliasToBean(sql, getFirst(pagePara), pagePara.getPageSize(), params, clazz);
        }
        return new PageResult<>(pagePara, count.intValue(), list);
    }
    /**
     * 根据SQL查询出PageResult对象
     *
     * @param pagePara
     *            页面参数
     * @param hql
     *            hql语句
     * @param params
     *            查询参数
     * @return PageResult 结果集
     */
    protected IPageResult<T> getPageResultByHQL(IPagePara pagePara, final String hql, final Map<String, ?> params) {
        Long count = countHqlResult(hql, params);
        List<T> list = new ArrayList<>();
        // 若统计出来的信息大于0，则发送查询语句，否则返回空的ArrayList
        if (count > 0) {
            list = listByHQL(hql, getFirst(pagePara), pagePara.getPageSize(), params);
        }
        return new PageResult<>(pagePara, count.intValue(), list);
    }
    /**
     * 根据SQL查询出PageResult对象
     *
     * @param pagePara
     *            页面参数
     * @param hql
     *            HQL语句
     * @param params
     *            查询参数
     * @param clazz
     *            转换为指定Bean
     * @param <U>
     *            Bean
     * @return PageResult 结果集
     */
    protected <U> IPageResult<U> getPageResultByHQL(IPagePara pagePara, final String hql, final Map<String, ?> params,
            final Class<U> clazz) {
        Long count = countHqlResult(hql, params);
        List<U> list = new ArrayList<>();
        // 若统计出来的信息大于0，则发送查询语句，否则返回空的ArrayList
        if (count > 0) {
            list = listByHQLAliasToBean(hql, getFirst(pagePara), pagePara.getPageSize(), params, clazz);
        }
        return new PageResult<>(pagePara, count.intValue(), list);
    }



    /***
     * 使用该方法必须是登录成功以后，如果没有登录返回为空，在使用地方就会报空指针，就知道那里不能调用该方法。
     */
    protected ICurrentUser getCurrentUser() {
        return UserContextHelper.helper().getCurrentUser();
    }

    protected String getCurrentUserTenantId(){
        String tenantId = "";
        ICurrentUser currentUser = getCurrentUser();
        if (ObjectUtil.isNotEmpty(currentUser)){
            // 如果当前用户对象里面租户ID为空，则根据用户ID查询用户表的租户ID，app登录的时候，没有把租户ID放入当前用户对象里面
            if (StringUtils.isBlank(currentUser.getTenantId())){
                String sql = "select tenant_id as tenantId from sys_user where id = :id ";
                Map<String,Object> params = new HashMap<>();
                params.put("id",currentUser.getId());
                String tempId = (String) createSQLQuery(sql.toString(), params).uniqueResult();
                if (StringUtils.isNotBlank(tempId)){
                    tenantId = tempId;
                    logger.debug("根据用户ID[{}]查询用户表租户ID[{}]",currentUser.getId(),tenantId);
                }else{
                    logger.error("根据用户ID[{}]查询用户表租户ID为空！",currentUser.getId());
                }
            }else{
                tenantId = currentUser.getTenantId();
            }
        }
        return tenantId;
    }
}
