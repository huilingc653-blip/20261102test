package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.dao.ErpBtCurrencyDao;
import com.bytter.biz.erp.entity.BtCurrency;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统币别表
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service
public class ErpBtCurrencyService extends BaseService<BtCurrency, String> {

    @Autowired
    private ErpBtCurrencyDao erpBtCurrencyDao;
    @Autowired
    private RedisService redisService;

    @Override
    public BaseDao<BtCurrency, String> getEntityDao() {
        return erpBtCurrencyDao;
    }

    public BtCurrency getByEnglishCode(String tenantId, String englishCode) {
        String cacheKey = ErpConstant.REDIS_KEY_CUR_INFO + tenantId;
        String itemKey = englishCode;

        // 先从缓存取
        BtCurrency cachedInfo = redisService.getBtCurrency(cacheKey, itemKey);
        if (ObjectUtil.isNotEmpty(cachedInfo)) {
            return cachedInfo;
        }

        List<String> codes = new ArrayList<>();
        codes.add(englishCode);
        if ("CNY".equals(englishCode)) {
            codes.add("RMB");
        }
        if ("RMB".equals(englishCode)) {
            codes.add("CNY");
        }

        List<BtCurrency> btCurrencies = erpBtCurrencyDao.listBtCurrency(tenantId, codes);

        if (ObjectUtil.isNotEmpty(btCurrencies)) {
            BtCurrency btCurrency = btCurrencies.get(0);
            redisService.setBtCurrency(cacheKey, itemKey, btCurrency);
            return btCurrency;
        }
        return null;
    }
}
