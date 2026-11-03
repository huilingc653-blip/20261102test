package com.bytter.system.dao;
import com.bytter.system.entity.DictGroup;
import com.bytter.system.api.dto.I18nDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础数据国际化、业务基础数据国际化
 *
 * @author zhoul
 * @since 2019-11-11
 */
@Repository
public class I18nDao implements InitializingBean {
    /**
     * 基础数据
     */
    public static final String BASE_DATA = "0";
    /**
     * 基础业务数据
     */
    public static final String BUSINESS_DATA = "1";
    private static final String I18N_BIZ_TABLE = "SYS_INTERNATION";
    private static final String I18N_BASE_TABLE = "SYS_INTERNATION_BASE";
    private static final String CACHE_NAME = "i18nCache";
    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcOperations namedParameterJdbcOperations;
    @Override
    public void afterPropertiesSet() throws Exception {
        this.namedParameterJdbcOperations = new NamedParameterJdbcTemplate(dataSource);
    }
    private String getTable(String type) {
        if (BASE_DATA.equals(type)) {
            return I18N_BASE_TABLE;
        } else {
            return I18N_BIZ_TABLE;
        }
    }
    /**
     * 保持或更新操作
     *
     * @param type    识别是基础数据表还是基础业务表
     * @param locale  语言
     * @param keyId   基础数据ID
     * @param context 翻译内容
     */
    @CacheEvict(value = CACHE_NAME, key = "#type.concat(#locale).concat(#keyId)")
    public void saveOrUpdate(String type, String locale, String keyId, String context) {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("keyId", keyId);
        params.put("langCode", locale);
        params.put("context", context);
        String countSql = "select count(*) from " + getTable(type) + " where KEY_ID=:keyId and LANG_CODE=:langCode";
        Long count = namedParameterJdbcOperations.queryForObject(countSql, params, Long.class);
        String actionSql;
        // count>0做更新操作
        if (count > 0) {
            actionSql = "update " + getTable(type) + " set LANG_CONTEXT=:context where LANG_CODE=:langCode and KEY_ID=:keyId";
        } else {
            actionSql = "insert into " + getTable(type) + " (KEY_ID,LANG_CODE,LANG_CONTEXT) VALUES(:keyId,:langCode,:context)";
        }
        namedParameterJdbcOperations.update(actionSql, params);
    }
    /**
     * 获取国际化内容
     *
     * @param type   识别是基础数据表还是基础业务表
     * @param locale 语言
     * @param keyId  基础数据ID
     * @return 翻译后的内容
     */
    @Cacheable(value = CACHE_NAME, key = "#type.concat(#locale).concat(#keyId)")
    public String getContext(String type, String locale, String keyId) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("keyId", keyId);
        params.put("langCode", locale);
        String sql = "select LANG_CONTEXT from " + getTable(type) + " where KEY_ID=:keyId and LANG_CODE=:langCode";
        try {
            return namedParameterJdbcOperations.queryForObject(sql, params, String.class);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 批量获取国际化内容
     *
     * @param type   识别是基础数据表还是基础业务表
     * @param locale 语言
     * @param keyIds 基础数据ID列表
     * @return keyId到国际化内容的映射
     */
    public Map<String, String> getContextBatch(String type, String locale, List<String> keyIds) {
        if (keyIds == null || keyIds.isEmpty()) {
            return new HashMap<>();
        }

        // 构建IN查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("keyIds", keyIds);
        params.put("langCode", locale);

        // 构建IN查询语句，使用占位符避免SQL注入
        String placeholders = keyIds.stream().map(id -> ":keyId" + id).collect(Collectors.joining(","));
        String sql = "select KEY_ID, LANG_CONTEXT from " + getTable(type) +
                    " where KEY_ID in (" + placeholders + ") and LANG_CODE=:langCode";

        // 将keyIds作为参数添加到params中
        for (String keyId : keyIds) {
            params.put("keyId" + keyId, keyId);
        }

        try {
            List<Map<String, Object>> results = namedParameterJdbcOperations.queryForList(sql, params);
            Map<String, String> resultMap = new HashMap<>();
            for (Map<String, Object> result : results) {
                String keyId = (String) result.get("KEY_ID");
                String context = (String) result.get("LANG_CONTEXT");
                if (keyId != null && context != null) {
                    resultMap.put(keyId, context);
                }
            }
            return resultMap;
        } catch (Exception e) {
            // 如果查询失败，返回空Map
            return new HashMap<>();
        }
    }

    /**
     * 获取全部国际化内容
     *
     * @param type   识别是基础数据表还是基础业务表
     * @param locale 语言
     * @return 翻译后的内容
     */
    public List<DictGroup> getAllContext(String type, String locale) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("langCode", "zh_CN");
        String sql = "select d.ID as id,d.TYPE_GROUP_CODE as groupCode,i.LANG_CONTEXT as groupName from SYS_DICT_GROUP d LEFT JOIN " + getTable(type) + " i ON d.ID=i.KEY_ID where i.LANG_CODE=:langCode";
        try {
            List<DictGroup> dictGroupList = namedParameterJdbcOperations.query(sql, params, new BeanPropertyRowMapper<>(DictGroup.class));
            return dictGroupList;
        } catch (Exception e) {
            e.printStackTrace();
            return Arrays.asList();
        }
    }
    /**
     * 根据keyId获取值
     *
     * @param type
     * @param keyId
     * @return
     */
    public List<I18nDTO> listI18nByKeyId(String type, String keyId) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("keyId", keyId);
        String sql = "select LANG_CODE as locale,LANG_CONTEXT as context from " + getTable(type) + " where KEY_ID=:keyId";
        List<I18nDTO> dtoList = namedParameterJdbcOperations.query(sql, params, new BeanPropertyRowMapper<>(I18nDTO.class));
        return dtoList;
    }

    public List<I18nDTO> listI18n(String type,String locale) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("langCode", locale);
        String sql = "select KEY_ID,LANG_CODE as locale,LANG_CONTEXT as context from " + getTable(type) + " where LANG_CODE=:langCode";
        List<I18nDTO> dtoList = namedParameterJdbcOperations.query(sql, params, new BeanPropertyRowMapper<>(I18nDTO.class));
        return dtoList;
    }

    @CacheEvict(value = CACHE_NAME, key = "#type.concat(#locale).concat(#keyId)")
    public void delete(String type, String locale, String keyId) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("keyId", keyId);
        params.put("langCode", locale);
        String sql = "delete from " + getTable(type) + "  where KEY_ID=:keyId and LANG_CODE=:langCode";
        namedParameterJdbcOperations.update(sql, params);
    }
}
