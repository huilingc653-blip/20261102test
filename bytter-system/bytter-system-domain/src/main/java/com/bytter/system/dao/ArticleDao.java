package com.bytter.system.dao;
import com.bytter.system.entity.Article;
import com.bytter.system.vo.ArticleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 政策法规、重要公告 DAO
 *
 * @author zhoul
 * @since 2020-05-14
 */
@Repository
public class ArticleDao extends BaseDao<Article, String> implements InitializingBean {
    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    /**
     * 新增文章与角色关联关系
     *
     * @param articleId 文章
     * @param roleList  角色集合
     */
    public void saveArticleRole(String articleId, List<String> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return;
        }
        Map<String, Object>[] maps = new Map[roleList.size()];
        for (int i = 0; i < roleList.size(); i++) {
            Map<String, Object> map = new HashMap<>(4);
            map.put("id", StringUtils.getUUId());
            map.put("articleId", articleId);
            map.put("roleId", roleList.get(i));
            maps[i] = map;
        }
        String sql = "insert into SYS_ARTICLE_ROLE (ID,ARTICLE_ID,ROLE_ID) values(:id,:articleId,:roleId)";
        namedParameterJdbcTemplate.batchUpdate(sql, maps);
    }
    public void updateStatus(String id, Integer status, String opUser) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("status", status);
        map.put("id", id);
        StringBuilder sql = new StringBuilder();
        sql.append("update SYS_ARTICLE set STATUS=:status");
        //发布
        if (status == 1) {
            map.put("updateBy", opUser);
            map.put("updateDate", new Date());
            sql.append(",UPDATE_BY=:updateBy,UPDATE_DATE=:updateDate ");
        }
        sql.append(" where ID=:id");
        namedParameterJdbcTemplate.update(sql.toString(), map);
    }
    public void cancelTop(String id, String opUser) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("isTop", 0);
        map.put("id", id);
        String sql = "update SYS_ARTICLE set IS_TOP=:isTop where ID=:id";
        namedParameterJdbcTemplate.update(sql, map);
    }
    /**
     * 删除文章与角色关联关系
     *
     * @param id 文章
     */
    public void deleteArticleRole(String id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", id);
        String sql = "delete from SYS_ARTICLE_ROLE where ID =:id";
        namedParameterJdbcTemplate.update(sql, map);
    }
    /**
     * 查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        List<Column> columns = dataTablesInput.getColumns();
        List<Order> orders = dataTablesInput.getOrder();
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        //是否为首页请求
        boolean isHomeRequest = dataTablesInput.getFormdata().get("home") != null;
        StringBuilder sql = new StringBuilder();
        Map<String, Object> queryMap = new HashMap<>();
        sql.append("select DISTINCT a.ID as id,a.TITLE as title,a.ATTACHMENT_ID AS attachmentId,a.IS_TOP AS isTop,a.TYPE AS type,a.RECEIVER_TYPE AS receiverType,a.STATUS AS status,");
        sql.append("a.CREATE_BY AS createBy,a.CREATE_DATE AS createDate,a.UPDATE_BY AS updateBy,a.UPDATE_DATE AS updateDate,u.USERNAME AS createByUserName from sys_article a ");
        if (isHomeRequest) {
            sql.append(" left join sys_article_role ar on a.id=ar.ARTICLE_ID ");
        }
        sql.append(" left join sys_user u on a.create_by=u.id  where 1=1 ");
        String title = (String) formdata.get("title");
        if (StringUtils.isNotBlank(title)) {
            sql.append(" AND a.title like :title");
            queryMap.put("title", "%" + title + "%");
        }
        String username = (String) formdata.get("username");
        if (StringUtils.isNotBlank(username)) {
            sql.append(" AND u.USERNAME like :username");
            queryMap.put("username", "%" + username + "%");
        }
        Object type = formdata.get("type");
        if (type != null) {
            sql.append(" AND a.TYPE=:type");
            queryMap.put("type", type);
        }
        String status = (String) formdata.get("status");
        if (StringUtils.isNotBlank(status)) {
            sql.append(" AND a.STATUS=:status");
            queryMap.put("status", status);
        }
        if (isHomeRequest) {
            Object roleIds = formdata.get("roleIds");
            if (roleIds != null) {
                List<String> roleIdList = (List<String>) roleIds;
                sql.append(" AND (a.RECEIVER_TYPE = 1 OR ( a.RECEIVER_TYPE = 2 AND ar.ROLE_ID in (:roleIdList) ))");
                queryMap.put("roleIdList", roleIdList);
            }
        }
        //复核界面，过滤发布者和审核者是同一个人
        String review = (String) formdata.get("review");
        String currentUserId = dataTablesInput.getCurrentUserId();
        if (StringUtils.isNotBlank(review) && "1".equals(review)) {
            sql.append(" AND a.STATUS=2 AND a.CREATE_BY!=:userId");
            queryMap.put("userId", currentUserId);
        } else {
            String list = (String) formdata.get("list");
            if (StringUtils.isBlank(list) || !"1".equals(list)) {
                //首页接口调用，返回已发布
                sql.append(" AND a.STATUS=1");
            } else {
                sql.append(" AND a.CREATE_BY=:userId");
                queryMap.put("userId", currentUserId);
            }
        }
        String startTime = (String) formdata.get("startTime");
        String endTime = (String) formdata.get("endTime");
        if (StringUtils.isNotBlank(startTime)) {
            sql.append(" AND a.UPDATE_DATE>:startTime");
            queryMap.put("startTime", DateUtils.str2Date(startTime, DateUtils.date_sdf()));
        }
        if (StringUtils.isNotBlank(endTime)) {
            sql.append(" AND a.UPDATE_DATE<:endTime");
            queryMap.put("endTime", DateUtils.str2Date(endTime + " 23:59:59", DateUtils.datetimeFormat()));
        }
        String countSql = "select count(*) from(" + sql.toString() + " ) total";
        Integer total = namedParameterJdbcTemplate.queryForObject(countSql, queryMap, Integer.class);
        if (total > 0) {
            if (orders.size() == 1) {
                Order order = orders.get(0);
                Column column = columns.get(order.getColumn());
                String name = column.getName();
                String columnName;
                if ("isTop".equals(name)) {
                    columnName = "IS_TOP";
                } else if ("updateDate".equals(name)) {
                    columnName = "UPDATE_DATE";
                } else {
                    columnName = "CREATE_DATE";
                }
                sql.append(" order by a.").append(columnName).append(" ").append(order.getDir());
            } else {
                if (isHomeRequest) {
                    sql.append(" order by a.IS_TOP desc,a.CREATE_DATE DESC");
                } else {
                    sql.append(" order by a.CREATE_DATE desc");
                }
            }
            List<ArticleVO> articleList = listBySQLAliasToBean(sql.toString(), dataTablesInput.getStart(), dataTablesInput.getLength(), queryMap, ArticleVO.class);
            return new DataTablesOutput(dataTablesInput.getDraw(), articleList, total, total);
        }
        return new DataTablesOutput(dataTablesInput.getDraw(), null, total, total);
    }
    /**
     * 列出6条重要通知和政策法规
     *
     * @param type    1、重要通知，2政策法规
     * @param roleIds 角色集合
     * @return 文章VO对象
     */
    public List<ArticleVO> getArticleByType(Integer type, List<String> roleIds) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("type", type);
        map.put("roleIds", roleIds);
        String sql = "SELECT DISTINCT a.ID as id,a.TITLE as title,a.ATTACHMENT_ID AS attachmentId,a.IS_TOP AS isTop," +
                " a.TYPE AS type,a.RECEIVER_TYPE AS receiverType,a.STATUS AS status,a.CREATE_DATE as createDate,a.UPDATE_DATE as updateDate  " +
                " FROM sys_article a LEFT JOIN SYS_ARTICLE_ROLE ar ON a.ID = ar.ARTICLE_ID WHERE TYPE=:type " +
                " AND a.STATUS=1 AND (RECEIVER_TYPE = 1 OR ( RECEIVER_TYPE = 2 AND ar.ROLE_ID in :roleIds )) " +
                " order by a.IS_TOP DESC,a.CREATE_DATE DESC";
        return listBySQLAliasToBean(sql, 0, 6, map, ArticleVO.class);
    }
    /**
     * 根据文章ID查找角色ID
     *
     * @param articleId
     */
    public List<String> getArticleRoles(String articleId) {
        String sql = "select ROLE_ID from SYS_ARTICLE_ROLE where ARTICLE_ID=?";
        SQLQuery sqlQuery = createSQLQuery(sql, articleId);
        sqlQuery.addScalar("ROLE_ID", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    /**
     * 获取公告信息的统计数量
     *
     * @param userId
     * @return
     */
    public Integer getArticleCountByUserId(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append("select count(distinct a.id) from sys_article a ");
        sql.append(" inner join sys_article_user r on a.id = r.article_id");
        sql.append(" where a.status = 1 and r.user_id = :userId ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        BigDecimal count = (BigDecimal) sqlQuery.uniqueResult();
        //增加收到的消息
        StringBuilder sqlMessge = new StringBuilder();
        sqlMessge.append("select count(a.id) from sys_user_message a ");
        sqlMessge.append(" where a.status = 0 and a.receiver = :userId ");
        //排除自己发送的消息
        sqlMessge.append(" and a.receiver <> a.create_by");
        sqlQuery = createSQLQuery(sqlMessge.toString(), map);
        BigDecimal countMsg = (BigDecimal) sqlQuery.uniqueResult();
        return count.add(countMsg).intValue();
    }
    /**
     * 新增文章与用户关联关系
     *
     * @param articleId 文章
     * @param userList  用户集合
     */
    public void saveArticleUser(String articleId, List<String> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        Map<String, Object>[] maps = new Map[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            Map<String, Object> map = new HashMap<>(4);
            map.put("id", StringUtils.getUUId());
            map.put("articleId", articleId);
            map.put("userId", userList.get(i));
            maps[i] = map;
        }
        String sql = "insert into SYS_ARTICLE_USER (ID,ARTICLE_ID,USER_ID) values(:id,:articleId,:userId)";
        namedParameterJdbcTemplate.batchUpdate(sql, maps);
    }
    /**
     * 根据文章ID查找角色ID
     *
     * @param articleId
     */
    public List<String> getArticleUsers(String articleId, String userId) {
        String sql = "select ID from SYS_ARTICLE_USER where ARTICLE_ID=? and USER_ID = ? ";
        SQLQuery sqlQuery = createSQLQuery(sql, articleId, userId);
        sqlQuery.addScalar("ID", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }
    /**
     * 删除文章与用户关联关系
     *
     * @param articleId 文章
     * @param userId    用户ID
     */
    public int deleteArticleUser(String articleId, String userId) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("articleId", articleId);
        map.put("userId", userId);
        String sql = "delete from SYS_ARTICLE_USER where article_Id = :articleId and user_id = :userId ";
        return namedParameterJdbcTemplate.update(sql, map);
    }
    /**
     * 转移文章与用户关联关系
     *
     * @param articleId 文章
     * @param userList  用户集合
     */
    public void moveArticleUser(String articleId, List<String> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        Map<String, Object>[] maps = new Map[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            Map<String, Object> map = new HashMap<>(4);
            map.put("id", StringUtils.getUUId());
            map.put("articleId", articleId);
            map.put("userId", userList.get(i));
            maps[i] = map;
        }
        String sql = "insert into SYS_ARTICLE_USER_HIS (ID,ARTICLE_ID,USER_ID) values(:id,:articleId,:userId)";
        namedParameterJdbcTemplate.batchUpdate(sql, maps);
    }
    /**
     * 查询当前用户未读的消息
     *
     * @param type   1、重要通知，2政策法规
     * @param userId 用户ID
     * @return 文章VO对象
     */
    public List<String> getArticleByUserType(Integer type, String userId) {
        String sql = "SELECT DISTINCT a.ID as id FROM sys_article a inner JOIN SYS_ARTICLE_USER au " +
                " ON a.ID = au.ARTICLE_ID WHERE TYPE = ? AND a.STATUS=1 " +
                "AND au.user_id = ? ";
        SQLQuery sqlQuery = createSQLQuery(sql, type, userId);
        sqlQuery.addScalar("ID", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }
}
