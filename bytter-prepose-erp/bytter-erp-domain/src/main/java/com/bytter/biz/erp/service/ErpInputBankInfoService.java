package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpInputBankInfoDao;
import com.bytter.biz.erp.entity.InputBankInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 联行号
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service
public class ErpInputBankInfoService extends BaseService<InputBankInfo, String> {

    @Autowired
    private ErpInputBankInfoDao erpInputBankInfoDao;
    @Autowired
    private RedisService redisService;

    @Override
    public BaseDao<InputBankInfo, String> getEntityDao() {
        return null;
    }

    /***
     * 根据联行号获取对应的联行号信息
     *
     * @param sysBankCode
     * @return 联行号信息
     */
    public InputBankInfo getByCode(String sysBankCode) {
        String cacheKey = ErpConstant.REDIS_KEY_BANK_CODE_INFO;
        String itemKey = sysBankCode;

        //  先从缓存取
        InputBankInfo cachedInfo = redisService.getInputBankInfo(cacheKey, itemKey);
        if (ObjectUtil.isNotEmpty(cachedInfo)) {
            return cachedInfo;
        }

        InputBankInfo inputBankInfo = erpInputBankInfoDao.getOneInputBankInfo(sysBankCode);
        if (ObjectUtil.isNotEmpty(inputBankInfo)) {
            redisService.setInputBankInfo(cacheKey, itemKey, inputBankInfo);
            return inputBankInfo;
        }
        return null;
    }

}
