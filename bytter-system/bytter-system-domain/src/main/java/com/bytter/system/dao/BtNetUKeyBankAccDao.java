package com.bytter.system.dao;
import com.bytter.system.entity.BtNetUKeyBankAcc;
import com.bytter.system.vo.BtNetUKeyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author qinl
 * @version 1.0
 * @Title: Dao
 * @date 2020-04-16
 */
@Repository
public class BtNetUKeyBankAccDao extends BaseDao<BtNetUKeyBankAcc, String> {
    /**
     * 获取关联数量
     * @param table 表名
     * @param map 列名/值
     * @return
     */
    public long addSum(String table, Map<String,String> map){
        StringBuilder sql = new StringBuilder();
        sql.append("select count(id) from ");
        sql.append(table);
        sql.append(" WHERE ");
        for(String key:map.keySet()){
            sql.append(key).append("=").append("'").append( map.get(key)).append("'").append(" OR ");
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
    public List<BtNetUKeyBankAcc> selectListByUKeyId(String uKeyId){
        if (StringUtils.isBlank(uKeyId)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM bt_net_ukey_bankacc ");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND ukey_id = '").append(uKeyId).append("'");
        return this.listBySQLAliasToBean(sql.toString(), null, BtNetUKeyBankAcc.class);
    }
    public List<BtNetUKeyBankAcc> selectListByUKeyCode(String uKeyCode){
        if (StringUtils.isBlank(uKeyCode)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id,a.UKEY_ID,a.BANK_ACC_ID ");
        sql.append(" FROM bt_net_ukey_bankacc a ");
        sql.append(" LEFT JOIN BT_NET_UKEY b ON a.UKEY_ID = b.id ");
        sql.append(" WHERE 1 = 1");
        sql.append(" AND b.UKEY_NO = '").append(uKeyCode).append("'");
        return this.listBySQLAliasToBean(sql.toString(), null, BtNetUKeyBankAcc.class);
    }
}
