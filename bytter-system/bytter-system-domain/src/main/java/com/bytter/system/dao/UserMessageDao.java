package com.bytter.system.dao;
import com.bytter.system.entity.UserMessage;
import com.bytter.core.base.AbstractDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.*;
/**
 * 用户消息DAO，站内信
 *
 * @author zhoul
 * @since 2020-05-14
 */
@Component
public class UserMessageDao extends AbstractDao implements InitializingBean {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public UserMessage get(String id) {
        Map<String, Object> queryMap = new HashMap<>(1);
        queryMap.put("id", id);
        String sql = "select ID as id,TITLE as title,CONTEXT as context,STATUS as status,RECEIVER as receiver,CREATE_BY as createBy,CREATE_DATE as createDate,UPDATE_BY as updateBy,UPDATE_DATE as updateDate from SYS_USER_MESSAGE where ID=:id";
        UserMessage userMessage = namedParameterJdbcTemplate.queryForObject(sql, queryMap, new BeanPropertyRowMapper<>(UserMessage.class));
        //inject(userMessage);
        return userMessage;
    }
    public Page<UserMessage> list(Pageable pageable, String receiver, String status, boolean isMySelf) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> queryMap = new HashMap<>();
        sql.append("select ID as id,TITLE as title,CONTEXT as context,STATUS as status,RECEIVER as receiver,");
        sql.append("CREATE_BY as createBy,CREATE_DATE as createDate,UPDATE_BY as updateBy,UPDATE_DATE as updateDate from SYS_USER_MESSAGE where 1=1");
        //我收到的消息
        sql.append(" AND RECEIVER=:receiver");
        queryMap.put("receiver", receiver);
        queryMap.put("createBy", "createBy");
        if (!isMySelf) {
            sql.append(" AND CREATE_BY != :receiver ");
        } else {
            //我发送的消息
            sql.append(" AND CREATE_BY = :receiver ");
        }
        if ("0".equals(status) || "1".equals(status)) {
            queryMap.put("status", status);
            sql.append(" AND STATUS=:status ");
        }
        String countSql = "select count(*) from(" + sql.toString() + " ) total";
        Integer total = namedParameterJdbcTemplate.queryForObject(countSql, queryMap, Integer.class);
        if (total > 0) {
            sql.append(" order by createDate desc");
            List<UserMessage> userMessages = listBySQLAliasToBean(sql.toString(), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize(), queryMap, UserMessage.class);
            return new PageImpl<>(userMessages, pageable, total);
        }
        return new PageImpl<>(new ArrayList<UserMessage>(), pageable, total);
    }
    /**
     * 新增用户消息
     *
     * @param userMessage 用户消息对象
     */
    public void insert(UserMessage userMessage, List<String> userIds) {
        userIds.remove(userMessage.getCreateBy());
        userMessage.setCreateDate(new Date());
        Map<String, Object> map = BeanUtils.beanToMap(userMessage);
        Map<String, Object>[] maps = new Map[userIds.size() + 1];
        for (int i = 0; i < userIds.size(); i++) {
            HashMap<String, Object> objectObjectHashMap = new HashMap<>(map);
            objectObjectHashMap.put("id", StringUtils.getUUId());
            objectObjectHashMap.put("receiver", userIds.get(i));
            objectObjectHashMap.put("status", 0);
            maps[i] = objectObjectHashMap;
        }
        Map<String, Object> senderMessage = new HashMap<>(map);
        senderMessage.put("id", StringUtils.getUUId());
        senderMessage.put("receiver", userMessage.getCreateBy());
        senderMessage.put("status", 0);
        maps[userIds.size()] = senderMessage;
        String sql = "insert into SYS_USER_MESSAGE (ID,TITLE,CONTEXT,STATUS,RECEIVER,CREATE_BY,CREATE_DATE) " +
                "values(:id,:title,:context,:status,:receiver,:createBy,:createDate)";
        namedParameterJdbcTemplate.batchUpdate(sql, maps);
    }
    /**
     * 标记已读
     *
     * @param ids      消息主键
     * @param updateBy 更新人
     */
    public void tagRead(List<String> ids, String updateBy) {
        Map<String, Object>[] maps = new Map[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            Map<String, Object> map = new HashMap<>(4);
            map.put("id", ids.get(i));
            map.put("receiver", updateBy);
            map.put("updateBy", updateBy);
            map.put("updateDate", new Date());
            maps[i] = map;
        }
        namedParameterJdbcTemplate.batchUpdate("update SYS_USER_MESSAGE set STATUS=1,UPDATE_BY=:updateBy,UPDATE_DATE=:updateDate where id=:id AND RECEIVER=:receiver", maps);
    }
    /**
     * 删除用户消息
     *
     * @param ids      消息集合
     * @param updateBy 操作人，区分是否是本地人操作，确保数据权限
     */
    public void delete(List<String> ids, String updateBy) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("ids", ids);
        map.put("receiver", updateBy);
        namedParameterJdbcTemplate.update("delete from SYS_USER_MESSAGE where ID in(:ids) and RECEIVER=:receiver", map);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
