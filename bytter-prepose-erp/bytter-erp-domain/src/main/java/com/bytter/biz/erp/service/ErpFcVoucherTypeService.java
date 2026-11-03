package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpFcVoucherTypeDao;
import com.bytter.biz.erp.entity.FcVoucherType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 付款方式
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service
public class ErpFcVoucherTypeService extends BaseService<FcVoucherType, String> {

    @Autowired
    private ErpFcVoucherTypeDao erpFcVoucherTypeDao;
    @Autowired
    private RedisService redisService;

    @Override
    public BaseDao<FcVoucherType, String> getEntityDao() {
        return erpFcVoucherTypeDao;
    }

    /***
     * 根据租户id和付款方式编码查询付款方式
     *
     * @param tenantId 租户id
     * @param voucherType 付款方式编码
     * @return 付款方式
     */
    public FcVoucherType listVoucherTypeByType(String tenantId, String voucherType) {
        String cacheKey = ErpConstant.REDIS_KEY_VOUCHER_TYPE_INFO + tenantId;
        String itemKey = voucherType;

        //  先从缓存取
        FcVoucherType cachedInfo = redisService.getFcVoucherType(cacheKey, itemKey);
        if (ObjectUtil.isNotEmpty(cachedInfo)) {
            return cachedInfo;
        }

        List<FcVoucherType> fcVoucherTypes = erpFcVoucherTypeDao.listVoucherTypeByType(tenantId, voucherType);
        if (ObjectUtil.isNotEmpty(fcVoucherTypes)) {
            FcVoucherType fcVoucherType = fcVoucherTypes.get(0);
            redisService.setFcVoucherType(cacheKey, itemKey, fcVoucherType);
            return fcVoucherType;
        }
        return null;
    }
}
