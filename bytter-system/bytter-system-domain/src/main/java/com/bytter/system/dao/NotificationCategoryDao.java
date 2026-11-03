package com.bytter.system.dao;
import com.bytter.system.entity.NotificationCategory;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class NotificationCategoryDao extends BaseDao<NotificationCategory, String> {
    public NotificationCategory findByTag(String tag) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tag", tag));
        return (NotificationCategory) criteria.uniqueResult();
    }
    public IPageResult<NotificationCategory> listCategory(TableGridInput input) {
        String parentId = (String) input.get("parentId");
        String menuId = (String) input.get("menuId");
        String name = (String) input.get("name");
        Map<String, Object> map = Maps.newHashMap();
        StringBuilder hql = new StringBuilder("from NotificationCategory where 1 = 1");
        if (StringUtils.isNotBlank(parentId)) {
            hql.append(" and hw.parentId = :parentId ");
            map.put("parentId", parentId);
        }
        if (StringUtils.isNotBlank(menuId)) {
            hql.append(" and hw.menuId = :menuId ");
            map.put("type", menuId);
        }
        if (StringUtils.isNotBlank(name)) {
            hql.append(" and hw.name like :name ");
            map.put("name", "%" + name + "%");
        }
        hql.append(" order by sort ");
        return getPageResultByHQL(input, hql.toString(), map);
    }
}
