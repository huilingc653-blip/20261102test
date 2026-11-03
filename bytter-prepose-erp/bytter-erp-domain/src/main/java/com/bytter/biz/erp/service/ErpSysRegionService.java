package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpSysRegionDao;
import com.bytter.biz.erp.entity.SysRegion;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 省市信息
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service
public class ErpSysRegionService extends BaseService<SysRegion, String> {

    @Autowired
    private ErpSysRegionDao erpSysRegionDao;

    @Autowired
    private RedisService redisService;

    @Override
    public BaseDao<SysRegion, String> getEntityDao() {
        return erpSysRegionDao;
    }

    /***
     * 根据城市编码查询省市信息
     *
     * @param bankInputCity
     * @return 省市信息
     */
    public SysRegion queryRegionByCity(String bankInputCity) {
        String cacheKey = ErpConstant.REDIS_KEY_SYS_REGION_INFO;
        String itemKey = bankInputCity;
        //  先从缓存取
        SysRegion cachedInfo = redisService.getSysRegion(cacheKey, itemKey);
        if (ObjectUtil.isNotEmpty(cachedInfo)) {
            return cachedInfo;
        }

        List<SysRegion> sysRegions = erpSysRegionDao.listRegionByCity(bankInputCity);
        if (ObjectUtil.isNotEmpty(sysRegions)) {
            SysRegion sysRegion = sysRegions.get(0);
            redisService.setSysRegion(cacheKey, itemKey, sysRegion);
            return sysRegion;
        }
        return null;
    }
}
