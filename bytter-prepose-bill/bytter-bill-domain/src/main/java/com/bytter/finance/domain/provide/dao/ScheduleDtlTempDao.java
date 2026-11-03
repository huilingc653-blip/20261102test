package com.bytter.finance.domain.provide.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.finance.domain.provide.entity.ScheduleDtlTemp;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangyl
 * @version 1.0
 * @Description: 还款计划明细暂存表
 * @date 017-08-01 09:58:43
 */
@Repository
public class ScheduleDtlTempDao extends BaseDao<ScheduleDtlTemp, String> {

    public void deleteBySendBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        String sql = "delete from CMS_SCHEDULE_DTL_TEMP where bill_code = :billCode ";
        createSQLQuery(sql, params).executeUpdate();
        getSession().flush();
    }

    public List<ScheduleDtlTemp> getListByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from ScheduleDtlTemp i ");
        hql.append(" where i.billCode = :billCode ");
        params.put("billCode", billCode);
        return listByHQL(hql.toString(), params);
    }
    public List<ScheduleDtlTemp> getListByIds(List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from ScheduleDtlTemp i ");
        hql.append(" where i.id in (:id) ");
        params.put("id", ids);
        return listByHQL(hql.toString(), params);
    }
}
