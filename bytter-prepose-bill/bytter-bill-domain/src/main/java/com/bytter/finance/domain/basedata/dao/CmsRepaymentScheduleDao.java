package com.bytter.finance.domain.basedata.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.core.base.BaseDao;
/**
 * @Title: Dao
 * @Description: 还款计划主表
 * @author onlineGenerator
 * @date 2017-08-01 09:58:44
 * @version 1.0
 *
 */
@Repository
public class CmsRepaymentScheduleDao extends BaseDao<CmsRepaymentSchedule, String> {
    @SuppressWarnings("unchecked")
    public CmsRepaymentSchedule findById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from CmsRepaymentSchedule rs where rs.id = :id";
        params.put("id", id);
        List<CmsRepaymentSchedule> list = createQuery(hql, params).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
