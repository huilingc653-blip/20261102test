package com.bytter.basic.service;
import com.bytter.basic.dao.BtCurrencyDao;
import com.bytter.basic.entity.BtCurrency;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.RedisUtils;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.constant.SysCacheConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 货币资料维护
 * @date 2017-05-23 11:14:22
 */
@Service("btCurrencyServiceBase")
@Transactional
public class BtCurrencyService extends BaseService<BtCurrency, String> {
    @Autowired
    private BtCurrencyDao btCurrencyDao;
    @Autowired
    private IFcExcrateAdjustService fcExcrateAdjustServiceI;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public BaseDao<BtCurrency, String> getEntityDao() {
        return btCurrencyDao;
    }
    /**
     * 查询货币信息列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput<BtCurrencyVO> getDataTablesUnion(DataTablesInput input) {
        return this.btCurrencyDao.getDataTablesUnion(input);
    }
    /**
     * 保存货币信息
     *
     * @param entity
     */
    public void saveBtCurrency(BtCurrency entity, String userLoginName) {
        // 校验币种信息
        verifyCur(entity);
        entity.setCreateBy(userLoginName);
        entity.setCreateDate(DateUtils.getDate());
        if (!EmptyUtils.isBlank(entity.getCurSign())) {
            //todo
            byte[] encode = java.util.Base64.getEncoder().encode(entity.getCurSign().getBytes(StandardCharsets.UTF_8));
            entity.setCurSign(new String(encode));
        }
        super.saveEntity(entity);
//        updateIsNaturalSign(entity);
        // 清理缓存
        clearCache();
    }

    private void clearCache(){
        String hashKey = RedisCacheKey.makeRedisKey("bytter-system", RedisCacheKey.SYS_CURRENCY);
        redisUtils.remove(hashKey);
    }

