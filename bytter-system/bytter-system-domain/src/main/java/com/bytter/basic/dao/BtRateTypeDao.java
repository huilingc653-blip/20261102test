package com.bytter.basic.dao;
import com.bytter.basic.entity.BtRateType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.ListUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 汇率类型
 * @date 2020-1-19 11:14:22
 */
@Repository
public class BtRateTypeDao extends BaseDao<BtRateType, String> {
    /**
     * 获取跟其他业务的关联数量
     *
     * @param id
     * @return
     */
    public long checkAssociated(String id) {
        long check = 0;
        Map<String, String> map = new HashMap<>();
        map.put("type_id", id);
        check = check + addSum("FC_EXCRATE_ADJUST", map);
        return check;
    }
    /**
     * 获取关联数量
     *
     * @param table 表名
     * @param map   列名/值
     * @return
     */
    private long addSum(String table, Map<String, String> map) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(id) from ");
        sql.append(table);
        sql.append(" WHERE ");
        for (String key : map.keySet()) {
            sql.append(key).append("=").append("'").append(map.get(key)).append("'").append(" OR ");
        }
        String substring = sql.toString().substring(0, sql.toString().length() - 3);
        SQLQuery query = getSession().createSQLQuery(substring);
        List list = query.list();
        //数据库兼容
        if (ListUtils.isNotNullOrEmpty(list)) {
            Object value = list.get(0);
            if (value instanceof BigDecimal) {
                return ((BigDecimal) value).intValue();
            }
            if (value instanceof BigInteger) {
                return ((BigInteger) value).intValue();
            }
        }
        return 0L;
    }
}
