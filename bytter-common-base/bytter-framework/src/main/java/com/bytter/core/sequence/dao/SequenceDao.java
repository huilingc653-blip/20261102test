package com.bytter.core.sequence.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.sequence.entity.MultiTable;
import com.bytter.core.sequence.entity.Sequence;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 流水号DAO
 *
 * @author zhoul
 * @date 2017/5/31
 */
@Repository
public class SequenceDao extends BaseDao<Sequence, String> implements Serializable {
    private static final long serialVersionUID = -3972585823570078271L;
    public String getSequence(MultiTable[] multiTables, String businessPrefix) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> sqlMap = new HashMap<>();
        sql.append("select max(currentSequence) as currentSequence from ( ");
        for (int i = 0, length = multiTables.length; i < length; i++) {
            if (i > 0) {
                sql.append(" union ");
            }
            MultiTable multiTable = multiTables[i];
            sql.append("select max(").append(multiTable.getFieldName()).
                    append(") as currentSequence from ").append(multiTable.getTableName());
            if (StringUtils.isNotBlank(businessPrefix)) {
                sql.append(" where ").append(multiTable.getFieldName()).append(" like :businessPrefix");
                sqlMap.put("businessPrefix", businessPrefix + "%");
            }
        }
        sql.append(" ) seq ");
        SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
        if (!CollectionUtils.isEmpty(sqlMap)) {
            sqlQuery.setProperties(sqlMap);
        }
        Object sqlValue = sqlQuery.uniqueResult();
        return sqlValue == null ? "" : sqlValue.toString();
    }
    /**
     * 获取业务表中以业务作为前缀的最大流水号
     *
     * @param tableName      表名
     * @param fieldName      字段名
     * @param businessPrefix 业务前缀
     * @return
     */
    public String getSequence(String tableName, String fieldName, String businessPrefix) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(").append(fieldName).append(") as currentSequence from ").append(tableName);
        Map<String, String> sqlMap = new HashMap<>();
        if (StringUtils.isNotBlank(businessPrefix)) {
            sql.append(" where ").append(fieldName).append(" like :businessPrefix");
            sqlMap.put("businessPrefix", businessPrefix + "%");
        }
        System.out.println("Sequence sql = " + sql.toString() + " , businessPrefix = " + businessPrefix);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), sqlMap);
        Object sqlValue = sqlQuery.uniqueResult();
        return sqlValue == null ? "" : sqlValue.toString();
    }
    /**
     * 获取流水号序列
     *
     * @param tableName
     * @param fieldName
     * @return
     */
    public String getSequenceByTableNameAndFieldName(String tableName, String fieldName, String specialCode) {
        Map<String, String> map = new HashMap<>();
        map.put("fieldName", fieldName);
        map.put("tableName", tableName);
        StringBuilder hql = new StringBuilder("select s.currentSequence from Sequence s where s.tableName=:tableName and s.fieldName=:fieldName");
        if (StringUtils.isNotBlank(specialCode)) {
            map.put("specialCode", specialCode);
            hql.append(" and s.specialCode=:specialCode");
        }
        Query query = createQuery(hql.toString(), map);
        query.setLockOptions(LockOptions.UPGRADE);
        Object values = query.uniqueResult();
        return values == null ? "" : values.toString();
    }
    public String getSequenceFromTable(String tableName, String fieldName, String prefix, String specialCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(").append(fieldName).append(") as currentSequence from ").append(tableName).append(" where 1=1 ");
        Map<String, String> sqlMap = new HashMap<>();
        if (StringUtils.isNotBlank(prefix)) {
            sql.append(" and ").append(fieldName).append(" like :prefix");
            sqlMap.put("prefix", prefix + "%");
        }
        if (StringUtils.isNotBlank(specialCode)) {
            sql.append(" and ").append(fieldName).append(" like :specialCode");
            //特殊编号目前只放到最前面，没有出现后面的情况
//        sqlMap.put("specialCode", "%" + specialCode + "%");
            sqlMap.put("specialCode", specialCode + "%");
        }
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), sqlMap);
        Object sqlValue = sqlQuery.uniqueResult();
        return sqlValue == null ? "" : sqlValue.toString();
    }
    public boolean checkIsNewSequence(String tableName, String fieldName, String specialCode) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tableName", tableName));
        criteria.add(Restrictions.eq("fieldName", fieldName));
        criteria.setProjection(Projections.rowCount());
        if (StringUtils.isNotBlank(specialCode)) {
            criteria.add(Restrictions.eq("specialCode", specialCode));
        }
        Long count = (Long) criteria.uniqueResult();
        return count == 0;
    }
    /**
     * 更新水流号序列
     *
     * @param tableName
     * @param fieldName
     * @param currentSequence
     * @param specialCode
     * @param isNew
     */
    public void updateCurrentSequence(String tableName, String fieldName, String currentSequence, String specialCode, Boolean isNew) {
        //再次检查是否是新的流水号 2017-12-01 15:09 by zhoul
        if (isNew) {
            isNew = checkIsNewSequence(tableName, fieldName, specialCode);
        }
        //如果是新的，则往数据库插入
        if (isNew) {
            // todo:似乎已经去掉了这种模式
            Sequence sequence = new Sequence();
            sequence.setFieldName(fieldName);
            sequence.setTableName(tableName);
            sequence.setCurrentSequence(currentSequence);
            sequence.setSpecialCode(specialCode);
            save(sequence);
        } else {
            //不是新的更新数据库
            Map<String, Object> map = new HashMap<>();
            map.put("fieldName", fieldName);
            map.put("tableName", tableName);
            map.put("currentSequence", currentSequence);
            StringBuilder hql = new StringBuilder("update Sequence set currentSequence=:currentSequence where tableName=:tableName and fieldName=:fieldName");
            if (StringUtils.isNotBlank(specialCode)) {
                map.put("specialCode", specialCode);
                hql.append(" and specialCode=:specialCode");
            }
            Query query = createQuery(hql.toString(), map);
            query.executeUpdate();
        }
    }
}
