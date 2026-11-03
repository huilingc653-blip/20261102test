package com.bytter.system.dao;
import com.bytter.system.entity.Home;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class HomeDao extends BaseDao<Home, String> {
    public Home getUserNewest(String userId) {
        String maxRecored = "from Home home where home.userId=?0 order by home.createTime desc";
        Query query = createQuery(maxRecored, userId);
        query.setFirstResult(0);
        query.setMaxResults(1);
        List list = query.list();
        if (!EmptyUtils.isEmpty(list)) {
            Home home = (Home) list.get(0);
            return home;
        }
        return null;
    }
    /**
     * 删除用户相关信息
     *
     * @param userId
     */
    public void resetDefault(String userId) {
        String hql = "delete from Home where userId = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        createQuery(hql, map).executeUpdate();
    }
}
