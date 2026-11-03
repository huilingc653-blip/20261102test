package com.bytter.system.dao;
import com.bytter.system.entity.MenuCollect;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/10/23.
 */
@Repository
public class MenuCollectDao extends BaseDao<MenuCollect, String> {
    public MenuCollect collect(String menuId, String userId) {
        Criteria criterion = createCriteria();
        criterion.add(Restrictions.eq("menuId", menuId));
        criterion.add(Restrictions.eq("userId", userId));
        List<MenuCollect> menuCollectList = criterion.list();
        if (!EmptyUtils.isEmpty(menuCollectList)) {
            return menuCollectList.get(0);
        }
        return null;
    }
    public void deleteByMenuId(String menuId, String userId) {
        String sql = " delete from sys_menu_collect where menu_Id=:menuId and user_Id=:userId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("menuId", menuId);
        params.put("userId", userId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }
    public List<MenuCollect> listByUserId(String userId) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        hql.append(" from MenuCollect where userId = :userId ");
        hql.append(" order by sort desc, createTime desc ");
        params.put("userId", userId);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 根据用户id获取此用户收藏菜单的最大排序序号
     *
     * @param userId
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public Integer getMaxSortByUserId(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            String sql = "select max(sort) as maxsort from SYS_MENU_COLLECT where user_id=:userId";
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            List<?> list = this.listMapBySQL(sql, params);
            if (ListUtils.isNotNullOrEmpty(list)) {
                Map info = (Map) list.get(0);
                return Integer.parseInt(info.get("maxsort").toString());
            }
        }
        return 0;
    }
}
