package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.system.entity.RoleMenu;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class RoleMenuDao extends BaseDao<RoleMenu, String> {
    public List<RoleMenu> listByMenuId(String menuId) {
        String hql = "from RoleMenu where menuId = :menuId ";
        Map<String, Object> map = new HashMap<>();
        map.put("menuId", menuId);
        return createQuery(hql, map).list();
    }
}
