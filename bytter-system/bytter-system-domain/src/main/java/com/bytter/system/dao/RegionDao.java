package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.RegionDTO;
import com.bytter.system.entity.Region;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author zhoul
 * @date 2017/4/17
 */
@Repository
public class RegionDao extends BaseDao<Region, String> {
    public List<Region> findByParentId(String parentId) {
        Criteria criteria = createCriteria();
        if (StringUtils.isBlank(parentId)) {
            criteria.add(Restrictions.isNull("parentId"));
        } else {
            criteria.add(Restrictions.eq("parentId", parentId));
        }
        criteria.addOrder(Order.asc("bankProv"));
        criteria.addOrder(Order.asc("bankCity"));
        criteria.setCacheable(true);
        return criteria.list();
    }
    public Region getByIdCache(String id) {
        Criteria criteria = createCriteria(Restrictions.eq("id", id));
        criteria.setCacheable(true);
        return (Region) criteria.uniqueResult();
    }
    /**
     * 根据条件查询
     *
     * @param parentId 父类ID
     * @param name     当前省市名称
     * @return Region
     */
    public Region getCityBy(String parentId, String name) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("parentId", parentId));
        criteria.add(Restrictions.eq("name", name));
        return (Region) criteria.uniqueResult();
    }
    /**
     * 根据id查询
     *
     * @param id 当前查询省市名称
     * @return Region
     * @author wangf 2023-05-06
     */
    public Region getById(String id) {
        HashMap<String, String> params = new HashMap();
        params.put("id", id);
        List<Region> list = this.listByHQL(" from Region r where r.id = :id ", params, true);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据名称查询
     *
     * @param name 当前查询省市名称
     * @param prov null - 全部 turn - 省  false - 市
     * @return
     * @author zuoy 2018-05-23
     */
    public Region getByName(String name, Boolean prov) {
        StringBuilder hql = new StringBuilder(" from Region r where r.name = :name ");
        HashMap<String, String> params = new HashMap();
        params.put("name", name);
        if (!EmptyUtils.isEmpty(prov)) {
            if (prov) {
                hql.append(" and r.parentId is null ");
            } else {
                hql.append(" and r.parentId is not null ");
            }
        }
        List<Region> list = this.listByHQL(hql.toString(), params, true);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    public List<Region> listProvAll() {
        return this.listByHQL("from Region r where level = 1 and parentId is null");
    }
    public List<Region> listCityByParentId(String parentId) {
        return this.listByHQL("from Region r where parentId = ?0 ", parentId);
    }
    /**
     * 根据名称查询
     *
     * @param provName 当前查询省市名称
     * @return
     */
    public Region getRegionByLikeProvName(String provName) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.like("name", "%" + provName + "%"));
        criteria.add(Restrictions.eq("level", 1));
        return (Region) criteria.uniqueResult();
    }
    public List<RegionDTO> listByIds(List<String> regionIds) {
        HashMap<String, Object> params = new HashMap();
        params.put("ids", regionIds);
        List<Region> list = this.listByHQL(" from Region r where r.id in (:ids) ", params, true);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return BeanUtils.copyListProperties(list, RegionDTO.class);
    }

    public List<Region> findByIds(List<String> ids){
        Criteria criteria = createCriteria();
        if (null == ids ) {
            criteria.add(Restrictions.isNull("id"));
        } else {
            criteria.add(Restrictions.in("id", ids));
        }
        criteria.addOrder(Order.asc("bankProv"));
        criteria.addOrder(Order.asc("bankCity"));
        criteria.setCacheable(true);
        return criteria.list();
    }
}
