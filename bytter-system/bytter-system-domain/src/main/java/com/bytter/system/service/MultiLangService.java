package com.bytter.system.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.MutiLangConstant;
import com.bytter.system.api.constant.SysCacheConstant;
import com.bytter.system.api.enums.MultiLangEnum;
import com.bytter.system.dao.MultiLangDao;
import com.bytter.system.entity.MultiLang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service("multiLangService")
public class MultiLangService extends BaseService<MultiLang, String> implements CommandLineRunner {
    @Autowired
    private MultiLangDao multiLangDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public BaseDao getEntityDao() {
        return multiLangDao;
    }

    public IPageResult<MultiLang> getTableGrid(TableGridInput tableGridInput) {
        return multiLangDao.listLang(tableGridInput);
    }

    /**
     * 根据ajax对象，动态获取国际化语言
     *
     * @param ajaxJson
     * @return
     */
    @Transactional(readOnly = true)
    public String getLangByAjaxJson(AjaxJson ajaxJson) {
        String message;
        if (null != ajaxJson.getMessageEnum()) {
            IMessageEnum messageEnum = ajaxJson.getMessageEnum();
            // 自定义枚举是否带有占位符参数
            if (null != ajaxJson.getParams() && ajaxJson.getParams().length != 0) {
                message = getLangByEnum(messageEnum, ajaxJson.getParams());
            } else {
                message = getLangByEnum(messageEnum);
            }
        } else {
            // 提示用户操作成功
            message = getLangByEnum(CommonMessageEnum.COMMON_MESSAGE_SUCCESS);
        }
        return message;
    }

    /**
     * 获取国际化自定义枚举的国际化值
     *
     * @param exceptionEnum
     * @return
     */
    public String getLangByEnum(IMessageEnum exceptionEnum) {
        return getLang(buildLangKey(exceptionEnum));
    }

    /**
     * 组装langKey
     *
     * @param exceptionEnum
     * @return
     */
    private String buildLangKey(IMessageEnum exceptionEnum) {
        String enumName = exceptionEnum.getEnum().name();
        return enumName.replace("_", ".").toLowerCase();
    }

    /**
     * 获取自定义枚举的国际化语言值，带占位符参数
     *
     * @param exceptionEnum
     * @param params
     * @return
     */
    public String getLangByEnum(IMessageEnum exceptionEnum, String... params) {
        String langName;
        langName = getLang(buildLangKey(exceptionEnum), params);
        return langName;
    }

    /**
     * 获取国际化语言编码
     * todo shiro去掉
     *
     * @return
     */
    private Locale getLocale() {
        // shiro 国际化不能创建session只能获取
        return Locale.SIMPLIFIED_CHINESE;
    }

    /**
     * 根据langKey取语言
     *
     * @param langKey
     * @return
     * @author 周亮
     */
    public String getLang(String langKey) {
        return getLang(langKey, getLocale());
    }

    public String getLang(String langKey, Locale locale) {
        return getText(langKey, locale);
    }

    /**
     * 如果语言有占位符的，根据传入的多个参数填充占位符
     *
     * @param langKey 传入的值
     * @param args    替换占位符的数组
     * @return
     * @author 周亮
     */
    public String getLang(String langKey, String... args) {
        String langContext = getLang(langKey);
        langContext = MessageFormat.format(langContext, args);
        return langContext;
    }

    public String getLang(Locale locale, String langKey, String... args) {
        String langContext = getLang(langKey, locale);
        if (StringUtils.isNotEmpty(langContext)) {
            langContext = MessageFormat.format(langContext, args);
        }
        return langContext;
    }

