package com.bytter.system.dao;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.entity.Notification;
import com.bytter.system.vo.NotificationResponseVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class NotificationDao extends BaseDao<Notification, String> {
    public Notification getByUserIdAndTagAndBusinessId(String userId, String tag, String businessId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select n.id, n.category_id, n.user_id, n.corp_id, n.name, n.read_flag, n.type, n.business_id, n.create_date ");
        sql.append("  from sys_notification n left join sys_notification_category nc on n.category_id = nc.id ");
        sql.append(" where nc.tag = :tag and n.business_Id = :businessId ");
        params.put("tag", tag);
        params.put("businessId", businessId);
        sql.append(" order by n.create_date desc ");
        List<Notification> list = this.listBySQLAliasToBean(sql.toString(), params, Notification.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    public List<Notification> listByNotificationCategoryIdAndUserId(String notificationCategoryId, String userId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from Notification where categoryId = :categoryId and userId = :userId ");
        Map<String, String> params = new HashMap<>();
        params.put("categoryId", notificationCategoryId);
        params.put("userId", userId);
        Query query = createQuery(hql.toString(), params);
        return query.list();
    }
    public List<Notification> listByNotificationCategoryId(String notificationCategoryId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from Notification where categoryId = :categoryId ");
        Map<String, String> params = new HashMap<>();
        params.put("categoryId", notificationCategoryId);
        Query query = createQuery(hql.toString(), params);
        return query.list();
    }
    /**
     * 删除消息
     *
     * @param notificationCategoryId 消息分类ID
     */
    public void deleteByNotificationCategoryId(String notificationCategoryId) {
        if (EmptyUtils.isEmpty(notificationCategoryId)) {
            return;
        }
        StringBuilder hql = new StringBuilder();
        hql.append("delete from Notification ")
                .append(" where categoryId = :categoryId");
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", notificationCategoryId);
        Query query = createQuery(hql.toString(), map);
        query.executeUpdate();
    }
    /**
     * 根据业务ID删除数据
     *
     * @param businessId 业务ID
     */
    public void deleteByBusinessId(String businessId) {
        String hql = "delete from Notification where businessId=?0";
        createQuery(hql, businessId).executeUpdate();
    }
    public Integer getUserAllNotificationCount(String userId, List<String> roleIds,List<String> corpIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleIds", roleIds);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("count(n.ID) ");
        sql.append("from SYS_NOTIFICATION n ");
        sql.append("left join SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        if (!CollectionUtils.isEmpty(roleIds)) {
            sql.append("left join SYS_NOTIFICATION_ROLE nr on nc.ID = nr.CATEGORY_ID ");
        }
        // 当前用户的待处理和通知
        sql.append("where n.USER_ID = :userId and nc.TYPE in (1,2) ");
        if (CollectionUtil.isNotEmpty(corpIds)) {
            sql.append(buildCorpCond(corpIds));
        }
        sql.append(" and ( ");
        // 不需要角色过滤，为空默认为不需要角色过滤
        sql.append(" (nc.ROLE_FILTER = 0) or (nc.ROLE_FILTER is null) ");
        if (!CollectionUtils.isEmpty(roleIds)) {
            // 需要角色过滤
            sql.append(" or ");
            sql.append("(nc.ROLE_FILTER = 1 and nr.ROLE_ID in (:roleIds)) ");
        }
        sql.append(") ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        BigDecimal count = (BigDecimal) sqlQuery.uniqueResult();
        return count.intValue();
    }
    public Integer getUserNotificationCount(String userId, List<String> roleIds,List<String> corpIds) {
        if (EmptyUtils.isEmpty(roleIds)) {
            return 0;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleIds", roleIds);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("count(nc.ID) ");
        sql.append("from SYS_NOTIFICATION n ");
        sql.append("left join SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        sql.append("where n.USER_ID = :userId and nc.TYPE =2 ");
        if (CollectionUtil.isNotEmpty(corpIds)) {
            sql.append(buildCorpCond(corpIds));
        }
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        BigDecimal count = (BigDecimal) sqlQuery.uniqueResult();
        return count.intValue();
    }
    /**
     * 获取未读消息数量
     *
     * @param locale
     * @param userId
     * @param roleIds
     * @return
     */
    public Integer getUserUnreadNotificationCount(String locale,
                                                  String userId,
                                                  List<String> roleIds,List<String> corpIds) {
        if (EmptyUtils.isEmpty(roleIds)) {
            return 0;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locale", locale);
        map.put("roleIds", roleIds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM SYS_NOTIFICATION n ");
        sql.append(" LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON n.CATEGORY_ID = nc.ID ");
        sql.append(" LEFT JOIN SYS_NOTIFICATION_MESSAGE SNM ON n.MSG_ID = SNM.ID AND SNM.N_LOCALE=:locale ");
        //sql.append(" LEFT JOIN SYS_NOTIFICATION_ROLE nr on nc.ID = nr.CATEGORY_ID ");
        sql.append(" WHERE n.USER_ID = :userId ");
        if (CollectionUtil.isNotEmpty(corpIds)) {
            sql.append(buildCorpCond(corpIds));
        }
        // 根据角色进行过滤
        /*sql.append(" AND ( ");
        sql.append("(nc.ROLE_FILTER = 0 AND nr.id is null) ");
        sql.append("OR ");
        sql.append("(nc.ROLE_FILTER = 1 AND nr.ROLE_ID in (:roleIds)) ");
        sql.append(" ) ");*/
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        BigDecimal count = (BigDecimal) sqlQuery.uniqueResult();
        return count.intValue();
    }
    public List<Map<String, Object>> getUserNotification(String userId, List<String> roleIds,List<String> corpIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleIds", roleIds);
        StringBuilder sql = new StringBuilder();
        sql.append("select nc.ID as id, ");
        sql.append("min(nc.tag) as tag, ");
        sql.append("min(nc.name) as name, ");
        sql.append("min(nc.list_url) as listUrl, ");
        sql.append("min(nc.detail_url) as detailUrl, ");
        sql.append("count(nc.ID) as num ");
        sql.append("from SYS_NOTIFICATION n ");
        sql.append("left join SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        //sql.append("left join SYS_NOTIFICATION_ROLE nr on nc.ID = nr.CATEGORY_ID ");
        sql.append("where n.USER_ID = :userId and nc.TYPE =2 ");
        if (CollectionUtil.isNotEmpty(corpIds)) {
            sql.append(buildCorpCond(corpIds));
        }
        /*sql.append("(nc.TYPE =2 and nc.ROLE_FILTER = 0 and n.USER_ID = :userId and nr.id is null) ");
        sql.append("or ");
        sql.append("(nc.TYPE =2 and  nc.ROLE_FILTER = 1 and n.USER_ID = :userId and nr.ROLE_ID in (:roleIds)) ");*/
        sql.append("group by nc.ID");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        sqlQuery.addScalar("name", StandardBasicTypes.STRING);
        sqlQuery.addScalar("tag", StandardBasicTypes.STRING);
        sqlQuery.addScalar("listUrl", StandardBasicTypes.STRING);
        sqlQuery.addScalar("detailUrl", StandardBasicTypes.STRING);
        sqlQuery.addScalar("num", StandardBasicTypes.INTEGER);
        sqlQuery.addScalar("id", StandardBasicTypes.STRING);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }
//    public IPageResult<NotificationResponseVO> getAPIUserNotificationNote(String userId, IParameterInput parameterInput) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("userId", userId);
//        StringBuilder sql = new StringBuilder();
//        sql.append("select * from ( ");
//        sql.append("SELECT n.id AS id,nc.name AS categoryName,n.name AS name,n.CREATE_DATE AS createDate,nc.LIST_URL AS listUrl,0 as status FROM SYS_NOTIFICATION n ");
//        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON n.CATEGORY_ID = nc.ID WHERE n.USER_ID = :userId AND n.TYPE =2");
//        sql.append(" union all ");
//        sql.append("SELECT nh.id AS id,nc.name AS categoryName,nh.name AS name,nh.END_DATE AS createDate,nc.LIST_URL AS listUrl,1 as status FROM SYS_NOTIFICATION_HISTORIC nh ");
//        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON nh.CATEGORY_ID = nc.ID WHERE nh.USER_ID = :userId AND nh.TYPE =2 ");
//        sql.append(") notice order by notice.createDate desc");
//        return getPageResultBySQL(parameterInput, sql.toString(), map, NotificationResponseVO.class);
//    }
    /**
     * 提供给APP调用的消息列表查询方法
     * created by chenlw on 2020/03  修正消息内容为空的问题。
     *
     * @param userId         用户ID
     * @param parameterInput 参数
     * @return 分页数据
     */
    public IPageResult<NotificationResponseVO> getAPIUserNotificationNote(String locale, String userId, IParameterInput parameterInput) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locale", locale);
        StringBuilder sql = new StringBuilder();
        String searchValue = parameterInput.getParameter().getPara("searchValue", null);
        sql.append("select * from ( ");
        sql.append("SELECT n.id AS id,nc.id as categoryId,nc.name AS categoryName,SNM.N_MESSAGE AS name,n.CREATE_DATE AS createDate,nc.LIST_URL AS listUrl,0 as status FROM SYS_NOTIFICATION n ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON n.CATEGORY_ID = nc.ID  LEFT JOIN SYS_NOTIFICATION_MESSAGE SNM ON n.MSG_ID = SNM.ID AND SNM.N_LOCALE=:locale WHERE n.USER_ID = :userId ");
        if (!StringUtils.isEmpty(searchValue)) {
            sql.append(" AND SNM.N_MESSAGE LIKE '%");
            sql.append(searchValue);
            sql.append("%' ");
        }
        sql.append(" union all ");
        sql.append("SELECT nh.id AS id,nc.id as categoryId,nc.name AS categoryName,SNM.N_MESSAGE AS name,nh.END_DATE AS createDate,nc.LIST_URL AS listUrl,1 as status FROM SYS_NOTIFICATION_HISTORIC nh ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON nh.CATEGORY_ID = nc.ID  LEFT JOIN SYS_NOTIFICATION_MESSAGE SNM ON nh.MSG_ID = SNM.ID AND SNM.N_LOCALE=:locale WHERE nh.USER_ID = :userId ");
        if (!StringUtils.isEmpty(searchValue)) {
            sql.append(" AND SNM.N_MESSAGE LIKE '%");
            sql.append(searchValue);
            sql.append("%' ");
        }
        sql.append(") notice order by notice.createDate desc");
        return getPageResultBySQL(parameterInput, sql.toString(), map, NotificationResponseVO.class);
    }
    /**
     * 提供给APP调用的消息列表查询方法
     *
     * @param userId         用户ID
     * @param parameterInput 参数
     * @return 分页数据
     */
    public IPageResult<NotificationResponseVO> getAPIUserNotificationNote(String locale,
                                                                          String userId,
                                                                          IParameterInput parameterInput,
                                                                          List<String> roleIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locale", locale);
        map.put("roleIds", roleIds);
        StringBuilder sql = new StringBuilder();
        String searchValue = parameterInput.getParameter().getPara("searchValue", null);
        sql.append("select * from ( ");
        sql.append("SELECT n.id AS id,nc.id as categoryId,nc.name AS categoryName,SNM.N_MESSAGE AS name,n.CREATE_DATE AS createDate,nc.LIST_URL AS listUrl,0 as status FROM SYS_NOTIFICATION n ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON n.CATEGORY_ID = nc.ID ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_MESSAGE SNM ON n.MSG_ID = SNM.ID AND SNM.N_LOCALE=:locale ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_ROLE nr on nc.ID = nr.CATEGORY_ID ");
        sql.append("WHERE n.USER_ID = :userId ");
        // 根据角色进行过滤
        sql.append(" AND ( ");
        sql.append("(nc.ROLE_FILTER = 0 AND nr.id is null) ");
        if (!CollectionUtils.isEmpty(roleIds)) {
            sql.append("OR ");
            sql.append("(nc.ROLE_FILTER = 1 AND nr.ROLE_ID in (:roleIds)) ");
        }
        sql.append(" ) ");
        if (!StringUtils.isEmpty(searchValue)) {
            sql.append(" AND SNM.N_MESSAGE LIKE '%");
            sql.append(searchValue);
            sql.append("%' ");
        }
        sql.append(" union all ");
        sql.append("SELECT nh.id AS id,nc.id as categoryId,nc.name AS categoryName,SNM.N_MESSAGE AS name,nh.END_DATE AS createDate,nc.LIST_URL AS listUrl,1 as status FROM SYS_NOTIFICATION_HISTORIC nh ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON nh.CATEGORY_ID = nc.ID  ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_MESSAGE SNM ON nh.MSG_ID = SNM.ID AND SNM.N_LOCALE=:locale ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_ROLE nr on nc.ID = nr.CATEGORY_ID ");
        sql.append("WHERE nh.USER_ID = :userId ");
        // 根据角色进行过滤
        sql.append(" AND ( ");
        sql.append("(nc.ROLE_FILTER = 0 AND nr.id is null) ");
        if (!CollectionUtils.isEmpty(roleIds)) {
            sql.append("OR ");
            sql.append("(nc.ROLE_FILTER = 1 AND nr.ROLE_ID in (:roleIds)) ");
        }
        sql.append(" ) ");
        if (!StringUtils.isEmpty(searchValue)) {
            sql.append(" AND SNM.N_MESSAGE LIKE '%");
            sql.append(searchValue);
            sql.append("%' ");
        }
        sql.append(") notice order by notice.createDate desc");
        return getPageResultBySQL(parameterInput, sql.toString(), map, NotificationResponseVO.class);
    }
    /**
     * 获取未读消息列表
     * created by chenlw on 2020/04
     *
     * @param userId         用户ID
     * @param parameterInput 参数
     * @param roleIds        roleIds
     * @return 分页数据
     */
    public IPageResult<NotificationResponseVO> getUnreadUserNotificationNote(String locale,
                                                                             String userId,
                                                                             IParameterInput parameterInput,
                                                                             List<String> roleIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locale", locale);
        map.put("roleIds", roleIds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT n.id AS id,nc.id as categoryId,nc.name AS categoryName,SNM.N_MESSAGE AS name,n.CREATE_DATE AS createDate,nc.LIST_URL AS listUrl,0 as status FROM SYS_NOTIFICATION n ");
        sql.append(" LEFT JOIN SYS_NOTIFICATION_CATEGORY nc ON n.CATEGORY_ID = nc.ID  ");
        sql.append(" LEFT JOIN SYS_NOTIFICATION_MESSAGE SNM ON n.MSG_ID = SNM.ID AND SNM.N_LOCALE=:locale ");
        //sql.append(" LEFT JOIN SYS_NOTIFICATION_ROLE nr on nc.ID = nr.CATEGORY_ID ");
        sql.append(" WHERE n.USER_ID = :userId  ");
        // 根据角色进行过滤
        /*sql.append(" AND ( ");
        sql.append("(nc.ROLE_FILTER = 0 AND nr.id is null) ");
        if (!CollectionUtils.isEmpty(roleIds)) {
            sql.append("OR ");
            sql.append("(nc.ROLE_FILTER = 1 AND nr.ROLE_ID in (:roleIds)) ");
        }
        sql.append(" ) ");*/
        String searchValue = parameterInput.getParameter().getPara("searchValue", null);
        if (!StringUtils.isEmpty(searchValue)) {
            sql.append(" AND SNM.N_MESSAGE LIKE '%");
            sql.append(searchValue);
            sql.append("%' ");
        }
        sql.append("  ORDER BY createDate DESC");
        return getPageResultBySQL(parameterInput, sql.toString(), map, NotificationResponseVO.class);
    }
    /**
     * 获取已读消息列表
     *
     * @param userId         用户ID
     * @param parameterInput 参数
     * @param roleIds        roleIds
     * @return 分页数据
     */
    public IPageResult<NotificationResponseVO> getHaveReadUserNotificationNote(String locale,
                                                                               String userId,
                                                                               IParameterInput parameterInput,
                                                                               List<String> roleIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locale", locale);
        map.put("roleIds", roleIds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nh.id AS id,nc.id as categoryId,nc.name AS categoryName,SNM.N_MESSAGE AS name,nh.CREATE_DATE AS createDate,nc.LIST_URL AS listUrl,1 as status FROM SYS_NOTIFICATION_HISTORIC nh ");
        sql.append("  JOIN SYS_NOTIFICATION_CATEGORY nc ON nh.CATEGORY_ID = nc.ID  ");
        sql.append("  JOIN SYS_NOTIFICATION_MESSAGE SNM ON nh.MSG_ID = SNM.ID AND SNM.N_LOCALE=:locale ");
        //sql.append(" LEFT JOIN SYS_NOTIFICATION_ROLE nr on nc.ID = nr.CATEGORY_ID ");
        sql.append(" WHERE nh.USER_ID = :userId ");
        // 根据角色进行过滤
        /*sql.append(" AND ( ");
        sql.append("(nc.ROLE_FILTER = 0 AND nr.id is null) ");
        if (!CollectionUtils.isEmpty(roleIds)) {
            sql.append("OR ");
            sql.append("(nc.ROLE_FILTER = 1 AND nr.ROLE_ID in (:roleIds)) ");
        }
        sql.append(" ) ");*/
        String searchValue = parameterInput.getParameter().getPara("searchValue", null);
        if (!StringUtils.isEmpty(searchValue)) {
            sql.append(" AND SNM.N_MESSAGE LIKE '%");
            sql.append(searchValue);
            sql.append("%' ");
        }
        sql.append(" ORDER BY createDate DESC");
        return getPageResultBySQL(parameterInput, sql.toString(), map, NotificationResponseVO.class);
    }
//    public IPageResult<NotificationResponseVO> getUserNotificationNote(String locale, String userId, IParameterInput parameterInput) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("userId", userId);
//        map.put("locale", locale);
//        IParameter parameter = parameterInput.getParameter();
//        String status = parameter.getPara("status");
//
//        StringBuilder sql = new StringBuilder();
//        sql.append("select n.id as id,nc.name as categoryName,nc.id as categoryId,bt_nvl_char(nm.N_MESSAGE,n.name) as name,n.CREATE_DATE as createDate,nc.LIST_URL as listUrl ");
//        if ("historic".equals(status)) {
//            sql.append("from SYS_NOTIFICATION_HISTORIC n ");
//        } else {
//            sql.append("from SYS_NOTIFICATION n ");
//        }
//        sql.append("LEFT JOIN SYS_NOTIFICATION_MESSAGE nm on n.MSG_ID = nm.ID and nm.N_LOCALE=:locale ");
//        sql.append("left join SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
//
//        sql.append("where n.USER_ID = :userId ");
//
//        // type:1,通知；2，待处理
//        Integer type = parameter.get("type");
//        if (type.equals(2)) {
//            sql.append(" and n.type=2");
//        } else {
//            sql.append(" and n.type=1");
//        }
//
//        sql.append(" order by createDate desc");
//        return getPageResultBySQL(parameterInput, sql.toString(), map, NotificationResponseVO.class);
//    }
    public IPageResult<NotificationResponseVO> getUserNotificationNote(String locale,
                                                                       String userId,
                                                                       IParameterInput parameterInput,
                                                                       List<String> corpIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locale", locale);
        IParameter parameter = parameterInput.getParameter();
        String status = parameter.getPara("status");
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,nc.name as categoryName,nc.id as categoryId,bt_nvl_char(nm.N_MESSAGE,n.name) as name,n.CREATE_DATE as createDate,nc.menu_id as menuId,nc.LIST_URL as listUrl ");
        sql.append(",n.business_Id as businessId, nc.tag ");
        if ("historic".equals(status)) {
            sql.append("from SYS_NOTIFICATION_HISTORIC n ");
        } else {
            sql.append("from SYS_NOTIFICATION n ");
        }
        sql.append("LEFT JOIN SYS_NOTIFICATION_MESSAGE nm on n.MSG_ID = nm.ID and nm.N_LOCALE=:locale ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        sql.append("where n.USER_ID = :userId ");
        // type:1,通知；2，待处理
        Integer type = parameter.get("type");
        if (type.equals(2)) {
            sql.append(" and n.type=2 ");
        } else {
            sql.append(" and n.type=1 ");
        }
        if (CollectionUtil.isNotEmpty(corpIds)) {
            //通知直接发给个人 未设置单位ID
            sql.append(buildCorpCond(corpIds));
        }
        sql.append(" order by createDate desc");
        return getPageResultBySQL(parameterInput, sql.toString(), map, NotificationResponseVO.class);
    }

    private String buildCorpCond(List<String> corpIds) {
        return " and (" + SqlUtil.getInSql(corpIds, "n.corp_id", true) + " or n.corp_id = '' or n.corp_id is null ) ";
    }

    /**
     * 获取未读消息列表
     *
     * @param userId 用户ID
     * @return 未读消息列表
     */
    public List<Notification> listByUserId(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SN.ID, SN.CATEGORY_ID, SN.USER_ID, SN.CORP_ID, SN.NAME, SN.READ_FLAG, SN.TYPE, SN.BUSINESS_ID, SN.CREATE_DATE, SN.MSG_ID AS MESSAGE_ID FROM SYS_NOTIFICATION SN WHERE USER_ID = :userId ");
        return listBySQLAliasToBean(sql.toString(), params, Notification.class);
    }
    /**
     * 获取未读消息内容的ID列表
     *
     * @param userId 用户ID
     * @return 未读消息内容的ID列表
     */
    public List<String> listMsgIdByUserId(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SN.MSG_ID FROM SYS_NOTIFICATION SN WHERE USER_ID = :userId ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        return sqlQuery.list();
    }
    public int deleteByUserId(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE FROM SYS_NOTIFICATION WHERE USER_ID = :userId ");
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 批量删除消息内容
     *
     * @param ids       消息内容ID列表
     * @param batchSize 每次批量删除的个数。 使用in子句的参数个数不能超过1000
     */
    public void deleteBatchByIds(List<String> ids, int batchSize) {
        List<List<String>> batchIdsList = Lists.partition(ids, batchSize);
        for (List<String> deleteIds : batchIdsList) {
            delete(deleteIds);
        }
    }
    /**
     * 获取重点关注的信息
     *
     * @param userId
     * @param roleIds
     * @return
     */
    public List<NotificationResponseVO> listStarTarget(String userId, List<String> roleIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleIds", roleIds);
        map.put("starTarget", CommonConstant.CHECKBOX_ON);
        StringBuilder sql = new StringBuilder();
        sql.append("select n.category_Id as categoryId,nc.name as categoryName,nc.LIST_URL as listUrl,nc.type,nc.tag,count(*) as startTargetCount ");
        sql.append("from SYS_NOTIFICATION n ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        sql.append("where n.USER_ID = :userId ");
        sql.append(" and nc.star_Target=:starTarget");
        sql.append(" group by n.category_Id,nc.name,nc.LIST_URL,nc.type,nc.tag");
        return listBySQLAliasToBean(sql.toString(), map, NotificationResponseVO.class);
    }
    /**
     * 获取到期的信息
     *
     * @param parameterInput
     * @param userId
     * @return
     */
    public List<NotificationResponseVO> listExpire(IParameterInput parameterInput, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append("select bt_to_char(n.create_date, 'yyyy-MM-dd') as expireDate, count(*) as expireCount");
        sql.append(" from SYS_NOTIFICATION n ");
        sql.append(" LEFT JOIN SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        sql.append(" where n.USER_ID = :userId ");
        sql.append(" and nc.name like '%到期%'");
        IParameter parameter = parameterInput.getParameter();
        String beginDate = parameter.getPara("beginDate");
        String endDate = parameter.getPara("endDate");
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and n.create_date >= :beginDate");
            map.put("beginDate", DateUtils.parse(beginDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and n.create_date <= :endDate");
            map.put("endDate", DateUtils.parse(endDate, DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        sql.append(" group by bt_to_char(n.create_date, 'yyyy-MM-dd')");
        return listBySQLAliasToBean(sql.toString(), map, NotificationResponseVO.class);
    }
    /**
     * 获取到期的信息
     *
     * @param userId
     * @param parameterInput
     * @return
     */
    public List<NotificationResponseVO> listExpireByCreateDate(String userId, IParameterInput parameterInput) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append("select n.category_Id as categoryId,nc.name as categoryName,nc.LIST_URL as listUrl,nc.type,nc.tag,count(*) as expireCount ");
        sql.append("from SYS_NOTIFICATION n ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        sql.append("where n.USER_ID = :userId ");
        sql.append(" and nc.name like '%到期%'");
        IParameter parameter = parameterInput.getParameter();
        String searchDate = parameter.getPara("searchDate");
        if (StringUtils.isNotBlank(searchDate)) {
            sql.append(" and bt_to_char(n.create_date, 'yyyy-MM-dd') = :searchDate");
            map.put("searchDate", searchDate);
        }
        sql.append(" group by n.category_Id,nc.name,nc.LIST_URL,nc.type,nc.tag");
        return listBySQLAliasToBean(sql.toString(), map, NotificationResponseVO.class);
    }
    /**
     * 根据截止日期删除消息
     *
     * @param endDate
     * @return
     */
    public int deleteByEndDate(Date endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("endDate", endDate);
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE FROM SYS_NOTIFICATION WHERE CREATE_DATE <= :endDate ");
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 获取相关资源，相关业务的消息记录
     *
     * @param businessId
     * @param tag
     * @return
     */
    public List<NotificationResponseVO> listByBusinessIdAndTag(String businessId, String tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("businessId", businessId);
        map.put("tag", tag);
        StringBuilder sql = new StringBuilder();
        sql.append("select n.category_Id as categoryId,nc.name as categoryName,nc.LIST_URL as listUrl,nc.type,nc.tag ");
        sql.append("from SYS_NOTIFICATION n ");
        sql.append("LEFT JOIN SYS_NOTIFICATION_CATEGORY nc on n.CATEGORY_ID = nc.ID ");
        sql.append("where n.BUSINESS_ID = :businessId ");
        sql.append(" and nc.TAG = :tag ");
        return listBySQLAliasToBean(sql.toString(), map, NotificationResponseVO.class);
    }
}
