package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolQueryExc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 票据池指令查询业务Dao
 * @Description: 票据池指令查询业务数据操作类
 * @author: zhenghj
 * @date: 2020-11-12
 */
@Repository
public class AbPoolQueryExcDao extends BaseDao<AbPoolQueryExc, String> {
    /**
     * 根据ids批量修改票据池指令数据
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AbPoolQueryExc "
                + " set "
                + "updateTime = :updateTime ");
        if (params.containsKey("status")) {
            hql.append(",status = :status ");
        }
        hql.append(" where ").append(SqlUtil.getInSqlByParams("ids", "id", params));
        return this.batchExecute(hql.toString(), params);
    }
}
