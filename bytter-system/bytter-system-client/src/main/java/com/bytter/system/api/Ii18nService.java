package com.bytter.system.api;
import com.bytter.system.api.dto.I18nDTO;
import java.util.List;
import java.util.Map;

/**
 * 基础数据国际化
 * i18n（其来源是英文单词 internationalization的首末字符i和n，18为中间的字符数）是"国际化"的简称。
 *
 * @author zhoul
 * @since 2019-11-12
 */
public interface Ii18nService {
    /**
     * 获取基础数据（菜单、资源、字典）国际化
     *
     * @param keyId 基础数据ID
     * @return 翻译后语言
     */
    String getBaseContext(String keyId);
    /**
     * 获取基础数据（菜单、资源、字典）国际化
     *
     * @param locale 语言
     * @param keyId  基础数据ID
     * @return 翻译后语言
     */
    String getBaseContext(String locale, String keyId);
    /**
     * 获取业务基础数据国际化
     *
     * @param keyId 基础数据ID
     * @return 翻译后语言
     */
    String getBusinessBaseContext(String keyId);
    /**
     * 获取业务基础数据国际化
     *
     * @param locale 语言
     * @param keyId  基础数据ID
     * @return 翻译后语言
     */
    String getBusinessBaseContext(String locale, String keyId);
    /**
     * 通过keyId，列出基础数据内容
     *
     * @param keyId 基础数据ID
     * @return 基础数据DTO
     */
    List<I18nDTO> listBaseByKeyId(String keyId);
    /**
     * 通过keyId，列出业务基础数据内容
     *
     * @param keyId 业务ID
     * @return 基础业务数据DTO
     */
    List<I18nDTO> listBusinessBaseByKeyId(String keyId);
    /**
     * 新增或保存基础业务数据
     *
     * @param i18nList 基础业务数据DTO
     */
    void saveOrUpdateBusinessBaseContext(List<I18nDTO> i18nList);
    /**
     * 新增或保存基础业务数据
     *
     * @param i18n 基础业务数据DTO
     */
    void saveOrUpdateBusinessBaseContext(I18nDTO i18n);
    /**
     * 删除
     *
     * @param locale 语言
     * @param keyId  业务ID
     */
    void deleteBusinessBaseContext(String locale, String keyId);
    /**
     * 删除
     *
     * @param keyId 业务ID
     */
    void deleteBusinessBaseContext(String keyId);
    
    /**
     * 批量获取基础数据（菜单、资源、字典）国际化
     *
     * @param keyIds 基础数据ID列表
     * @return keyId到国际化内容的映射
     */
    Map<String, String> getBaseContextBatch(List<String> keyIds);
    
    /**
     * 批量获取基础数据（菜单、资源、字典）国际化
     *
     * @param locale 语言
     * @param keyIds 基础数据ID列表
     * @return keyId到国际化内容的映射
     */
    Map<String, String> getBaseContextBatch(String locale, List<String> keyIds);
}