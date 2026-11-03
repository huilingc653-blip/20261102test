package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpBankAccInfoDao;
import com.bytter.biz.erp.entity.BankAccInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账号视图
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service
public class ErpBankAccInfoService extends BaseService<BankAccInfo, String> {

    @Autowired
    private ErpBankAccInfoDao erpBankAccInfoDao;
    @Autowired
    private RedisService redisService;

    @Override
    public BaseDao<BankAccInfo, String> getEntityDao() {
        return erpBankAccInfoDao;
    }

    public BankAccInfo getByAccNo(String tenantId, String bankAcc, String curId) {
        String cacheKey = ErpConstant.REDIS_KEY_BANK_ACC_INFO + tenantId;
        String itemKey = bankAcc;
        if (ObjectUtil.isNotEmpty(curId)) {
            itemKey += "_" + curId;
        }

        //  先从缓存取
        BankAccInfo cachedInfo = redisService.getBankAccInfo(cacheKey, itemKey);
        if (ObjectUtil.isNotEmpty(cachedInfo)) {
            return cachedInfo;
        }

        List<BankAccInfo> bankAccInfoList = erpBankAccInfoDao.queryBankAccInfoByAcc(tenantId, bankAcc, curId);
        if (ObjectUtil.isNotEmpty(bankAccInfoList)) {
            BankAccInfo bankAccInfo = bankAccInfoList.get(0);
            // 缓存数据
            redisService.setBankAccInfo(cacheKey, itemKey, bankAccInfo);
            return bankAccInfo;
        }
        return null;
    }
}
