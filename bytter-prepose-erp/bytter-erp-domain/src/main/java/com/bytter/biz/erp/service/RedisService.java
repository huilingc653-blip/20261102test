package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.erp.component.CommonParam;
import com.bytter.biz.erp.entity.*;
import com.bytter.core.utils.RedisUtils;
import com.bytter.erp.common.constant.ErpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Redis服务类
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Service
public class RedisService {

    @Autowired
    private CommonParam commonParam;

    @Resource
    private RedisUtils redisUtils;

    public String getTenantId(String cacheKey) {
        String result = "";
        if (commonParam.isRedisEnable()) {
            Object cachedTenantIdObj = redisUtils.get(cacheKey);
            if (ObjectUtil.isNotEmpty(cachedTenantIdObj)) {
                result = String.valueOf(cachedTenantIdObj);
            }
        }
        return result;
    }

    public void setTenantId(String cacheKey, String tenantId) {
        if (commonParam.isRedisEnable()) {
            redisUtils.put(cacheKey, tenantId, 60 * 60 * 24); // 1天
        }
    }

    public BankAccInfo getBankAccInfo(String cacheKey, String item) {
        BankAccInfo cachedItem = null;
        if (commonParam.isRedisEnable()) {
            Object cachedObj = redisUtils.hashGet(cacheKey, item);
            if (ObjectUtil.isNotEmpty(cachedObj)) {
                cachedItem = (BankAccInfo) cachedObj;
            }
        }
        return cachedItem;
    }

    public void setBankAccInfo(String cacheKey, String itemKey, BankAccInfo bankAccInfo) {
        if (commonParam.isRedisEnable()) {
            long time = commonParam.getLongVal(ErpConstant.PARAM_BANKACC_CACHE_TIME);
            if (time < 0) {
                time = 300L;// 5分钟 单位秒
            }
            redisUtils.hashPut(cacheKey, itemKey, bankAccInfo, time);
        }
    }

    public BtCurrency getBtCurrency(String cacheKey, String itemKey) {
        BtCurrency cachedItem = null;
        if (commonParam.isRedisEnable()) {
            Object cachedObj = redisUtils.hashGet(cacheKey, itemKey);
            if (ObjectUtil.isNotEmpty(cachedObj)) {
                cachedItem = (BtCurrency) cachedObj;
            }
        }
        return cachedItem;
    }

    public void setBtCurrency(String cacheKey, String itemKey, BtCurrency btCurrency) {
        if (commonParam.isRedisEnable()) {
            long time = commonParam.getLongVal(ErpConstant.PARAM_CUR_CACHE_TIME);
            if (time < 0) {
                time = 86400L;// 1天 单位秒
            }
            redisUtils.hashPut(cacheKey, itemKey, btCurrency, time);
        }
    }

    public FcVoucherType getFcVoucherType(String cacheKey, String itemKey) {
        FcVoucherType cachedItem = null;
        if (commonParam.isRedisEnable()) {
            Object cachedObj = redisUtils.hashGet(cacheKey, itemKey);
            if (ObjectUtil.isNotEmpty(cachedObj)) {
                cachedItem = (FcVoucherType) cachedObj;
            }
        }
        return cachedItem;
    }

    public void setFcVoucherType(String cacheKey, String itemKey, FcVoucherType fcVoucherType) {
        if (commonParam.isRedisEnable()) {
            long time = commonParam.getLongVal(ErpConstant.PARAM_VOUCHER_TYPE_CACHE_TIME);
            if (time < 0) {
                time = 86400L;// 1天 单位秒
            }
            redisUtils.hashPut(cacheKey, itemKey, fcVoucherType, time);
        }
    }

    public InputBankInfo getInputBankInfo(String cacheKey, String itemKey) {
        InputBankInfo cachedItem = null;
        if (commonParam.isRedisEnable()) {
            Object cachedObj = redisUtils.hashGet(cacheKey, itemKey);
            if (ObjectUtil.isNotEmpty(cachedObj)) {
                cachedItem = (InputBankInfo) cachedObj;
            }
        }
        return cachedItem;
    }

    public void setInputBankInfo(String cacheKey, String itemKey, InputBankInfo inputBankInfo) {
        if (commonParam.isRedisEnable()) {
            long time = commonParam.getLongVal(ErpConstant.PARAM_BANK_CODE_CACHE_TIME);
            if (time < 0) {
                time = 86400L;// 1天 单位秒
            }
            redisUtils.hashPut(cacheKey, itemKey, inputBankInfo, time);
        }
    }

    public SysCorp getSysCorp(String cacheKey, String itemKey) {
        SysCorp cachedItem = null;
        if (commonParam.isRedisEnable()) {
            Object cachedObj = redisUtils.hashGet(cacheKey, itemKey);
            if (ObjectUtil.isNotEmpty(cachedObj)) {
                cachedItem = (SysCorp) cachedObj;
            }
        }
        return cachedItem;
    }

    public void setSysCorp(String cacheKey, String itemKey, SysCorp corp) {
        if (commonParam.isRedisEnable()) {
            long time = commonParam.getLongVal(ErpConstant.PARAM_SYS_CORP_CACHE_TIME);
            if (time < 0) {
                time = 86400L;// 1天 单位秒
            }
            redisUtils.hashPut(cacheKey, itemKey, corp, time);
        }
    }

    public SysRegion getSysRegion(String cacheKey, String itemKey) {
        SysRegion cachedItem = null;
        if (commonParam.isRedisEnable()) {
            Object cachedObj = redisUtils.hashGet(cacheKey, itemKey);
            if (ObjectUtil.isNotEmpty(cachedObj)) {
                cachedItem = (SysRegion) cachedObj;
            }
        }
        return cachedItem;
    }

    public void setSysRegion(String cacheKey, String itemKey, SysRegion sysRegion) {
        if (commonParam.isRedisEnable()) {
            long time = commonParam.getLongVal(ErpConstant.PARAM_SYS_REGION_CACHE_TIME);
            if (time < 0) {
                time = 86400L;// 1天 单位秒
            }
            redisUtils.hashPut(cacheKey, itemKey, sysRegion, time);
        }
    }

    /**
     * 检查流水号是否已经处理过
     *
     * @param serialNo 流水号
     * @return true: 已处理过, false: 未处理过
     */
    public boolean checkSerialNoProcessed(String serialNo) {
        boolean result = false;
        if (commonParam.isRedisEnable()) {
            String key = ErpConstant.REDIS_KEY_SERIAL_NO + serialNo;
            result = redisUtils.hasKey(key);
        }
        return result;
    }

    /**
     * 标记流水号为已处理
     *
     * @param serialNo 流水号
     */
    public void markSerialProcessed(String serialNo) {
        if (commonParam.isRedisEnable()) {
            String key = ErpConstant.REDIS_KEY_SERIAL_NO + serialNo;
            // 设置过期时间，
            redisUtils.put(key, "processed", 24 * 60 * 60); // 24小时
        }
    }
}