    /**
     * 获取国际化语言
     *
     * @param langKey
     * @param locale
     * @return
     * @author 周亮
     * @since 2017-05-03 09:48:33
     */
    @Transactional(readOnly = true)
    public String getText(String langKey, Locale locale) {
        String hashKey = RedisCacheKey.makeRedisKey(CommonConstant.EMPTY_STR, RedisCacheKey.MUL_LANG_PRE);
        String language = locale.toString();
        String redisLangKey = language + CommonConstant.STRIKETHROUGH + langKey;
        String content = (String) redisTemplate.opsForHash().get(hashKey, redisLangKey);
        if (EmptyUtils.isNotEmpty(content)) {
            return content;
        }
        MultiLang multiLang = multiLangDao.getByCodeAndKey(language, langKey);
        // 获取不到，在用zh_CN中文去查询一遍            update  20200109 如果是zh开头，
        if (EmptyUtils.isEmpty(multiLang)) {
            if (language.contains(MutiLangConstant.ZH_PREFIX)) {
                multiLang = multiLangDao.getByCodeAndKey(MutiLangConstant.ZH_CN, langKey);
            }
        }
        if (ObjectUtil.isNotEmpty(multiLang)) {
            redisTemplate.opsForHash().put(hashKey, redisLangKey, multiLang.getLangContext());
        }
        return (null == multiLang) ? langKey : multiLang.getLangContext();
    }

    /**
     * 获取所有的国际化语言数据，仅提供动态生成国际化js
     *
     * @return
     */
    public List<MultiLang> buildMultiLangJs(String langCode) {
        return multiLangDao.listByCode(langCode);
    }

    /**
     * 保存国际化
     *
     * @param multiLang 国际化对象
     * @param userId    当前用户ID
     */
    @CacheEvict(value = SysCacheConstant.MULTI_LANG_CACHE_NAME, key = "#multiLang.langCode.concat(#multiLang.langKey)")
    public void save(MultiLang multiLang, String userId) {
        MultiLang db = multiLangDao.getByCodeAndKey(multiLang.getLangCode(), multiLang.getLangKey());
        if (db != null) {
            throw new BusinessException(MultiLangEnum.SYS_MULTILANG_CODE_EXISTS);
        }
        multiLang.setCreateDate(new Date());
        multiLang.setCreateBy(userId);
        saveEntity(multiLang);
        // 删除缓存
        clearCache(multiLang);
    }

    /**
     * 辅助方法：删除国际化缓存。
     *
     * @param multiLang 国际化对象。
     */
    private void clearCache(MultiLang multiLang) {
        // 清除缓存
        String hashKey = RedisCacheKey.makeRedisKey(CommonConstant.EMPTY_STR, RedisCacheKey.MUL_LANG_PRE);
        String redisLangKey = multiLang.getLangCode() + CommonConstant.STRIKETHROUGH + multiLang.getLangKey();
        logger.info("clearCache redisLangKey:{}", redisLangKey);
        redisTemplate.opsForHash().delete(hashKey, redisLangKey);
    }

    /**
     * 更新国际化
     *
     * @param multiLang 国际化对象
     * @param userId    当前用户ID
     */
    @CacheEvict(value = SysCacheConstant.MULTI_LANG_CACHE_NAME, key = "#multiLang.langCode.concat(#multiLang.langKey)")
    public void update(MultiLang multiLang, String userId) {
        multiLang.setUpdateBy(userId);
        multiLang.setUpdateDate(new Date());
        multiLang.setLangCode(multiLang.getLangCode().trim());
        updateEntity(multiLang);
        // 删除缓存
        clearCache(multiLang);
    }

    @Override
    public void delete(List<String> langIds) {
        multiLangDao.delete(langIds);
    }

    public List<MultiLang> findList(String sqlWhere) {
        return multiLangDao.findList(sqlWhere);
    }

    /**
     * 初始化
     */

    @Override
    public void run(String... args) throws Exception {
        List<MultiLang> multiLangs = findList("");
        String hashKey = RedisCacheKey.makeRedisKey("", RedisCacheKey.MUL_LANG_PRE);
        redisTemplate.delete(hashKey);
        multiLangs.forEach(item -> redisTemplate.opsForHash().put(hashKey, item.getLangCode() + CommonConstant.STRIKETHROUGH + item.getLangKey(), item.getLangContext()));
    }

    public List<MultiLang> getLangList(Locale locale, List<String> langKeyList) {
        return multiLangDao.getLangList(locale.toString(), langKeyList);
    }
}
