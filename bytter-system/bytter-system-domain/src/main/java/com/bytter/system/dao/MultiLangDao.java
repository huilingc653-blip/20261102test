package com.bytter.system.dao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.entity.MultiLang;
import com.bytter.system.api.constant.SysCacheConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 多语言国际化
 *
 * @author zhoul
 * @date 2017/5/2
 */
@Repository
public class MultiLangDao extends BaseDao<MultiLang, String> {
    /**
     * 列表页面查询
     *
     * @param tableGridInput
     * @return
     */
    public IPageResult<MultiLang> listLang(TableGridInput tableGridInput) {
        Map<String, Object> map = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        IParameter parameter = tableGridInput.getParameter();
        String langCode = parameter.getPara("langCode");
        String langKey = parameter.getPara("langKey");
        String langContext = parameter.getPara("langContext");
        hql.append("from MultiLang m where 1=1 ");
        if (StringUtils.isNotBlank(langCode)) {
            hql.append("and m.langCode = :langCode ");
            map.put("langCode", langCode);
        }
        if (StringUtils.isNotBlank(langKey)) {
            hql.append("and m.langKey like :langKey ");
            map.put("langKey", "%" + langKey + "%");
        }
        if (StringUtils.isNotBlank(langContext)) {
            hql.append("and m.langContext like :langContext ");
            map.put("langContext", "%" + langContext + "%");
        }
        if (StringUtils.isNotBlank(tableGridInput.getOrder())) {
            hql.append("order by ").append(tableGridInput.getOrder()).append(" ").append(tableGridInput.getSort());
        }
        return getPageResultByHQL(tableGridInput, hql.toString(), map);
    }
    @Override
    public void delete(List<String> idList) {
        for (String s : idList) {
            delete(s);
        }
    }
    /**
     * 查询国际化信息
     *
     * @param langCode 语言
     * @param langKey  语言主键
     * @return
     */
    @Cacheable(value = SysCacheConstant.MULTI_LANG_CACHE_NAME, key = "#langCode.concat(#langKey)")
    public MultiLang getByCodeAndKey(String langCode, String langKey) {
        String sql = "select LANG_CODE,LANG_CONTEXT from sys_multi_lang where LANG_CODE=:langCode and LANG_KEY=:langKey";
        Map<String, Object> map = Maps.newHashMap();
        map.put("langCode", langCode);
        map.put("langKey", langKey);
        SQLQuery sqlQuery = createSQLQuery(sql, map);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map result = (Map) sqlQuery.uniqueResult();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        MultiLang multiLang = new MultiLang();
        multiLang.setLangCode((String) result.get("LANG_CODE"));
        multiLang.setLangContext(BeanUtils.getMapClobToString(result, "LANG_CONTEXT"));
        return multiLang;
    }
    /**
     * 查询国际化
     *
     * @param langCode 语言主键
     * @return
     */
    public List<MultiLang> listByCode(String langCode) {
        String hql = "select new map(m.langCode as langCode,m.langKey as langKey,m.langContext as langContext) from MultiLang m where m.langCode=?0";
        Query query = createQuery(hql, langCode);
        return query.list();
    }
    /**
     * 根据sql查询国际化
     *
     * @param sqlWhere sql查询条件
     * @return
     */
    public List<MultiLang> findList(String sqlWhere) {
        String sql = "select t.id,t.lang_code,t.lang_context,t.lang_key from sys_multi_lang t where 1 = 1 and lang_code = 'zh_CN'";
        return this.listBySQLAliasToBean(sql + sqlWhere, null, MultiLang.class);
    }

    public List<MultiLang> getLangList(String langCode, List<String> langKeys) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id as id, lang_code as langCode, lang_key as langKey, lang_context as langContext ");
        sql.append(" from SYS_MULTI_LANG ");
        sql.append(" where 1=1 and lang_code = :langCode ");
        sql.append(" and " + SqlUtil.getInSql(langKeys, "lang_key", true));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("langCode", langCode);
        return listBySQLAliasToBean(sql.toString(), params, MultiLang.class);
    }
}
