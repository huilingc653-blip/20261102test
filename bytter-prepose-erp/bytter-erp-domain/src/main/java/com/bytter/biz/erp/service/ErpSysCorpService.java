package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpSysCorpDao;
import com.bytter.biz.erp.entity.SysCorp;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统单位表
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service
public class ErpSysCorpService extends BaseService<SysCorp, String> {

    @Autowired
    private ErpSysCorpDao erpSysCorpDao;
    @Autowired
    private RedisService redisService;

    @Override
    public BaseDao<SysCorp, String> getEntityDao() {
        return erpSysCorpDao;
    }

    public SysCorp getByCode(String tenantId, String corpCode) {
        String cacheKey = ErpConstant.REDIS_KEY_SYS_CORP_INFO + tenantId;
        String itemKey = corpCode;
        // 先从缓存取
        SysCorp cachedInfo = redisService.getSysCorp(cacheKey, itemKey);
        if (ObjectUtil.isNotEmpty(cachedInfo)) {
            return cachedInfo;
        }

        SysCorp sysCorp = erpSysCorpDao.getOneCorp(tenantId, corpCode);
        if (ObjectUtil.isNotEmpty(sysCorp)) {
            redisService.setSysCorp(cacheKey, itemKey, sysCorp);
            return sysCorp;
        }
        return null;
    }

    public SysCorp getEntityById(String tenantId, String corpId) {
        String cacheKey = ErpConstant.REDIS_KEY_SYS_CORP_INFO + tenantId;
        String itemKey = corpId;
        // 先从缓存取
        SysCorp cachedInfo = redisService.getSysCorp(cacheKey, itemKey);
        if (ObjectUtil.isNotEmpty(cachedInfo)) {
            return cachedInfo;
        }

        SysCorp sysCorp = erpSysCorpDao.getByCorpId(corpId);
        if (ObjectUtil.isNotEmpty(sysCorp)) {
            redisService.setSysCorp(cacheKey, itemKey, sysCorp);
            return sysCorp;
        }
        return null;
    }
}
