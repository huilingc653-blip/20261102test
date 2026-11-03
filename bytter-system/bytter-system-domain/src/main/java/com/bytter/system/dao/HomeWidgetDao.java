package com.bytter.system.dao;
import com.bytter.system.entity.HomeWidget;
import com.bytter.system.vo.HomeWidgetVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class HomeWidgetDao extends BaseDao<HomeWidget, String> {
    public IPageResult listAllWidget(TableGridInput input) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder hql = new StringBuilder();
        String type = (String) input.get("type");
        Integer width = (Integer) input.get("width");
        Integer height = (Integer) input.get("height");
        String categoryId = (String) input.get("categoryId");
        String name = (String) input.get("name");
        hql.append("from HomeWidget hw where 1=1 ");
        if (StringUtils.isNotBlank(categoryId)) {
            hql.append(" and hw.categoryId = :categoryId ");
            map.put("categoryId", categoryId);
        }
        if (StringUtils.isNotBlank(type)) {
            hql.append(" and hw.type = :type ");
            map.put("type", type);
        }
        if (StringUtils.isNotBlank(name)) {
            hql.append(" and hw.name like :name ");
            map.put("name", "%" + name + "%");
        }
        if (!EmptyUtils.isEmpty(width)) {
            hql.append(" and (hw.minWidth <= :width and hw.maxWidth >= :width )");
            map.put("width", width);
        }
        if (!EmptyUtils.isEmpty(height)) {
            hql.append(" and hw.height = :height ");
            map.put("height", height);
        }
        // 排序
        hql.append(" order by ");
        // 页面标题排序
        String sort = input.getSort();
        String para = input.getOrder();
        if (StringUtils.isNotEmpty(para)) {
            hql.append(para).append(" ").append(sort);
            hql.append(",");
        }
        hql.append(" hw.sort");
        return getPageResultByHQL(input, hql.toString(), map, HomeWidgetVO.class);
    }
    public IPageResult listWidget(IPagePara pagePara, String type, Integer width, Integer height) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder hql = new StringBuilder();
        hql.append("from HomeWidget hw where 1=1 ");
        if (StringUtils.isNotBlank(type)) {
            hql.append(" and hw.type = :type ");
            map.put("type", type);
        }
        if (!EmptyUtils.isEmpty(width)) {
            hql.append(" and (hw.minWidth <= :width and hw.maxWidth >= :width )");
            map.put("width", width);
        }
        if (!EmptyUtils.isEmpty(height)) {
            hql.append(" and hw.height = :height ");
            map.put("height", height);
        }
        hql.append(" order by hw.sort");
        return getPageResultByHQL(pagePara, hql.toString(), map);
    }
}
