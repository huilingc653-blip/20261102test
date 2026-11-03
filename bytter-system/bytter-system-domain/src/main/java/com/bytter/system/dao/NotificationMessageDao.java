package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.NotificationMessage;
import com.bytter.system.vo.NotificationMessageVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/17.
 */
@Repository
public class NotificationMessageDao extends BaseDao<NotificationMessage, String> {
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
     * 删除消息内容
     *
     * @param msgIdList 消息分类ID
     */
    public void deleteMsgByIdList(List<String> msgIdList) {
        if (EmptyUtils.isEmpty(msgIdList)) {
            return;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from sys_notification_message where ");
        sql.append(SqlUtil.getInSql(msgIdList, "id", true));
        Map<String, Object> map = new HashMap<>();
        map.put("msgIdList", msgIdList);
        createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 消息查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.category_id as categoryId,nc.name as categoryName,nm.n_message as message,n.create_date as createDate");
        sql.append(" from sys_notification n left join sys_notification_category nc on n.category_id = nc.id");
        sql.append(" left join sys_notification_message nm on n.msg_id = nm.id where 1=1");
        sql.append(joinSql);
        String categoryId = (String) dataTablesInput.getFormdata().get("categoryId");
        if (StringUtils.isNotBlank(categoryId)) {
            dataTablesInput.getFormdata().put("eqs_categoryId", categoryId);
        }
        //申请日期
        String gedCreateDate = (String) dataTablesInput.getFormdata().get("ged_createDate");
        String ledCreateDate = (String) dataTablesInput.getFormdata().get("led_createDate");
        if (StringUtils.isNotEmpty(gedCreateDate)) {
            gedCreateDate = gedCreateDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_createDate", gedCreateDate);
        }
        if (StringUtils.isNotEmpty(ledCreateDate)) {
            ledCreateDate = ledCreateDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_createDate", ledCreateDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NotificationMessageVO.class, "b", false);
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
        sql.append(" DELETE FROM sys_notification_message where id in (select msg_id from SYS_NOTIFICATION_HISTORIC WHERE CREATE_DATE <= :endDate)");
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