    /**
     * 修改货币信息
     *
     * @param entity
     */
    @Caching(evict = {
            @CacheEvict(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#entity.id"),
            @CacheEvict(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#entity.curName"),
            @CacheEvict(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#entity.curCode"),
    })
    public void updateBtCurrency(BtCurrency entity, String userLoginName) {
        // 校验币种信息
        verifyCur(entity);
        entity.setUpdateBy(userLoginName);
        entity.setUpdateDate(DateUtils.getDate());
        if (!EmptyUtils.isBlank(entity.getCurSign())) {
            entity.setCurSign(Base64Utils.encodeToString(entity.getCurSign().getBytes(StandardCharsets.UTF_8)));
        }
        super.saveOrUpdate(entity);
//        updateIsNaturalSign(entity);
        // 清理缓存
        clearCache();
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#entity.id"),
            @CacheEvict(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#entity.curName"),
            @CacheEvict(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#entity.curCode"),
    })
    public void deleteEntity(BtCurrency entity) {
        super.deleteEntity(entity);
        // 清理缓存
        clearCache();
    }
    /**
     * 根据id查找货币信息，注入使用
     *
     * @param id
     * @return
     */
    public BtCurrency getByIdCache(String id) {
        return btCurrencyDao.getByIdCache(id);
    }
    /**
     * 根据id查找货币信息
     *
     * @param id
     * @return
     */
    @Cacheable(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#id")
    public BtCurrencyVO getByUnion(String id) {
        return this.btCurrencyDao.getByUnion(id);
    }
    /**
     * 判断币别编码是否重复
     *
     * @param id
     * @param curCode
     * @return
     */
    public boolean isRepeatCurCode(String id, String curCode) {
        List<BtCurrency> list = btCurrencyDao.listBy("curCode", curCode);
        if (ListUtils.isNullOrEmpty(list)) {
            return false;
        } else {
            BtCurrency btCurrency = btCurrencyDao.get(id);
            if (null == btCurrency) {
                return true;
            }
            BtCurrency obj = list.get(0);
            return !obj.getId().equals(btCurrency.getId());
        }
    }
    /**
     * 判断是否存在多个本位币，若存在则将旧货币设置为否
     *
     * @param entity
     */
    public void updateIsNaturalSign(BtCurrency entity) {
        // 判断当前货币是否为本位币
        if (BtCurrencyConstant.NATURAL_SIGN_YES.equals(entity.getNaturalSign())) {
            // 查询为本位币的货币信息
            List<BtCurrency> list = btCurrencyDao.listBy("naturalSign", BtCurrencyConstant.NATURAL_SIGN_YES);
            for (BtCurrency oldBtCurrency : list) {
                if (!oldBtCurrency.getId().equals(entity.getId())) {
                    // 将旧货币的本位币改为否
                    oldBtCurrency.setNaturalSign(BtCurrencyConstant.NATURAL_SIGN_NO);
                    updateEntity(oldBtCurrency);
                }
            }
        }
    }
    /**
     * 获取有效的货币资料信息
     *
     * @return
     */
    public List<BtCurrencyVO> getBtCurrency() {
        return btCurrencyDao.getBtCurrencyVO();
    }
    /**
     * 查询非本位币的货币信息
     *
     * @return
     */
    public List<BtCurrency> listByNotNatualCurrency() {
        return this.btCurrencyDao.listByNotNatualCurrency();
    }
    /**
     * 获取除本位币以外的所有有效货币信息(无效的当前所属的币别)
     *
     * @param curId 币别id
     * @return
     */
    public List<BtCurrency> listByNotNatualCurrencyAndSelf(String curId) {
        return this.btCurrencyDao.listByNotNatualCurrencyAndSelf(curId);
    }
    /**
     * 查询本位币
     *
     * @return
     */
    public BtCurrency getNatualCurrency() {
        return btCurrencyDao.getNatualCurrency();
    }
    /**
     * 查询人民币
     *
     * @return
     */
    public BtCurrency getRMBCurrency() {
        return getBtCurrencyByName(BtCurrencyConstant.RMB);
    }
    /**
     * 根据币别名称获取币别ID
     *
     * @param curName 币别名称
     * @return id
     */
    public String getCurrencyByName(String curName) {
        BtCurrency currency = getBtCurrencyByName(curName);
        if (EmptyUtils.isEmpty(currency)) {
            return null;
        }
        return currency.getId();
    }
    /**
     * 根据币别名称获取币别信息
     *
     * @param curName 币别名称
     * @return BtCurrency
     */
    @Cacheable(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#curName")
    public BtCurrency getBtCurrencyByName(String curName) {
        List<BtCurrency> list = this.btCurrencyDao.listBy("curName", curName);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据币别编码获取币别信息
     *
     * @param curCode 币别编码
     * @return BtCurrency
     */
    @Cacheable(value = SysCacheConstant.CURRENCY_CACHE_NAME, key = "#curCode")
    public BtCurrency getCurrencyByCode(String curCode) {
        List<BtCurrency> list = this.btCurrencyDao.listBy("curCode", curCode);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据币别名称或者英文简码获取ID
     *
     * @param eqMatchName 匹配字
     * @return id
     */
    public String getIdByNameOrEnglishCode(String eqMatchName) {
        return this.btCurrencyDao.getIdByNameOrEnglishCode(eqMatchName);
    }
    /**
     * 获取币别跟其他业务的关联数量
     *
     * @param curId 币别ID
     * @return
     */
    public boolean checkAssociated(String curId) {
        //验证是否已存在
        return this.btCurrencyDao.checkAssociated(curId) > 0;
    }
    /**
     * 根据币别名称或英文见吗获取币种
     *
     * @param search 查询条件
     * @return id
     */
    public List<BtCurrencyVO> getByCurNameOrEnglishCode(String search) {
        return btCurrencyDao.getByCurNameOrEnglishCode(search);
    }
    public List<BtCurrencyVO> listByIds(List<String> curIds) {
        return btCurrencyDao.listByIds(curIds);
    }
    public List<BtCurrencyDTO> queryValidCur() {
        return btCurrencyDao.queryValidCur();
    }

    public List<BtCurrencyVO> listByNames(List<String> curNames) {
        return btCurrencyDao.listByNames(curNames);
    }

    private void verifyCur(BtCurrency btCurrency){
        //判断判断币别编码是否重复
        boolean result = isRepeatCurCode(btCurrency.getId(), btCurrency.getCurCode());
        if (result) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_MESSAGE_CURCODE_UNIQUE);
        }
        // 校验系统币种是否重复
        String sysCurId = btCurrency.getSysCurId();
        List<BtCurrency> sysCurIds = btCurrencyDao.listBy("sysCurId", sysCurId);
        if (EmptyUtils.isNotEmpty(sysCurIds)) {
            for (BtCurrency cur : sysCurIds) {
                if (!cur.getId().equals(btCurrency.getId())) {
                    throw new ParameterException(CurrencyMessageEnum.BT_CURRENCY_MESSAGE_SYSCURID_UNIQUE);
                }
            }
        }
    }
}