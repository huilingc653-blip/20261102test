package com.bytter.system.dao;
import com.bytter.system.entity.DictGroup;
import com.bytter.core.base.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoul on 2017/5/15.
 */
@Repository
public class DictGroupDao extends BaseDao<DictGroup, String> implements Serializable {
    public void delete(DictGroup dictGroup) {
        super.delete(dictGroup);
    }

    public DictGroup getUniqueByCode(String groupCode) {
        String hql = "from DictGroup where groupCode = :groupCode ";
        Map<String, Object> params = new HashMap<>();
        params.put("groupCode", groupCode);
        return this.getUnique(hql, params);
    }
}
