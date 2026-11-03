package com.bytter.system.dao;
import com.bytter.system.entity.UserConfig;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class UserConfigDao extends BaseDao<UserConfig, String> {
    public UserConfig getUserIndexSkin(String userId) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("type", "indexSkin");
        hql.append(" from UserConfig uc where uc.userId =:userId and uc.type =:type");
        List<UserConfig> list = listByHQL(hql.toString(), map);
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
}
