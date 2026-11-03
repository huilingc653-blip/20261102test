package com.bytter.dual.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.dual.entity.DualManager;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/9/1.
 */
@Repository
public class DualManagerDao extends BaseDao<DualManager, String> {
    /**
     * 根据校验类型和业务ID查找对象
     *
     * @param manager     管理类型
     * @param businessKey 业务id
     * @return 返回该对象
     */
    public DualManager findByManagerTypeAndBusinessKey(String manager, String businessKey) {
        String hql = "from DualManager where managerType=?0 and businessKey=?1 and status=0";
        return (DualManager) createQuery(hql, manager, businessKey).uniqueResult();
    }
    public List<DualManager> listByIds(Integer first, Integer maxResult, List<String> ids) {
        String hql = "from DualManager where id in :ids";
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        return listByHQL(hql, first, maxResult, map);
    }
}
