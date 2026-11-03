package com.bytter.workflow.domain.dao;

import com.bytter.workflow.domain.entity.Option;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoul on 2017/8/3.
 *
 * @history
 * 1. 2019-12-30  chenlw  移除单据配置查询的相关方法。
 * 2. 2019-12-31  chenlw  新增方法：getExtendParamsListByCategoryKey()、getStableExtendParamsListByCategoryId()。
 */
@Repository
public class OptionDao extends BaseDao<Option, String> {

    /**
     * Created by chenlw on 2019/03/01.
     * 获取定制显示的字段列表
     *
     * @param categoryKey 流程关键字
     * @return
     */
    public List<Option> getExtendParamsListByCategoryKey(String categoryKey) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bo.* from bpm_option bo inner join bpm_flow_category bfc on bfc.id = bo.category ");
        sql.append("and bo.type = 5 ");
        sql.append("and bfc.key_= :categoryKey ");

        Map params = new HashMap<>();
        params.put("categoryKey", categoryKey);
        List<Option> list = listBySQLAliasToBean(sql.toString(), params, Option.class);
        return list;
    }


    /**
     * 获取单据配置
     *
     * @param categoryKey 流程关键字
     * @return 单据配置
     */
    public Option getInvoiceConfigByCategoryKey(String categoryKey) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bo.* from bpm_option bo inner join bpm_flow_category bfc on bfc.id = bo.category ");
        sql.append("and bo.type = 4 ");
        sql.append("and bfc.key_= :categoryKey ");

        Map params = new HashMap<>();
        params.put("categoryKey", categoryKey);
        List<Option> list = listBySQLAliasToBean(sql.toString(), params, Option.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 2019-03-04
     * chenlw
     * 获取定制显示的字段列表
     *
     * @param categoryId
     * @return
     */
    public List<Option> getExtendParamsListByCategoryId(String categoryId) {
        String hql = "from Option where category=?0 and type = 5 ";
        return createQuery(hql, categoryId).list();
    }

    /**
     * 获取定制显示字段中的固定显示字段列表
     *
     * @param categoryId 流程分类ID
     * @return 固定显示字段列表
     */
    public List<Option> getStableExtendParamsListByCategoryId(String categoryId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bo.* from bpm_option bo inner join bpm_flow_category bfc on bfc.id = bo.category ");
        sql.append("and bo.type = 5 ");
        sql.append("and bo.is_stable_field = 1 ");
        sql.append("and bo.category= :categoryId ");
        Map params = new HashMap<>();
        params.put("categoryId", categoryId);
        List<Option> list = listBySQLAliasToBean(sql.toString(), params, Option.class);
        return list;
    }


    /**
     * Created by chenlw on 2019/03/05.
     * 获取定制显示的字段列表（状态为可见的）
     *
     * @param categoryKey 流程关键字
     * @return
     */
    public List<Option> getVisibleExtendParamsListByCategoryKey(String categoryKey) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bo.* from bpm_option bo inner join bpm_flow_category bfc on bfc.id = bo.category ");
        sql.append("and bo.type = 5 ");
        sql.append("and bo.visible = 1 ");
        sql.append("and bfc.key_ = :categoryKey ");
        Map params = new HashMap<>();
        params.put("categoryKey", categoryKey);
        List<Option> list = listBySQLAliasToBean(sql.toString(), params, Option.class);
        return list;
    }

    /**
     * 获取定制显示字段中的固定显示字段列表
     *
     * @param categoryKey 流程关键字
     * @return 固定显示字段列表
     */
    public List<Option> getStableExtendParamsListByCategoryKey(String categoryKey) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bo.* from bpm_option bo inner join bpm_flow_category bfc on bfc.id = bo.category ");
        sql.append("and bo.type = 5 ");
        sql.append("and bo.is_stable_field = 1 ");
        sql.append("and bfc.key_ = :categoryKey ");
        Map params = new HashMap<>();
        params.put("categoryKey", categoryKey);
        List<Option> list = listBySQLAliasToBean(sql.toString(), params, Option.class);
        return list;
    }

    public List<Option> listByCategoryId(String categoryId) {
        String hql = "from Option where category=?0  order by createTime asc";
        return createQuery(hql, categoryId).list();
    }

    public Option getMessageTemplateByCategory(String categoryId) {
        String hql = "from Option where category=?0 and type = 3";
        return (Option) createQuery(hql, categoryId).uniqueResult();
    }

    public List<Option> listConditionByCategoryId(String categoryId) {
        String hql = "from Option where category=?0 and type = 1";
        return createQuery(hql, categoryId).list();
    }


}
