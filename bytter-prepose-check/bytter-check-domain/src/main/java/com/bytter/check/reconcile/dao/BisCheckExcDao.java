package com.bytter.check.reconcile.dao;

import com.bytter.check.reconcile.entity.BisCheckExc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.Map;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Dao
 * @Description: 银企对账指令表Dao
 * @date 2020-10-16
 */
@Repository
public class BisCheckExcDao extends BaseDao<BisCheckExc, String> {
    /**
     * 根据map修改指令表信息
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update BisCheckExc "
                + " set updateDate = :updateDate ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy");
        }
        if (params.containsKey("voucherStat")) {
            hql.append(",voucherStat = :voucherStat ");
        }
        hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params));
        int size = this.batchExecute(hql.toString(), params);
        this.flush();
        return size;
    }
}
