package com.bytter.system.dao;
import com.bytter.system.entity.NotificationHistoric;
import com.bytter.core.base.BaseDao;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class NotificationHistoricDao extends BaseDao<NotificationHistoric, String> {
    /**
     * 获取消息列表
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<NotificationHistoric> listByUserId(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SNH.ID, SNH.CATEGORY_ID, SNH.USER_ID, SNH.CORP_ID, SNH.NAME, SNH.DURATION, SNH.READ_FLAG, SNH.TYPE, SNH.BUSINESS_ID, SNH.END_DATE, SNH.CREATE_DATE, SNH.MSG_ID AS MESSAGE_ID FROM SYS_NOTIFICATION_HISTORIC SNH WHERE USER_ID = :userId ");
        return listBySQLAliasToBean(sql.toString(), params, NotificationHistoric.class);
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
        sql.append(" SELECT SNH.MSG_ID FROM SYS_NOTIFICATION_HISTORIC SNH WHERE USER_ID = :userId ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        return sqlQuery.list();
    }
    public int deleteByUserId(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE FROM SYS_NOTIFICATION_HISTORIC WHERE USER_ID = :userId ");
        return createSQLQuery(sql.toString(), params).executeUpdate();
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
        sql.append(" DELETE FROM SYS_NOTIFICATION_HISTORIC WHERE CREATE_DATE <= :endDate ");
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
