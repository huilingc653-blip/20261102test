package com.bytter.system.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.RegionDTO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.RedisUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.RegionMessageEnum;
import com.bytter.system.dao.RegionDao;
import com.bytter.system.entity.Region;
import com.bytter.system.vo.RegionVO;
/**
 * @author zhoul
 * @date 2017/4/12
 */
@Service("regionService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegionService extends BaseService<Region, String> {
    @Autowired
    private RegionDao regionDao;
    @Autowired
    RedisUtils redisUtils;
    private final String PROVINCE = "province:";
    private final String CITY = "city:";
    @Override
    public BaseDao<Region, String> getEntityDao() {
        return regionDao;
    }
    /**
     * 根据父节点查找
     *
     * @param parentId
     * @return
     */
    public List<RegionVO> findByParentId(String parentId) {
        List<Region> treeList = regionDao.findByParentId(parentId);
        List<RegionVO> reList = new ArrayList<RegionVO>();
        for (Region region : treeList) {
            RegionVO regionVO = new RegionVO();
            regionVO.setId(region.getId());
            regionVO.setParentId(region.getParentId());
            regionVO.setBankCity(region.getBankCity());
            regionVO.setBankProv(region.getBankProv());
            regionVO.setName(region.getName());
            regionVO.setLevel(region.getLevel());
            regionVO.setProvName(region.getProvName());
            List<Region> subtreeList = regionDao.findByParentId(region.getId());
            regionVO.setIsParent(subtreeList.size() > 0);
            reList.add(regionVO);
        }
        return reList;
    }
    public List<Region> listByParentId(String parentId) {
        Criteria criteria = createCriteria();
        if (StringUtils.isNotBlank(parentId)) {
            criteria.add(Restrictions.eq("parentId", parentId));
        } else {
            criteria.add(Restrictions.isNull("parentId"));
        }
        criteria.addOrder(Order.asc("bankProv"));
        criteria.addOrder(Order.asc("bankCity"));
        return criteria.list();
    }
    private void checkRepeatRoleName(String name) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", name));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        if (count > 0) {
            throw new BusinessException(RegionMessageEnum.SYS_REGION_NAME_WAS_EXIST);
        }
    }
    /**
     * 保存省市
     *
     * @param region
     */
    public void saveRegion(Region region) {
        checkRepeatRoleName(region.getName());
        saveEntity(region);
    }
    /**
     * 修改省市
     *
     * @param region
     */
    public void updateRegion(Region region) {
        Region dbRegion = get(region.getId());
        if (!region.getName().equals(dbRegion.getName())) {
            checkRepeatRoleName(region.getName());
        }
        BeanUtils.copyNotNullProperties(region, dbRegion);
        updateEntity(dbRegion);
    }
    /**
     * 删除省市
     *
     * @param id
     */
    public void deleteRegion(String id) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("parentId", id));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        if (count > 0) {
            throw new BusinessException(RegionMessageEnum.SYS_REGION_HAS_CHILD);
        }
        regionDao.delete(id);
    }
    /**
     * 根据条件查询
     *
     * @param parentId 父类ID
     * @param name     当前省市名称
     * @return Region
     */
    public Region getCityBy(String parentId, String name) {
        return this.regionDao.getCityBy(parentId, name);
    }
    /**
     * 根据ID查找，注入使用
     *
     * @param id 省市ID
     * @return
     */
    public Region getByIdCache(String id) {
        return regionDao.getByIdCache(id);
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
        return this.regionDao.getByName(name, prov);
    }
    /**
     * 获取所有省份
     *
     * @return
     */
    public List<Region> listProvAll() {
        return regionDao.listProvAll();
    }
    /**
     * 获取省份下面的城市
     *
     * @param parentId
     * @return
     */
    public List<Region> listCityByParentId(String parentId) {
        return regionDao.listCityByParentId(parentId);
    }
    public Region getRegionByLikeProvName(String provName) {
        return this.regionDao.getRegionByLikeProvName(provName);
    }
    /**
     * 初始化redis省市缓存数据
     */
    public void initCacheRegionData() {
        List<Region> list = regionDao.listProvAll();
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(reg -> {
                redisUtils.put(PROVINCE + reg.getId(), reg);//每次强制刷新
                List<Region> cityList = regionDao.listCityByParentId(reg.getId());
                if (!CollectionUtils.isEmpty(cityList)) {
                    list.stream().forEach(cityre -> {
                        redisUtils.put(CITY + cityre.getId(), cityre);//每次强制刷新
                    });
                }
            });
        }
    }
    /**
     * 根据id查询
     *
     * @param id 当前查询省市名称
     * @return Region
     * @author wangf 2023-05-06
     */
    public Region getById(String id) {
        return regionDao.getById(id);
    }
    /**
     * 根据id集合获取省市信息
     *
     * @param regionIds 省市id集合
     * @return 省市信息集合
     */
    public List<RegionDTO> listByIds(List<String> regionIds) {
        return regionDao.listByIds(regionIds);
    }

    public List<Region> findByIds(List<String> ids) {
        return regionDao.findByIds(ids);
    }
}
